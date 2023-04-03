package com.jjzhong.mall.cloud.cartorder.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 订单号生成工具类，使用 ThreadLocal 保存 DateTimeFormatter，可以避免其被重复创建
 */
public class OrderNumFactory {
    public static ThreadLocal<DateTimeFormatter> dateTimeFormatterThreadLocal = ThreadLocal.withInitial(
            () -> DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
    );

    private static String getDateTime() {
        DateTimeFormatter dateTimeFormatter = dateTimeFormatterThreadLocal.get();
        return dateTimeFormatter.format(LocalDateTime.now());
    }

    private static int getRandom(Long seed) {
        Random random = new Random(seed);
        return random.nextInt(90000) + 10000;
    }

    public static String getOrderNo() {
        return getDateTime() + getRandom(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
    }
}
