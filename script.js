const taskInput = document.getElementById('taskInput');
const addBtn = document.getElementById('addBtn');
const tasksList = document.getElementById('tasksList');
const emptyState = document.getElementById('emptyState');
const scoreEl = document.getElementById('score');
const levelEl = document.getElementById('level');
const progressEl = document.getElementById('progress');
const progressTextEl = document.getElementById('progressText');

// سطوح: [امتیاز مورد نیاز، نام سطح]
const LEVELS = [
  { min: 0, name: 'تازه‌کار 🌱' },
  { min: 50, name: 'مهربان 🌿' },
  { min: 120, name: 'خوب‌قلب 🌳' },
  { min: 250, name: 'قهرمان 🌟' },
  { min: 500, name: 'افسانه‌ای ✨' },
  { min: 1000, name: 'فرشته 👼' },
];

let tasks = JSON.parse(localStorage.getItem('goodDeeds')) || [];

// رندر اولیه
renderTasks();
updateScore();

// افزودن کار جدید
addBtn.addEventListener('click', addTask);
taskInput.addEventListener('keydown', (e) => {
  if (e.key === 'Enter') addTask();
});

function addTask() {
  const text = taskInput.value.trim();
  if (!text) {
    taskInput.focus();
    return;
  }

  const points = Math.floor(Math.random() * 3) + 3; // بین 3 تا 5 امتیاز
  const task = {
    id: Date.now(),
    text: text,
    points: points,
    done: false,
  };

  tasks.unshift(task);
  save();
  renderTasks();
  updateScore();

  taskInput.value = '';
  taskInput.focus();
}

function renderTasks() {
  tasksList.innerHTML = '';

  if (tasks.length === 0) {
    emptyState.classList.remove('hidden');
    return;
  }
  emptyState.classList.add('hidden');

  tasks.forEach((task) => {
    const card = document.createElement('div');
    card.className = 'task-card' + (task.done ? ' done' : '');
    card.dataset.id = task.id;

    card.innerHTML = `
      <button class="task-check ${task.done ? 'done' : ''}"></button>
      <span class="task-text">${escapeHtml(task.text)}</span>
      <span class="task-points">+${toFa(task.points)}</span>
      <button class="task-delete">✕</button>
    `;

    card.querySelector('.task-check').addEventListener('click', () => toggleTask(task.id));
    card.querySelector('.task-delete').addEventListener('click', () => deleteTask(task.id, card));

    tasksList.appendChild(card);
  });
}

function toggleTask(id) {
  const task = tasks.find((t) => t.id === id);
  if (!task) return;
  task.done = !task.done;
  save();
  renderTasks();
  updateScore();
}

function deleteTask(id, card) {
  card.classList.add('removing');
  setTimeout(() => {
    tasks = tasks.filter((t) => t.id !== id);
    save();
    renderTasks();
    updateScore();
  }, 300);
}

function updateScore() {
  const total = tasks.filter((t) => t.done).reduce((sum, t) => sum + t.points, 0);

  scoreEl.textContent = toFa(total);
  scoreEl.classList.add('pop');
  setTimeout(() => scoreEl.classList.remove('pop'), 300);

  // سطح فعلی و بعدی
  let currentLevel = LEVELS[0];
  let nextLevel = LEVELS[1];
  for (let i = 0; i < LEVELS.length; i++) {
    if (total >= LEVELS[i].min) {
      currentLevel = LEVELS[i];
      nextLevel = LEVELS[i + 1] || null;
    }
  }

  levelEl.textContent = 'سطح: ' + currentLevel.name;

  if (nextLevel) {
    const range = nextLevel.min - currentLevel.min;
    const progress = ((total - currentLevel.min) / range) * 100;
    progressEl.style.width = Math.min(progress, 100) + '%';
    progressTextEl.textContent = `${toFa(total)} / ${toFa(nextLevel.min)} امتیاز تا سطح بعدی`;
  } else {
    progressEl.style.width = '100%';
    progressTextEl.textContent = 'به بالاترین سطح رسیدی! 🎉';
  }
}

function save() {
  localStorage.setItem('goodDeeds', JSON.stringify(tasks));
}

// تبدیل اعداد انگلیسی به فارسی
function toFa(num) {
  const fa = ['۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹'];
  return String(num).replace(/\d/g, (d) => fa[d]);
}

// جلوگیری از XSS
function escapeHtml(str) {
  const div = document.createElement('div');
  div.textContent = str;
  return div.innerHTML;
}