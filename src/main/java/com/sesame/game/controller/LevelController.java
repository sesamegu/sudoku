package com.sesame.game.controller;

import java.util.List;

import com.sesame.game.dto.LevelInfoDTO;
import com.sesame.game.service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/levels")
public class LevelController {

    @Autowired
    private LevelService levelService;

    /**
     * 获取所有可用的游戏关卡信息
     *
     * @return 包含所有难度及其关卡数量的列表
     */
    @GetMapping
    public List<LevelInfoDTO> getLevels() {
        return levelService.getLevels();
    }
} 