package com.sesame.game.library;

import java.io.IOException;
import java.util.List;

import com.sesame.game.common.GameLevel;

/**
 * Introduction: file puzzles
 *
 * @author sesame 2022/11/2
 */
public class FileLibrary {

    private static List<String[][]> easyLibrary;
    private static List<String[][]> normalLibrary;
    private static List<String[][]> hardLibrary;
    private static List<String[][]> vipLibrary;

    private static boolean isInit = false;

    public synchronized static void init() {
        if (isInit) {
            return;
        }
        try {
            easyLibrary = ReadGameFromFile.readFile("/data/easy.txt");
            normalLibrary = ReadGameFromFile.readFile("/data/normal.txt");
            hardLibrary = ReadGameFromFile.readFile("/data/hard.txt");
            vipLibrary = ReadGameFromFile.readFile("/data/vip.txt");
            isInit = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static int size(GameLevel gameLevel) {
        if (!isInit) {
            init();
        }
        if (gameLevel == GameLevel.EASY) {
            return easyLibrary.size();
        } else if (gameLevel == GameLevel.NORMAL) {
            return normalLibrary.size();
        } else if (gameLevel == GameLevel.HARD) {
            return hardLibrary.size();
        } else if (gameLevel == GameLevel.VIP) {
            return vipLibrary.size();
        } else {
            throw new RuntimeException("should n't be here");
        }

    }

    public static String[][] caseArray(int number, GameLevel gameLevel) {
        if (!isInit) {
            init();
        }
        String[][] original;
        if (gameLevel == GameLevel.EASY) {
            original = easyLibrary.get(number - 1);
        } else if (gameLevel == GameLevel.NORMAL) {
            original = normalLibrary.get(number - 1);
        } else if (gameLevel == GameLevel.HARD) {
            original = hardLibrary.get(number - 1);
        } else if (gameLevel == GameLevel.VIP) {
            original = vipLibrary.get(number - 1);
        } else {
            throw new RuntimeException("should n't be here");
        }

        String[][] copy = new String[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }

}
