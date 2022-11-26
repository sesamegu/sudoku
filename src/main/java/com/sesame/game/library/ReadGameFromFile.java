package com.sesame.game.library;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;

/**
 * Introduction:read game from file
 *
 * @author sesame 2022/11/25
 */
public class ReadGameFromFile {

    public static List<String[][]> readFile(String path) throws IOException {
        List<String[][]> library = new ArrayList<>();
        String file = ReadGameFromFile.class.getResource(path).getFile();
        BufferedReader bufferedReader = new BufferedReader(
            new FileReader(file));

        String oneLine;
        while ((oneLine = bufferedReader.readLine()) != null) {
            if (oneLine.indexOf("{") == -1) {
                continue;
            }
            String[][] nine = new String[9][9];
            String[] oneArray = buildOneArray(oneLine);
            nine[0] = oneArray;
            for (int i = 0; i < 8; i++) {
                String tempLine = bufferedReader.readLine();
                Validate.isTrue(oneLine.indexOf("{") != -1, "should contain {");
                nine[i + 1] = buildOneArray(tempLine);
            }

            library.add(nine);
        }

        return library;
    }

    private static String[] buildOneArray(String oneLine) {
        int startPoint = oneLine.indexOf("{");
        int endPoint = oneLine.indexOf("}");
        String substring = oneLine.substring(startPoint + 1, endPoint);
        String[] array = substring.split(",");
        Validate.isTrue(array.length == 9, "length is 9");
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].replace("\"", "");
        }

        return array;
    }
}
