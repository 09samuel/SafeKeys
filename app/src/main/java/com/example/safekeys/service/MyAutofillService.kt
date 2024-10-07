package com.example.safekeys.service

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.app.assist.AssistStructure
import android.app.assist.AssistStructure.ViewNode
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import android.os.CancellationSignal
import android.service.autofill.AutofillService
import android.service.autofill.Dataset
import android.service.autofill.FillCallback
import android.service.autofill.FillRequest
import android.service.autofill.FillResponse
import android.service.autofill.SaveCallback
import android.service.autofill.SaveInfo
import android.service.autofill.SaveRequest
import android.telephony.TelephonyManager
import android.text.InputType
import android.util.Log
import android.view.autofill.AutofillId
import android.view.autofill.AutofillValue
import android.widget.RemoteViews
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.safekeys.R
import com.example.safekeys.data.model.Credential
import com.example.safekeys.domain.repository.CredentialRepository
import com.example.safekeys.domain.repository.UserRepository
import com.example.safekeys.utils.CryptoManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class MyAutofillService : AutofillService() {

    private val coroutineScope = CoroutineScope(context = Dispatchers.Unconfined)

    @Inject
    lateinit var credentialRepository: CredentialRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var cryptoManager: CryptoManager

//    private var authResultReceiver = Broadcast()

    var currentFillCallback: FillCallback? = null
    private var usernameHints = arrayOf<String>()
    private var passwordHints = arrayOf<String>()

    private var ready = false
    private var idPackage = ""
    private var viewWebDomain = ""
    private var saveUsername = ""
    private var savePassword = ""
    private var usernameId = mutableListOf<AutofillId>()
    private var passwordId = mutableListOf<AutofillId>()
    private var fillResponse = FillResponse.Builder()


    //private val autofillResultReceiver = AutofillResultBroadcastReceiver()

    //private val authenticationResultReceiver = AuthenticationResultReceiver()

    override fun onCreate() {

        super.onCreate()

//        try {
//            val filter = IntentFilter("com.example.AUTHENTICATION_RESULT")
//            this.registerReceiver(authenticationResultReceiver, filter)
//            Log.i("register12345","created")
//        } catch (e: Exception) {
//            Log.i("register12345","e.toString()")
//        }

        usernameHints = resources.getStringArray(R.array.username_hints)
        passwordHints = resources.getStringArray(R.array.password_hints)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onFillRequest(
        request: FillRequest,
        cancellationSignal: CancellationSignal,
        callback: FillCallback
    ) {
        coroutineScope.launch {
            ready = false
            idPackage = ""
            viewWebDomain = ""
            saveUsername = ""
            savePassword = ""
            usernameId = mutableListOf()
            passwordId = mutableListOf()
            fillResponse = FillResponse.Builder()

            val context = request.fillContexts
            val structure = context.last().structure

            traverseStructure(structure = structure, mode = false)

            if (passwordId.isNotEmpty() && usernameId.isNotEmpty()) {
//                if (!isUserLoggedIn()) {
//                    Log.i("AutofillService123", "1")
//                    currentFillCallback = callback
//                    requestAuthentication()
//                } else {
                    Log.i("AutofillService123", "2")
                    fetchAndFillCredentials(callback)
                //}
            } else {
                Log.i("AutofillService", "No password or username IDs found")
            }
        }
    }

    private fun fetchAndFillCredentials(callback: FillCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val credentials =
                    credentialRepository.getCredentialsForAutofill(viewWebDomain)

                if (credentials.isNotEmpty()) {

                    val fillResponseBuilder =
                        FillResponse.Builder()  // Create a FillResponse builder

                    for (c in credentials) {
                        val decryptedPassword = cryptoManager.decrypt(c.iv, c.password)

                        // Create a new RemoteViews object for each credential
                        val credentialsPresentation = RemoteViews(packageName, R.layout.autofill)
                        credentialsPresentation.setTextViewText(R.id.username, c.username)
                        credentialsPresentation.setTextViewText(
                            R.id.label,
                            decryptedPassword.decodeToString()
                        )

                        // Create a new Dataset builder for each credential
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
                    // Set SaveInfo once after adding all datasets
                    val saveInfo = SaveInfo.Builder(
                        SaveInfo.SAVE_DATA_TYPE_USERNAME or SaveInfo.SAVE_DATA_TYPE_PASSWORD,
                        arrayOf(usernameId.last(), passwordId.last())
                    ).build()

                    fillResponseBuilder.setSaveInfo(saveInfo)

                    ready = true
                    callback.onSuccess(fillResponseBuilder.build())  // Build and return the FillResponse
                } else {
                    callback.onFailure("No credentials found")
                }

            } catch (e: Exception) {
                Log.e("AutofillService", "Error fetching credentials", e)
                callback.onFailure(e.message)
            }
        }
    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
        idPackage = ""
        viewWebDomain = ""

        saveUsername = ""
        savePassword = ""

        fillResponse = FillResponse.Builder()
        usernameId = mutableListOf()
        passwordId = mutableListOf()
        ready = false

        val context = request.fillContexts
        val structure = context.last().structure

        traverseStructure(structure = structure, mode = true)

        if (saveUsername.isNotEmpty() && savePassword.isNotEmpty()) {

            val cryptoManager = CryptoManager()
            val bytes = savePassword.encodeToByteArray()
            val encryptedData = cryptoManager.encrypt(bytes)

            val credential = Credential(
                username = saveUsername,
                password = encryptedData.encryptedBytes,
                website = viewWebDomain,
                title = viewWebDomain,
                iv = encryptedData.iv,
                dateCreated = LocalDateTime.now()
            )

            CoroutineScope(Dispatchers.IO).launch {
                credentialRepository.upsertCredential(credential)
            }

            coroutineScope.launch {
                try {
                    callback.onSuccess()
                } catch (e: Exception) {
                    Log.i("saveinfo", "save cred failed")
                }
            }
        }
    }

    private fun traverseStructure(structure: AssistStructure, mode: Boolean) {
        val windowNodes =
            structure.run { (0 until windowNodeCount).map { getWindowNodeAt(it) } }
        windowNodes.forEach { traverseNode(viewNode = it.rootViewNode, mode = mode) }
    }

    @SuppressLint("RestrictedApi")
    private fun traverseNode(viewNode: ViewNode, mode: Boolean) {
        viewWebDomain = getWebsiteOrAppName(viewNode)

        if (!mode) {
            if (usernameId.isNotEmpty() && passwordId.isNotEmpty() && !ready) {
                ready = true
            }
            if (checkUsernameHints(viewNode = viewNode) &&
                !usernameId.contains(element = viewNode.autofillId)
            ) {
                usernameId.add(element = viewNode.autofillId!!)

                if (usernameId.size == passwordId.size) ready = false
            } else if (checkPasswordHints(viewNode = viewNode) &&
                !passwordId.contains(element = viewNode.autofillId)
            ) {
                passwordId.add(element = viewNode.autofillId!!)

                if (passwordId.size < usernameId.size) ready = false
            } else fillResponse.setIgnoredIds(viewNode.autofillId)

        } else {
            if (checkUsernameHints(viewNode = viewNode) && viewNode.text?.isNotEmpty() == true)
                saveUsername = viewNode.text.toString()
            else if (checkPasswordHints(viewNode = viewNode) && viewNode.text?.isNotEmpty() == true)
                savePassword = viewNode.text.toString()
            else fillResponse.setIgnoredIds(viewNode.autofillId)
        }

        val children = viewNode.run { (0 until childCount).map { getChildAt(it) } }
        children.forEach { childNode -> traverseNode(viewNode = childNode, mode = mode) }
    }

    private fun checkUsernameHints(viewNode: ViewNode): Boolean {
        return usernameHints.any { hint ->
            viewNode.autofillHints?.any {
                it.contains(other = hint, ignoreCase = true) ||
                        hint.contains(other = it, ignoreCase = true)
            } == true || viewNode.hint?.contains(other = hint, ignoreCase = true) == true ||
                    hint.contains(other = viewNode.hint.toString(), ignoreCase = true)
        }
    }

    private fun checkPasswordHints(viewNode: ViewNode): Boolean {
        return passwordHints.any { hint ->
            viewNode.autofillHints?.any {
                it.contains(other = hint, ignoreCase = true) ||
                        hint.contains(other = it, ignoreCase = true)
            } == true || viewNode.hint?.contains(other = hint, ignoreCase = true) == true ||
                    hint.contains(other = viewNode.hint.toString(), ignoreCase = true)
        } && (viewNode.inputType and InputType.TYPE_TEXT_VARIATION_PASSWORD == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                viewNode.inputType and InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD ||
                viewNode.inputType and InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ||
                viewNode.inputType and InputType.TYPE_NUMBER_VARIATION_PASSWORD == InputType.TYPE_NUMBER_VARIATION_PASSWORD ||
                viewNode.inputType and InputType.TYPE_DATETIME_VARIATION_NORMAL == InputType.TYPE_DATETIME_VARIATION_NORMAL) // this is necessary for autofill to work on Amazon's apps
    }

    private fun getWebsiteOrAppName(viewNode: ViewNode): String {
        val packageName = viewNode.idPackage ?: ""
        val webDomain = viewNode.webDomain ?: ""

        val domain = webDomain.removePrefix("www.")
        return when {
            domain.isNotEmpty() -> {
                // Extract the main domain name
                domain.substringBefore('.').replaceFirstChar { it.uppercaseChar() }

            }

            packageName.isNotEmpty() -> {
                // Convert the package name to a readable format
                val appName = packageName.substringAfterLast(".")

                try {
                    // Try to get the application label using the package manager
                    packageManager.getApplicationLabel(
                        packageManager.getApplicationInfo(packageName, 0)
                    ).toString()

                } catch (e: Exception) {
                    // Fallback to a readable version of the package name
                    appName
                }
            }

            else -> "Unknown"
        }
    }

    private fun isUserLoggedIn(): Boolean {
        return userRepository.isUserLoggedIn()
    }


//    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//        // Handle the returned Uri
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//        try {
//            unregisterReceiver(authenticationResultReceiver)
//        } catch (e: Exception) {
//            Log.i("register12345",e.toString())
//        }
//
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            MY_CHILD_ACTIVITY -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    // TODO Extract the data returned from the child Activity.
//                    val returnValue = data.getStringExtra("some_key")
//                }
//            }
//        }
//    }

}

//class AuthenticationResultReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent) {
//        Log.i("AuthenticationResultReceiver", "Broadcast received")
//        if (intent.action == "com.example.AUTHENTICATION_RESULT") {
//            val datasetName = intent.getStringExtra("MY_EXTRA_DATASET_NAME")
//            val fillResponse: FillResponse? = intent.getParcelableExtra("EXTRA_AUTHENTICATION_RESULT")
//            val callback: FillCallback? = (context as? MyAutofillService)?.currentFillCallback
//
//            if (fillResponse != null) {
//                callback?.onSuccess(fillResponse)
//                Log.i("AuthenticationResultReceiver", "Fill response received")
//                // Process the fillResponse
//            } else {
//                Log.i("AuthenticationResultReceiver", "No fill response received")
//                // Handle the error or cancellation
//                callback?.onFailure("Authentication failed or cancelled")
//            }
//        }
//    }
//}
