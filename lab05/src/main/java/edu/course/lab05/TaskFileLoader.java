package edu.course.lab05;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Читает задачи из файла формата:
// id;title;category;estimatedHours;status
public class TaskFileLoader {

    public static final String HEADER = "id;title;category;estimatedHours;status";
    private static final int FIELD_COUNT = 5;

    public LoadResult load(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("Путь не может быть null");
        }

        // 1. Ошибка источника: файла нет или его нельзя прочитать
        List<String> lines;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new DataLoadingException("Cannot read " + path, e);
        }

        // 2. Заголовок проверяем отдельно
        if (lines.isEmpty()) {
            throw new DataLoadingException("File is empty: " + path);
        }
        if (!lines.get(0).trim().equals(HEADER)) {
            throw new DataLoadingException("Wrong header in " + path + ": " + lines.get(0));
        }

        // 3. Остальные строки: ошибка одной строки не останавливает чтение
        List<ProjectTask> tasks = new ArrayList<>();
        List<ValidationError> errors = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            int lineNumber = i + 1; // нумерация строк в файле с единицы

            if (line.isBlank()) {
                continue; // пустые строки пропускаем
            }

            ProjectTask task = parseLine(line, lineNumber, errors);
            if (task != null) {
                tasks.add(task);
            }
        }

        return new LoadResult(tasks, errors);
    }

    // Возвращает задачу или null, если в строке ошибка (тогда ошибка добавлена в errors).
    private ProjectTask parseLine(String line, int lineNumber, List<ValidationError> errors) {
        // -1 нужен, чтобы "a;b;" дало 3 части, а не 2
        String[] parts = line.split(";", -1);
        if (parts.length != FIELD_COUNT) {
            errors.add(new ValidationError(lineNumber, "line",
                    "Expected " + FIELD_COUNT + " fields, got " + parts.length));
            return null;
        }

        String id = parts[0].trim();
        String title = parts[1].trim();
        String category = parts[2].trim();
        String hoursText = parts[3].trim();
        String statusText = parts[4].trim();

        if (id.isEmpty()) {
            errors.add(new ValidationError(lineNumber, "id", "Empty value"));
            return null;
        }
        if (title.isEmpty()) {
            errors.add(new ValidationError(lineNumber, "title", "Empty value"));
            return null;
        }
        if (category.isEmpty()) {
            errors.add(new ValidationError(lineNumber, "category", "Empty value"));
            return null;
        }
        if (hoursText.isEmpty()) {
            errors.add(new ValidationError(lineNumber, "estimatedHours", "Empty value"));
            return null;
        }
        if (statusText.isEmpty()) {
            errors.add(new ValidationError(lineNumber, "status", "Empty value"));
            return null;
        }

        int hours;
        try {
            hours = Integer.parseInt(hoursText);
        } catch (NumberFormatException e) {
            errors.add(new ValidationError(lineNumber, "estimatedHours", "Not a number: " + hoursText));
            return null;
        }
        if (hours <= 0) {
            errors.add(new ValidationError(lineNumber, "estimatedHours", "Must be positive: " + hours));
            return null;
        }

        TaskStatus status;
        try {
            status = TaskStatus.valueOf(statusText);
        } catch (IllegalArgumentException e) {
            errors.add(new ValidationError(lineNumber, "status", "Unknown status: " + statusText));
            return null;
        }

        // все предметные проверки уже пройдены, конструктор не бросит исключение
        return new ProjectTask(id, title, category, hours, status);
    }
}
