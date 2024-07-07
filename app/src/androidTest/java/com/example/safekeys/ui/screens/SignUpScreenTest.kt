package com.example.safekeys.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.safekeys.di.DatabaseModule
import com.example.safekeys.main.HiltTestActivity
import com.example.safekeys.navigation.Screen
import com.example.safekeys.ui.auth.AuthViewModel
import com.example.safekeys.ui.screens.HomeScreen
import com.example.safekeys.ui.screens.SignUpScreen
import com.example.safekeys.ui.theme.SafeKeysTheme
import com.example.safekeys.utils.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
class SignUpScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<HiltTestActivity>()

    @Before
    fun setUp(){
        hiltRule.inject()
        composeRule.setContent {
            val authViewModel = hiltViewModel<AuthViewModel>()
            val navController = rememberNavController()
            SafeKeysTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.SignUp
                ) {
                    composable<Screen.SignUp> {
                        SignUpScreen(
                            onClick = {
                                navController.navigate(Screen.Home)
                            }
                        )
                    }
                    composable<Screen.Home> {
                        HomeScreen(
                            navController
                        )
                    }
                }
            }
        }
    }
    @Test
    fun registerNewUser_andHomeScreenDisplayed(){
        //Enter credentials
        composeRule.onNodeWithTag(TestTags.REG_PASSWORD_TEXT_FIELD).performTextInput("12345678A")
        composeRule.onNodeWithTag(TestTags.CONFIRM_PASSWORD_TEXT_FIELD).performTextInput("12345678A")
        composeRule.onNodeWithContentDescription("Register").performClick()

        composeRule.waitUntil {
            composeRule.onNodeWithText("SafeKeys").isDisplayed()
        }

        // Assert that "SafeKeys" text is displayed
        composeRule.onNodeWithText("SafeKeys").assertIsDisplayed()
    }
}