package com.latikaseafood.utils

import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import java.sql.Time
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    private val TAG = "DateTimeUtils"
    val DAY_KEY = "DAY_KEY"
    val TIME_KEY = "TIME_KEY"

    @Throws(ParseException::class)
    fun TimeStampConverter(inputFormat: String,
                           input_value_in_string: String, outputFormat: String): String {
        return SimpleDateFormat(outputFormat).format(SimpleDateFormat(inputFormat).parse(input_value_in_string)!!)
    }



    fun getDayDependingOnJavaTime(s: String): String {
        val day = Integer.parseInt(s.trim { it <= ' ' })
        var day_temp = ""
        if (day == Calendar.SUNDAY) {
            day_temp = "Sun"
        } else if (day == Calendar.MONDAY) {
            day_temp = "Mon"
        } else if (day == Calendar.TUESDAY) {
            day_temp = "Tue"
        } else if (day == Calendar.WEDNESDAY) {
            day_temp = "Wed"
        } else if (day == Calendar.THURSDAY) {
            day_temp = "Thurs"
        } else if (day == Calendar.FRIDAY) {
            day_temp = "Fri"
        } else if (day == Calendar.SATURDAY) {
            day_temp = "Sat"
        }
        return day_temp
    }

    fun getDay(s: String): String {
        var day_temp = ""
        if (s.trim { it <= ' ' } == "1") {
            day_temp = "Mon"
        } else if (s.trim { it <= ' ' } == "2") {
            day_temp = "Tue"
        } else if (s.trim { it <= ' ' } == "3") {
            day_temp = "Wed"
        } else if (s.trim { it <= ' ' } == "4") {
            day_temp = "Thurs"
        } else if (s.trim { it <= ' ' } == "5") {
            day_temp = "Fri"
        } else if (s.trim { it <= ' ' } == "6") {
            day_temp = "Sat"
        } else if (s.trim { it <= ' ' } == "7") {
            day_temp = "Sun"
        }
        return day_temp
    }
    /* return as yyyy-MM-dd */
    fun getFormattedDate(date: Date): String {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.time = date
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val result = Date(calendar.time.time)
        Log.d(TAG, "getFormattedDate: " + dateFormat.format(result))
        return dateFormat.format(result)
    }

    /* return as given pattern */
    fun getFormattedDate(date: Date?, pattern: String?): String {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.time = date
        val dateFormat: DateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val result = Date(calendar.time.time)
        Log.d(TAG, "getFormattedDate: " + dateFormat.format(result))
        return dateFormat.format(result)
    }

    fun getDateByGivenString(dateValue: String?, pattern: String?): Date? {
//        String dtStart = "2010-10-15T09:27:37Z";
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        var date: Date? = null
        if (dateValue!=null) {
            try {
                date = format.parse(dateValue)
                println(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return date
    }
}
