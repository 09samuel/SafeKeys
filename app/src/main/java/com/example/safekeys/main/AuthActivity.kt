package com.example.safekeys.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.safekeys.ui.auth.AuthViewModel
import com.example.safekeys.ui.screens.AutofillAuthDialogue
import com.example.safekeys.ui.theme.SafeKeysTheme
import com.example.safekeys.utils.ExitBroadcastReceiver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            Log.d("AuthActivity123", "Activity created")

            setContent {
                SafeKeysTheme {
                    val authViewModel = hiltViewModel<AuthViewModel>()
                    val autofillAuthState by authViewModel.autofillAuthState.collectAsState()

                    Surface(modifier = Modifier.fillMaxSize()) {
                        AutofillAuthDialogue(
                            state = autofillAuthState,
                            onEvent = authViewModel::onAutofillAuthEvent,
                            onLoginSuccess = { sendAuthResult() })
                    }
                }
            }
        } catch (e: Exception) {
            Log.i("efuso", e.toString())
        }
    }

    private fun sendAuthResult() {
        val autofillResultIntent = Intent(this, ExitBroadcastReceiver::class.java).apply {
            action = "AUTOFILL_ACTION"
        }
        this.sendBroadcast(autofillResultIntent)
    }
}
