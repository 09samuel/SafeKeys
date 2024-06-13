// ExitBroadcastReceiver.kt
package com.example.safekeys.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.example.safekeys.main.MainActivity
import javax.inject.Inject
import kotlin.system.exitProcess

class ExitBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "EXIT_ACTION") {
            val exitIntent = Intent(context, MainActivity::class.java).apply {
                action = "EXIT_ACTION"
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context?.startActivity(exitIntent)
        }
    }
}
