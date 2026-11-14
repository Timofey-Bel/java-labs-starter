package edu.course.lab05;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskFileLoaderTest {

    @TempDir
    Path tempDir;

    private final TaskFileLoader loader = new TaskFileLoader();

    // вспомогательный метод: создает файл с заголовком и строками
    private Path writeFile(String... rows) throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        List<String> lines = new ArrayList<>();
        lines.add(TaskFileLoader.HEADER);
        for (String row : rows) {
            lines.add(row);
        }
        Files.write(file, lines, StandardCharsets.UTF_8);
        return file;
    }

    @Test
    void loadsCorrectFile() throws IOException {
        Path file = writeFile(
                "T-101;Prepare release;backend;8;READY",
                "T-102;Fix login;frontend;3;TODO");

        LoadResult result = loader.load(file);

        assertFalse(result.hasErrors());
        assertEquals(2, result.getTasks().size());
        ProjectTask first = result.getTasks().get(0);
        assertEquals("T-101", first.getId());
        assertEquals("Prepare release", first.getTitle());
        assertEquals("backend", first.getCategory());
        assertEquals(8, first.getEstimatedHours());
        assertEquals(TaskStatus.READY, first.getStatus());
    }

    @Test
    void readsCyrillicText() throws IOException {
        Path file = writeFile("T-1;Подготовить релиз;бэкенд;5;DONE");

        LoadResult result = loader.load(file);

        assertEquals("Подготовить релиз", result.getTasks().get(0).getTitle());
        assertEquals("бэкенд", result.getTasks().get(0).getCategory());
    }

    @Test
    void emptyFieldIsError() throws IOException {
        Path file = writeFile("T-1;;backend;5;READY");

        LoadResult result = loader.load(file);

        assertEquals(0, result.getTasks().size());
        assertEquals(1, result.getErrors().size());
        ValidationError error = result.getErrors().get(0);
        assertEquals(2, error.getLineNumber());
        assertEquals("title", error.getField());
    }

    @Test
    void wrongNumberIsError() throws IOException {
        Path file = writeFile("T-1;Task;backend;eight;READY");

        LoadResult result = loader.load(file);

        assertEquals(1, result.getErrors().size());
        assertEquals("estimatedHours", result.getErrors().get(0).getField());
    }

    @Test
    void notPositiveHoursIsError() throws IOException {
        Path file = writeFile("T-1;Task;backend;0;READY");

        LoadResult result = loader.load(file);

        assertEquals(1, result.getErrors().size());
        assertEquals("estimatedHours", result.getErrors().get(0).getField());
    }

    @Test
    void unknownStatusIsError() throws IOException {
        Path file = writeFile("T-1;Task;backend;5;FINISHED");

        LoadResult result = loader.load(file);

        assertEquals(1, result.getErrors().size());
        assertEquals("status", result.getErrors().get(0).getField());
    }

    @Test
    void wrongColumnCountIsError() throws IOException {
        Path file = writeFile(
                "T-1;Task;backend;5",
                "T-2;Task;backend;5;READY;extra");

        LoadResult result = loader.load(file);

        assertEquals(0, result.getTasks().size());
        assertEquals(2, result.getErrors().size());
        assertEquals("line", result.getErrors().get(0).getField());
        assertEquals(3, result.getErrors().get(1).getLineNumber());
    }

    @Test
    void goodAndBadLinesTogether() throws IOException {
        Path file = writeFile(
                "T-1;First;backend;5;READY",   // строка 2 - ок
                "T-2;Second;backend;x;READY",  // строка 3 - ошибка
                "",                             // строка 4 - пустая, пропускаем
                "T-3;Third;frontend;2;DONE");  // строка 5 - ок

        LoadResult result = loader.load(file);

        assertEquals(2, result.getTasks().size());
        assertEquals("T-1", result.getTasks().get(0).getId());
        assertEquals("T-3", result.getTasks().get(1).getId());
        assertEquals(1, result.getErrors().size());
        assertEquals(3, result.getErrors().get(0).getLineNumber());
    }

    @Test
    void missingFileThrowsWithCause() {
        Path file = tempDir.resolve("no-such-file.txt");

        DataLoadingException e = assertThrows(DataLoadingException.class, () -> loader.load(file));

        assertInstanceOf(NoSuchFileException.class, e.getCause());
    }

    @Test
    void wrongHeaderThrows() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.write(file, List.of("id;name;hours", "T-1;Task;5"), StandardCharsets.UTF_8);

        assertThrows(DataLoadingException.class, () -> loader.load(file));
    }

    @Test
    void emptyFileThrows() throws IOException {
        Path file = tempDir.resolve("empty.txt");
        Files.write(file, List.of(), StandardCharsets.UTF_8);

        assertThrows(DataLoadingException.class, () -> loader.load(file));
    }

    @Test
    void resultListsCannotBeChangedFromOutside() throws IOException {
        Path file = writeFile("T-1;Task;backend;5;READY");
        LoadResult result = loader.load(file);

        result.getTasks().clear();

        assertEquals(1, result.getTasks().size());
    }
}
