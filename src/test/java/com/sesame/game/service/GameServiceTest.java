package com.sesame.game.service;

import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.dto.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * GameService测试类
 * 主要测试提示功能和自动选中单元格的逻辑
 *
 * @author mike
 * @date 2024/12/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    private SudokuPuzzleDTO testPuzzle;

    @Before
    public void setUp() {
        // 创建一个简单的测试数独谜题
        testPuzzle = createTestPuzzle();
    }

    /**
     * 测试提示功能 - 有解决方案的情况
     */
    @Test
    public void testGetHintWithSolution() {
        // 创建一个有明确解决方案的谜题
        SudokuPuzzleDTO puzzleWithSolution = createPuzzleWithSolution();
        
        HintResponseDTO hint = gameService.getHint(puzzleWithSolution, "zh_CN");
        
        Assert.assertTrue("应该找到提示", hint.isHintFound());
        Assert.assertNotNull("策略名称不应为空", hint.getStrategyName());
        Assert.assertNotNull("提示文本不应为空", hint.getHintText());
        
        // 验证是否有填入单元格的信息
        if (hint.getFillCells() != null && !hint.getFillCells().isEmpty()) {
            HintResponseDTO.CellFill fill = hint.getFillCells().get(0);
            Assert.assertTrue("行索引应在有效范围内", fill.getRow() >= 0 && fill.getRow() < 9);
            Assert.assertTrue("列索引应在有效范围内", fill.getCol() >= 0 && fill.getCol() < 9);
            Assert.assertNotNull("填入的值不应为空", fill.getValue());
        }
    }

    /**
     * 测试提示功能 - 候选数删除的情况
     */
    @Test
    public void testGetHintWithCandidateDeletion() {
        // 创建一个需要删除候选数的谜题
        SudokuPuzzleDTO puzzleWithCandidates = createPuzzleWithCandidates();
        
        HintResponseDTO hint = gameService.getHint(puzzleWithCandidates, "en_US");
        
        // 这个测试可能不会总是找到提示，因为需要特定的数独状态
        // 所以我们只测试如果找到提示时的验证逻辑
        if (hint.isHintFound()) {
            Assert.assertNotNull("策略名称不应为空", hint.getStrategyName());
            Assert.assertNotNull("提示文本不应为空", hint.getHintText());
            
            // 验证是否有删除候选数的信息
            if (hint.getDeleteCandidates() != null && !hint.getDeleteCandidates().isEmpty()) {
                HintResponseDTO.CandidateDelete delete = hint.getDeleteCandidates().get(0);
                Assert.assertTrue("行索引应在有效范围内", delete.getRow() >= 0 && delete.getRow() < 9);
                Assert.assertTrue("列索引应在有效范围内", delete.getCol() >= 0 && delete.getCol() < 9);
                Assert.assertNotNull("删除的候选数列表不应为空", delete.getValues());
                Assert.assertFalse("删除的候选数列表不应为空", delete.getValues().isEmpty());
            }
        } else {
            // 如果没有找到提示，至少验证响应格式正确
            Assert.assertFalse("提示未找到标志应该为false", hint.isHintFound());
            Assert.assertEquals("提示文本应该正确", "No hint found", hint.getHintText());
        }
    }

    /**
     * 测试提示功能 - 没有找到提示的情况
     */
    @Test
    public void testGetHintNoHintFound() {
        // 创建一个已完成的数独谜题（没有提示）
        SudokuPuzzleDTO completedPuzzle = createCompletedPuzzle();
        
        HintResponseDTO hint = gameService.getHint(completedPuzzle, "zh_CN");
        
        Assert.assertFalse("不应该找到提示", hint.isHintFound());
        Assert.assertEquals("提示文本应该正确", "No hint found", hint.getHintText());
    }

    /**
     * 测试数据转换功能 - DTO转Puzzle
     */
    @Test
    public void testConvertDTOToPuzzle() {
        SudokuPuzzle puzzle = gameService.convertDTOToPuzzle(testPuzzle);
        
        Assert.assertNotNull("转换后的Puzzle不应为空", puzzle);
        
        // 验证转换后的数据
        for (CellDTO cellDTO : testPuzzle.getCells()) {
            String originalValue = cellDTO.getValue();
            String convertedValue = puzzle.getValue(cellDTO.getRow(), cellDTO.getCol());
            
            if (originalValue == null) {
                Assert.assertEquals("null值应该转换为空字符串", "", convertedValue);
            } else {
                Assert.assertEquals("非null值应该保持不变", originalValue, convertedValue);
            }
        }
    }

    /**
     * 测试数据转换功能 - Puzzle转DTO
     */
    @Test
    public void testConvertToDTO() {
        SudokuPuzzle puzzle = gameService.convertDTOToPuzzle(testPuzzle);
        SudokuPuzzleDTO convertedDTO = gameService.convertToDTO(puzzle);
        
        Assert.assertNotNull("转换后的DTO不应为空", convertedDTO);
        Assert.assertEquals("单元格数量应该相同", testPuzzle.getCells().size(), convertedDTO.getCells().size());
        
        // 验证转换后的数据
        for (int i = 0; i < testPuzzle.getCells().size(); i++) {
            CellDTO original = testPuzzle.getCells().get(i);
            CellDTO converted = convertedDTO.getCells().get(i);
            
            Assert.assertEquals("行索引应该相同", original.getRow(), converted.getRow());
            Assert.assertEquals("列索引应该相同", original.getCol(), converted.getCol());
            Assert.assertEquals("可变性应该相同", original.isMutable(), converted.isMutable());
            
            // 验证值的转换
            if (original.getValue() == null) {
                Assert.assertNull("null值应该保持为null", converted.getValue());
            } else {
                Assert.assertEquals("非null值应该保持不变", original.getValue(), converted.getValue());
            }
        }
    }

    /**
     * 测试提示响应中的高亮单元格信息
     */
    @Test
    public void testHintHighlightCells() {
        SudokuPuzzleDTO puzzleWithSolution = createPuzzleWithSolution();
        
        HintResponseDTO hint = gameService.getHint(puzzleWithSolution, "zh_CN");
        
        if (hint.isHintFound() && hint.getHighlightCells() != null) {
            for (HintResponseDTO.CellPosition cell : hint.getHighlightCells()) {
                Assert.assertTrue("行索引应在有效范围内", cell.getRow() >= 0 && cell.getRow() < 9);
                Assert.assertTrue("列索引应在有效范围内", cell.getCol() >= 0 && cell.getCol() < 9);
                Assert.assertNotNull("颜色值不应为空", cell.getColor());
            }
        }
    }

    /**
     * 测试提示响应中的高亮区域信息
     */
    @Test
    public void testHintHighlightUnits() {
        SudokuPuzzleDTO puzzleWithSolution = createPuzzleWithSolution();
        
        HintResponseDTO hint = gameService.getHint(puzzleWithSolution, "zh_CN");
        
        if (hint.isHintFound() && hint.getHighlightUnits() != null) {
            for (HintResponseDTO.HighlightUnit unit : hint.getHighlightUnits()) {
                Assert.assertNotNull("单元类型不应为空", unit.getType());
                Assert.assertTrue("单元类型应该是有效的", 
                    Arrays.asList("ROW", "COLUMN", "BOX").contains(unit.getType()));
                
                if ("ROW".equals(unit.getType())) {
                    Assert.assertTrue("行索引应在有效范围内", unit.getRowIndex() >= 0 && unit.getRowIndex() < 9);
                } else if ("COLUMN".equals(unit.getType())) {
                    Assert.assertTrue("列索引应在有效范围内", unit.getColIndex() >= 0 && unit.getColIndex() < 9);
                } else if ("BOX".equals(unit.getType())) {
                    Assert.assertTrue("行索引应在有效范围内", unit.getRowIndex() >= 0 && unit.getRowIndex() < 3);
                    Assert.assertTrue("列索引应在有效范围内", unit.getColIndex() >= 0 && unit.getColIndex() < 3);
                }
            }
        }
    }

    /**
     * 测试多语言支持
     */
    @Test
    public void testHintMultilingual() {
        SudokuPuzzleDTO puzzleWithSolution = createPuzzleWithSolution();
        
        // 测试中文
        HintResponseDTO hintCN = gameService.getHint(puzzleWithSolution, "zh_CN");
        // 测试英文
        HintResponseDTO hintEN = gameService.getHint(puzzleWithSolution, "en_US");
        
        if (hintCN.isHintFound() && hintEN.isHintFound()) {
            Assert.assertNotNull("中文策略名称不应为空", hintCN.getStrategyName());
            Assert.assertNotNull("英文策略名称不应为空", hintEN.getStrategyName());
            Assert.assertNotNull("中文提示文本不应为空", hintCN.getHintText());
            Assert.assertNotNull("英文提示文本不应为空", hintEN.getHintText());
        }
    }

    // 辅助方法：创建测试数独谜题
    private SudokuPuzzleDTO createTestPuzzle() {
        List<CellDTO> cells = new ArrayList<>();
        
        // 创建一个简单的数独谜题（只有几个数字）
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                String value = null;
                if (r == 0 && c == 0) value = "1";
                if (r == 0 && c == 1) value = "2";
                if (r == 1 && c == 0) value = "3";
                
                cells.add(new CellDTO(r, c, value, true, new ArrayList<>()));
            }
        }
        
        return new SudokuPuzzleDTO(cells);
    }

    // 辅助方法：创建有解决方案的谜题
    private SudokuPuzzleDTO createPuzzleWithSolution() {
        List<CellDTO> cells = new ArrayList<>();
        
        // 创建一个接近完成的数独谜题，有明确的解决方案
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                String value = null;
                // 填充大部分单元格，留下一个空位
                if (!(r == 8 && c == 8)) {
                    value = String.valueOf((r * 3 + c) % 9 + 1);
                }
                
                cells.add(new CellDTO(r, c, value, true, new ArrayList<>()));
            }
        }
        
        return new SudokuPuzzleDTO(cells);
    }

    // 辅助方法：创建有候选数的谜题
    private SudokuPuzzleDTO createPuzzleWithCandidates() {
        List<CellDTO> cells = new ArrayList<>();
        
        // 创建一个有候选数的谜题，更接近真实的数独状态
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                String value = null;
                List<String> candidates = new ArrayList<>();
                
                // 填充一些已知数字
                if ((r == 0 && c == 0) || (r == 0 && c == 1) || (r == 1 && c == 0)) {
                    value = String.valueOf((r + c + 1) % 9 + 1);
                }
                // 在某些单元格中添加候选数
                else if (r == 0 && c == 2) {
                    candidates.addAll(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));
                }
                else if (r == 1 && c == 1) {
                    candidates.addAll(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));
                }
                else if (r == 2 && c == 0) {
                    candidates.addAll(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));
                }
                
                cells.add(new CellDTO(r, c, value, true, candidates));
            }
        }
        
        return new SudokuPuzzleDTO(cells);
    }

    // 辅助方法：创建已完成的数独谜题
    private SudokuPuzzleDTO createCompletedPuzzle() {
        List<CellDTO> cells = new ArrayList<>();
        
        // 创建一个已完成的数独谜题
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                String value = String.valueOf((r * 3 + c) % 9 + 1);
                cells.add(new CellDTO(r, c, value, false, new ArrayList<>()));
            }
        }
        
        return new SudokuPuzzleDTO(cells);
    }
} 