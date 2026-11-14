package edu.course.lab04;

// Неизменяемый ключ задачи: код проекта + номер, например "WEB" и 42 -> WEB-42.

public final class IssueId {

    private final String projectCode;
    private final int number;

    public IssueId(String projectCode, int number) {
        if (projectCode == null || projectCode.isBlank()) {
            throw new IllegalArgumentException("Код проекта не может быть пустым");
        }
        if (number < 0) {
            throw new IllegalArgumentException("Номер не может быть отрицательным");
        }
        this.projectCode = projectCode;
        this.number = number;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof IssueId)) {
            return false;
        }
        IssueId id = (IssueId) other;
        return number == id.number && projectCode.equals(id.projectCode);
    }

    @Override
    public int hashCode() {
        // те же два поля, что и в equals
        return 31 * projectCode.hashCode() + number;
    }

    @Override
    public String toString() {
        return projectCode + "-" + number;
    }
}
