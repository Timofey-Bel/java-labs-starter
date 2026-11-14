package edu.course.lab04;

// Минимальная версия задачи из ЛР 2. В registry она хранится как значение.
public class ProjectTask {

    private final String title;
    private TaskStatus status;
    private int estimatedHours;

    public ProjectTask(String title, int estimatedHours) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title не может быть пустым");
        }
        if (estimatedHours <= 0) {
            throw new IllegalArgumentException("Оценка в часах должна быть больше нуля");
        }
        this.title = title;
        this.estimatedHours = estimatedHours;
        this.status = TaskStatus.TODO;
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

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }
}
