package com.company;

import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static String expression;

    public static void main(String[] args) throws CalcExeption {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите вычисляемое выражение: ");
        expression = sc.nextLine();
        //Номализуем выражение: убираем лишние пробелы и переводим символы в верхний регистр
        expression = expression.replaceAll(" ", "").toUpperCase(Locale.ROOT);
        printResult(calc(expression));
    }

    //Возвращает результат вычисления выражения
    private static int calc(String expression) throws CalcExeption {
        char MathSymbol = recognizeSymbol(expression);
        int[] args = recognizeArgs(expression);
        switch (MathSymbol) {
            case ('+'):
                return args[0] + args[1];
            case ('-'):
                return args[0] - args[1];
            case ('/'):
                return args[0] / args[1];
            case ('*'):
                return args[0] * args[1];
        }
        return 0;
    }

    //Возвращает символ математической операции в выражении
    private static char recognizeSymbol(String expression) throws CalcExeption {
        Pattern p = Pattern.compile("[/+*-]");
        Matcher m = p.matcher(expression);
        //Если найден один знак, то возвращает его, если больше или меньше одного, то кидает Exeption
        if (m.find()) {
            String str = m.group();
            if (m.find()) {
                throw new CalcExeption("Строка не является математическим выражением или запрошена неподдерживаемая математическая операция");
            } else {
                return str.charAt(0);
            }
        } else {
            throw new CalcExeption("Строка не является математическим выражением или запрошена неподдерживаемая математическая операция");
        }
    }

    //Возвращает массив из двух операндов выражения
    private static int[] recognizeArgs(String expression) throws CalcExeption {
        String[] strArgs = expression.split("[+-/*]");
        int[] result = new int[2];
        if (isRoman(expression)) {
            //Проверяем, присутствуют ли введенные римские цифры в Enum
            //(на этом этапе в запись результата могут попасть числа 20, 30 и т.д.)
            try {
                result[0] = Digit.valueOf(strArgs[0]).getArabic();
                result[1] = Digit.valueOf(strArgs[1]).getArabic();
            } catch (Exception e) {
                throw new CalcExeption("Калькулятор принимает на вход только числа от 1 до 10 включительно");
            }
        } else {
            result[0] = Integer.parseInt(strArgs[0]);
            result[1] = Integer.parseInt(strArgs[1]);
        }
        //Проверяем, находятся ли записанные операнды в диапазоне [1-10]
        if (result[0] > 10 | result[1] > 10 | result[0] < 1 | result[1] < 1) {
            throw new CalcExeption("Калькулятор принимает на вход только числа от 1 до 10 включительно");
        }
        return result;
    }

    //Конвертирует арабское число в римское
    private static String toRoman(int num) {
        StringBuilder result = new StringBuilder();
        //Создаем массив цифр, где позиция в массиве определяет разряд числа
        char[] numberPosition = ("" + num).toCharArray();
        //Собираем в String римское число, поочередно добавляя по одному разряду числа
        for (int i = 0; i < numberPosition.length; i++) {
            int digit = (int) ((numberPosition[i] - 48) * Math.pow(10, numberPosition.length - 1 - i));
            result.append(Digit.toRoman(digit));
        }
        return result.toString();
    }

    //Возвращает true, если выражение записано в формате римских цифр
    private static boolean isRoman(String expression) throws CalcExeption {
        if (expression.matches(".*[IVXCLDM].*")) {
            //Если выражение содержит и римские и арабские цифры, то кидаем Exeption
            if (expression.matches(".*[1234567890].*")) {
                throw new CalcExeption("Можно использовать только римские или арабские числа одновременно");
            }
            return true;
        }
        return false;
    }

    //Выводит в консоль результат вычисления
    private static void printResult(int result) throws CalcExeption {
        if (isRoman(expression)) {
            if (result < 1) {
                throw new CalcExeption("Результатом работы калькулятора с римскими числами могут быть только положительные числа");
            }
            System.out.println(toRoman(result));
        } else {
            System.out.println(result);
        }
    }
}
