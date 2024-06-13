package com.example.safekeys.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.service.autofill.Dataset
import android.service.autofill.FillResponse
import android.util.Log
import android.view.autofill.AutofillId
import android.view.autofill.AutofillValue
import android.widget.RemoteViews
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.safekeys.R
import com.example.safekeys.ui.auth.AuthViewModel
import com.example.safekeys.ui.screens.AutofillAuthDialogue
import com.example.safekeys.ui.theme.SafeKeysTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope

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
        Log.i("efuso",e.toString())
        }
    }

    private fun sendAuthResult() {
        val intent = Intent("com.example.safekeys.AUTH_RESULT")
        applicationContext.sendBroadcast(intent)
        finish()
    }
}
