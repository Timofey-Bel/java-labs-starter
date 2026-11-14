package edu.course.lab04;

import java.util.HashMap;

// Реестр задач. Внутри обычный HashMap (композиция),
// снаружи доступны только нужные операции.
public class IssueRegistry {

    private final HashMap<IssueId, ProjectTask> tasks = new HashMap<>();

    // Добавляет задачу или заменяет старую с таким же ключом.
    public void put(IssueId id, ProjectTask task) {
        if (id == null) {
            throw new IllegalArgumentException("Ключ не может быть null");
        }
        if (task == null) {
            throw new IllegalArgumentException("Задача не может быть null");
        }
        tasks.put(id, task);
    }

    // Возвращает задачу или null, если такого ключа нет.
    public ProjectTask get(IssueId id) {
        return tasks.get(id);
    }

    // Удаляет задачу и возвращает ее (или null, если ключа не было).
    public ProjectTask remove(IssueId id) {
        return tasks.remove(id);
    }

    public boolean contains(IssueId id) {
        return tasks.containsKey(id);
    }

    public int size() {
        return tasks.size();
    }
}
