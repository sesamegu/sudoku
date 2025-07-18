package com.sesame.game.controller;

import com.sesame.game.common.SudokuPuzzle;
import com.sesame.game.dto.CandidateRequestDTO;
import com.sesame.game.dto.EraseRequestDTO;
import com.sesame.game.dto.FillRequestDTO;
import com.sesame.game.dto.HintResponseDTO;
import com.sesame.game.dto.SudokuPuzzleDTO;
import com.sesame.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    /**
     * 创建一个新游戏
     *
     * @param level  游戏难度 (可选)
     * @param number 关卡编号 (可选)
     * @return 新的数独谜题
     */
    @GetMapping("/new")
    public SudokuPuzzleDTO newGame(@RequestParam Optional<String> level,
                                   @RequestParam Optional<Integer> number) {
        SudokuPuzzle puzzle = gameService.startNewGame(level, number);
        return gameService.convertToDTO(puzzle);
    }

    /**
     * 在指定单元格填入数字
     *
     * @param request 包含棋盘状态、行列和数值的请求
     * @return 更新后的数独谜题
     */
    @PostMapping("/fill")
    public SudokuPuzzleDTO fillNumber(@RequestBody FillRequestDTO request) {
        return gameService.fillNumber(request);
    }

    /**
     * 删除指定单元格的数字
     *
     * @param request 包含棋盘状态和行列的请求
     * @return 更新后的数独谜题
     */
    @PostMapping("/erase")
    public SudokuPuzzleDTO eraseNumber(@RequestBody EraseRequestDTO request) {
        return gameService.eraseNumber(request);
    }

    /**
     * 切换指定单元格中某个候选数的状态（添加/删除）
     *
     * @param request 包含棋盘状态、行列和候选数值的请求
     * @return 更新后的数独谜题
     */
    @PostMapping("/candidate")
    public SudokuPuzzleDTO toggleCandidate(@RequestBody CandidateRequestDTO request) {
        return gameService.toggleCandidate(request);
    }

    /**
     * 获取一个解题提示
     *
     * @param puzzle 当前的棋盘状态
     * @param lang   语言 ("en_US" 或 "zh_CN")
     * @return 包含提示信息的响应对象
     */
    @PostMapping("/hint")
    public HintResponseDTO getHint(@RequestBody SudokuPuzzleDTO puzzle, @RequestParam(defaultValue = "en_US") String lang) {
        return gameService.getHint(puzzle, lang);
    }

    /**
     * 使用暴力破解算法解决数独
     *
     * @param puzzle 当前的棋盘状态
     * @return 解出的数独谜题
     */
    @PostMapping("/solve")
    public SudokuPuzzleDTO solvePuzzle(@RequestBody SudokuPuzzleDTO puzzle) {
        return gameService.solvePuzzle(puzzle);
    }

    /**
     * 根据当前棋盘状态计算所有候选数
     *
     * @param puzzle 当前的棋盘状态
     * @return 带有最新候选数的棋盘状态
     */
    @PostMapping("/calculate-candidates")
    public SudokuPuzzleDTO calculateCandidates(@RequestBody SudokuPuzzleDTO puzzle) {
        return gameService.calculateCandidates(puzzle);
    }
} 