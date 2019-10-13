package com.example.stackoverflowclient.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String toNormalDate(Integer seconds) {
        Date date = new Date(seconds*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        simpleDateFormat.format(date);
        return simpleDateFormat.format(date);
    }
}
