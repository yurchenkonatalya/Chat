package com.example.chat.locale

interface Language {
    val currentLanguage: String

    val frSigninLogin: String
    val frSigninPassword: String
    val frSigninEnter: String
    val frSigninQuestion: String
    val frSigninRegister: String

    val frRegistrationName: String
    val frRegistrationSurname: String
    val frRegistrationMail: String
    val frRegistrationBirthday: String
    val frRegistrationPhone: String
    val frRegistrationLogin: String
    val frRegistrationPassword: String
    val frRegistrationLoadPhoto: String
    val frRegistrationRegister: String

    val frSettingsMail: String
    val frSettingsPhone: String
    val frSettingsBirthday: String
    val frSettingsChangeLanguage: String
    val frSettingsLogoutExit: String

    val frUserWrite: String

    val frDialogWrite: String
    val frDialogLastActive: String
    val frDialogOnline: String
}

object LanguageEn : Language {
    override val currentLanguage: String
        get() = "En"
    override val frSigninLogin: String
        get() = "Login"
    override val frSigninPassword: String
        get() = "Password"
    override val frSigninEnter: String
        get() = "Enter"
    override val frSigninQuestion: String
        get() = "Do you not have an account yet?"
    override val frSigninRegister: String
        get() = "Register"
    override val frRegistrationName: String
        get() = "Name"
    override val frRegistrationSurname: String
        get() = "Surname"
    override val frRegistrationMail: String
        get() = "Mail"
    override val frRegistrationBirthday: String
        get() = "Birthday"
    override val frRegistrationPhone: String
        get() = "Phone"
    override val frRegistrationLogin: String
        get() = "Login"
    override val frRegistrationPassword: String
        get() = "Password"
    override val frRegistrationLoadPhoto: String
        get() = "Load photo"
    override val frRegistrationRegister: String
        get() = "Register:"
    override val frSettingsMail: String
        get() = "Mail:"
    override val frSettingsPhone: String
        get() = "Phone:"
    override val frSettingsBirthday: String
        get() = "Birthday:"
    override val frSettingsChangeLanguage: String
        get() = "Change language"
    override val frSettingsLogoutExit: String
        get() = "Exit"
    override val frUserWrite: String
        get() = "Write"
    override val frDialogWrite: String
        get() = "Write..."
    override val frDialogLastActive: String
        get() = "Last seen"
    override val frDialogOnline: String
        get() = "Online"
}

object LanguageRu : Language {
    override val currentLanguage: String
        get() = "Ru"
    override val frSigninLogin: String
        get() = "??????????"
    override val frSigninPassword: String
        get() = "????????????"
    override val frSigninEnter: String
        get() = "??????????"
    override val frSigninQuestion: String
        get() = "?? ?????? ?????? ?????? ?????????????????"
    override val frSigninRegister: String
        get() = "????????????????????????????????????"
    override val frRegistrationName: String
        get() = "??????"
    override val frRegistrationSurname: String
        get() = "??????????????"
    override val frRegistrationMail: String
        get() = "??????????"
    override val frRegistrationBirthday: String
        get() = "???????? ????????????????"
    override val frRegistrationPhone: String
        get() = "??????????????"
    override val frRegistrationLogin: String
        get() = "??????????"
    override val frRegistrationPassword: String
        get() = "????????????"
    override val frRegistrationLoadPhoto: String
        get() = "?????????????????? ????????"
    override val frRegistrationRegister: String
        get() = "????????????????????????????????????"
    override val frSettingsMail: String
        get() = "??????????:"
    override val frSettingsPhone: String
        get() = "??????????????:"
    override val frSettingsBirthday: String
        get() = "???????? ????????????????:"
    override val frSettingsChangeLanguage: String
        get() = "???????????????? ????????"
    override val frSettingsLogoutExit: String
        get() = "??????????"
    override val frUserWrite: String
        get() = "????????????????"
    override val frDialogWrite: String
        get() = "????????????????..."
    override val frDialogLastActive: String
        get() = "??????(??) ?? ????????"
    override val frDialogOnline: String
        get() = "?? ????????"
}