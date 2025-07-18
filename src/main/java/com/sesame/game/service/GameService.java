package com.sesame.game.service;

import com.sesame.game.common.GameLevel;
import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.dto.*;
import com.sesame.game.i18n.I18nProcessor;
import com.sesame.game.strategy.Strategy;
import com.sesame.game.strategy.StrategyExecute;
import com.sesame.game.strategy.model.*;
import com.sesame.game.tool.BruteForceSolver;
import com.sesame.game.tool.SudokuGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 游戏核心服务类，封装了所有游戏逻辑
 *
 * @author mike
 * @date 2024/7/26
 */
@Service
public class GameService {

    @Autowired
    private LevelService levelService;

    /**
     * 启动一个新游戏
     *
     * @param levelOpt  关卡难度 (可选)
     * @param numberOpt 关卡序号 (可选)
     * @return 初始化后的数独谜题
     */
    public SudokuPuzzle startNewGame(Optional<String> levelOpt, Optional<Integer> numberOpt) {
        SudokuPuzzle puzzle;
        if (levelOpt.isPresent() && numberOpt.isPresent()) {
            // 如果指定了关卡，则从LevelService加载
            GameLevel level = GameLevel.valueOf(levelOpt.get().toUpperCase().toUpperCase());
            puzzle = levelService.getPuzzle(level, numberOpt.get());
        } else {
            // 否则，生成一个随机的谜题
            puzzle = SudokuGenerator.generateRandomSudoku();
        }
        return puzzle;
    }

    /**
     * 在指定的单元格中填入一个数字。
     *
     * @param request 包含棋盘状态、行、列和要填入的数字的请求对象
     * @return 更新后的数独谜题DTO
     */
    public SudokuPuzzleDTO fillNumber(FillRequestDTO request) {
        SudokuPuzzle puzzle = convertDTOToPuzzle(request.getPuzzle());
        puzzle.makeMove(request.getRow(), request.getCol(), request.getValue(), true);
        return convertToDTO(puzzle);
    }

    /**
     * 删除指定单元格中的数字，并恢复该单元格的候选数。
     *
     * @param request 包含棋盘状态、行和列的请求对象
     * @return 更新后的数独谜题DTO
     */
    public SudokuPuzzleDTO eraseNumber(EraseRequestDTO request) {
        SudokuPuzzle puzzle = convertDTOToPuzzle(request.getPuzzle());
        puzzle.makeSlotEmpty(request.getRow(), request.getCol());
        return convertToDTO(puzzle);
    }

    /**
     * 切换指定单元格中候选数的状态（添加或删除）。
     *
     * @param request 包含棋盘状态、行、列和要切换的候选数的请求对象
     * @return 更新后的数独谜题DTO
     */
    public SudokuPuzzleDTO toggleCandidate(CandidateRequestDTO request) {
        SudokuPuzzle puzzle = convertDTOToPuzzle(request.getPuzzle());

        List<String> existingCandidates = puzzle.getCandidate(request.getRow(), request.getCol());
        List<String> newCandidates = (existingCandidates != null) ? new ArrayList<>(existingCandidates) : new ArrayList<>();
        String value = request.getValue();
        if (newCandidates.contains(value)) {
            newCandidates.remove(value);
        } else {
            newCandidates.add(value);
            Collections.sort(newCandidates); // Keep candidates sorted
        }
        puzzle.setCandidate(request.getRow(), request.getCol(), newCandidates);
        return convertToDTO(puzzle);
    }

