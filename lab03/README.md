# Лабораторная работа 3: Интерфейсы и полиморфизм

Условие: [`assignments/lab03-polymorphism.md`](../assignments/lab03-polymorphism.md).

Из корня репозитория запустите:

```bash
./mvnw -pl lab03 test
```

Windows PowerShell: `.\\mvnw.cmd -pl lab03 test`.

Основной код размещайте в `src/main/java/edu/course/lab03/`, тесты — в `src/test/java/edu/course/lab03/`. В начале каждого Java-файла укажите `package edu.course.lab03;`.

Модуль независим от остальных лабораторных. Если нужны модели из предыдущей работы, перенесите и при необходимости адаптируйте только нужные классы в этот модуль. Начальная успешная сборка не является выполнением задания: создайте реализацию и собственные тесты по условию.

## Вариант

Программная инженерия: `PriorityPolicy`, `EffortPriorityPolicy`, `StatusPriorityPolicy`, `TaskPrioritizer`.

## Решение

- `PriceCalculator` и `TaskPrioritizer` хранят поле интерфейсного типа и получают реализацию через конструктор.
- `EffortPriorityPolicy`: приоритет равен числу часов (большую задачу лучше начать раньше).
- `StatusPriorityPolicy`: `IN_PROGRESS` = 2, `TODO` = 1, `DONE` = 0.
- `TaskPrioritizer.choose` возвращает задачу с большим приоритетом, при равенстве — первую.
- Новая политика добавляется новым классом с `implements PriorityPolicy`, `TaskPrioritizer` при этом не меняется.
