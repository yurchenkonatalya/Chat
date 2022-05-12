package com.example.chat

object InfoHelper {
    fun getDeviceName(): String {
        return android.os.Build.MODEL
    }

    fun checkPhoneIsValid(phone: String): Boolean {
        if (phone.length != 9)
            return false
        val code = phone.substring(0, 2)
        if (code != "29" && code != "33" && code != "25" && code != "44")
            return false
        val number = phone.substring(2)
        if (!number.matches(Regex("[0-9]+")))
            return false
        return true
    }

    fun checkEmailIsValid(email: String): Boolean {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return true
        return false
    }

    fun systemDateTimeToDate(dateTime: String): String {
        val date = dateTime.split(" ")[0]
        val dateMas = date.split("-")
        return dateMas.reversed().joinToString(".")
    }

    fun systemDateTimeToDateTime(dateTime: String): String {
        val mas = dateTime.split(" ")
        val date = mas[0]
        val time = mas[1]
        val dateMas = date.split("-")
        val timeMas = time.split(":")
        return dateMas.reversed().joinToString(".") + " " + timeMas[0] + ":" + timeMas[1]
    }
}