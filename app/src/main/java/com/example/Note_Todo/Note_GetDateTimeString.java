package com.example.Note_Todo;

import android.annotation.SuppressLint;

import java.util.Calendar;

public class Note_GetDateTimeString {
    @SuppressLint("DefaultLocale")
    public static String getDateTimeString(long timestamp, boolean isInNote) {
        // 获取当前时间
        Calendar now_time = Calendar.getInstance();
        // 获取指定时间
        Calendar note_time = Calendar.getInstance();
        note_time.setTimeInMillis(timestamp);
        int hour = note_time.get(Calendar.HOUR_OF_DAY);
        String yearString;
        yearString = String.format("%1$d年", note_time.get(Calendar.YEAR));
        String dateString;
        dateString = String.format("%1$d月%2$02d日", note_time.get(Calendar.MONTH) + 1, note_time.get(Calendar.DAY_OF_MONTH));
        String timeString;
        if (hour < 6) {
            timeString = "凌晨" + String.format("%1$d:%2$02d", note_time.get(Calendar.HOUR_OF_DAY), note_time.get(Calendar.MINUTE));
        } else if (hour < 12) {
            timeString = "上午" + String.format("%1$d:%2$02d", note_time.get(Calendar.HOUR_OF_DAY), note_time.get(Calendar.MINUTE));
        } else if (hour == 12){
            timeString = "下午" + String.format("%1$d:%2$02d", note_time.get(Calendar.HOUR_OF_DAY), note_time.get(Calendar.MINUTE));
        } else if (hour < 18) {
            timeString = "下午" + String.format("%1$d:%2$02d", note_time.get(Calendar.HOUR_OF_DAY) - 12, note_time.get(Calendar.MINUTE));
        } else {
            timeString = "晚上" + String.format("%1$d:%2$02d", note_time.get(Calendar.HOUR_OF_DAY) - 12, note_time.get(Calendar.MINUTE));
        }
        //今年
        if (now_time.get(Calendar.YEAR) == note_time.get(Calendar.YEAR)) {
            if (!isInNote) {
                //今天
                if (now_time.get(Calendar.DAY_OF_YEAR) == note_time.get(Calendar.DAY_OF_YEAR)) {
                    return timeString;
                }
                //昨天
                now_time.add(Calendar.DAY_OF_YEAR, -1);
                if (now_time.get(Calendar.DAY_OF_YEAR) == note_time.get(Calendar.DAY_OF_YEAR)) {
                    return "昨天" + " " + timeString;
                }
                // 判断是否为前天
                now_time.add(Calendar.DAY_OF_YEAR, -1);
                if (now_time.get(Calendar.DAY_OF_YEAR) == note_time.get(Calendar.DAY_OF_YEAR)) {
                    return "前天" + " " + timeString;
                }
            }
            return dateString + " " + timeString;
        } else {//不是今年
            if (isInNote){
                return yearString + dateString + " " + timeString;
            }
            return yearString + dateString;
        }
    }
}