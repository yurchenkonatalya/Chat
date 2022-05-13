package com.example.chat

import android.icu.text.SimpleDateFormat
import android.util.Log
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

object InfoHelper {
    fun systemDateTimeToDate(dateTime: String): String {
        val date = dateTime.split(" ")[0]
        val dateMas = date.split("-")
        return dateMas.reversed().joinToString(".")
    }

    fun systemDateTimeToTime(dateTime: String): String {
        val mas = dateTime.split(" ")
        val time = mas[1]
        val timeMas = time.split(":")
        return timeMas[0] + ":" + timeMas[1]
    }

    fun getCurrentTimeIsOnline(serverTime: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val curTime = sdf.format(Date())
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val zone: ZoneId = ZoneId.systemDefault()
        val start: ZonedDateTime = LocalDateTime.parse(serverTime, formatter).atZone(zone)
        val end: ZonedDateTime = LocalDateTime.parse(curTime, formatter).atZone(zone)
        val diffSeconds: Long = ChronoUnit.SECONDS.between(start, end)
        if (diffSeconds <= 10)
            return true
        return false
    }

    fun getTimeOlder2(serverTime: String, curTime: String): Boolean {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val zone: ZoneId = ZoneId.systemDefault()
        val start: ZonedDateTime = LocalDateTime.parse(serverTime, formatter).atZone(zone)
        val end: ZonedDateTime = LocalDateTime.parse(curTime, formatter).atZone(zone)
        val diffSeconds: Long = ChronoUnit.MINUTES.between(start, end)
        Log.e("OLDER", diffSeconds.toString())
        if (kotlin.math.abs(diffSeconds) >= 2)
            return true
        return false
    }
}