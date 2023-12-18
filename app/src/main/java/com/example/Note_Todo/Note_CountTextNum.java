package com.example.Note_Todo;

public class Note_CountTextNum {
    public static int countTextNum(String text) {
        int rawTextLength = text.length(); // 原始文本长度
        int elseCount = 0;
        char[] chars = text.toCharArray();
        for (char c : chars) {
            if (c == '\n') {
                elseCount++;
            } else if (c == '\t') {
                elseCount++;
            } else if (c == '\r') {
                elseCount++;
            } else if (c == ' ') {
                elseCount++;
            }
        }
        return rawTextLength - elseCount;
    }
}