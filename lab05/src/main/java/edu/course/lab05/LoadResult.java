package edu.course.lab05;

import java.util.ArrayList;
import java.util.List;

// Результат загрузки: корректные задачи и ошибки отдельными списками.
public final class LoadResult {

    private final List<ProjectTask> tasks;
    private final List<ValidationError> errors;

    public LoadResult(List<ProjectTask> tasks, List<ValidationError> errors) {
        // копируем списки, чтобы снаружи их нельзя было поменять
        this.tasks = new ArrayList<>(tasks);
        this.errors = new ArrayList<>(errors);
    }

    public List<ProjectTask> getTasks() {
        return new ArrayList<>(tasks);
    }

    public List<ValidationError> getErrors() {
        return new ArrayList<>(errors);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
