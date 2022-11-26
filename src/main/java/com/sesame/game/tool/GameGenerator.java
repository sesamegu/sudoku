package com.sesame.game.tool;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.sesame.game.common.Const;
import com.sesame.game.common.PuzzleTools;
import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.i18n.I18nProcessor;
import com.sesame.game.strategy.DetailTypeEnum;
import com.sesame.game.strategy.Strategy;
import com.sesame.game.strategy.StrategyExecute;
import com.sesame.game.strategy.model.CandidateModel;
import com.sesame.game.strategy.model.HintModel;
import com.sesame.game.strategy.model.Position;
import com.sesame.game.strategy.model.SolutionModel;
import com.sesame.game.strategy.model.Unit;
import com.sesame.game.strategy.model.UnitModel;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Introduction: smart game generator
 *
 * @author sesame 2022/10/31
 */
public class GameGenerator {

    public static final int EASY_CELL_NUMBER = 40;
    public static final int NORMAL_CELL_NUMBER = 32;
    public static final int HARD_CELL_NUMBER = 26;
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

        //random the number in the each box
        int[] numberInCell = new int[9];
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

        //make the digital in the each box
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

    public static boolean resolve(SudokuPuzzle puzzle, List<Integer> interval, BufferedWriter bufferedWrite)
        throws IOException {
        SudokuPuzzle temp = new SudokuPuzzle(puzzle);

        // try the strategy util the end
        List<HintModel> allHint = new ArrayList<>(81);
        Optional<HintModel> result = StrategyExecute.tryAllStrategy(puzzle);
        while (result.isPresent()) {
            HintModel hm = result.get();
            allHint.add(hm);
            if (hm.isCandidateModel()) {
                CandidateModel candidateModel = hm.getCandidateModel();
                Map<Position, List<String>> deleteMap = candidateModel.getDeleteMap();
                deleteMap.entrySet().forEach(
                    one -> puzzle.deleteCandidate(one.getKey().getRow(), one.getKey().getCol(), one.getValue())
                );
            } else {
                SolutionModel solutionModel = hm.getSolutionModel();
                puzzle.makeMove(solutionModel.getPosition().getRow(), solutionModel.getPosition().getCol(),
                    solutionModel.getSolutionDigital(), true);
            }
            result = StrategyExecute.tryAllStrategy(puzzle);

            // this shouldn't happen only if there is a bug :-(
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
            Map<String, Integer> strategySet = new TreeMap<String, Integer>() {};

            Map<Strategy, Integer> strategy = new HashMap<>();
            for (int i = 0; i < allHint.size(); i++) {
                HintModel hintModel = allHint.get(i);

                //build a detail strategy name
                StringBuilder sb = new StringBuilder(
                    I18nProcessor.getValue(hintModel.getStrategy().getName()));
                sb.append("__");
                List<UnitModel> unitModelList = hintModel.getUnitModelList();
                if (!CollectionUtils.isEmpty(unitModelList)) {
                    String collect = unitModelList.stream().map(UnitModel::getUnit).map(Unit::getDesc).map(
                        I18nProcessor::getValue).collect(
                        Collectors.joining("/"));
                    sb.append(collect);
                    sb.append("__");
                }
                DetailTypeEnum detailTypeEnum = hintModel.getDetailTypeEnum();
                if (detailTypeEnum != null) {
                    sb.append(detailTypeEnum.getType());
                }
                String detailName = sb.toString();
                if (strategySet.containsKey(detailName)) {
                    strategySet.put(detailName, strategySet.get(detailName) + 1);
                } else {
                    strategySet.put(detailName, 1);
                }

                if (strategy.containsKey(hintModel.getStrategy())) {
                    strategy.put(hintModel.getStrategy(), strategy.get(hintModel.getStrategy()) + 1);
                } else {
                    strategy.put(hintModel.getStrategy(), 1);
                }

            }

            if (strategy.size() < interval.get(0) || strategy.size() > interval.get(1)) {
                return false;
            }

            StringBuilder sb = new StringBuilder();

            sb.append("puzzle is:\n ");
            sb.append(temp.printBoard() + "\n ");
            sb.append("used Strategy: \n");

            for (Entry<String, Integer> entry : strategySet.entrySet()) {
                sb.append("strategy name:").append(entry.getKey()).append("\t");
                sb.append("count:").append(entry.getValue()).append("\n");
            }
            bufferedWrite.write(sb.toString());
            bufferedWrite.flush();
            return true;

        }

        return false;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        String filepath = "./puzzle.txt";
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath));

        CountDownLatch count = new CountDownLatch(3000);

        for (int i = 0; i < 3; i++) {
            new Thread(new TaskThread(count, i, bufferedWriter)).start();
        }

        count.await();
        bufferedWriter.flush();
        bufferedWriter.close();
    }

}

class TaskThread implements Runnable {

    private final CountDownLatch count;
    private final int number;
    private final BufferedWriter bufferedWrite;

    public TaskThread(CountDownLatch count, int number, BufferedWriter bufferedWrite) {
        this.count = count;
        this.number = number;
        this.bufferedWrite = bufferedWrite;
    }

    @SneakyThrows
    @Override
    public void run() {

        for (int i = 0; ; i++) {
            SudokuPuzzle puzzle = GameGenerator.generateByLevel(GameGenerator.EASY_CELL_NUMBER);
            boolean b = GameGenerator.resolve(puzzle, GameGenerator.EASY_STRATEGY, bufferedWrite);
            if (b) {
                System.out.print("numberL " + number + "\t found one.");
                count.countDown();
            } else {
                if (i % 1000 == 0) {
                    System.out.print("numberL " + number + " solving " + i);
                }
            }

            if (count.getCount() <= 0) {
                break;
            }
        }

    }
}