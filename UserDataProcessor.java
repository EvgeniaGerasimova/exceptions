import java.io.*;
import java.nio.file.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserDataProcessor {
private static final int EXPECTED_FIELDS = 6;
private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Введите данные (Фамилия Имя Отчество дата рождения номер телефона пол):");

    String input = scanner.nextLine();
    String[] fields = input.split("\\s+");

    if (fields.length != EXPECTED_FIELDS) {
        System.err.println("Ошибка: введено неверное количество данных. Ожидалось " + EXPECTED_FIELDS + " полей.");
        return;
    }

    try {
        String surname = fields[0];
        String name = fields[1];
        String patronymic = fields[2];
        String birthDateStr = fields[3];
        long phoneNumber = parsePhoneNumber(fields[4]);
        char gender = parseGender(fields[5]);

        validateDate(birthDateStr);

        String data = String.join(" ", surname, name, patronymic, birthDateStr, String.valueOf(phoneNumber), String.valueOf(gender));
        writeToFile(surname, data);

    } catch (NumberFormatException e) {
        System.err.println("Ошибка: неверный формат номера телефона. " + e.getMessage());
    } catch (ParseException e) {
        System.err.println("Ошибка: неверный формат даты. " + e.getMessage());
    } catch (IllegalArgumentException e) {
        System.err.println("Ошибка: неверный формат пола. " + e.getMessage());
    } catch (IOException e) {
        System.err.println("Ошибка записи в файл: " + e.getMessage());
        e.printStackTrace();
    }
}

private static long parsePhoneNumber(String phoneNumberStr) {
    return Long.parseLong(phoneNumberStr);
}

private static char parseGender(String genderStr) {
    if (genderStr.length() != 1) {
        throw new IllegalArgumentException("Пол должен быть один символ: 'f' или 'm'.");
    }
    char gender = genderStr.charAt(0);
    if (gender != 'f' && gender != 'm') {
        throw new IllegalArgumentException("Пол должен быть 'f' или 'm'.");
    }
    return gender;
}

private static void validateDate(String dateStr) throws ParseException {
    DATE_FORMAT.setLenient(false);
    DATE_FORMAT.parse(dateStr);
}

private static void writeToFile(String surname, String data) throws IOException {
    Path filePath = Paths.get(surname + ".txt");
    try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
        writer.write(data);
        writer.newLine();
    }
}
}
