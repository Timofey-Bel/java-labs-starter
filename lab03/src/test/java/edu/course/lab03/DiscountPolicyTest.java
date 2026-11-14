package edu.course.lab03;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DiscountPolicyTest {

    @Test
    void noDiscountReturnsSamePrice() {
        DiscountPolicy policy = new NoDiscount();
        assertEquals(100.0, policy.apply(100.0), 0.0001);
    }

    @Test
    void noDiscountNegativePriceThrows() {
        DiscountPolicy policy = new NoDiscount();
        assertThrows(IllegalArgumentException.class, () -> policy.apply(-1.0));
    }

    @Test
    void twentyPercentOfHundredIsEighty() {
        DiscountPolicy policy = new PercentDiscount(20);
        assertEquals(80.0, policy.apply(100.0), 0.0001);
    }

    @Test
    void percentDiscountNegativePriceThrows() {
        DiscountPolicy policy = new PercentDiscount(20);
        assertThrows(IllegalArgumentException.class, () -> policy.apply(-5.0));
    }

    @Test
    void percentOutOfRangeThrows() {
        assertThrows(IllegalArgumentException.class, () -> new PercentDiscount(-1));
        assertThrows(IllegalArgumentException.class, () -> new PercentDiscount(101));
    }

    @Test
    void calculatorWorksWithDifferentPolicies() {
        // один и тот же класс PriceCalculator, разные политики
        PriceCalculator withoutDiscount = new PriceCalculator(new NoDiscount());
        PriceCalculator withDiscount = new PriceCalculator(new PercentDiscount(20));

        assertEquals(100.0, withoutDiscount.calculate(100.0), 0.0001);
        assertEquals(80.0, withDiscount.calculate(100.0), 0.0001);
    }

    @Test
    void calculatorWithNullPolicyThrows() {
        assertThrows(IllegalArgumentException.class, () -> new PriceCalculator(null));
    }
}
