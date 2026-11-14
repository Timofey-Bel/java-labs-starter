package edu.course.lab05;

// Ошибка источника: файл нельзя прочитать или он совсем не того формата.
public final class DataLoadingException extends RuntimeException {

    public DataLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    // для ошибок без исходной причины, например неправильный заголовок
    public DataLoadingException(String message) {
        super(message);
    }
}
