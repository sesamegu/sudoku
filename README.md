# 数独 【依宝】
项目是基于java Swing 实现的一个数独游戏。项目的主要特点：精良关卡、操作功能丰富、实现了主流的求解技巧、解题器、关卡设计器、支持多语言。
项目部分代码基于  [sudoku](https://github.com/mattnenterprise/Sudoku)
<img src="src/main/resources/picture/index_cn.png" width="550" height="430" >
## 特点
* 精良关卡   
   提供简单、普通、困难、专项四种难度模式，在简单模式中涉及2种技巧即可过关，普通模式3、4种，困难模式5、6种，而在专项模式中涉及
   到8种以上的技巧才能过关。
* 操作功能丰富     
   提示功能：所有关卡中，每一步都能提供对应各种技巧的展示和答案
   候选数笔记：在空白格中标记数字
   删除：删除单元格数字
* 实现了主流的求解技巧    
    唯余空白格、唯一候选数、隐性单一数、显性数对、显性三数对、隐形数对、隐形三数对、宫区块数对、x翼、xy翼、剑鱼、三三三 等
* 解题器      
  在解题器模式中，你可以输入自己的数独谜题，程序会自动求解。求解支持两种方式：技巧求解和暴力求解。技巧求解就是本程序支持的技巧求解，整体
  解题过程和思路清晰可见；暴力求解则是程序直接遍历所有可能找出解
* 关卡设计器      
  采用随机程序，自动输出各种关卡。本游戏的关卡为这个功能自动产生。
* 多语言      
   支持中文、英语

## 如何运行 
* 准对开发者     
   程序的入口类 com.sesame.game.Sudoku
* Mac or Linux    
   to do
* Windows    
   to do
## 策略介绍
* 唯余空白格   
介绍：一个宫、一行或一列中只剩下一个空白单元格，找出缺少哪个数字，将它填入这个空白单元格
场景行   
<img src="src/main/resources/picture/last_free_cell_row_CN.png" width="550" height="430" >
场景列    
<img src="src/main/resources/picture/last_free_cell_col_CN.png" width="550" height="430" >
场景宫   
<img src="src/main/resources/picture/last_free_cell_box_CN.png" width="550" height="430" >
   
* 唯一候选数  
介绍：一个宫、一行或一列中只剩下一个空白单元格，找出缺少哪个数字，将它填入这个空白单元格
<img src="src/main/resources/picture/last_possible_number_CN.png" width="550" height="430" >

* 隐性单一数    
介绍：    
<img src="src/main/resources/picture/_CN.png" width="550" height="430" >

* 显性数对    
介绍：    
<img src="src/main/resources/picture/_CN.png" width="550" height="430" >

* 显性三数对    
介绍：    
<img src="src/main/resources/picture/_CN.png" width="550" height="430" >

* 隐形数对    
介绍：    
<img src="src/main/resources/picture/_CN.png" width="550" height="430" >

* 隐形三数对    
介绍：    
<img src="src/main/resources/picture/_CN.png" width="550" height="430" >

* 宫区块数对    
介绍：    
<img src="src/main/resources/picture/_CN.png" width="550" height="430" >

* x翼    
介绍：    
<img src="src/main/resources/picture/_CN.png" width="550" height="430" >

* xy翼    
介绍：    
<img src="src/main/resources/picture/_CN.png" width="550" height="430" >

* 剑鱼    
介绍：    
<img src="src/main/resources/picture/_CN.png" width="550" height="430" >

* 三三三    
介绍：    
<img src="src/main/resources/picture/_CN.png" width="550" height="430" >

*     
介绍：    
<img src="src/main/resources/picture/_CN.png" width="550" height="430" >


## 数独解题器

## 数独游戏设计器

