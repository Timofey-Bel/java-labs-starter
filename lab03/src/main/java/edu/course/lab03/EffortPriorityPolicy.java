package edu.course.lab03;

// Приоритет по трудоемкости: чем больше часов, тем раньше надо начать задачу.
public class EffortPriorityPolicy implements PriorityPolicy {

    @Override
    public int calculatePriority(ProjectTask task) {
        if (task == null) {
            throw new IllegalArgumentException("Задача не может быть null");
        }
        return task.getEstimatedHours();
    }
}
