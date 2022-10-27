package com.sesame.game.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import com.sesame.game.SudokuPanel;
import com.sesame.game.SudokuPuzzle;
import org.springframework.util.CollectionUtils;

/**
 * Introduction:
 *
 * @author sesame 2022/10/26
 */

public class NumActionListener implements ActionListener {

    private SudokuPanel panel;

    public NumActionListener(SudokuPanel sPanel) {
        panel = sPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        SudokuPuzzle puzzle = panel.puzzle;
        if (panel.currentlySelectedCol != -1 && panel.currentlySelectedRow != -1) {
            String digital = ((JButton)e.getSource()).getText();

            if (panel.isNoteMode) {
                //已填充数字，则无响应
                if (puzzle.isSlotValid(panel.currentlySelectedRow, panel.currentlySelectedCol)) {
                    return;
                }

                List<String> candidate = puzzle.getCandidate(panel.currentlySelectedRow, panel.currentlySelectedCol);
                //为空直接添加
                if (CollectionUtils.isEmpty(candidate)) {
                    List<String> canList = new ArrayList<>();
                    canList.add(digital);
                    puzzle.setCandidate(panel.currentlySelectedRow, panel.currentlySelectedCol, canList);
                } else {
                    //取反
                    if (candidate.contains(digital)) {
                        candidate.remove(digital);
                    } else {
                        candidate.add(digital);
                    }
                }
                panel.isUserNoted = true;
            } else {
                puzzle.makeMove(panel.currentlySelectedRow, panel.currentlySelectedCol, digital,
                    true);
            }

            panel.repaint();
        }
    }
}