    /**
     * 根据当前棋盘状态获取一个解题提示。
     *
     * @param puzzleDTO 当前棋盘状态的DTO
     * @param lang      所需的语言 ("en_US" 或 "zh_CN")
     * @return 包含详细提示信息的HintResponseDTO
     */
    public HintResponseDTO getHint(SudokuPuzzleDTO puzzleDTO, String lang) {
        SudokuPuzzle puzzle = convertDTOToPuzzle(puzzleDTO);
        Locale locale = I18nProcessor.getLocale(lang);
        Optional<HintModel> hintModelOptional = StrategyExecute.tryAllStrategy(puzzle, locale);

        if (!hintModelOptional.isPresent()) {
            HintResponseDTO response = new HintResponseDTO();
            response.setHintFound(false);
            response.setHintText("No hint found");
            return response;
        }

        HintModel hintModel = hintModelOptional.get();
        Strategy strategy = hintModel.getStrategy();

        HintResponseDTO response = new HintResponseDTO();
        response.setHintFound(true);
        response.setStrategyName(I18nProcessor.getValue(strategy.getName(), locale));
        response.setHintText(StrategyExecute.buildDesc(hintModel));

        // Process units to highlight
        if (hintModel.getUnitModelList() != null) {
            List<HintResponseDTO.HighlightUnit> highlightUnits = hintModel.getUnitModelList().stream()
                .map(this::convertUnitModelToDTO)
                .collect(Collectors.toList());
            response.setHighlightUnits(highlightUnits);
        }

        List<HintResponseDTO.CellPosition> highlightCells = new ArrayList<>();
        if (hintModel.isCandidateModel()) {
            // Handle candidate hint
            CandidateModel candidateModel = hintModel.getCandidateModel();
            response.setDeleteCandidates(convertDeleteMapToDTO(candidateModel.getDeleteMap()));

            // Highlight cause cells
            for (Map.Entry<Position, List<String>> entry : candidateModel.getCauseMap().entrySet()) {
                Position pos = entry.getKey();
                highlightCells.add(new HintResponseDTO.CellPosition(pos.getRow(), pos.getCol(), "rgba(135, 206, 250, 0.5)")); // Light Sky Blue for causes
            }
            // Highlight cells where candidates are deleted
            for (Map.Entry<Position, List<String>> entry : candidateModel.getDeleteMap().entrySet()) {
                Position pos = entry.getKey();
                highlightCells.add(new HintResponseDTO.CellPosition(pos.getRow(), pos.getCol(), "rgba(255, 182, 193, 0.5)")); // Light Pink for removals
            }

        } else {
            // Handle solution hint
            SolutionModel solutionModel = hintModel.getSolutionModel();
            HintResponseDTO.CellFill fill = new HintResponseDTO.CellFill(
                solutionModel.getPosition().getRow(),
                solutionModel.getPosition().getCol(),
                solutionModel.getSolutionDigital()
            );
            response.setFillCells(Collections.singletonList(fill));

            // Highlight solution cell
            highlightCells.add(new HintResponseDTO.CellPosition(solutionModel.getPosition().getRow(), solutionModel.getPosition().getCol(), "rgba(144, 238, 144, 0.7)")); // Light Green for solution

            // Highlight related cells
            if (solutionModel.getRelated() != null) {
                for (Position pos : solutionModel.getRelated()) {
                    highlightCells.add(new HintResponseDTO.CellPosition(pos.getRow(), pos.getCol(), "rgba(135, 206, 250, 0.5)")); // Light Sky Blue for related
                }
            }
        }
        response.setHighlightCells(highlightCells);

        return response;
    }

    /**
     * 使用暴力回溯算法解决当前的数独谜题。
     *
     * @param puzzleDTO 当前的数独谜题状态
     * @return 如果找到解，则返回包含解的SudokuPuzzleDTO；否则返回一个表示失败的DTO。
     */
    public SudokuPuzzleDTO solvePuzzle(SudokuPuzzleDTO puzzleDTO) {
        try {
            SudokuPuzzle puzzleToSolve = convertDTOToPuzzle(puzzleDTO);
            SudokuPuzzle puzzleCopy = new SudokuPuzzle(puzzleToSolve);

            // 打印输入状态用于调试
            System.out.println("开始暴力破解算法，输入状态：");
            int filledCount = 0;
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    String value = puzzleToSolve.getValue(r, c);
                    if (value != null && !value.isEmpty()) {
                        filledCount++;
                        System.out.println("位置 (" + r + "," + c + ") = " + value);
                    }
                }
            }
            System.out.println("总共有 " + filledCount + " 个已填充的单元格");

