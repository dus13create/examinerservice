-- Шаг 3. JOIN-запросы

-- Запрос 1: Все студенты с названиями факультетов
SELECT s.name, s.age, f.name AS faculty_name
FROM students s
LEFT JOIN faculties f ON s.faculty_id = f.id;

-- Запрос 2: Студенты, у которых есть аватарки
SELECT s.name, s.age
FROM students s
INNER JOIN avatars a ON s.id = a.student_id;
