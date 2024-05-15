package player.test.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.Random;

public class TestUtils {

    private static final Random RANDOM = new Random();

    public static int generateAge() {
        return RandomUtils.nextInt(17, 60); // возраст от 17 до 59 включительно
    }

    public static String generateGender() {
        return RANDOM.nextBoolean() ? "male" : "female";
    }

    public static String generateLogin() {
        return "user" + System.currentTimeMillis(); // уникальный логин
    }

    public static String generateScreenName() {
        return "User" + System.currentTimeMillis(); // уникальное экранное имя
    }

    public static String generatePassword() {
        int length = RandomUtils.nextInt(7, 16); // длина пароля от 7 до 15
        return RandomStringUtils.random(length, true, true); // латинские буквы и цифры
    }
}
