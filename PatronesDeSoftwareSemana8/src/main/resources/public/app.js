/**
 * Tower Defense — Frontend Application
 * Connects to the Spark Java backend via fetch API.
 * All UI text is in Spanish.
 */

const API_BASE = '';

// State
let selectedUpgrades = [];
let currentTower = null;

// ===========================
// DOM Elements
// ===========================

const elements = {
    // Base tower stats
    baseDamage: document.getElementById('base-damage'),
    baseRange: document.getElementById('base-range'),
    baseSpeed: document.getElementById('base-speed'),
    baseCost: document.getElementById('base-cost'),

    // Upgrades
    upgradesGrid: document.getElementById('upgrades-grid'),

    // Build
    selectedUpgrades: document.getElementById('selected-upgrades'),
    btnBuild: document.getElementById('btn-build'),
    btnClear: document.getElementById('btn-clear'),

    // Final stats
    finalStatsSection: document.getElementById('final-stats-section'),
    towerDescription: document.getElementById('tower-description'),
    finalDamage: document.getElementById('final-damage'),
    finalRange: document.getElementById('final-range'),
    finalSpeed: document.getElementById('final-speed'),
    finalCost: document.getElementById('final-cost'),
    effectsContainer: document.getElementById('effects-container'),

    // Wave
    waveSection: document.getElementById('wave-section'),
    waveNumber: document.getElementById('wave-number'),
    btnSimulate: document.getElementById('btn-simulate'),

    // Result
    resultSection: document.getElementById('result-section'),
    resultBadge: document.getElementById('result-badge'),
    resultVictory: document.getElementById('result-victory'),
    resultEnemies: document.getElementById('result-enemies'),
    resultDamage: document.getElementById('result-damage'),
    resultGold: document.getElementById('result-gold'),
    resultTicks: document.getElementById('result-ticks'),

    // Log
    logSection: document.getElementById('log-section'),
    battleLog: document.getElementById('battle-log'),
};

// ===========================
// API Functions
// ===========================

