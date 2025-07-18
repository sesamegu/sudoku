document.addEventListener('DOMContentLoaded', () => {
    const boardElement = document.getElementById('sudoku-board');
    const numberPanel = document.getElementById('number-panel');
    const newGameButton = document.getElementById('new-game-button');
    const levelSelectButton = document.getElementById('level-select-button');
    const eraseButton = document.getElementById('erase-button');
    const candidateButton = document.getElementById('candidate-button');
    const notesToggleButton = document.getElementById('notes-toggle-button');
    const hintButton = document.getElementById('hint-button');
    const langButton = document.getElementById('lang-button');
    const infoPanel = document.getElementById('info-panel');

    // 模态框元素
    const levelModal = document.getElementById('level-modal');
    const closeModalButton = document.querySelector('.close-button');
    const levelsContainer = document.getElementById('levels-container');


    let gameState = null;
    let selectedCell = null;
    let isCandidateMode = false;
    let areNotesVisible = true;
    let currentLang = 'zh_CN';

    const translations = {
        'en_US': {
            'page_title': 'Sudoku',
            'game_title': 'Sudoku Game',
            'go_to_solver': 'Go to Solver',
            'select_puzzle': 'Select a Puzzle',
            'new_game': 'New Game',
            'easy': 'Easy',
            'normal': 'Normal',
            'hard': 'Hard',
            'vip': 'VIP',
            'erase': 'Erase',
            'candidate_on': 'Exit Candidate Mode',
            'candidate_off': 'Enter Candidate Mode',
            'notes_on': 'Notes OFF',
            'notes_off': 'Notes ON',
            'hint': 'Hint',
            'lang_button': '中文',
            'loading': 'Loading a new game...',
            'new_game_started': 'New game started. Select a cell.',
            'error_loading': 'Error loading game. Please try again.',
            'update_failed': 'Failed to update. Please check connection.',
            'get_hint_error': 'Error getting hint.',
            'no_hint_found': 'No hint found.',
            'start_game_first': 'Start a new game first.',
            'select_cell_or_hint': 'Select a cell or get a hint.',
            'level_select': 'Select Level'
        },
        'zh_CN': {
            'page_title': '数独',
            'game_title': '数独游戏',
            'go_to_solver': '前往数独破解器',
            'select_puzzle': '选择谜题',
            'new_game': '新游戏',
            'easy': '简单',
            'normal': '普通',
            'hard': '困难',
            'vip': '专家',
            'erase': '删除',
            'candidate_on': '退出笔记模式',
            'candidate_off': '进入笔记模式',
            'notes_on': '笔记 关',
            'notes_off': '笔记 开',
            'hint': '提示',
            'lang_button': 'English',
            'loading': '正在加载新游戏...',
            'new_game_started': '新游戏开始，请选择一个单元格。',
            'error_loading': '加载游戏出错，请重试。',
            'update_failed': '更新失败，请检查网络连接。',
            'get_hint_error': '获取提示时出错。',
            'no_hint_found': '未找到提示。',
            'start_game_first': '请先开始一个新游戏。',
            'select_cell_or_hint': '请选择一个单元格或获取提示。',
            'level_select': '选择关卡'
        }
    };

    /**
     * I18n翻译辅助函数
     * @param {string} key - 要翻译的键
     * @returns {string} 翻译后的文本
     */
    function t(key) {
        return translations[currentLang][key];
    }

    /**
     * 根据当前语言更新所有UI元素的文本内容。
     */
    function updateUIForLanguage() {
        // 更新页面标题和主要文案
        document.title = t('page_title');
        document.querySelector('h1').textContent = t('game_title');
        document.querySelector('.solver-link').textContent = t('go_to_solver');
        document.querySelector('#level-modal h2').textContent = t('select_puzzle');
        
        // 更新按钮文字
        newGameButton.textContent = t('new_game');
        levelSelectButton.textContent = t('level_select');
        eraseButton.textContent = t('erase');
        candidateButton.textContent = isCandidateMode ? t('candidate_on') : t('candidate_off');
        notesToggleButton.textContent = areNotesVisible ? t('notes_on') : t('notes_off');
        hintButton.textContent = t('hint');
        langButton.textContent = t('lang_button');
        infoPanel.textContent = t('select_cell_or_hint');
        
        // 同时更新笔记可见性状态
        updateNotesButtonState();
    }

    /**
     * 切换当前语言 (中文/英文)。
     */
    function toggleLanguage() {
        currentLang = (currentLang === 'en_US') ? 'zh_CN' : 'en_US';
        updateUIForLanguage();
        loadLevels(); // 重新加载关卡以更新标题
    }

    /**
     * 切换全局笔记（候选数）的可见性。
     */
    function toggleNotesVisibility() {
        areNotesVisible = !areNotesVisible;
        updateNotesButtonState();
    }

    /**
     * 根据笔记是否可见，更新棋盘样式和“候选模式”按钮的可用状态。
     */
    function updateNotesButtonState() {
        const gameContainer = document.querySelector('.game-container');
        if (areNotesVisible) {
            gameContainer.classList.remove('notes-hidden');
            candidateButton.disabled = false;
        } else {
            gameContainer.classList.add('notes-hidden');
            candidateButton.disabled = true;
        }
        notesToggleButton.textContent = areNotesVisible ? t('notes_on') : t('notes_off');
    }

    // --- 事件监听器 ---
    newGameButton.addEventListener('click', () => initGame());
    levelSelectButton.addEventListener('click', () => levelModal.style.display = 'block');
    closeModalButton.addEventListener('click', () => levelModal.style.display = 'none');
    window.addEventListener('click', (event) => {
        if (event.target == levelModal) {
            levelModal.style.display = 'none';
        }
    });

    eraseButton.addEventListener('click', handleErase);
    candidateButton.addEventListener('click', toggleCandidateMode);
    notesToggleButton.addEventListener('click', toggleNotesVisibility);
    hintButton.addEventListener('click', getHint);
    langButton.addEventListener('click', toggleLanguage);
    boardElement.addEventListener('click', handleCellClick);
    numberPanel.addEventListener('click', handleNumberClick);

    // --- 初始化 ---
    /**
     * 初始化一个新游戏。
     * 可以加载一个指定的关卡，或者一个随机生成的谜题。
     * @param {string} [level] - 游戏难度 (可选)
     * @param {number} [number] - 关卡编号 (可选)
     */
    async function initGame(level, number) {
        infoPanel.textContent = t('loading');
        let url = '/api/game/new';
        if (level && number) {
            url += `?level=${level.toUpperCase()}&number=${number}`;
        }

        try {
            const response = await fetch(url);
            if (!response.ok) {
                throw new Error('Failed to start a new game.');
            }
            gameState = await response.json();
            renderBoard(gameState);
            infoPanel.textContent = t('new_game_started');
            // 新游戏时重置笔记可见性为默认状态
            if (!areNotesVisible) {
                toggleNotesVisibility();
            } else {
                updateNotesButtonState();
            }
        } catch (error) {
            console.error(error);
            infoPanel.textContent = t('error_loading');
        }
    }

    /**
     * 从后端加载所有关卡信息并渲染到模态框中。
     */
    async function loadLevels() {
        try {
            const response = await fetch('/api/levels');
            const levels = await response.json();
            levelsContainer.innerHTML = ''; // 清除之前的内容

            levels.forEach(level => {
                const categoryDiv = document.createElement('div');
                categoryDiv.className = 'level-category';

                const title = document.createElement('h3');
                title.textContent = t(level.level.toLowerCase()); // 使用翻译
                categoryDiv.appendChild(title);

                const buttonsDiv = document.createElement('div');
                buttonsDiv.className = 'puzzle-buttons';

                for (let i = 1; i <= level.count; i++) {
                    const button = document.createElement('button');
                    button.textContent = i;
                    button.addEventListener('click', () => {
                        initGame(level.level, i);
                        levelModal.style.display = 'none'; // 选择后关闭模态框
                    });
                    buttonsDiv.appendChild(button);
                }
                categoryDiv.appendChild(buttonsDiv);
                levelsContainer.appendChild(categoryDiv);
            });
        } catch (error) {
            console.error('Failed to load levels:', error);
            levelsContainer.innerHTML = 'Error loading levels. Please try again later.';
        }
    }

    // --- 渲染 ---
    /**
     * 根据给定的游戏状态渲染整个数独棋盘。
     * @param {object} puzzle - 包含所有单元格数据的谜题对象。
     */
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
                cellElement.classList.add(cellData.mutable ? 'user-filled' : 'fixed');
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
        createNumberPanel();
    }


    /**
     * 创建右侧的数字面板 (1-9)。
     */
    function createNumberPanel() {
        numberPanel.innerHTML = '';
        for (let i = 1; i <= 9; i++) {
            const btn = document.createElement('button');
            btn.textContent = i;
            btn.dataset.number = i;
            numberPanel.appendChild(btn);
        }
    }

    // --- 用户操作 ---
    /**
     * 处理用户在棋盘上点击单元格的事件。
     * @param {Event} event - 点击事件对象。
     */
    function handleCellClick(event) {
        clearHighlights();
        const target = event.target.closest('.cell');
        if (!target) return;

        const row = parseInt(target.dataset.row);
        const col = parseInt(target.dataset.col);

        // 如果点击相同单元格，则取消选择
        if (selectedCell && selectedCell.row === row && selectedCell.col === col) {
            clearSelection();
        } else {
            // 在选择新单元格之前清除之前的选择
            if (selectedCell && selectedCell.cell) {
                selectedCell.cell.classList.remove('selected');
                document.querySelectorAll('.cell.peer').forEach(c => c.classList.remove('peer'));
            }
            selectedCell = { cell: target, row, col };
            target.classList.add('selected');
            highlightPeers(row, col);
            infoPanel.textContent = `Selected cell (${row + 1}, ${col + 1}). Enter a number.`;
        }
    }

    /**
     * 处理用户点击右侧数字面板的事件。
     * @param {Event} event - 点击事件对象。
     */
    function handleNumberClick(event) {
        const number = event.target.dataset.number;
        if (!number || !selectedCell) {
            infoPanel.textContent = 'Please select a cell first.';
            return;
        }

        if (isCandidateMode) {
            updateCandidate(selectedCell.row, selectedCell.col, number);
        } else {
            fillNumber(selectedCell.row, selectedCell.col, number);
        }
    }

    /**
     * 处理用户点击“删除”按钮的事件。
     */
    function handleErase() {
        if (!selectedCell) {
            infoPanel.textContent = 'Please select a cell to erase.';
            return;
        }
        eraseNumber(selectedCell.row, selectedCell.col);
    }

    /**
     * 切换“笔记模式”的开/关状态。
     */
    function toggleCandidateMode() {
        isCandidateMode = !isCandidateMode;
        candidateButton.textContent = isCandidateMode ? t('candidate_on') : t('candidate_off');
        candidateButton.classList.toggle('active', isCandidateMode);
        if (isCandidateMode) {
            infoPanel.textContent = 'Candidate mode is ON. Click numbers to add/remove notes.';
        } else {
            infoPanel.textContent = 'Candidate mode is OFF. Click numbers to fill cells.';
        }
    }


    // --- API Communication ---
    /**
     * 向后端发送请求，在指定单元格填入数字。
     * @param {number} row - 单元格的行索引。
     * @param {number} col - 单元格的列索引。
     * @param {string} value - 要填入的数字。
     */
    async function fillNumber(row, col, value) {
        const body = { puzzle: gameState, row, col, value };
        sendUpdateRequest('/api/game/fill', body);
    }

    /**
     * 向后端发送请求，删除指定单元格的数字。
     * @param {number} row - 单元格的行索引。
     * @param {number} col - 单元格的列索引。
     */
    async function eraseNumber(row, col) {
        const body = { puzzle: gameState, row, col };
        sendUpdateRequest('/api/game/erase', body);
    }

    /**
     * 向后端发送请求，更新指定单元格的候选数。
     * @param {number} row - 单元格的行索引。
     * @param {number} col - 单元格的列索引。
     * @param {string} value - 要添加/删除的候选数。
     */
    async function updateCandidate(row, col, value) {
        const body = { puzzle: gameState, row, col, value };
        sendUpdateRequest('/api/game/candidate', body);
    }

    /**
     * 发送一个通用的更新请求 (POST) 到后端。
     * @param {string} url - 请求的URL。
     * @param {object} body - 请求体。
     */
    async function sendUpdateRequest(url, body) {
        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(body)
            });
            if (!response.ok) {
                throw new Error('Update failed.');
            }
            gameState = await response.json();
            renderBoard(gameState);

            // 重新渲染后，重新选择单元格
            if (selectedCell) {
                const reselected = boardElement.querySelector(`[data-row='${selectedCell.row}'][data-col='${selectedCell.col}']`);
                if (reselected) {
                    selectedCell.cell = reselected;
                    reselected.classList.add('selected');
                    highlightPeers(selectedCell.row, selectedCell.col);
                }
            }

        } catch (error) {
            console.error(error);
            infoPanel.textContent = t('update_failed');
        }
    }


    /**
     * 从后端获取一个解题提示。
     */
    async function getHint() {
        if (!gameState) {
            infoPanel.textContent = t('start_game_first');
            return;
        }
        clearSelection();
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
            infoPanel.textContent = t('get_hint_error');
        }
    }

    /**
     * 应用从后端获取到的提示，高亮相关的单元格和区域。
     * @param {object} hint - 包含提示信息的对象。
     */
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
    
    /**
     * 自动选中提示相关的单元格。
     * @param {object} hint - 包含提示信息的对象。
     */
    function selectHintCell(hint) {
        // 清除当前选择
        clearSelection();
        
        // 优先选择需要填入数字的单元格
        if (hint.fillCells && hint.fillCells.length > 0) {
            const fill = hint.fillCells[0]; // 选择第一个需要填入的单元格
            const cellElem = boardElement.querySelector(`[data-row='${fill.row}'][data-col='${fill.col}']`);
            if (cellElem) {
                selectedCell = { cell: cellElem, row: fill.row, col: fill.col };
                cellElem.classList.add('selected');
                highlightPeers(fill.row, fill.col);
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
                highlightPeers(del.row, del.col);
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
                highlightPeers(cell.row, cell.col);
                return;
            }
        }
        
        // 如果都没有，不选择任何单元格
        selectedCell = null;
    }


    // --- Highlighting and Selection ---
    /**
     * 高亮与指定单元格同处一行、一列和一宫的单元格。
     * @param {number} row - 单元格的行索引。
     * @param {number} col - 单元格的列索引。
     */
    function highlightPeers(row, col) {
        // 高亮行和列
        for (let i = 0; i < 9; i++) {
            boardElement.querySelector(`[data-row='${row}'][data-col='${i}']`).classList.add('peer');
            boardElement.querySelector(`[data-row='${i}'][data-col='${col}']`).classList.add('peer');
        }
        // 高亮宫
        const startRow = Math.floor(row / 3) * 3;
        const startCol = Math.floor(col / 3) * 3;
        for (let r = 0; r < 3; r++) {
            for (let c = 0; c < 3; c++) {
                boardElement.querySelector(`[data-row='${startRow + r}'][data-col='${startCol + c}']`).classList.add('peer');
            }
        }
    }

    /**
     * 根据提示高亮一个完整的区域 (行、列或宫)。
     * @param {object} unit - 包含区域类型和索引的对象。
     */
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

    /**
     * 使用指定颜色高亮单个单元格。
     * @param {number} row - 单元格的行索引。
     * @param {number} col - 单元格的列索引。
     * @param {string} color - CSS 颜色值。
     */
    function highlightCell(row, col, color) {
        const cell = boardElement.querySelector(`[data-row='${row}'][data-col='${col}']`);
        if (cell) {
            const highlightDiv = document.createElement('div');
            highlightDiv.className = 'highlight-overlay';
            highlightDiv.style.backgroundColor = color;
            cell.appendChild(highlightDiv);
        }
    }


    /**
     * 清除所有单元格的“选中”状态。
     */
    function clearSelection() {
        if (selectedCell && selectedCell.cell) {
            selectedCell.cell.classList.remove('selected');
        }
        selectedCell = null;
        document.querySelectorAll('.cell.peer').forEach(c => c.classList.remove('peer'));
    }

    /**
     * 清除所有单元格上的高亮效果。
     */
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

    // --- 游戏逻辑 ---
    initGame();
    createNumberPanel();
    loadLevels();
    updateUIForLanguage();

}); 