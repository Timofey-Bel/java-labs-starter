package edu.course.lab04;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IssueRegistryTest {

    @Test
    void newRegistryIsEmpty() {
        IssueRegistry registry = new IssueRegistry();
        assertEquals(0, registry.size());
        assertFalse(registry.contains(new IssueId("WEB", 1)));
    }

    @Test
    void putAndGetTask() {
        IssueRegistry registry = new IssueRegistry();
        ProjectTask task = new ProjectTask("Сверстать форму", 3);
        registry.put(new IssueId("WEB", 1), task);

        assertSame(task, registry.get(new IssueId("WEB", 1)));
        assertTrue(registry.contains(new IssueId("WEB", 1)));
        assertEquals(1, registry.size());
    }

    // Эксперимент 1: два разных объекта равного ключа находят одно значение
    @Test
    void differentInstancesOfEqualKeyFindSameValue() {
        IssueRegistry registry = new IssueRegistry();
        ProjectTask task = new ProjectTask("Задача", 1);
        IssueId keyForPut = new IssueId("WEB", 7);
        IssueId keyForGet = new IssueId("WEB", 7);

        registry.put(keyForPut, task);

        assertSame(task, registry.get(keyForGet));
    }

    // Эксперимент 2: равный ключ заменяет значение, размер не растет
    @Test
    void putWithEqualKeyReplacesValue() {
        IssueRegistry registry = new IssueRegistry();
        ProjectTask oldTask = new ProjectTask("Старая", 1);
        ProjectTask newTask = new ProjectTask("Новая", 2);

        registry.put(new IssueId("WEB", 1), oldTask);
        registry.put(new IssueId("WEB", 1), newTask);

        assertEquals(1, registry.size());
        assertSame(newTask, registry.get(new IssueId("WEB", 1)));
    }

    // Эксперимент 3: коллизия. hashCode = 31 * code.hashCode() + number
    // "A".hashCode() = 65, "B".hashCode() = 66
    // A-31: 31 * 65 + 31 = 2046
    // B-0:  31 * 66 + 0  = 2046
    @Test
    void collidingKeysKeepDifferentValues() {
        IssueId first = new IssueId("A", 31);
        IssueId second = new IssueId("B", 0);
        assertEquals(first.hashCode(), second.hashCode()); // хеши одинаковые
        assertNotEquals(first, second);                    // а ключи разные

        IssueRegistry registry = new IssueRegistry();
        ProjectTask firstTask = new ProjectTask("Первая", 1);
        ProjectTask secondTask = new ProjectTask("Вторая", 2);
        registry.put(first, firstTask);
        registry.put(second, secondTask);

        assertEquals(2, registry.size());
        assertSame(firstTask, registry.get(first));
        assertSame(secondTask, registry.get(second));
    }

    @Test
    void removeDeletesTask() {
        IssueRegistry registry = new IssueRegistry();
        ProjectTask task = new ProjectTask("Задача", 1);
        registry.put(new IssueId("WEB", 1), task);

        assertSame(task, registry.remove(new IssueId("WEB", 1)));
        assertFalse(registry.contains(new IssueId("WEB", 1)));
        assertNull(registry.get(new IssueId("WEB", 1)));
        assertEquals(0, registry.size());
    }

    @Test
    void removeMissingKeyReturnsNull() {
        IssueRegistry registry = new IssueRegistry();
        assertNull(registry.remove(new IssueId("WEB", 99)));
    }

    @Test
    void putNullThrows() {
        IssueRegistry registry = new IssueRegistry();
        ProjectTask task = new ProjectTask("Задача", 1);
        assertThrows(IllegalArgumentException.class, () -> registry.put(null, task));
        assertThrows(IllegalArgumentException.class, () -> registry.put(new IssueId("WEB", 1), null));
    }

    // Эксперимент 5: изменяемый ключ ломает поиск.
    // Такой ключ специально объявлен только в тесте.
    static class MutableKey {
        private String code;

        MutableKey(String code) {
            this.code = code;
        }

        void setCode(String code) {
            this.code = code;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof MutableKey)) {
                return false;
            }
            return code.equals(((MutableKey) other).code);
        }

        @Override
        public int hashCode() {
            return code.hashCode();
        }
    }

    @Test
    void changedMutableKeyIsNotFound() {
        HashMap<MutableKey, String> map = new HashMap<>();
        MutableKey key = new MutableKey("WEB");
        map.put(key, "значение");

        key.setCode("APP"); // меняем поле, которое участвует в hashCode

        // объект тот же самый, но HashMap ищет его в другой корзине
        assertNull(map.get(key));
        assertFalse(map.containsKey(new MutableKey("WEB")));
        assertEquals(1, map.size()); // значение никуда не делось, просто его не найти
    }
}
