import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws ThrowsException {

        Scanner in = new Scanner(System.in);
        //Инструкция при запуске
        System.out.println("Введите задачу. Пример: \"a\" + \"b\", \"a\" - \"b\", \"a\" * b, \"a\" / b.");
        String input = in.nextLine();
        char operation = 0;

        // Разделить запрос в месте оператора
        String [] taskElements = new String[0];
        if (input.contains(" + ")) {
            taskElements = input.split(" \\+ ");
            operation = '+';
        } else if (input.contains(" - ")) {
            taskElements = input.split(" - ");
            operation = '-';
        } else if (input.contains(" * ")) {
            taskElements = input.split(" \\* ");
            operation = '*';
        } else if (input.contains(" / ")) {
            taskElements = input.split(" / ");
            operation = '/';
        }
        //Проверка, чтоб было не меньше 3 элементов
        if (taskElements.length != 2) {
            throw new ThrowsException("ОШИБКА! Неверный формат запроса");
        }

        String element1 = taskElements[0];
        String element2 = taskElements[1];

        //   Проверки
        Checks inputItem = new Checks();
        inputItem.object = element1;
        inputItem.operation = operation;
        inputItem.task = element2;

        //   Проверка первого элемента
        inputItem.objectType();

        //   Оператор верный?
        inputItem.operatorCheck();

        //   Второй элемент число или строка?
        boolean taskNumber = inputItem.taskType();

        //   Преобразования первого и второго элементов
        element1 = taskElements[0].replaceAll("\"", "");
        element2 = taskElements[1].replaceAll("\"", "");

        //   Проверка, чтобы оба числа были целыми
        if (taskNumber) {
            double taskInteger = Double.parseDouble(element2);
            if (taskInteger % 1 != 0) {
                throw new ThrowsException("ОШИБКА! Калькулятор умеет работать только с целыми числами.");
            }
        }

        //   Размер элемента
        if (taskNumber) {
            inputItem.numberCheck();
        } else if (!taskNumber) {
            inputItem.taskSize();
        }
        inputItem.objectSize();
        inputItem.taskNumber = taskNumber;

        double number;
        //   Проверка, являются ли введенные значения числами
        if (taskNumber) {
            try {
                number = Double.parseDouble(element2);
            } catch (NumberFormatException e) {
                throw new ThrowsException("ОШИБКА!  Неверный второй элемент. Допустимо либо строка в кавычках, либо число без кавычек");
            }
        }

        //   Выполнение операции из запроса
        Calculator inputCalc = new Calculator();
        inputCalc.object = element1;
        inputCalc.operation = operation;
        inputCalc.task = element2;
        inputCalc.taskNumber = taskNumber;
        String answer = inputCalc.calc();

        if (answer.length() > 40) {
            answer = answer.substring(0,40) + "...";
        }

        System.out.println("\"" + answer + "\"");
    }
}


class Checks {

    String object;    // Основная строка
    char operation;    // Знак операции
    String task;    // Значение задачи

    boolean taskNumber;

    //Проверка первого элемента
    void objectType() throws ThrowsException {
        char firstChar = object.toCharArray()[0];
        char lastChar = object.toCharArray()[object.length() - 1];
        if (firstChar != '\"' || lastChar != '\"') {
            throw new ThrowsException("ОШИБКА! Первый элемент в выражении необходимо выделять двойными кавычками.");
        }
    }

    //Проверка второго элемента
    boolean taskType() {
        boolean taskNumber = false;
        char firstChar = task.toCharArray()[0];
        char lastChar = task.toCharArray()[task.length() - 1];
        if (firstChar != '\"' || lastChar != '\"') {
            taskNumber = true;
        }
        return taskNumber;
    }

    //Проверка оператора
    void operatorCheck() {
        if (operation != '+'  && operation != '-'  && operation != '/'  && operation != '*') {
            throw new IllegalArgumentException("ОШИБКА! Калькулятор может принимать только операторы +, -, *, /.");
        }
    }

    //Проверка на длину и дробность
    void numberCheck() throws ThrowsException {
        if (Integer.parseInt(task) < 1 || Integer.parseInt(task) > 10) {
            throw new ThrowsException("ОШИБКА! Калькулятор должен принимать на вход числа от 1 до 10 включительно, не более.");
        }
    }

    //Проверка длины объекта
    void objectSize() throws ThrowsException {
        if (object.length() > 12) {
            throw new ThrowsException("ОШИБКА! Слишком большой элемент");
        }
    }

    //Проверка длины задачи
    void taskSize() throws ThrowsException {
        if (task.length() > 12) {
            throw new ThrowsException("ОШИБКА! Слишком большой элемент");
        }
    }

}

class Calculator {
    String object;    // Основная строка
    char operation;    // Знак операции
    String task;    // Значение задачи
    boolean taskNumber;
    String answer;

    String calc() throws ThrowsException {
        if (!taskNumber) {
            switch(operation){
                case '+' :
                    answer = object + task;
                    break;
                case '-' :
                    answer = object.replaceAll(task, "");
                    break;
                case '*' :
                    throw new ThrowsException("ОШИБКА! Нельзя умножить строку на строку");
                case '/' :
                    throw new ThrowsException("ОШИБКА! Нельзя делить строку на строку");
            }
        } else if (taskNumber) {
            switch(operation){
                case '+' :
                    throw new ThrowsException("ОШИБКА! Нельзя прибавить к строке число(если оно не в кавычках)");
                case '-' :
                    throw new ThrowsException("ОШИБКА! Нельзя отнять от строки число(если оно не в кавычках)");
                case '*' :
                    answer = object.repeat(Integer.parseInt(task));
                    break;
                case '/' :
                    int objectLength = object.length();
                    objectLength = objectLength / Integer.parseInt(task) ;
                    answer = object.substring(0,objectLength);
                    break;
            }
        }
        return answer;
    }
}

class ThrowsException extends Exception {
    public ThrowsException(String description) {
        super(description);
    }
}
