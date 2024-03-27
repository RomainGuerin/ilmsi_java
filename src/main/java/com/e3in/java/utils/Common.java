package com.e3in.java.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Common {
    public static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return formatter.format(LocalDateTime.now());
    }
}
