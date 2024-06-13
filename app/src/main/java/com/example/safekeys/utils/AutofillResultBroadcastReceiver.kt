package com.example.safekeys.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.safekeys.main.MainActivity
import com.example.safekeys.service.MyAutofillService

class AutofillResultBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "AUTOFILL_ACTION") {

        }
    }

}