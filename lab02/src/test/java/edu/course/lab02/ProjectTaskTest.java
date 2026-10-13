package edu.course.lab02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProjectTaskTest {

    @Test
    void createsCorrectTask() {
        ProjectTask task = new ProjectTask("T-1", "Сделать лабу", 4);
        assertEquals("T-1", task.getId());
        assertEquals("Сделать лабу", task.getTitle());
        assertEquals(4, task.getEstimatedHours());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertFalse(task.isDone());
    }

    @Test
    void nullIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> new ProjectTask(null, "Задача", 1));
    }

    @Test
    void emptyIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> new ProjectTask("", "Задача", 1));
        assertThrows(IllegalArgumentException.class, () -> new ProjectTask("   ", "Задача", 1));
    }

    @Test
    void nullTitleThrows() {
        assertThrows(IllegalArgumentException.class, () -> new ProjectTask("T-1", null, 1));
    }

    @Test
    void emptyTitleThrows() {
        assertThrows(IllegalArgumentException.class, () -> new ProjectTask("T-1", "", 1));
        assertThrows(IllegalArgumentException.class, () -> new ProjectTask("T-1", "  ", 1));
    }

    @Test
    void zeroOrNegativeHoursThrows() {
        assertThrows(IllegalArgumentException.class, () -> new ProjectTask("T-1", "Задача", 0));
        assertThrows(IllegalArgumentException.class, () -> new ProjectTask("T-1", "Задача", -3));
    }

    @Test
    void changeStatusChangesStatus() {
        ProjectTask task = new ProjectTask("T-1", "Задача", 2);
        task.changeStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertFalse(task.isDone());
    }

    @Test
    void taskIsDoneAfterStatusDone() {
        ProjectTask task = new ProjectTask("T-1", "Задача", 2);
        task.changeStatus(TaskStatus.DONE);
        assertTrue(task.isDone());
    }

    @Test
    void changeStatusToNullThrows() {
        ProjectTask task = new ProjectTask("T-1", "Задача", 2);
        assertThrows(IllegalArgumentException.class, () -> task.changeStatus(null));
        assertEquals(TaskStatus.TODO, task.getStatus()); // статус не изменился
    }

    @Test
    void increaseEstimateAddsHours() {
        ProjectTask task = new ProjectTask("T-1", "Задача", 2);
        task.increaseEstimate(3);
        assertEquals(5, task.getEstimatedHours());
    }

    @Test
    void increaseEstimateByNotPositiveThrows() {
        ProjectTask task = new ProjectTask("T-1", "Задача", 2);
        assertThrows(IllegalArgumentException.class, () -> task.increaseEstimate(0));
        assertThrows(IllegalArgumentException.class, () -> task.increaseEstimate(-1));
        assertEquals(2, task.getEstimatedHours()); 
    }
}
