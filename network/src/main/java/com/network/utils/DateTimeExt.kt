package com.network.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

const val DATE_FORMAT_SIMPLE = "dd/MM/yyyy"
const val CALENDAR_DATE_FORMAT = "dd/MM/yyyy : HH:mm"
const val DATE_FORMAT_SIMPLE_DASH = "dd-MM-yyyy"
const val CALENDAR_DATE_FORMAT_ONLY = "dd/MM/yyyy"

//format date
fun String.formatDate(inputFormat: String, outputFormat: String): String {
    return try {
        val date = SimpleDateFormat(inputFormat, Locale.getDefault()).parse(this)    // parse input
        SimpleDateFormat(outputFormat, Locale.getDefault()).format(date as Date)    // format output
    } catch (e: ParseException) {
        e.toString()
    }
}

//today date
fun getTodayDate(format: String): String {
    val currentDate = Calendar.getInstance().time
    return currentDate.toString(format)

}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

//convert date to long

fun String.dateToLong(): Long {
    var dateInLong = 0L
    try {
        val sdf = SimpleDateFormat(DATE_FORMAT_SIMPLE, Locale.getDefault())
        val date = sdf.parse(this)
        if (date != null) {
            dateInLong = date.time
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return dateInLong
}


//get two dates difference in days

//
fun dateDifferenceInDays(lastDate: String): Int {
    val d1 = Date(lastDate.dateToLong())
    val d2 = Date(System.currentTimeMillis())
    val diff: Long = d1.time - d2.time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    return days.toInt()
}

fun getYearsDiff(date1: String): Long {
    val d1 = Date(date1.dateToLong())
    val d2 = Date(System.currentTimeMillis())
    val diff: Long = d2.time - d1.time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    return days / 365

}

// new date - old date
// current date - passed date
fun dateDifferenceInDaysForNewDate(startDate: String, endDate: String): Int {
    val d1 = Date(startDate.dateToLong())
    val d2 = Date(endDate.dateToLong())
    val diff: Long = d2.time - d1.time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    return days.toInt()
}

//check if selected time is passed or not
fun isTimeInPast(hourOfDay: Int, minute: Int): Boolean {
    val datetime = Calendar.getInstance()
    val c = Calendar.getInstance()
    datetime[Calendar.HOUR_OF_DAY] = hourOfDay
    datetime[Calendar.MINUTE] = minute
    return datetime.timeInMillis <= c.timeInMillis //returns true if time is passed
}


fun convertLongToDate(time: Long, format: String): String {
    val date = Date(time)
    val format = SimpleDateFormat(format, Locale.getDefault())
    return format.format(date)
}

fun currentTimeToLong(): Long {
    return System.currentTimeMillis()
}

fun convertDateToLong(date: String): Long {
    val df = SimpleDateFormat(CALENDAR_DATE_FORMAT, Locale.getDefault())
    return df.parse(date)?.time ?: 0
}

//create an extension function on a date class which returns a string
fun Date.dateToString(): String {
    //simple date formatter
    val dateFormatter = SimpleDateFormat(CALENDAR_DATE_FORMAT, Locale.getDefault())

    //return the formatted date string
    return dateFormatter.format(this)
}

//today calendar date
fun todayCalendarDate(): Calendar {
    val today = Calendar.getInstance()
    return today
}

//today calendar date
fun yesterdayCalendarDate(): Calendar {
    val today = Calendar.getInstance()
    today.add(Calendar.DATE, -1)
    return today
}

//check if date is less than today or date is in past
fun isDateInPast(date: String): Boolean {
    try {
        return SimpleDateFormat(
            CALENDAR_DATE_FORMAT,
            Locale.getDefault()
        ).parse(date)
            ?.before(todayCalendarDate().time) == true
    } catch (e: Exception) {
        return true
    }
}

//check if date is less than today or date is in past
// this funciton is used for calendar
fun isDateYesterdayOrBefore(date: String): Boolean {
    try {
        return SimpleDateFormat(
            CALENDAR_DATE_FORMAT,
            Locale.getDefault()
        ).parse(date)
            ?.before(yesterdayCalendarDate().time) == true
    } catch (e: Exception) {
        return true
    }
}

//check if date is less than today or date is in past
fun isDateInFuture(date: String): Boolean {
    try {
        return SimpleDateFormat(
            CALENDAR_DATE_FORMAT,
            Locale.getDefault()
        ).parse(date)
            ?.after(todayCalendarDate().time) == true
    } catch (e: Exception) {
        return true
    }

}

fun stringDateToCalendar(date: String, pattern: String? = CALENDAR_DATE_FORMAT): Calendar {
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    val d = format.parse(date)
    val c: Calendar = Calendar.getInstance()
    if (d != null) {
        c.time = d
        c.add(Calendar.DATE, -1)
    }
    return c
}


//returns true if first date is less than second date
fun isDateLessThan(firstDate: String, secondDate: String): Boolean {
    return SimpleDateFormat(
        CALENDAR_DATE_FORMAT,
        Locale.getDefault()
    ).parse(firstDate)
        ?.before(stringDateToCalendar(secondDate).time) == true

}

fun String.todayDate(): String {
    val sdf = SimpleDateFormat(this, Locale.getDefault())
    return try {
        sdf.format(Date())
    } catch (e: Exception) {
        "Format not valid"
    }

}

//string to date conversion
fun String.stringToDate(format: String): Date {
    return SimpleDateFormat(format, Locale.getDefault()).parse(this)!!
}


fun calculateTotalHoursAndMinutes(startTime: String, endTime: String): Pair<String, String> {

    val startHour = getHour(startTime)
    val startMinute = getMinute(startTime)

    val endHour = getHour(endTime)
    val endMinute = getMinute(endTime)

    var totalHours = endHour - startHour
    var totalMinutes = endMinute - startMinute

    if (totalHours < 0) {
        totalHours += 24
    }

    if (totalMinutes < 0) {
        totalMinutes += 60
        totalHours -= 1
    }

    val formattedHours = String.format("%02d", totalHours)
    val formattedMinutes = String.format("%02d", totalMinutes)
    return Pair(formattedHours, formattedMinutes)
}

private fun getHour(time: String): Int {
    val calendar = Calendar.getInstance()
    calendar.time = SimpleDateFormat("hh:mm aa", Locale.getDefault()).parse(time)!!
    return calendar.get(Calendar.HOUR_OF_DAY)
}

private fun getMinute(time: String): Int {
    val calendar = Calendar.getInstance()
    calendar.time = SimpleDateFormat("hh:mm aa", Locale.getDefault()).parse(time)!!
    return calendar.get(Calendar.MINUTE)
}

//extension function to convert the 12-hour format time to the 24-hour format.

fun String.to24HourFormat(): String {
    val inputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val time = inputFormat.parse(this)

    return outputFormat.format(time as Date)
}

/*
add days to date
  val date = "2023/05/18 : 12:44:26"
  val format = "yyyy/MM/dd : HH:mm:ss"
    date.minusDaysFromDate(format, daysToAdd = 6, monthsToAdd = 4, yearsToAdd = 1)
        //out put will be  2024/09/24 : 12:44:26
* */
fun String.addDaysToDate(
    formatPattern: String, daysToAdd: Int? = 0, monthsToAdd: Int? = 0, yearsToAdd: Int? = 0
): String {
    var formattedDate = ""
    try {

        val dateFormat = SimpleDateFormat(formatPattern, Locale.getDefault())
        val date = dateFormat.parse(this)
        val calendar = Calendar.getInstance()
        calendar.time = date as Date
        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd!!)
        calendar.add(Calendar.MONTH, monthsToAdd!!)
        calendar.add(Calendar.YEAR, yearsToAdd!!)

        val addedDate = calendar.time
        formattedDate = dateFormat.format(addedDate)
    } catch (e: Exception) {
        e.localizedMessage
    }
    return formattedDate
}