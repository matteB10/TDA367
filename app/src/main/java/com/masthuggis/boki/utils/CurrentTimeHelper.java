package com.masthuggis.boki.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CurrentTimeHelper {

    public static String getCurrentTime() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }
    public static long getCurrentTimeNumerical(){
        DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        return Long.parseLong(dateFormat.format(calendar.getTime()));
    }


}
