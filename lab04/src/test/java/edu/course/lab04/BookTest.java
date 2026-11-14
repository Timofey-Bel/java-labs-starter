package edu.course.lab04;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookTest {

    @Test
    void valueIsFoundByAnotherBookWithSameIsbn() {
        Book first = new Book("978-5-00-000000-1", "Java");
        Book second = new Book("978-5-00-000000-1", "Java, 2-е издание");

        HashMap<Book, String> shelf = new HashMap<>();
        shelf.put(first, "полка 3");

        assertNotSame(first, second); // это разные объекты
        assertEquals("полка 3", shelf.get(second)); // но ключ находится
    }

    @Test
    void booksWithDifferentIsbnAreNotEqual() {
        Book first = new Book("111", "Java");
        Book second = new Book("222", "Java");
        assertNotEquals(first, second);
    }

    @Test
    void equalBooksHaveEqualHashCodes() {
        Book first = new Book("111", "Одна");
        Book second = new Book("111", "Другая");
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void emptyFieldsThrow() {
        assertThrows(IllegalArgumentException.class, () -> new Book(null, "Java"));
        assertThrows(IllegalArgumentException.class, () -> new Book(" ", "Java"));
        assertThrows(IllegalArgumentException.class, () -> new Book("111", ""));
    }
}
