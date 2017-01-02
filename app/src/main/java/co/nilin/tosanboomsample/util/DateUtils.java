package co.nilin.tosanboomsample.util;

import java.util.Calendar;

import Negasht.Android.Utility.ConvertDate;
import Negasht.Android.Utility.CustomDate;
import co.nilin.tosanboomsample.model.DateTime;

public class DateUtils {
    public static DateTime getGregorianDate(String date) {
        DateTime dt = new DateTime(date);
        CustomDate cd = new ConvertDate(dt.getYear(), dt.getMonth(), dt.getDay()).Jalali2Gregorian();
        return new DateTime(cd.getYear(), cd.getMonth(), cd.getDay(), dt.getHour(), dt.getMinute(), dt.getSecond());
    }

    public static DateTime getGregorianDate(Calendar cal) {
        DateTime dt = new DateTime(cal);
        CustomDate cd = new ConvertDate(dt.getYear(), dt.getMonth(), dt.getDay()).Jalali2Gregorian();
        return new DateTime(cd.getYear(), cd.getMonth(), cd.getDay(), dt.getHour(), dt.getMinute(), dt.getSecond());
    }

    public static DateTime getJalaliDate(String date) {
        DateTime dt = new DateTime(date);
        CustomDate cd = new ConvertDate(dt.getYear(), dt.getMonth(), dt.getDay()).Gregorian2Jalali();
        return new DateTime(cd.getYear(), cd.getMonth(), cd.getDay(), dt.getHour(), dt.getMinute(), dt.getSecond());
    }

    public static DateTime getJalaliDate(Calendar cal) {
        DateTime dt = new DateTime(cal);
        CustomDate cd = new ConvertDate(dt.getYear(), dt.getMonth(), dt.getDay()).Gregorian2Jalali();
        return new DateTime(cd.getYear(), cd.getMonth(), cd.getDay(), dt.getHour(), dt.getMinute(), dt.getSecond());
    }
}
