package com.sesame.game.service;

import com.sesame.game.common.GameLevel;
import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.dto.LevelInfoDTO;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LevelService {

    private List<String[][]> easyLibrary;
    private List<String[][]> normalLibrary;
    private List<String[][]> hardLibrary;
    private List<String[][]> vipLibrary;

    /**
     * 服务启动时，初始化加载所有本地数独文件。
     */
    @PostConstruct
    public void init() {
        try {
            easyLibrary = new ReadGameFromFile().readFile("/data/easy.txt");
            normalLibrary = new ReadGameFromFile().readFile("/data/normal.txt");
            hardLibrary = new ReadGameFromFile().readFile("/data/hard.txt");
            vipLibrary = new ReadGameFromFile().readFile("/data/vip.txt");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load puzzle files", e);
        }
    }

    /**
     * 获取所有游戏难度及其对应的关卡数量。
     *
     * @return 关卡信息列表
     */
    public List<LevelInfoDTO> getLevels() {
        return Arrays.stream(GameLevel.values())
            .map(level -> new LevelInfoDTO(level.name(), getLibrary(level).size()))
            .collect(Collectors.toList());
    }

    /**
     * 根据难度和关卡序号获取一个具体的数独谜题。
     * @param gameLevel 游戏难度
     * @param number 关卡序号 (从1开始)
     * @return SudokuPuzzle 对象
     */
    public SudokuPuzzle getPuzzle(GameLevel gameLevel, int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("Puzzle number must be positive.");
        }
        List<String[][]> library = getLibrary(gameLevel);
        if (number > library.size()) {
            throw new IndexOutOfBoundsException("Puzzle number " + number + " is out of bounds for level " + gameLevel);
        }

        String[][] boardData = library.get(number - 1);
        SudokuPuzzle puzzle = new SudokuPuzzle();
        puzzle.setBoard(boardData);

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (!boardData[r][c].isEmpty()) {
                    puzzle.makeMutable(r, c, false);
                }
            }
        }
        puzzle.resetCandidate();
        return puzzle;
    }

    /**
     * 根据游戏难度获取对应的题库列表。
     * @param gameLevel 游戏难度
     * @return 题库列表
     */
    private List<String[][]> getLibrary(GameLevel gameLevel) {
        switch (gameLevel) {
            case EASY:
                return easyLibrary;
            case NORMAL:
                return normalLibrary;
            case HARD:
                return hardLibrary;
            case VIP:
                return vipLibrary;
            default:
                throw new IllegalArgumentException("Unknown game level: " + gameLevel);
        }
    }
} 