package com.example.chat

object Constants {

    const val BASE_URL = "https://xaltura.by/api/"
    const val BASE_IMAGE_URL = "https://xaltura.by/resources/userphotos/"

    const val PAGE_SIZE = 15
    const val MAX_SIZE = PAGE_SIZE * 3

    const val SETTINGS_NAME = "xaltura_settings"

    const val MAX_MINUTES_FOR_RESTORE = 30

    const val MAX_HOURS_FOR_RESTORE_BASIC_ENTITIES = 4
    const val MAX_HOURS_FOR_RESTORE_ACTION_TYPES = 2

    const val LOGIN_LENGTH_MIN = 3
    const val LOGIN_LENGTH_MAX = 20

    const val PASSWORD_LENGTH_MIN = 8
    const val PASSWORD_LENGTH_MAX = 50

    const val AD_TITLE_LENGTH_MIN = 5
    const val AD_TITLE_LENGTH_MAX = 50

    const val AD_COST_MIN = 5
    const val AD_COST_MAX = 999

    const val AD_TEXT_LENGTH_MIN = 5
    const val AD_TEXT_LENGTH_MAX = 9999

    const val COMMENT_LENGTH_MIN = 5
    const val COMMENT_LENGTH_MAX = 500

    const val NO_CONNECTION_CODE = 1000

    const val STATUS_CODE_EMPTY = -1
    const val STATUS_CODE_SUCCESS = 0
    const val STATUS_CODE_SUCCESS_AND_TOKEN_REMOVE = 995
    const val WRONG_LOGIN_OR_PASSWORD_EXCEPTION = 2
    const val TOO_MANY_TRY_AUTHORIZE_EXCEPTION = 3
    const val DATA_ERROR_EXCEPTION = 4
    const val NULL_RESULT_EXCEPTION = 5
    const val NOT_ENOUGH_PRIVILEGES_EXCEPTION = 6
    const val AD_NOT_FOUND_EXCEPTION = 7
    const val NOT_ENOUGH_MONEY_EXCEPTION = 8
    const val AD_ALREADY_HAVE_INVITE_EXCEPTION = 9
    const val USER_NOT_FOUND_EXCEPTION = 10
    const val USER_IS_NOT_BLOCKED_EXCEPTION = 11
    const val USER_IS_BLOCKED_EXCEPTION = 12
    const val PHOTO_IS_NOT_VALID_EXCEPTION = 13
    const val PHOTO_IS_TOO_LARGE_EXCEPTION = 14
    const val EXCEPTION = 15
    const val LOGIN_EXIST_EXCEPTION = 16
    const val EMAIL_EXIST_EXCEPTION = 17
    const val EMAIL_NOT_FOUND_EXCEPTION = 18
    const val QUERY_ON_CHANGE_PASSWORD_EXIST_EXCEPTION = 19
    const val WRONG_TOKEN_EXCEPTION = 20
    const val SESSION_NOT_FOUND_EXCEPTION = 21
    const val DESTINATION_USER_IS_BLOCKED_EXCEPTION = 22
    const val WORKER_ALREADY_CONNECT_TO_AD_EXCEPTION = 23
    const val PRIVACY_POLICY_NO_ACCEPT_EXCEPTION = 24
    const val USE_RULES_NO_ACCEPT_EXCEPTION = 25
    const val CONSENT_ON_PERSONAL_DATA_PROCESSING_NO_ACCEPT_EXCEPTION = 26

    const val DB_SETTINGS_REGISTRATION_PHOTO = "registration_photo"

    const val DB_SETTINGS_CREATE_AD_PHOTO = "create_ad_photo"

    const val DB_SETTINGS_USER_EDIT_PHOTO = "user_edit_photo"
}
