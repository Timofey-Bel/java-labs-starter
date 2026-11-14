package edu.course.lab04;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IssueIdTest {

    @Test
    void sameCodeAndNumberAreEqual() {
        IssueId first = new IssueId("WEB", 42);
        IssueId second = new IssueId("WEB", 42);
        assertTrue(first.equals(second));
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void differentNumberOrCodeAreNotEqual() {
        assertNotEquals(new IssueId("WEB", 1), new IssueId("WEB", 2));
        assertNotEquals(new IssueId("WEB", 1), new IssueId("APP", 1));
    }

    @Test
    void notEqualToNullAndOtherType() {
        IssueId id = new IssueId("WEB", 1);
        assertFalse(id.equals(null));
        assertFalse(id.equals("WEB-1"));
    }

    @Test
    void invalidValuesThrow() {
        assertThrows(IllegalArgumentException.class, () -> new IssueId(null, 1));
        assertThrows(IllegalArgumentException.class, () -> new IssueId("", 1));
        assertThrows(IllegalArgumentException.class, () -> new IssueId("WEB", -1));
    }

    @Test
    void toStringLooksLikeIssueKey() {
        assertEquals("WEB-42", new IssueId("WEB", 42).toString());
    }
}
