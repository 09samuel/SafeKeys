//// NavGraph.kt
//package com.example.safekeys.navigation
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.lifecycle.Lifecycle
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import com.example.safekeys.ui.screens.CredentialsScreen
//import com.example.safekeys.ui.screens.LoginScreen
//import com.example.safekeys.ui.screens.SignUpScreen
//import com.example.safekeys.ui.auth.signup.SignUpViewModel
//import com.example.safekeys.ui.home.CredentialViewModel
//import com.example.safekeys.ui.auth.login.LoginViewModel
//
//@Composable
//fun NavGraph(
//    navController: NavHostController,
//    signUpViewModel: SignUpViewModel,
//    credentialViewModel: CredentialViewModel,
//    loginViewModel: LoginViewModel
//) {
//    val credentialState by credentialViewModel.state.collectAsState()
//    val signUpState by signUpViewModel.signUpState.collectAsState()
//    val loginState by loginViewModel.loginState.collectAsState()
//
//    NavHost(navController = navController, startDestination = if (signUpState.isRegistered) Screen.Login else Screen.SignUp) {
//        composable<Screen.SignUp> {
//            SignUpScreen(
//                state = signUpState,
//                onEvent = signUpViewModel::onEvent,
//                onClick = {
//                    navController.navigate(Screen.Home) {
//                        popUpTo(Screen.SignUp) { inclusive = true }
//                    }
//                }
//            )
//        }
//        composable<Screen.Login> {
//            LoginScreen(
//                state = loginState,
//                onEvent = loginViewModel::onEvent,
//                onClick = {
//                    navController.navigate(Screen.Home) {
//                        popUpTo(Screen.Login) { inclusive = true }
//                    }
//                }
//            )
//        }
//        composable<Screen.Home> {
//            CredentialsScreen(
//                state = credentialState,
//                viewModel = credentialViewModel,
//                onEvent = credentialViewModel::onEvent,
//                onClick = {
//                    if (navController.canGoBack) {
//                        navController.popBackStack()
//                    }
//                }
//            )
//        }
//    }
//}
//
//// Extension property to check if navigation controller can go back
//private val NavHostController.canGoBack: Boolean
//    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED
