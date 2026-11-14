package edu.course.lab03;

// Калькулятор не знает, какая именно скидка ему передана.
// Он зависит только от интерфейса DiscountPolicy.
public class PriceCalculator {

    private final DiscountPolicy policy;

    public PriceCalculator(DiscountPolicy policy) {
        if (policy == null) {
            throw new IllegalArgumentException("Политика скидки не может быть null");
        }
        this.policy = policy;
    }

    public double calculate(double price) {
        return policy.apply(price);
    }
}
