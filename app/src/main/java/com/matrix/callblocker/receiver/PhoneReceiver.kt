package com.matrix.callblocker.receiver

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.android.internal.telephony.ITelephony
import com.matrix.callblocker.domain.repository.ContactRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import java.lang.reflect.Method
import javax.inject.Inject


private const val REQUEST_ACTION_REC = "android.intent.action.PHONE_STATE"

@AndroidEntryPoint
class PhoneReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: ContactRepository

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == REQUEST_ACTION_REC ) {
            val phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (phoneState == TelephonyManager.EXTRA_STATE_RINGING && phoneNumber != null) {
                goAsync {
                    val contact = repository.searchContact(phoneNumber).firstOrNull()
                    Log.d(TAG, "onReceive: $phoneNumber $contact")
                    if (contact != null) {
                        if (cancelCall(context)) {
                            PushNotification.pushNotification(context, contact)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun cancelCall(context: Context): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || telecomManager == null) {
                    return false
                }
                try {
                    telecomManager.endCall()
                    true
                } catch (e: Exception) {
                    Log.d("CallReceiver", "endCall: $e")
                    false
                }

            } else {
                val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val c = Class.forName(telephony.javaClass.name)
                val m: Method = c.getDeclaredMethod("getITelephony")
                m.isAccessible = true
                val telephonyService = m.invoke(telephony) as ITelephony
                telephonyService.endCall()
                true
            }
        } catch (e: Exception) {
            Log.e("PhoneReceiver", "Error ending call: $e")
            false
        }
    }
}