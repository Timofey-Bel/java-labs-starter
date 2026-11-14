package edu.course.lab03;

// Общий контракт: по задаче посчитать приоритет.
// Чем больше число, тем выше приоритет.
public interface PriorityPolicy {
    int calculatePriority(ProjectTask task);
}
