package com.sesame.game;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Introduction: 全局常量
 *
 * @author sesame 2022/10/12
 */
public interface Const {
    int ROWS = 9;
    int COLUMNS = 9;
    int BOX_WIDTH = 3;
    int BOX_HEIGHT = 3;
    String[] VALID_VALUES = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    Set<String> SET_VALUES = new HashSet<>(Arrays.asList(VALID_VALUES));
}
