package data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DataGenerator {

    public static int randomBetween(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public static List<Character> getAllCyrillic() {
        List<Character> cyrillicChars = new ArrayList<>();
        for (char ch = 'А'; ch <= 'я'; ch++) {
            cyrillicChars.add(ch);
        }
        cyrillicChars.add('ё');
        cyrillicChars.add('Ё');
        return cyrillicChars;
    }

    public static List<Character> getAllCyrillicNumerical() {
        List<Character> cyrillicNumerical = getAllCyrillic();
        for (char ch = '0'; ch <= '9'; ch++) {
            cyrillicNumerical.add(ch);
        }
        return cyrillicNumerical;
    }

    public static String cyrillic(int length, boolean withNumbers) {
        List<Character> cyrillicChars = (withNumbers ? getAllCyrillicNumerical() : getAllCyrillic());
        Random random = new Random();
        String cyrillicString = "";

        int randomChar;
        for (int i = 0; i < length; i++) {
            randomChar = random.nextInt(cyrillicChars.size());
            cyrillicString += cyrillicChars.get(randomChar);
        }
        return cyrillicString;
    }

    public static String randomCyrillic(int min, int max, boolean withNumbers) {
        return cyrillic(randomBetween(min, max), withNumbers);
    }

    public static String randomNumeric(int length, boolean startsWithZero) {
        Random random = new Random();
        String numeric = "";
        int number;
        for (int i = 0; i < length; i++) {
            if (!startsWithZero && i == 0) {
                number = randomBetween(1, 9);
            } else number = random.nextInt(10);
            numeric += number;
        }
        return numeric;
    }

    public static String randomNumeric(int min, int max, boolean startsWithZero) {
        return randomNumeric(randomBetween(min, max), startsWithZero);
    }

    public static String randomDateFromNow(int untilYear, int untilMonth, int untilDay) {
        LocalDate now = LocalDate.now();
        long minDay = now.toEpochDay();
        long maxDay = LocalDate.of(untilYear, untilMonth, untilDay).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return randomDate.toString();
    }

}
