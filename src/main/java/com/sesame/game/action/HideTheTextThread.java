package com.sesame.game.action;

import java.util.concurrent.TimeUnit;

import com.sesame.game.SudokuFrame;

/**
 * Introduction: hide the hint's text thread
 *
 * @author sesame 2022/10/26
 */
public class HideTheTextThread implements Runnable {
    private final SudokuFrame sudokuFrame;

    public HideTheTextThread(SudokuFrame sudokuFrame) {this.sudokuFrame = sudokuFrame;}

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
        }
        sudokuFrame.setUnAvailableLabel("");
    }
}