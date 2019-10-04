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
    public static String getCurrentTimeNumerical(){
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyHHmmss");
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }


}
