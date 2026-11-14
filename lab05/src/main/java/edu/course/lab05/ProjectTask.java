package edu.course.lab05;

// Задача из ЛР 2, дополненная полем category из формата файла.
public class ProjectTask {

    private final String id;
    private final String title;
    private final String category;
    private int estimatedHours;
    private TaskStatus status;

    public ProjectTask(String id, String title, String category, int estimatedHours, TaskStatus status) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id не может быть пустым");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title не может быть пустым");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("category не может быть пустой");
        }
        if (estimatedHours <= 0) {
            throw new IllegalArgumentException("Оценка в часах должна быть больше нуля");
        }
        if (status == null) {
            throw new IllegalArgumentException("Статус не может быть null");
        }
        this.id = id;
        this.title = title;
        this.category = category;
        this.estimatedHours = estimatedHours;
        this.status = status;
    }

    public void changeStatus(TaskStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Статус не может быть null");
        }
        this.status = newStatus;
    }

    public boolean isDone() {
        return status == TaskStatus.DONE;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }

    public TaskStatus getStatus() {
        return status;
    }
}
