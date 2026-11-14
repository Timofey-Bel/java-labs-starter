package edu.course.lab05;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Подготовительная часть: чтение временного файла с числами.
class NumbersFileTest {

    @TempDir
    Path tempDir;

    private Path createFile() throws IOException {
        Path file = tempDir.resolve("numbers.txt");
        Files.write(file, List.of("10", "bad", "20"), StandardCharsets.UTF_8);
        return file;
    }

    @Test
    void readsAllLines() throws IOException {
        Path file = createFile();
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        assertEquals(List.of("10", "bad", "20"), lines);
    }

    @Test
    void parsesGoodNumbers() throws IOException {
        Path file = createFile();
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);

        List<Integer> numbers = new ArrayList<>();
        for (String line : lines) {
            if (!line.equals("bad")) {
                numbers.add(Integer.parseInt(line));
            }
        }
        assertEquals(List.of(10, 20), numbers);
    }

    @Test
    void badLineThrowsNumberFormatException() throws IOException {
        Path file = createFile();
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);

        assertThrows(NumberFormatException.class, () -> {
            List<Integer> numbers = new ArrayList<>();
            for (String line : lines) {
                numbers.add(Integer.parseInt(line));
            }
        });
    }
}
