package edu.course.lab03;

// Приоритет по статусу:
// начатую задачу важнее доделать, потом идут новые, завершенные не важны.
public class StatusPriorityPolicy implements PriorityPolicy {

    @Override
    public int calculatePriority(ProjectTask task) {
        if (task == null) {
            throw new IllegalArgumentException("Задача не может быть null");
        }
        switch (task.getStatus()) {
            case IN_PROGRESS:
                return 2;
            case TODO:
                return 1;
            default: // DONE
                return 0;
        }
    }
}
