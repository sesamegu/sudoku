document.addEventListener('DOMContentLoaded', () => {
    const boardElement = document.getElementById('sudoku-board');
    const numberPanel = document.getElementById('number-panel');
    const solveButton = document.getElementById('solve-button');
    const hintButton = document.getElementById('hint-button');
    const clearButton = document.getElementById('clear-button');
    const eraseButton = document.getElementById('erase-button');
    const langButton = document.getElementById('lang-button');
    const infoPanel = document.getElementById('info-panel');

    let selectedCell = null;
    let gameState = createEmptyPuzzle();
    let currentLang = 'zh_CN';

    const translations = {
        'en_US': {
            'page_title': 'Sudoku Solver',
            'title': 'Sudoku Solver',
            'solve': 'Solve',
            'hint': 'Hint',
            'clear': 'Clear',
            'erase': 'Erase',
            'lang_button': '中文',
            'back_to_game': 'Back to Game',
            'info_default': 'Fill in the puzzle and click "Solve".',
            'info_select_cell': 'Please select a cell first.',
            'info_select_to_erase': 'Please select a cell to erase.',
            'info_cleared': 'Board cleared. Fill in the puzzle and click "Solve".',
            'info_solving': 'Solving...',
            'info_solved': 'Puzzle Solved!',
            'info_unsolvable': 'Could not solve the puzzle. Please check your input.',
            'info_error': 'Error: Could not connect to solver.',
            'getting_hint': 'Getting hint...',
            'no_hint_found': 'No hint found.',
            'hint_error': 'Error getting hint.'
        },
        'zh_CN': {
            'page_title': '数独破解器',
            'title': '数独破解器',
            'solve': '破解',
            'hint': '提示',
            'clear': '清空',
            'erase': '删除',
            'lang_button': 'English',
            'back_to_game': '返回游戏',
            'info_default': '请填入数字并点击“破解”。',
            'info_select_cell': '请先选择一个单元格。',
            'info_select_to_erase': '请选择要删除的单元格。',
            'info_cleared': '棋盘已清空。请填入数字并点击“破解”。',
            'info_solving': '正在破解...',
            'info_solved': '破解成功！',
            'info_unsolvable': '无法破解该数独，请检查您的输入。',
            'info_error': '错误：无法连接到破解服务。',
            'getting_hint': '正在获取提示...',
            'no_hint_found': '未找到提示。',
            'hint_error': '获取提示时出错。'
        }
    };

    function t(key) {
        return translations[currentLang][key];
    }

    function updateUIForLanguage() {
        document.title = t('page_title');
        document.querySelector('h1').textContent = t('title');
        solveButton.textContent = t('solve');
        hintButton.textContent = t('hint');
        clearButton.textContent = t('clear');
        eraseButton.textContent = t('erase');
        langButton.textContent = t('lang_button');
        document.querySelector('.back-to-game-link').textContent = t('back_to_game');
        infoPanel.textContent = t('info_default');
    }

    function toggleLanguage() {
        currentLang = (currentLang === 'en_US') ? 'zh_CN' : 'en_US';
        updateUIForLanguage();
    }


    function createEmptyPuzzle() {
        const cells = [];
        for (let r = 0; r < 9; r++) {
            for (let c = 0; c < 9; c++) {
                cells.push({
                    row: r,
                    col: c,
                    value: null,
                    mutable: true,
                    candidates: []
                });
            }
        }
        return { cells };
    }

    function renderBoard(puzzle) {
        boardElement.innerHTML = '';
        puzzle.cells.forEach(cellData => {
            const cellElement = document.createElement('div');
            cellElement.className = 'cell';
            cellElement.dataset.row = cellData.row;
            cellElement.dataset.col = cellData.col;

            const valueDiv = document.createElement('div');
            valueDiv.className = 'cell-value';
            
            const candidatesDiv = document.createElement('div');
            candidatesDiv.className = 'candidates';

            if (cellData.value) {
                valueDiv.textContent = cellData.value;
                cellElement.classList.add('user-filled');
                candidatesDiv.style.display = 'none';
            } else if (cellData.candidates && cellData.candidates.length > 0) {
                 for (let i = 1; i <= 9; i++) {
                    const cand = document.createElement('div');
                    cand.className = 'candidate-num';
                    cand.dataset.cand = i;
                    cand.innerHTML = cellData.candidates.includes(String(i)) ? i : '&nbsp;';
                    candidatesDiv.appendChild(cand);
                }
            } else {
                 // 为了对齐也创建空的候选数div
                for (let i = 1; i <= 9; i++) {
                    const cand = document.createElement('div');
                    cand.className = 'candidate-num';
                    cand.innerHTML = '&nbsp;';
                    candidatesDiv.appendChild(cand);
                }
            }
            cellElement.appendChild(valueDiv);
            cellElement.appendChild(candidatesDiv);

            boardElement.appendChild(cellElement);
        });
    }

    function createNumberPanel() {
        numberPanel.innerHTML = '';
        for (let i = 1; i <= 9; i++) {
            const btn = document.createElement('button');
            btn.textContent = i;
            btn.dataset.number = i;
            numberPanel.appendChild(btn);
        }
    }

    function handleCellClick(event) {
        const target = event.target.closest('.cell');
        if (!target) return;

        const row = parseInt(target.dataset.row);
        const col = parseInt(target.dataset.col);

        // 如果点击相同单元格，则取消选择
        if (selectedCell && selectedCell.row === row && selectedCell.col === col) {
            if (selectedCell) {
                selectedCell.cell.classList.remove('selected');
            }
            selectedCell = null;
        } else {
            // 清除之前的高亮效果（包括提示高亮）
            clearHighlights();
            
            // 清除之前选中单元格的selected类
            if (selectedCell) {
                selectedCell.cell.classList.remove('selected');
            }
            
            // 选择新单元格
            selectedCell = { cell: target, row, col };
            target.classList.add('selected');
        }
    }

    function handleNumberClick(event) {
        const number = event.target.dataset.number;
        if (!number || !selectedCell) {
            infoPanel.textContent = t('info_select_cell');
            return;
        }
        fillNumber(selectedCell.row, selectedCell.col, number);
    }

    function handleErase() {
        if (!selectedCell) {
            infoPanel.textContent = t('info_select_to_erase');
            return;
        }
        fillNumber(selectedCell.row, selectedCell.col, null);
    }

    function fillNumber(row, col, value) {
        const cell = gameState.cells.find(c => c.row === row && c.col === col);
        if (cell) {
            cell.value = value;
            updateCandidatesOnBoard();
        }
    }

    function handleClear() {
        gameState = createEmptyPuzzle();
        clearHighlights();
        updateCandidatesOnBoard();
        infoPanel.textContent = t('info_cleared');
    }

    async function updateCandidatesOnBoard() {
        try {
            const response = await fetch('/api/game/calculate-candidates', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(gameState)
            });
            if (!response.ok) {
                throw new Error('Failed to update candidates');
            }
            const updatedPuzzle = await response.json();
            gameState = updatedPuzzle;
            renderBoard(gameState);
            
            // 重新渲染后重新选择单元格，但不恢复高亮效果
            if (selectedCell) {
                const reselected = boardElement.querySelector(`[data-row='${selectedCell.row}'][data-col='${selectedCell.col}']`);
                if (reselected) {
                    selectedCell.cell = reselected;
                    reselected.classList.add('selected');
                }
            }

        } catch (error) {
            console.error('Error updating candidates:', error);
            infoPanel.textContent = 'Error updating candidates.';
        }
    }

    // 获取提示功能
    async function getHint() {
        infoPanel.textContent = t('getting_hint');
        clearHighlights();

        try {
            const response = await fetch(`/api/game/hint?lang=${currentLang}`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(gameState)
            });
            if (!response.ok) {
                throw new Error('Server responded with an error.');
            }
            const hint = await response.json();
            applyHint(hint);
        } catch (error) {
            console.error('Error getting hint:', error);
            infoPanel.textContent = t('hint_error');
        }
    }

    // 应用提示，高亮相关的单元格和区域
    function applyHint(hint) {
        clearHighlights();
        if (!hint.hintFound) {
            infoPanel.textContent = t('no_hint_found');
            return;
        }
        infoPanel.innerHTML = `<strong>${hint.strategyName}:</strong> ${hint.hintText}`;

        if (hint.highlightUnits) {
            hint.highlightUnits.forEach(unit => highlightUnit(unit));
        }
        if (hint.highlightCells) {
            hint.highlightCells.forEach(cell => highlightCell(cell.row, cell.col, cell.color));
        }
        if (hint.deleteCandidates) {
            hint.deleteCandidates.forEach(del => {
                const cellElem = boardElement.querySelector(`[data-row='${del.row}'][data-col='${del.col}']`);
                if (cellElem) {
                    del.values.forEach(val => {
                        const candElem = cellElem.querySelector(`.candidate-num[data-cand='${val}']`);
                        if (candElem) {
                            candElem.classList.add('candidate-delete');
                        }
                    });
                }
            });
        }
        if (hint.fillCells) {
            hint.fillCells.forEach(fill => {
                const cellElem = boardElement.querySelector(`[data-row='${fill.row}'][data-col='${fill.col}']`);
                if (cellElem) {
                    const valueDiv = cellElem.querySelector('.cell-value');
                    valueDiv.textContent = fill.value;
                    valueDiv.classList.add('hint-fill');
                }
            });
        }
        
        // 自动选中提示相关的单元格
        selectHintCell(hint);
    }
    
    // 自动选中提示相关的单元格
    function selectHintCell(hint) {
        // 清除当前选择
        if (selectedCell) {
            selectedCell.cell.classList.remove('selected');
        }
        
        // 优先选择需要填入数字的单元格
        if (hint.fillCells && hint.fillCells.length > 0) {
            const fill = hint.fillCells[0]; // 选择第一个需要填入的单元格
            const cellElem = boardElement.querySelector(`[data-row='${fill.row}'][data-col='${fill.col}']`);
            if (cellElem) {
                selectedCell = { cell: cellElem, row: fill.row, col: fill.col };
                cellElem.classList.add('selected');
                return;
            }
        }
        
        // 如果没有需要填入的单元格，选择需要删除候选数的单元格
        if (hint.deleteCandidates && hint.deleteCandidates.length > 0) {
            const del = hint.deleteCandidates[0]; // 选择第一个需要删除候选数的单元格
            const cellElem = boardElement.querySelector(`[data-row='${del.row}'][data-col='${del.col}']`);
            if (cellElem) {
                selectedCell = { cell: cellElem, row: del.row, col: del.col };
                cellElem.classList.add('selected');
                return;
            }
        }
        
        // 如果都没有，选择第一个高亮的单元格
        if (hint.highlightCells && hint.highlightCells.length > 0) {
            const cell = hint.highlightCells[0];
            const cellElem = boardElement.querySelector(`[data-row='${cell.row}'][data-col='${cell.col}']`);
            if (cellElem) {
                selectedCell = { cell: cellElem, row: cell.row, col: cell.col };
                cellElem.classList.add('selected');
                return;
            }
        }
        
        // 如果都没有，不选择任何单元格
        selectedCell = null;
    }

    // 高亮一个完整的区域 (行、列或宫)
    function highlightUnit(unit) {
        if (unit.type === 'ROW') {
            for (let i = 0; i < 9; i++) {
                highlightCell(unit.row, i, 'rgba(255, 255, 0, 0.3)');
            }
        } else if (unit.type === 'COLUMN') {
            for (let i = 0; i < 9; i++) {
                highlightCell(i, unit.col, 'rgba(255, 255, 0, 0.3)');
            }
        } else if (unit.type === 'BOX') {
            const startRow = unit.row * 3;
            const startCol = unit.col * 3;
            for (let r = 0; r < 3; r++) {
                for (let c = 0; c < 3; c++) {
                    highlightCell(startRow + r, startCol + c, 'rgba(255, 255, 0, 0.3)');
                }
            }
        }
    }

    // 使用指定颜色高亮单个单元格
    function highlightCell(row, col, color) {
        const cell = boardElement.querySelector(`[data-row='${row}'][data-col='${col}']`);
        if (cell) {
            const highlightDiv = document.createElement('div');
            highlightDiv.className = 'highlight-overlay';
            highlightDiv.style.backgroundColor = color;
            cell.appendChild(highlightDiv);
        }
    }

    // 清除所有高亮效果
    function clearHighlights() {
        // 清除提示相关的高亮
        boardElement.querySelectorAll('.highlight, .highlight-red, .highlight-green, .highlight-row, .highlight-col, .highlight-box, .same-value').forEach(el => {
            el.classList.remove('highlight', 'highlight-red', 'highlight-green', 'highlight-row', 'highlight-col', 'highlight-box', 'same-value');
        });
        document.querySelectorAll('.candidate-delete').forEach(cand => cand.classList.remove('candidate-delete'));
        document.querySelectorAll('.hint-fill').forEach(val => {
            val.classList.remove('hint-fill');
            // 如果需要的话，也可以清除文本内容
            // val.textContent = '';
        });
        // 清除高亮覆盖层
        document.querySelectorAll('.highlight-overlay').forEach(overlay => overlay.remove());
    }

    async function handleSolve() {
        infoPanel.textContent = t('info_solving');
        solveButton.disabled = true;

        try {
            const response = await fetch('/api/game/solve', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(gameState)
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Solver failed: ${errorText}`);
            }

            const solvedPuzzle = await response.json();
            // 检查谜题是否真的被解决了
            const isSolved = solvedPuzzle.cells.every(cell => cell.value !== null);

            if (isSolved) {
                 // 将原始数字标记为固定，解决的数字标记为用户填入
                solvedPuzzle.cells.forEach(solvedCell => {
                    const originalCell = gameState.cells.find(c => c.row === solvedCell.row && c.col === solvedCell.col);
                    if (originalCell && originalCell.value) {
                        solvedCell.mutable = false; // 原始线索
                    } else {
                        solvedCell.mutable = true; // 解决的数字
                    }
                });
                gameState = solvedPuzzle;
                renderBoard(gameState);
                 // 解决后重新设置棋盘样式
                boardElement.querySelectorAll('.cell').forEach(cellElem => {
                    const r = parseInt(cellElem.dataset.row);
                    const c = parseInt(cellElem.dataset.col);
                    const cellData = gameState.cells.find(cell => cell.row === r && cell.col === c);
                    if (cellData) {
                        if (!cellData.mutable) {
                            cellElem.classList.remove('user-filled');
                            cellElem.classList.add('fixed');
                        }
                    }
                });

                infoPanel.textContent = t('info_solved');
            } else {
                 infoPanel.textContent = t('info_unsolvable');
            }


        } catch (error) {
            console.error('Error solving puzzle:', error);
            infoPanel.textContent = t('info_error');
        } finally {
            solveButton.disabled = false;
        }
    }

    // 事件监听器
    boardElement.addEventListener('click', handleCellClick);
    numberPanel.addEventListener('click', handleNumberClick);
    solveButton.addEventListener('click', handleSolve);
    hintButton.addEventListener('click', getHint);
    clearButton.addEventListener('click', handleClear);
    eraseButton.addEventListener('click', handleErase);
    langButton.addEventListener('click', toggleLanguage);

    // 初始设置
    createNumberPanel();
    updateUIForLanguage();
    updateCandidatesOnBoard();
}); 