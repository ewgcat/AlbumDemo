package com.lishuaihua.albumdemo;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by gg on 2018/3/14.
 */

public class Utils {
    public static File getAppRootPath(Context context) {
        if (sdCardIsAvailable()) {
            return Environment.getExternalStorageDirectory();
        } else {
            return context.getFilesDir();
        }
    }

    public static boolean sdCardIsAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().canWrite();
        } else
            return false;
    }

    /**
     * 获取时分
     *
     * @return
     */
    public static String getTimeHourMin() {
        Calendar c = Calendar.getInstance();
//        year = c.get(Calendar.YEAR)
//        month = c.grt(Calendar.MONTH)
//        day = c.get(Calendar.DAY_OF_MONTH)
//        取得系统时间：hour = c.get(Calendar.HOUR_OF_DAY);
//        minute = c.get(Calendar.MINUTE)
//        Calendar c = Calendar.getInstance();
//        取得系统日期:year = c.get(Calendar.YEAR)
//        month = c.grt(Calendar.MONTH)
//        day = c.get(Calendar.DAY_OF_MONTH)
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return hour + ":" + minute;
    }

    /**
     * 剩余支付时间
     *
     * @param time
     * @return
     */
    public static String remainTime(Long time) {
        String timeString = "";
        long day = 0;
        long hour = 0;
        long min = 0;
        long second = 0;
        if (time / (24 * 60 * 60) > 0) {//》1天
            day = time / (24 * 60 * 60);
            if ((time % (24 * 60 * 60)) / (60 * 60) > 0) {//大于1小时
                hour = (time % (24 * 60 * 60)) / (60 * 60);
                if (((time % (24 * 60 * 60)) % (60 * 60)) / (60) > 0) {//大于1分钟
                    min = ((time % (24 * 60 * 60)) % (60 * 60)) / (60);
                    second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;
                } else {//小于1分钟
                    second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;
                }
            } else {//小于1小时
                if ((time % (24 * 60 * 60)) / (60) > 0) {//大于1分钟
                    min = (time % (24 * 60 * 60)) / (60);
                    second = (time % (24 * 60 * 60)) % (60);
                } else {//小于1分钟
                    second = (time % (24 * 60 * 60)) % 60;
                }
            }
        } else {//<1天
            if (time / (60 * 60) > 0) {//大于1小时
                hour = time / (60 * 60);
                if ((time % (60 * 60)) / (60) > 0) {//大于1分钟
                    min = (time % (60 * 60)) / (60);
                    second = (time % (60 * 60)) % 60;
                } else {//小于1分钟
                    second = time % 60;
                }
            } else {//小于1小时
                if (time / (60) > 0) {//大于1分钟
                    min = time / (60);
                    second = time % 60;
                } else {//小于1分钟
                    second = time % 60;
                }
            }
        }

        if (day > 0) {
            timeString = day + "天";
        }
        if (hour > 0) {
            timeString = timeString + hour + "小时";
        } else {
            if (day > 0) {
                timeString = timeString + hour + "小时";
            }
        }
        if (min > 0) {
            timeString = timeString + min + "分";
        } else {
            if (day > 0 || hour > 0) {
                timeString = timeString + min + "分";
            }
        }
        if (second > 0) {
            timeString = timeString + second + "秒";
        } else {
            if (day > 0 || hour > 0 || min > 0) {
                timeString = timeString + second + "秒";
            }
        }

        return timeString;
    }

    /**
     * 获取时间戳
     */
    public static long getTimeLong(String time, String type, String timeid) {
        Calendar c = Calendar.getInstance();
        String time1 = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DATE);
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy/MM/dd").parse(time1);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (type.equals("-2") && timeid.equals("-2")) {
            return date1.getTime() + (24 * 60 * 60 * 1000);
        }
        if (time.contains("\\n")) {
            time = time.substring(0, time.indexOf("\\n"));
        }
        String sysTime = getTimeHourMin();
        int day = 0;
        int hour1 = 0;
        int min1 = 0;
        if (time.contains(":")) {
            String[] sysTimes = sysTime.split(":");
            String[] times = time.split(":");
            int a = Integer.valueOf(times[0].trim());
            int b = Integer.valueOf(times[1].trim());
            if (type.equals("-2")) {
                return date1.getTime() + (24 * 60 * 60 * 1000) + (a * 60 * 60 * 1000) + (b * 60 * 60);
            }

            if (Integer.valueOf(sysTimes[0]) > a) {

            } else if (Integer.valueOf(sysTimes[0]) == a) {
                int i = Integer.valueOf(times[1].trim());
                hour1 = a;
                if (Integer.valueOf(sysTimes[1]) >= i) {

                } else {//预热
                    min1 = i;
                }
            } else {// 预热
                hour1 = a;
                min1 = b;
            }

        } else {
            day = 1;
        }

        Log.d("data.getTime()", "------------------------------------data.getTime():" + date1.getTime() + (hour1 * 60 * 60 * 1000) + (min1 * 60 * 1000));

        return date1.getTime() + (hour1 * 60 * 60 * 1000) + (min1 * 60 * 1000);
    }

    /**
     * 比较抢购时间
     *
     * @param time
     * @return
     */
    public static String compareTime(String time, String type, String timeid) {
        if (time.contains("\\n")) {
            time = time.substring(0, time.indexOf("\\n"));
        }
        String sysTime = getTimeHourMin();
        if (type == null) {
            if (timeid.equals("-1")) {
                return "别错过";
            }
            if (timeid.equals("-2")) {
                return "预告";
            }
        } else {
            if (type.equals("-1")) {//昨日全部
                if (timeid.equals("-1")) {//全部
                    return "别错过";
                } else {
                    return "昨日精选";
                }
            } else if (type.equals("1")) {
                if (time.contains(":")) {
                    String[] sysTimes = sysTime.split(":");
                    String[] times = time.split(":");
                    int a = Integer.valueOf(times[0].trim());

                    if (Integer.valueOf(sysTimes[0]) > a) {
                        return "抢购中";
                    } else if (Integer.valueOf(sysTimes[0]) == a) {
                        int i = Integer.valueOf(times[1].trim());
                        if (Integer.valueOf(sysTimes[1]) > i) {
                            return "抢购中";
                        } else {
                            return "预热中";
                        }
                    } else {
                        return "预热中";
                    }
                }
            } else if (type.equals("-2")) {//明日
                return "预告";
            }
        }

        return "精选";
    }

    /**
     * @param time
     * @return
     */
    public static boolean compareTimeTwo(String time) {
        if (time.contains("\\n")) {
            time = time.substring(0, time.indexOf("\\n"));
        }
        String sysTime = getTimeHourMin();
        if (time.contains(":")) {
            String[] sysTimes = sysTime.split(":");
            String[] times = time.split(":");
            int a = Integer.valueOf(times[0].trim());

            if (Integer.valueOf(sysTimes[0]) > a) {
                return false;
            } else if (Integer.valueOf(sysTimes[0]) == a) {
                int i = Integer.valueOf(times[1].trim());
                if (Integer.valueOf(sysTimes[1]) > i) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }

        return true;
    }

    /**
     * 四舍五入， 数字param保留两位小数点
     */
    public static String round(String param) {
        if (TextUtils.isEmpty(param))
            param = "0";
        BigDecimal bd1 = new BigDecimal(param);
        BigDecimal bd2 = new BigDecimal(1);
        return bd1.divide(bd2, 2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static int getInt(double number) {
        BigDecimal bd = new BigDecimal(number).setScale(0, BigDecimal.ROUND_HALF_UP);
        return Integer.parseInt(bd.toString());
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date, int day) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + day); // Sunday 它就会自动找这个星期的
        return c.getTime();
    }

    /**
     * 取得过去七天数据
     * @param date
     * @param day
     * @return
     */
    /**
     * 17      * 获取过去第几天的日期(- 操作) 或者 未来 第几天的日期( + 操作)
     * 18      *
     * 19      * @param past
     * 20      * @return
     * 21
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        String result = format.format(today);
        return result;
    }


    //导致TextView异常换行的原因：安卓默认数字、字母不能为第一行以后每行的开头字符，因为数字、字母为半角字符
    //所以我们只需要将半角字符转换为全角字符即可，方法如下
    public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

}
