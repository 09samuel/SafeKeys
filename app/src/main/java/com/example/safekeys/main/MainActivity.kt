package com.example.safekeys.main

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.service.autofill.FillResponse
import android.util.Log
import android.view.autofill.AutofillManager.EXTRA_AUTHENTICATION_RESULT
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.safekeys.navigation.Screen
import com.example.safekeys.ui.NotificationViewModel
import com.example.safekeys.ui.auth.AuthViewModel
import com.example.safekeys.ui.screens.HomeScreen
import com.example.safekeys.ui.screens.LoginScreen
import com.example.safekeys.ui.screens.SignUpScreen
import com.example.safekeys.ui.theme.SafeKeysTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val notificationViewModel: NotificationViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent?.action == "EXIT_ACTION") {
            notificationViewModel.cancelNotification()
            finishAndRemoveTask()
            return
        }

        // Check for notification permissions
        notificationViewModel.checkNotificationPermissions(this)

        enableEdgeToEdge()
        setContent {
            SafeKeysTheme {

                //val authViewModel = hiltViewModel<AuthViewModel>()
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = if (authViewModel.checkRegistration()) if (authViewModel.checkLogin()) Screen.Home else Screen.Login else Screen.SignUp
                ) {
                    composable<Screen.SignUp> {
                        SignUpScreen(
                            onClick = {
                                navController.navigate(Screen.Home)
                            }
                        )
                    }
                    composable<Screen.Login> {
                        LoginScreen(
                            onClick = {
                                navController.navigate(Screen.Home)
                            }
                        )
                    }
                    composable<Screen.Home> {
                        var backPressCount = 0
                        BackHandler(true, onBack = {
                            if (backPressCount < 1) {

                                backPressCount++
                                Toast.makeText(
                                    this@MainActivity,
                                    "Press back again to exit",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                notificationViewModel.cancelNotification()
                                finishAndRemoveTask()
                                return@BackHandler
                            }
                        })

                        HomeScreen(
                           navController
                        )
                    }
                }
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        super.onDestroy()

    }

    // Extension property to check if navigation controller can go back
    private val NavHostController.canGoBack: Boolean
        get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

    override fun onStop() {

        super.onStop()
        if (authViewModel.checkLogin()) {
            authViewModel.setLogOut()
        }
    }

    override fun onRestart() {
        super.onRestart()
        authViewModel.setLogin()
    }

//    fun setContentInTemplate(content: @Composable () -> Unit) {
//        runOnUiThread {
//            composeView.setContent(content)
//        }
//    }

//    private val authenticationResultReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            // Handle the authentication result
//            if (intent?.action == "com.example.AUTHENTICATION_RESULT") {
//                // Process the result here
//                val datasetName = intent.getStringExtra("MY_EXTRA_DATASET_NAME")
//                val fillResponse: FillResponse = intent.getParcelableExtra(EXTRA_AUTHENTICATION_RESULT)!!
//
//                if (fillResponse != null) {
////                callback?.onSuccess(fillResponse)
//                Log.i("AuthenticationResultReceiver", "Fill response received")
////                // Process the fillResponse
//           } else {
//                Log.i("AuthenticationResultReceiver", "No fill response received")
////                // Handle the error or cancellation
////                callback?.onFailure("Authentication failed or cancelled")
//            }
//            }
//        }
//    }

}



