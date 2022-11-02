package com.sesame.game.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.sesame.game.Const;
import com.sesame.game.PuzzleTools;
import com.sesame.game.SudokuPuzzle;
import com.sesame.game.strategy.Strategy;
import com.sesame.game.strategy.StrategyExecute;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.SolutionModel;

/**
 * Introduction: smart game generator
 *
 * @author sesame 2022/10/31
 */
public class GameGenerator {

    public static final int EASY_CELL_NUMBER = 40;
    public static final int NORMAL_CELL_NUMBER = 32;
    public static final int HARD_CELL_NUMBER = 26;
    public static final int Hard_STRATEGY_NUMBER = 4;
    public static final List<Integer> EASY_STRATEGY = new ArrayList<>();
    public static final List<Integer> NORAML_STRATEGY = new ArrayList<>();
    public static final List<Integer> HARD_STRATEGY = new ArrayList<>();

    static {
        EASY_STRATEGY.add(1);
        EASY_STRATEGY.add(2);
    }

    static {
        NORAML_STRATEGY.add(3);
        NORAML_STRATEGY.add(4);
    }

    static {
        HARD_STRATEGY.add(5);
        HARD_STRATEGY.add(100);
    }

    public static SudokuPuzzle generateByLevel(int level) {
        SudokuPuzzle puzzle = new SudokuPuzzle();
        Optional<SudokuPuzzle> resultPuzzle = BruteForceSolver.buildSudokuPuzzle(puzzle);
        if (!resultPuzzle.isPresent()) {
            throw new RuntimeException("can't solve");
        }
        SudokuPuzzle copy = resultPuzzle.get();

        //按宫随机确定每个宫的可见数量
        int numberInCell[] = new int[9];
        for (int i = 0; i < numberInCell.length; i++) {
            numberInCell[i] = 2;
        }

        Random randomGenerator = new Random();
        int remaining = level - 18;
        while (remaining > 0) {
            int nextBox = randomGenerator.nextInt(9);
            if (numberInCell[nextBox] < 7) {
                numberInCell[nextBox] += 1;
                remaining--;
            }
        }

        //按宫填上数字
        for (int rowPoint = 0; rowPoint < Const.ROWS; rowPoint = rowPoint + Const.BOX_WIDTH) {
            for (int columnPoint = 0; columnPoint < Const.COLUMNS; columnPoint = columnPoint + Const.BOX_WIDTH) {
                int index = rowPoint + columnPoint / Const.BOX_WIDTH;
                List<Position> boxList = PuzzleTools.getPositionByBox(rowPoint, columnPoint);
                while (numberInCell[index] > 0) {
                    Position position = boxList.get(randomGenerator.nextInt(9));
                    int randomRow = position.getRow();
                    int randomColumn = position.getCol();
                    if (puzzle.isSlotAvailable(randomRow, randomColumn)) {
                        puzzle.makeMoveWithoutCandidate(randomRow, randomColumn, copy.getValue(randomRow, randomColumn),
                            false);
                        numberInCell[index]--;
                    }
                }
            }
        }
        puzzle.resetCandidate();

        return puzzle;
    }

    public static boolean resolve(SudokuPuzzle puzzle, List<Integer> interval) {
        SudokuPuzzle temp = new SudokuPuzzle(puzzle);

        //用策略尝试解，如果找到则结束，并保存策略
        List<HintModel> allHint = new ArrayList<>(81);
        // 执行策略
        Optional<HintModel> result = StrategyExecute.tryAllStrategy(puzzle);
        while (result.isPresent()) {
            HintModel hm = result.get();
            allHint.add(hm);
            if (hm.isCandidate()) {
                CandidateModel candidateModel = hm.getCandidateModel();
                Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
                deleteMap.entrySet().forEach(
                    one -> puzzle.deleteCandidate(one.getKey().getRow(), one.getKey().getCol(), one.getValue())
                );
            } else {
                SolutionModel solutionModel = hm.getSolutionModel();
                puzzle.makeMove(solutionModel.getPosition().getRow(), solutionModel.getPosition().getCol(),
                    solutionModel.getValue(), true);
            }
            result = StrategyExecute.tryAllStrategy(puzzle);

            if (allHint.size() > 81) {
                System.err.println("some thing goes wrong");

                System.out.println("Original puzzle is:\n ");
                System.out.println(temp.printBoard() + "\n ");

                System.out.println("here is:\n ");
                System.out.println(puzzle.printBoard() + "\n ");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                }
                System.exit(1);
            }
        }

        if (puzzle.boardFull()) {
            Map<Strategy, Integer> strategySet = new HashMap<>();

            for (int i = 0; i < allHint.size(); i++) {
                HintModel hintModel = allHint.get(i);
                if (strategySet.containsKey(hintModel.getStrategy())) {
                    strategySet.put(hintModel.getStrategy(), strategySet.get(hintModel.getStrategy()) + 1);
                } else {
                    strategySet.put(hintModel.getStrategy(), 1);
                }
            }

            if (strategySet.size() < interval.get(0) || strategySet.size() > interval.get(1)) {
                return false;
            }

            StringBuilder sb = new StringBuilder();

            sb.append("puzzle is:\n ");
            sb.append(temp.printBoard() + "\n ");
            sb.append("used Strategy: \n");
            for (Entry<Strategy, Integer> entry : strategySet.entrySet()) {
                sb.append("strategy name:").append(entry.getKey().getName()).append("\t");
                sb.append("count:").append(entry.getValue()).append("\n");
            }
            System.out.println(sb);
            return true;

        }

        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch count = new CountDownLatch(200);

        for (int i = 0; i < Runtime.getRuntime().availableProcessors() - 1; i++) {
            new Thread(new TaskThread(count, i)).start();
        }

        count.await();
    }

}

class TaskThread implements Runnable {

    private CountDownLatch count;
    private int number;

    public TaskThread(CountDownLatch count, int number) {
        this.count = count;
        this.number = number;
    }

    @Override
    public void run() {

        for (int i = 0; ; i++) {
            SudokuPuzzle puzzle = GameGenerator.generateByLevel(GameGenerator.EASY_CELL_NUMBER);
            boolean b = GameGenerator.resolve(puzzle, GameGenerator.EASY_STRATEGY);
            if (b) {
                System.out.print("numberL " + number + "\t found one.");
                count.countDown();
            } else {
                if (i % 300 == 0) {
                    System.out.print("numberL " + number + " solving " + i);
                }
            }

            if (count.getCount() <= 0) {
                break;
            }
        }

    }
}