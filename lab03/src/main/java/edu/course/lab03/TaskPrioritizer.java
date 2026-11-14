package edu.course.lab03;

// Выбирает более приоритетную задачу из двух.
// Как считать приоритет, решает переданная политика.
public class TaskPrioritizer {

    private final PriorityPolicy policy;

    public TaskPrioritizer(PriorityPolicy policy) {
        if (policy == null) {
            throw new IllegalArgumentException("Политика приоритета не может быть null");
        }
        this.policy = policy;
    }

    // При равном приоритете возвращается первая задача.
    public ProjectTask choose(ProjectTask first, ProjectTask second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Задачи не могут быть null");
        }
        int firstPriority = policy.calculatePriority(first);
        int secondPriority = policy.calculatePriority(second);
        if (secondPriority > firstPriority) {
            return second;
        }
        return first;
    }
}
