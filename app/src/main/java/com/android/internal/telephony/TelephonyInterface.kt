package com.android.internal.telephony

interface TelephonyInterface {
    fun endCall(): Boolean
    fun answerRingingCall()
    fun silenceRinger()
}
