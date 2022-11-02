package com.sesame.game.library;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

/**
 * Introduction:
 *
 * @author sesame 2022/11/2
 */
public class NormalLibrary {

    public String[][] caseArray(int number) {
        Assert.isTrue(number > 0 && number <= 10, "illegal number");
        List<String[][]> library = new ArrayList<>();

        // 1
        library.add(new String[][] {
                {"", "", "6", "1", "", "", "", "5", ""},
                {"1", "", "7", "", "", "", "6", "9", ""},
                {"", "", "8", "", "6", "", "1", "", ""},
                {"", "", "", "", "7", "", "8", "", "9"},
                {"8", "", "5", "", "", "1", "", "", ""},
                {"3", "7", "", "", "", "2", "", "1", "5"},
                {"", "2", "1", "7", "", "", "", "", "6"},
                {"7", "8", "", "9", "", "", "5", "", "4"},
                {"", "", "", "", "2", "", "7", "", ""},
            }
        );
        // 2
        library.add(new String[][] {
                {"", "", "7", "", "4", "8", "5", "", "9"},
                {"", "", "", "", "", "9", "2", "", ""},
                {"3", "", "9", "", "6", "", "", "", "8"},
                {"", "2", "", "4", "", "3", "6", "", ""},
                {"5", "", "4", "", "", "1", "", "9", ""},
                {"", "", "6", "", "", "2", "3", "", "4"},
                {"7", "", "", "", "", "4", "9", "", ""},
                {"", "", "", "", "2", "5", "", "", ""},
                {"", "9", "", "", "3", "", "", "2", "1"},
            }
        );
        //3
        library.add(new String[][] {
                {"", "", "7", "2", "", "8", "", "", ""},
                {"", "", "", "1", "6", "", "", "", "7"},
                {"2", "6", "9", "", "", "", "8", "4", ""},
                {"", "", "", "", "", "6", "", "", "8"},
                {"8", "2", "", "", "9", "", "3", "7", ""},
                {"6", "", "", "8", "7", "", "", "1", ""},
                {"3", "", "1", "6", "8", "", "", "9", ""},
                {"4", "", "", "9", "1", "", "", "8", ""},
                {"", "", "", "", "", "", "", "", "5"},
            }
        );
        // 4
        library.add(new String[][] {
                {"8", "", "4", "", "3", "6", "5", "", ""},
                {"5", "", "", "1", "", "", "", "", ""},
                {"", "", "", "", "", "8", "6", "", "1"},
                {"", "4", "", "5", "8", "", "", "9", ""},
                {"", "", "", "", "", "7", "4", "1", ""},
                {"", "8", "9", "", "1", "", "2", "", ""},
                {"1", "6", "", "8", "", "", "", "", "7"},
                {"", "", "", "9", "", "1", "", "6", ""},
                {"", "9", "8", "", "", "", "", "5", "2"},
            }
        );
        //5
        library.add(new String[][] {
                {"", "", "", "2", "", "7", "3", "", ""},
                {"3", "2", "", "1", "5", "", "", "", "8"},
                {"", "5", "9", "", "", "", "1", "", ""},
                {"", "", "2", "", "8", "1", "", "9", ""},
                {"", "4", "5", "7", "", "", "", "8", "1"},
                {"", "", "", "5", "", "", "", "", ""},
                {"", "6", "", "", "", "", "9", "", "5"},
                {"9", "", "", "", "1", "5", "", "", "2"},
                {"", "8", "4", "9", "", "", "", "1", ""},
            }
        );
        //6
        library.add(new String[][] {
                {"", "", "", "3", "4", "", "", "7", ""},
                {"6", "3", "", "", "", "", "", "", "8"},
                {"8", "", "9", "", "6", "", "1", "5", ""},
                {"", "", "1", "4", "", "3", "", "", "6"},
                {"", "5", "", "6", "", "", "", "8", ""},
                {"", "6", "8", "5", "", "", "3", "", ""},
                {"1", "", "", "", "", "", "9", "", ""},
                {"", "", "4", "9", "", "", "", "", "5"},
                {"", "9", "6", "", "2", "4", "8", "", ""},
            }
        );
        //7
        library.add(new String[][] {
                {"", "1", "", "", "4", "", "6", "", ""},
                {"6", "", "", "", "5", "", "2", "", "8"},
                {"", "4", "", "", "7", "", "", "", "5"},
                {"", "2", "3", "", "", "7", "", "", ""},
                {"", "7", "", "", "9", "", "", "", "1"},
                {"", "", "8", "6", "", "3", "4", "2", "7"},
                {"7", "", "", "8", "2", "", "9", "", ""},
                {"", "", "2", "7", "", "", "", "5", ""},
                {"", "", "4", "9", "", "", "", "", "2"},
            }
        );
        //8
        library.add(new String[][] {
                {"", "1", "", "2", "", "", "3", "", "8"},
                {"3", "2", "", "1", "", "", "", "", ""},
                {"", "", "7", "3", "", "6", "", "", "5"},
                {"", "", "1", "", "7", "", "", "", "6"},
                {"4", "5", "", "", "1", "", "", "9", ""},
                {"7", "", "", "5", "", "", "", "4", ""},
                {"", "7", "", "", "", "1", "", "", "4"},
                {"", "", "3", "", "", "4", "", "", "7"},
                {"1", "", "4", "", "", "5", "8", "3", ""},
            }
        );
        //9
        library.add(new String[][] {
                {"", "", "", "", "", "", "", "", "9"},
                {"", "3", "", "2", "", "", "", "", ""},
                {"6", "5", "9", "", "7", "8", "2", "1", ""},
                {"7", "", "", "4", "6", "", "8", "", "5"},
                {"", "4", "", "5", "", "", "", "", ""},
                {"9", "", "", "", "8", "", "", "4", "3"},
                {"5", "7", "1", "6", "", "", "", "", ""},
                {"", "", "", "9", "", "5", "7", "2", ""},
                {"", "9", "", "", "", "7", "", "5", ""},
            }
        );
        //10
        library.add(new String[][] {
                {"", "1", "7", "", "", "", "3", "8", ""},
                {"4", "", "", "", "", "9", "2", "6", ""},
                {"", "6", "", "", "7", "", "", "", ""},
                {"", "7", "2", "", "8", "", "", "", "6"},
                {"", "", "", "", "9", "", "1", "", "3"},
                {"", "9", "", "", "", "7", "", "", "2"},
                {"", "", "", "7", "", "", "6", "5", "8"},
                {"8", "4", "3", "", "6", "", "", "2", ""},
                {"", "", "6", "8", "2", "", "", "", ""},
            }
        );
        return library.get(number - 1);
    }

}