            // 确保暴力破解算法只在空单元格上工作，保留原有的固定值
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    String value = puzzleToSolve.getValue(r, c);
                    if (value != null && !value.isEmpty()) {
                        // 将有值的单元格设置为不可变，这样算法就不会修改它们
                        puzzleCopy.makeMutable(r, c, false);
                    } else {
                        // 空单元格设置为可变，算法可以在其上填入数字
                        puzzleCopy.makeMutable(r, c, true);
                    }
                }
            }

            System.out.println("开始执行暴力破解算法...");
            boolean solved = BruteForceSolver.backtrackSudokuSolver(0, 0, puzzleCopy);
            System.out.println("暴力破解算法结果: " + (solved ? "成功" : "失败"));

            if (solved) {
                return convertToDTO(puzzleCopy);
            } else {
                // 如果无法解决，抛出运行时异常以便调试
                throw new RuntimeException("无法解决当前数独谜题，请检查输入是否有效");
            }
        } catch (Exception e) {
            // 记录错误日志
            System.err.println("暴力破解算法执行失败: " + e.getMessage());
            e.printStackTrace();
            // 返回原始状态，前端应该显示错误信息
            return puzzleDTO;
        }
    }

    /**
     * 根据给定的棋盘状态，计算所有空格的候选数。
     *
     * @param puzzleDTO 当前的棋盘状态
     * @return 包含最新候选数的棋盘状态
     */
    public SudokuPuzzleDTO calculateCandidates(SudokuPuzzleDTO puzzleDTO) {
        SudokuPuzzle puzzle = convertDTOToPuzzle(puzzleDTO);
        puzzle.resetCandidate();
        return convertToDTO(puzzle);
    }

    /**
     * 将 SudokuPuzzleDTO 转换为内部使用的 SudokuPuzzle 实体对象。
     * @param puzzleDTO 前端传来的棋盘数据
     * @return 内部逻辑处理用的 SudokuPuzzle 对象
     */
    public SudokuPuzzle convertDTOToPuzzle(SudokuPuzzleDTO puzzleDTO) {
        SudokuPuzzle puzzle = new SudokuPuzzle();
        String[][] board = new String[9][9];

        for (CellDTO cellDTO : puzzleDTO.getCells()) {
            int r = cellDTO.getRow();
            int c = cellDTO.getCol();

            // 前端用null表示空单元格，内部用空字符串表示
            String value = cellDTO.getValue();
            board[r][c] = (value == null) ? "" : value;
            
            puzzle.makeMutable(r, c, cellDTO.isMutable());

            List<String> candidates = cellDTO.getCandidates() != null ? cellDTO.getCandidates() : new ArrayList<>();
            puzzle.setCandidate(r, c, candidates);
        }

        puzzle.setBoard(board);
        return puzzle;
    }

    /**
     * 将内部的 SudokuPuzzle 实体对象转换为用于前端展示的 SudokuPuzzleDTO。
     * @param puzzle 内部逻辑处理用的 SudokuPuzzle 对象
     * @return 用于传输到前端的 SudokuPuzzleDTO
     */
    public SudokuPuzzleDTO convertToDTO(SudokuPuzzle puzzle) {
        List<CellDTO> cells = new ArrayList<>();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                // 内部用空字符串表示空单元格，前端用null表示
                String value = puzzle.getValue(r, c);
                String cellValue = ("".equals(value)) ? null : value;
                
                cells.add(new CellDTO(
                    r,
                    c,
                    cellValue,
                    puzzle.isSlotMutable(r, c),
                    puzzle.getCandidate(r, c)
                ));
            }
        }
        return new SudokuPuzzleDTO(cells);
    }

    /**
     * 将提示模型中的“待删除候选数”Map转换为DTO列表。
     * @param deleteMap 包含位置和待删除候选数的Map
     * @return DTO列表
     */
    private List<HintResponseDTO.CandidateDelete> convertDeleteMapToDTO(Map<Position, List<String>> deleteMap) {
        if (deleteMap == null) {
            return Collections.emptyList();
        }
        return deleteMap.entrySet().stream()
            .map(entry -> new HintResponseDTO.CandidateDelete(
                entry.getKey().getRow(),
                entry.getKey().getCol(),
                entry.getValue()
            ))
            .collect(Collectors.toList());
    }

    /**
     * 将提示模型中的“高亮区域”模型转换为DTO。
     * @param unitModel 内部的区域模型
     * @return 用于高亮显示的DTO
     */
    private HintResponseDTO.HighlightUnit convertUnitModelToDTO(UnitModel unitModel) {
        if (unitModel.getUnit() == Unit.ROW) {
            return new HintResponseDTO.HighlightUnit("ROW", unitModel.getRow(), -1);
        } else if (unitModel.getUnit() == Unit.COLUMN) {
            return new HintResponseDTO.HighlightUnit("COLUMN", -1, unitModel.getColumn());
        } else if (unitModel.getUnit() == Unit.BOX) {
            return new HintResponseDTO.HighlightUnit("BOX", unitModel.getRow(), unitModel.getColumn());
        }
        return null; // Should not happen
    }
} 