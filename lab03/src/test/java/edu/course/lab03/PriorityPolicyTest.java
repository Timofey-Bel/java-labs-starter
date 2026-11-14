package edu.course.lab03;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PriorityPolicyTest {

    @Test
    void effortPolicyReturnsHours() {
        PriorityPolicy policy = new EffortPriorityPolicy();
        ProjectTask task = new ProjectTask("T-1", "Задача", 8);
        assertEquals(8, policy.calculatePriority(task));
    }

    @Test
    void effortPolicyBiggerTaskHasHigherPriority() {
        PriorityPolicy policy = new EffortPriorityPolicy();
        ProjectTask small = new ProjectTask("T-1", "Маленькая", 2);
        ProjectTask big = new ProjectTask("T-2", "Большая", 10);
        assertTrue(policy.calculatePriority(big) > policy.calculatePriority(small));
    }

    @Test
    void effortPolicyNullTaskThrows() {
        PriorityPolicy policy = new EffortPriorityPolicy();
        assertThrows(IllegalArgumentException.class, () -> policy.calculatePriority(null));
    }

    @Test
    void statusPolicyInProgressIsHigherThanTodo() {
        PriorityPolicy policy = new StatusPriorityPolicy();
        ProjectTask todo = new ProjectTask("T-1", "Новая", 1);
        ProjectTask inProgress = new ProjectTask("T-2", "Начатая", 1);
        inProgress.changeStatus(TaskStatus.IN_PROGRESS);

        assertEquals(1, policy.calculatePriority(todo));
        assertEquals(2, policy.calculatePriority(inProgress));
    }

    @Test
    void statusPolicyDoneHasZeroPriority() {
        PriorityPolicy policy = new StatusPriorityPolicy();
        ProjectTask done = new ProjectTask("T-1", "Готовая", 1);
        done.changeStatus(TaskStatus.DONE);
        assertEquals(0, policy.calculatePriority(done));
    }

    @Test
    void prioritizerResultDependsOnPolicy() {
        // большая новая задача и маленькая начатая
        ProjectTask bigTodo = new ProjectTask("T-1", "Большая", 10);
        ProjectTask smallInProgress = new ProjectTask("T-2", "Маленькая", 1);
        smallInProgress.changeStatus(TaskStatus.IN_PROGRESS);

        TaskPrioritizer byEffort = new TaskPrioritizer(new EffortPriorityPolicy());
        TaskPrioritizer byStatus = new TaskPrioritizer(new StatusPriorityPolicy());

        // класс TaskPrioritizer один и тот же, меняется только политика
        assertSame(bigTodo, byEffort.choose(bigTodo, smallInProgress));
        assertSame(smallInProgress, byStatus.choose(bigTodo, smallInProgress));
    }

    @Test
    void prioritizerReturnsFirstWhenEqual() {
        ProjectTask first = new ProjectTask("T-1", "Первая", 3);
        ProjectTask second = new ProjectTask("T-2", "Вторая", 3);
        TaskPrioritizer prioritizer = new TaskPrioritizer(new EffortPriorityPolicy());
        assertSame(first, prioritizer.choose(first, second));
    }

    @Test
    void prioritizerDoesNotChangeTasks() {
        ProjectTask first = new ProjectTask("T-1", "Первая", 3);
        ProjectTask second = new ProjectTask("T-2", "Вторая", 5);
        TaskPrioritizer prioritizer = new TaskPrioritizer(new EffortPriorityPolicy());

        prioritizer.choose(first, second);

        assertEquals(3, first.getEstimatedHours());
        assertEquals(TaskStatus.TODO, first.getStatus());
        assertEquals(5, second.getEstimatedHours());
        assertEquals(TaskStatus.TODO, second.getStatus());
    }

    @Test
    void prioritizerNullArgumentsThrow() {
        assertThrows(IllegalArgumentException.class, () -> new TaskPrioritizer(null));
        TaskPrioritizer prioritizer = new TaskPrioritizer(new EffortPriorityPolicy());
        ProjectTask task = new ProjectTask("T-1", "Задача", 1);
        assertThrows(IllegalArgumentException.class, () -> prioritizer.choose(null, task));
        assertThrows(IllegalArgumentException.class, () -> prioritizer.choose(task, null));
    }
}
