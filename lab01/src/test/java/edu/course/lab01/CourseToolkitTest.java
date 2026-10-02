package edu.course.lab01;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseToolkitTest {

    @Test
    void returnsTrueForEvenNumber() {
        assertTrue(CourseToolkit.isEven(10));
        System.out.println("all good!");
    }

    @Test
    void returnsFalseForOddNumber() {
        assertFalse(CourseToolkit.isEven(13));
    }

    @Test
    void returnsTrueForNegativeEvenNumber() {
        assertTrue(CourseToolkit.isEven(-4));
    }

    @Test
    void returnsFalseForNumbersLessThanTwo() {
        assertFalse(CourseToolkit.isPrime(-1));
        assertFalse(CourseToolkit.isPrime(0));
        assertFalse(CourseToolkit.isPrime(1));
    }

    @Test
    void returnsTrueForThree() { 
        assertTrue(CourseToolkit.isPrime(3));
    }

    @Test
    void returnsFalseForCompositeNumber() {
        assertFalse(CourseToolkit.isPrime(21));
    }

    @Test
    void returnsFalseForSquareOfPrime() {
        assertFalse(CourseToolkit.isPrime(121));
    }

    @Test
    void returnsTrueForLargePrime() {
        assertTrue(CourseToolkit.isPrime(89));
    }

    @Test
    void returnsTrueForSimplePalindrome() {
        assertTrue(CourseToolkit.isPalindrome("radar"));
    }

    @Test
    void returnsFalseWhenCaseDiffers() {
        assertFalse(CourseToolkit.isPalindrome("Radar"));
    }

    @Test
    void returnsFalseWhenSpacesDiffer() {
        assertFalse(CourseToolkit.isPalindrome("top spot"));
    }

    @Test
    void throwsForNullInIsPalindrome() {
        assertThrows(IllegalArgumentException.class,
                () -> CourseToolkit.isPalindrome(null));
    }

    @Test
    void returnsFractionalAverage() {
        assertEquals(5.5, CourseToolkit.average(new int[]{5, 6}), 0.0001);
    }

    @Test
    void handlesNegativeValues() {
        assertEquals(-7.5, CourseToolkit.average(new int[]{-5, -10}), 0.0001);
    }

    @Test
    void doesNotModifyInputArray() {
        int[] input = {9, 7, 8};
        int[] copy = input.clone();
        CourseToolkit.average(input);
        assertArrayEquals(copy, input);
    }

    @Test
    void throwsForNullOrEmptyArray() {
        assertThrows(IllegalArgumentException.class,
                () -> CourseToolkit.average(null));
        assertThrows(IllegalArgumentException.class,
                () -> CourseToolkit.average(new int[0]));
    }
}
