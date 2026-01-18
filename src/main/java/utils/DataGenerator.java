package utils;

import java.util.Random;

public class DataGenerator {

    // 生成随机姓名 (例如：Guest_482)
    public static String getRandomName() {
        String[] names = {"John", "Emma", "Michael", "Sophia", "David", "Olivia"};
        Random random = new Random();
        return names[random.nextInt(names.length)] + "_" + random.nextInt(1000);
    }

    // 生成随机邮箱
    public static String getRandomEmail() {
        return "testuser" + System.currentTimeMillis() + "@orangehrm.com";
    }

    // 生成指定长度的随机数字 (用于 Employee ID)
    public static String getRandomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
