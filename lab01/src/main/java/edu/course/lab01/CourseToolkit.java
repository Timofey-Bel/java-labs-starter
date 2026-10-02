package edu.course.lab01;

import java.util.Scanner; 

public final class CourseToolkit {

    private CourseToolkit() {
    }

    public static boolean isEven(int number) {
        return number % 2 == 0;
    }

    public static boolean isPrime(int number) {
        if (number < 2) {
            return false;
        }
        for (int divisor = 2; divisor * divisor <= number; divisor++) {
            if (number % divisor == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPalindrome(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Текст не должен быть равен null.");
        }
        int left = 0;
        int right = text.length() - 1;
        while (left < right) {
            if (text.charAt(left) != text.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    public static double average(int[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Значения не должны быть null или пустыми.");
        }
        int sum = 0;
        for (int value : values) {
            sum += value;
        }
        return (double) sum / values.length;
    }

    public static int toFahrenheit(int celsius) {
        return celsius * 9 / 5 + 32;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
      
        if (scanner.hasNextInt()) {
            int celsius = scanner.nextInt(); 
            System.out.println(toFahrenheit(celsius)); 
        }
        
        scanner.close(); 
    }
}