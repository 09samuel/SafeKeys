package com.example.safekeys.main

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.safekeys.navigation.Screen
import com.example.safekeys.ui.auth.AuthViewModel
import com.example.safekeys.ui.home.CredentialViewModel
import com.example.safekeys.ui.screens.HomeScreen
import com.example.safekeys.ui.screens.LoginScreen
import com.example.safekeys.ui.screens.SignUpScreen
import com.example.safekeys.ui.theme.SafeKeysTheme
import com.example.safekeys.utils.NotificationHelper
import com.example.safekeys.utils.SharedPreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var preferenceHelper: SharedPreferenceHelper

    var count = 0

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        if (intent?.action == "EXIT_ACTION") {
            notificationHelper.cancelNotification()
            finishAndRemoveTask()
            return
        }

        // Check for notification permissions
        notificationHelper.checkNotificationPermissions()

        enableEdgeToEdge()
        setContent {
            SafeKeysTheme {
                val credentialViewModel = hiltViewModel<CredentialViewModel>()
                val authViewModel = hiltViewModel<AuthViewModel>()

                val navController = rememberNavController()

                val credentialState by credentialViewModel.state.collectAsState()
                val signUpState by authViewModel.signUpState.collectAsState()
                val loginState by authViewModel.loginState.collectAsState()


                NavHost(
                    navController = navController,
                    startDestination = if (authViewModel.checkRegistration()) if (authViewModel.checkLogin()) Screen.Home else Screen.Login else Screen.SignUp
                ) {
                    composable<Screen.SignUp> {
                        SignUpScreen(
                            viewModel = authViewModel,
                            state = signUpState,
                            onEvent = authViewModel::onSignUpEvent,
                            onClick = {
                                navController.navigate(Screen.Home)
                            }
                        )
                    }
                    composable<Screen.Login> {
                        LoginScreen(
                            viewModel = authViewModel,
                            state = loginState,
                            onEvent = authViewModel::onLoginEvent,
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
                                notificationHelper.cancelNotification()
                                finishAndRemoveTask()
                                return@BackHandler
                            }
                        })

                        HomeScreen(
                            state = credentialState,
                            viewModel = credentialViewModel,
                            onEvent = credentialViewModel::onEvent,
                            onClick = {
                                if (navController.canGoBack) {
                                    navController.popBackStack()
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Extension property to check if navigation controller can go back
    private val NavHostController.canGoBack: Boolean
        get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

    override fun onStop() {

        super.onStop()
//        Handler(Looper.getMainLooper()).postDelayed({
//            finishAndRemoveTask()
//        }, 3000)
        count++
        preferenceHelper.setUserLogOut()
    }

    override fun onRestart() {
        super.onRestart()
        count--
        preferenceHelper.setUserLogin()
    }

}



