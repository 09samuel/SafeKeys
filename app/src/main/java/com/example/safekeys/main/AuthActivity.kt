package com.example.safekeys.main

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.os.Bundle
import android.service.autofill.Dataset
import android.service.autofill.FillResponse
import android.util.Log
import android.view.autofill.AutofillId
import android.view.autofill.AutofillManager.EXTRA_AUTHENTICATION_RESULT
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
import com.example.safekeys.domain.repository.CredentialRepository
import com.example.safekeys.ui.auth.AuthViewModel
import com.example.safekeys.ui.screens.AutofillAuthDialogue
import com.example.safekeys.ui.theme.SafeKeysTheme
import com.example.safekeys.utils.CryptoManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {

    @Inject
    lateinit var credentialRepository: CredentialRepository

    @Inject
    lateinit var cryptoManager: CryptoManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
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
        val datasetName: String = intent.getStringExtra("MY_EXTRA_DATASET_NAME").toString()
        val viewWebDomain: String = intent.getStringExtra("WEB_DOMAIN").toString()
        val usernameId: ArrayList<AutofillId> = intent.getParcelableArrayListExtra("USERNAME_IDS")!!
        val passwordId: ArrayList<AutofillId> = intent.getParcelableArrayListExtra("PASSWORD_IDS")!!
        CoroutineScope(Dispatchers.IO).launch {
            val credentials = credentialRepository.getCredentialsForAutofill(viewWebDomain)
            if (credentials.isNotEmpty()) {
                val fillResponseBuilder = FillResponse.Builder()  // Create a FillResponse builder
                for (c in credentials) {
                    val decryptedPassword = cryptoManager.decrypt(c.iv, c.password)
                    val credentialsPresentation =
                        RemoteViews(packageName, com.example.safekeys.R.layout.autofill)
                    credentialsPresentation.setTextViewText(
                        com.example.safekeys.R.id.username,
                        c.username
                    )
                    credentialsPresentation.setTextViewText(
                        com.example.safekeys.R.id.label,
                        decryptedPassword.decodeToString()
                    )
                    val dataset = Dataset.Builder()
                        .setValue(
                            usernameId.last(),
                            AutofillValue.forText(c.username),
                            credentialsPresentation
                        )
                        .setValue(
                            passwordId.last(),
                            AutofillValue.forText(decryptedPassword.decodeToString()),
                            credentialsPresentation
                        )
                    fillResponseBuilder.addDataset(dataset.build())  // Add each dataset to the FillResponse builder
                }
                val replyIntent = Intent().apply {
                    putExtra("MY_EXTRA_DATASET_NAME", datasetName)
                    putExtra(EXTRA_AUTHENTICATION_RESULT, fillResponseBuilder.build())
                }
//                PendingIntent.getActivity(
//                    this@AuthActivity,
//                    1001,
//                    replyIntent,
//                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
//                ).intentSender

                setResult(Activity.RESULT_OK, replyIntent)

                finish()
            } else {
                Log.i("ifjv", "nocred")
            }
        }
    }
}
