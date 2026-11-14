package edu.course.lab04;

// Неизменяемая книга. Две книги равны, если у них одинаковый ISBN.
// Название в сравнении не участвует.
public final class Book {

    private final String isbn;
    private final String title;

    public Book(String isbn, String title) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN не может быть пустым");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Название не может быть пустым");
        }
        this.isbn = isbn;
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Book)) {
            return false; // сюда попадает и null
        }
        Book book = (Book) other;
        return isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        // используем то же поле, что и в equals
        return isbn.hashCode();
    }

    @Override
    public String toString() {
        return "Book{" + isbn + ", " + title + "}";
    }
}
