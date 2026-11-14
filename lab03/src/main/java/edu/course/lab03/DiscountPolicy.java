package edu.course.lab03;

// Общий контракт для любой скидки.
public interface DiscountPolicy {
    double apply(double price);
}
