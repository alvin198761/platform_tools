package org.alvin;

import com.google.common.collect.Lists;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author 唐植超
 * @date 2020/05/18
 */
public class Test {

    //b为传入的字节，i为第几位（范围0-7），如要获取bit0，则i=0
    public static int getBit(byte b,int i) {
        int bit = (int)((b>>i) & 0x1);
        return bit;
    }

    //b为传入的字节，start是起始位，length是长度，如要获取bit0-bit4的值，则start为0，length为5
    public static int getBits(byte b,int start,int length) {
        int bit = (int)((b>>start)&(0xFF>>(8-length)));
        return bit;
    }


    public static void main(String[] args) {
//        String array = String.format("%08d", Integer.valueOf(Integer.toBinaryString(35)));
    }

    /**
     * 时标CP56Time2a解析
     */
    public static String TimeScale(int b[]) {
        String str = "";
        int year = b[6] & 0x7F;
        int month = b[5] & 0x0F;
        int day = b[4] & 0x1F;
        int week = (b[4] & 0xE0) / 32;
        int hour = b[3] & 0x1F;
        int minute = b[2] & 0x3F;
        int second = (b[1] << 8) + b[0];

        str += "时标CP56Time2a:" + "20" + year + "-"
                + String.format("%02d", month) + "-"
                + String.format("%02d", day) + "," + hour + ":" + minute + ":"
                + second / 1000 + "." + second % 1000;
        return str + "\n";
    }


    /**
     * 时间转16进制字符串
     */
    public static String date2HStr(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        System.out.println(calendar.get(Calendar.MILLISECOND));
        StringBuilder builder = new StringBuilder();
        String milliSecond = String.format("%04X", (calendar.get(Calendar.SECOND) * 1000) + calendar.get(Calendar.MILLISECOND));
        builder.append(milliSecond.substring(2, 4));
        builder.append(milliSecond.substring(0, 2));
        builder.append(String.format("%02X", calendar.get(Calendar.MINUTE) & 0x3F));
        builder.append(String.format("%02X", calendar.get(Calendar.HOUR_OF_DAY) & 0x1F));
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        if (week == Calendar.SUNDAY)
            week = 7;
        else week--;
        builder.append(String.format("%02X", (week << 5) + (calendar.get(Calendar.DAY_OF_MONTH) & 0x1F)));
        builder.append(String.format("%02X", calendar.get(Calendar.MONTH) + 1));
        builder.append(String.format("%02X", calendar.get(Calendar.YEAR) - 2000));
        return builder.toString();
    }

}
