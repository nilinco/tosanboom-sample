package co.nilin.tosanboomsample.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.TimeZone;

import co.nilin.tosanboomsample.util.TextUtils;

public class DateTime {
    public static final DateTime NULL = new DateTime(0, 0, 0);

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mSecond;

    public DateTime() {
        this(getCurrentCalendar());
    }

    public DateTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        this.mYear = cal.get(Calendar.YEAR);
        this.mMonth = cal.get(Calendar.MONTH) + 1;
        this.mDay = cal.get(Calendar.DAY_OF_MONTH);
        this.mHour = cal.get(Calendar.HOUR_OF_DAY);
        this.mMinute = cal.get(Calendar.MINUTE);
        this.mSecond = cal.get(Calendar.SECOND);
    }

    public DateTime(int year, int month, int day, int hour, int minute, int second) {
        this.mYear = year;
        this.mMonth = month;
        this.mDay = day;
        this.mHour = hour;
        this.mMinute = minute;
        this.mSecond = second;
    }

    public DateTime(Calendar cal) {
        this(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
    }

    public DateTime(int year, int month, int day) {
        this(year, month, day, 0, 0, 0);
    }

    public DateTime(String date) {
        if (date != null && !date.equals("")) {
            String splitter, delimiter;
            if (date.contains(" ")) {
                splitter = " ";
                delimiter = "/";
            } else if (date.contains("T")) {
                splitter = "T";
                delimiter = "-";
            } else return;

            String[] dateTokens = date.split(splitter);
            StringTokenizer tokenizer = new StringTokenizer(dateTokens[0], delimiter);
            this.mYear = Integer.parseInt(tokenizer.nextToken());
            this.mMonth = Integer.parseInt(tokenizer.nextToken());
            this.mDay = Integer.parseInt(tokenizer.nextToken());

            int index = dateTokens[1].indexOf("."); //For strings like "2014-01-31T07:34:16.000Z"
            if (index != -1) dateTokens[1] = dateTokens[1].substring(0, index);
            tokenizer = new StringTokenizer(dateTokens[1], ":");
            this.mHour = Integer.parseInt(tokenizer.nextToken());
            this.mMinute = Integer.parseInt(tokenizer.nextToken());
            this.mSecond = Integer.parseInt(tokenizer.nextToken());
        }
    }

    public static Calendar getCurrentCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran"));
    }

    public Date toDate() {
        Calendar calendar = new GregorianCalendar();
        if (mYear != 0 && mMonth != 0 && mDay != 0) {
            calendar.set(mYear, mMonth - 1, mDay, mHour, mMinute, mSecond);
            return calendar.getTime();
        }
        return null;
    }

    public String toStringWithTime() {
        if (mYear == 0 && mMonth == 0 && mDay == 0)
            return "";
        return mYear + "/" + TextUtils.pad(mMonth) + "/" + TextUtils.pad(mDay) + " " + TextUtils.pad(mHour) + ":" + TextUtils.pad(mMinute) + ":" + TextUtils.pad(mSecond);
    }

    public String toStringWithTimeStandard() {
        if (mYear == 0 && mMonth == 0 && mDay == 0)
            return "";
        return mYear + "/" + TextUtils.pad(mMonth) + "/" + TextUtils.pad(mDay) + " " + TextUtils.pad(mHour) + ":" + TextUtils.pad(mMinute);
    }

    public String toJibitString() {
        if (mYear == 0 && mMonth == 0 && mDay == 0)
            return "";
        return TextUtils.pad(mMonth) + "/" + TextUtils.pad(mDay) + " - " + TextUtils.pad(mHour) + ":" + TextUtils.pad(mMinute);
    }

    public String toJibitStringReverse() {
        if (mYear == 0 && mMonth == 0 && mDay == 0)
            return "";
        return TextUtils.pad(mHour) + ":" + TextUtils.pad(mMinute) + " - " + TextUtils.pad(mMonth) + "/" + TextUtils.pad(mDay);
    }

    @Override
    public String toString() {
        if (mYear == 0 && mMonth == 0 && mDay == 0)
            return "";
        return mYear + "/" + TextUtils.pad(mMonth) + "/" + TextUtils.pad(mDay);
    }

    public long compare(Calendar cal) {
        Calendar oldCal = getCurrentCalendar();
        oldCal.set(mYear, mMonth - 1, mDay, mHour, mMinute, mSecond);
        return cal.getTimeInMillis() - oldCal.getTimeInMillis();
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        this.mYear = year;
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        this.mMonth = month;
    }

    public int getDay() {
        return mDay;
    }

    public void setDay(int day) {
        this.mDay = day;
    }

    public int getHour() {
        return mHour;
    }

    public void setHour(int hour) {
        this.mHour = hour;
    }

    public int getMinute() {
        return mMinute;
    }

    public void setMinute(int minute) {
        this.mMinute = minute;
    }

    public int getSecond() {
        return mSecond;
    }

    public void setSecond(int second) {
        this.mSecond = second;
    }
}