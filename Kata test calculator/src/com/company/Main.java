package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws CalcExeption {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите вычисляемое выражение: ");
        MathExpression exp = new MathExpression(sc.nextLine());
        exp.calc();
        exp.printResult();
    }
}
