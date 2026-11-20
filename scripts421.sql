-- Шаг 1. Ограничения для таблиц Student и Faculty

-- 1. Возраст студента не может быть меньше 16 лет
ALTER TABLE students
    ADD CONSTRAINT chk_student_age_min CHECK (age >= 16);

-- 2. Имена студентов должны быть уникальными и не равны нулю
ALTER TABLE students
    ALTER COLUMN name SET NOT NULL;

ALTER TABLE students
    ADD CONSTRAINT uq_student_name UNIQUE (name);

-- 3. Пара "название" - "цвет факультета" должна быть уникальной
ALTER TABLE faculties
    ADD CONSTRAINT uq_faculty_name_color UNIQUE (name, color);

-- 4. При создании студента без возраста ему автоматически присваивается 20 лет
ALTER TABLE students
    ALTER COLUMN age SET DEFAULT 20;