async function fetchJSON(url, options = {}) {
    try {
        const response = await fetch(API_BASE + url, {
            headers: { 'Content-Type': 'application/json' },
            ...options,
        });
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.error || `HTTP ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

async function loadBaseTower() {
    const data = await fetchJSON('/api/tower/base');
    elements.baseDamage.textContent = data.damage;
    elements.baseRange.textContent = data.range.toFixed(1);
    elements.baseSpeed.textContent = data.attackSpeed.toFixed(2);
    elements.baseCost.textContent = data.cost + ' 🪙';
}

async function loadUpgrades() {
    const upgrades = await fetchJSON('/api/upgrades');
    elements.upgradesGrid.innerHTML = '';

    upgrades.forEach(upgrade => {
        const card = document.createElement('div');
        card.className = 'upgrade-card';
        card.dataset.id = upgrade.id;
        card.innerHTML = `
            <span class="upgrade-name">${getUpgradeEmoji(upgrade.category)} ${upgrade.name}</span>
            <span class="upgrade-desc">${translateDescription(upgrade.description)}</span>
            <div class="upgrade-meta">
                <span class="upgrade-cost">🪙 ${upgrade.cost}</span>
                <span class="upgrade-category ${upgrade.category}">${translateCategory(upgrade.category)}</span>
            </div>
        `;
        card.addEventListener('click', () => addUpgrade(upgrade));
        elements.upgradesGrid.appendChild(card);
    });
}

async function buildTower() {
    if (selectedUpgrades.length === 0) return;

    const upgradeIds = selectedUpgrades.map(u => u.id);
    const data = await fetchJSON('/api/tower/build', {
        method: 'POST',
        body: JSON.stringify({ upgrades: upgradeIds }),
    });

    currentTower = data;
    displayFinalStats(data);
}

async function simulateWave() {
    if (!currentTower) return;

    const waveNum = parseInt(elements.waveNumber.value);
    const upgradeIds = selectedUpgrades.map(u => u.id);

    elements.btnSimulate.disabled = true;
    elements.btnSimulate.textContent = '⏳ Simulando...';

    try {
        const data = await fetchJSON('/api/wave/simulate', {
            method: 'POST',
            body: JSON.stringify({ upgrades: upgradeIds, waveNumber: waveNum }),
        });

        displayResults(data);
        displayBattleLog(data.battleLog);
    } finally {
        elements.btnSimulate.disabled = false;
        elements.btnSimulate.textContent = '⚔️ Simular Combate';
    }
}

// ===========================
// UI Functions
// ===========================

function addUpgrade(upgrade) {
    selectedUpgrades.push(upgrade);
    renderSelectedUpgrades();
}

function removeUpgrade(index) {
    selectedUpgrades.splice(index, 1);
    renderSelectedUpgrades();
    // Hide final stats if upgrades change after build
    elements.finalStatsSection.classList.add('hidden');
    elements.waveSection.classList.add('hidden');
    elements.resultSection.classList.add('hidden');
    elements.logSection.classList.add('hidden');
    currentTower = null;
}

function clearUpgrades() {
    selectedUpgrades = [];
    renderSelectedUpgrades();
    elements.finalStatsSection.classList.add('hidden');
    elements.waveSection.classList.add('hidden');
    elements.resultSection.classList.add('hidden');
    elements.logSection.classList.add('hidden');
    currentTower = null;
}

function renderSelectedUpgrades() {
    elements.selectedUpgrades.innerHTML = '';

    if (selectedUpgrades.length === 0) {
        elements.selectedUpgrades.innerHTML = '<p class="empty-message">Selecciona mejoras para construir tu torre</p>';
        elements.btnBuild.disabled = true;
        return;
    }

    selectedUpgrades.forEach((upgrade, index) => {
        const tag = document.createElement('span');
        tag.className = 'selected-tag';
        tag.innerHTML = `
            ${getUpgradeEmoji(upgrade.category)} ${upgrade.name}
            <span class="remove-tag" data-index="${index}">&times;</span>
        `;
        tag.querySelector('.remove-tag').addEventListener('click', (e) => {
            e.stopPropagation();
            removeUpgrade(index);
        });
        elements.selectedUpgrades.appendChild(tag);
    });

    elements.btnBuild.disabled = false;
}

function displayFinalStats(tower) {
    elements.finalStatsSection.classList.remove('hidden');
    elements.waveSection.classList.remove('hidden');

    elements.towerDescription.textContent = tower.description;
    elements.finalDamage.textContent = tower.damage;
    elements.finalRange.textContent = tower.range.toFixed(1);
    elements.finalSpeed.textContent = tower.attackSpeed.toFixed(2);
    elements.finalCost.textContent = tower.cost + ' 🪙';

    // Special effects
    elements.effectsContainer.innerHTML = '';
    if (tower.specialEffects && tower.specialEffects.length > 0) {
        tower.specialEffects.forEach(effect => {
            const tag = document.createElement('span');
            tag.className = 'effect-tag';
            tag.textContent = translateEffect(effect);
            elements.effectsContainer.appendChild(tag);
        });
    }
}

function displayResults(data) {
    elements.resultSection.classList.remove('hidden');

    // Victory/defeat badge
    if (data.victory) {
        elements.resultBadge.textContent = '¡VICTORIA!';
        elements.resultBadge.className = 'badge badge-victory';
        elements.resultVictory.textContent = '¡Victoria! 🎉';
        elements.resultVictory.style.color = 'var(--accent-success)';
    } else {
        elements.resultBadge.textContent = 'DERROTA';
        elements.resultBadge.className = 'badge badge-defeat';
        elements.resultVictory.textContent = 'Derrota 💀';
        elements.resultVictory.style.color = 'var(--accent-danger)';
    }

    elements.resultEnemies.textContent = data.enemiesDefeated + ' / ' + data.totalEnemies;
    elements.resultDamage.textContent = data.totalDamageDealt.toLocaleString();
    elements.resultGold.textContent = data.goldEarned.toLocaleString() + ' 🪙';
    elements.resultTicks.textContent = data.totalTicks;
}

function displayBattleLog(log) {
    elements.logSection.classList.remove('hidden');
    elements.battleLog.innerHTML = '';

    log.forEach(entry => {
        const div = document.createElement('div');
        div.className = 'log-entry';

        // Classify log entry for styling
        if (entry.startsWith('===')) {
            div.classList.add('log-header');
        } else if (entry.includes('defeated!') || entry.includes('KILLED!')) {
            div.classList.add('log-kill');
        } else if (entry.includes('CRITICAL')) {
            div.classList.add('log-critical');
        } else if (entry.includes('FREEZE')) {
            div.classList.add('log-freeze');
        } else if (entry.includes('DEFEAT') || entry.includes('reached the tower')) {
            div.classList.add('log-defeat');
        } else if (entry.includes('VICTORY')) {
            div.classList.add('log-victory');
        }

        div.textContent = entry;
        elements.battleLog.appendChild(div);
    });

    // Scroll to bottom
    elements.battleLog.scrollTop = elements.battleLog.scrollHeight;
}

// ===========================
// Translation Helpers
// ===========================

function translateDescription(desc) {
    const translations = {
        'Increases attack speed by 50%': 'Aumenta la velocidad de ataque en un 50%',
        'Slows enemies by 50% for 2 ticks after each attack': 'Ralentiza enemigos un 50% por 2 turnos tras cada ataque',
        'Reinforces the tower, adding +3 damage per hit': 'Refuerza la torre, añadiendo +3 de daño por golpe',
        'Generates +5 gold per attack, +50% enemy reward on kill': 'Genera +5 de oro por ataque, +50% de recompensa al eliminar',
        'Increases attack range by 40%': 'Aumenta el alcance de ataque en un 40%',
        '25% chance for critical hit dealing double damage': '25% de probabilidad de golpe crítico con daño doble',
    };
    return translations[desc] || desc;
}

function translateCategory(cat) {
    const translations = {
        'offense': 'Ofensivo',
        'defense': 'Defensivo',
        'control': 'Control',
        'utility': 'Utilidad',
    };
    return translations[cat] || cat;
}

function translateEffect(effect) {
    const translations = {
        'Freeze': '❄️ Congelación',
        'Shield': '🛡️ Escudo',
        'Gold Gen': '🪙 Gen. Oro',
        'Critical': '💥 Crítico',
    };
    return translations[effect] || effect;
}

function getUpgradeEmoji(category) {
    const emojis = {
        'offense': '⚔️',
        'defense': '🛡️',
        'control': '❄️',
        'utility': '✨',
    };
    return emojis[category] || '🔧';
}

// ===========================
// Event Listeners
// ===========================

elements.btnBuild.addEventListener('click', buildTower);
elements.btnClear.addEventListener('click', clearUpgrades);
elements.btnSimulate.addEventListener('click', simulateWave);

// ===========================
// Initialization
// ===========================

async function init() {
    try {
        await Promise.all([loadBaseTower(), loadUpgrades()]);
    } catch (error) {
        console.error('Failed to initialize:', error);
    }
}

init();
