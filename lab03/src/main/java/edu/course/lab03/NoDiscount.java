package edu.course.lab03;

// Политика без скидки: цена не меняется.
public class NoDiscount implements DiscountPolicy {

    @Override
    public double apply(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }
        return price;
    }
}
