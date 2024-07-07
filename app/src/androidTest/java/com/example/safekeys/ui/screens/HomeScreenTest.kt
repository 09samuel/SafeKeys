package com.example.safekeys.ui.screens


import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.safekeys.R
import com.example.safekeys.di.DatabaseModule
import com.example.safekeys.main.HiltTestActivity
import com.example.safekeys.navigation.Screen
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
class HomeScreenTest{

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<HiltTestActivity>()

    @Before
    fun setUp(){
        hiltRule.inject()
        composeRule.setContent {
            val navController = rememberNavController()
            SafeKeysTheme {
               NavHost(navController = navController, startDestination = Screen.Home) {
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
    fun clickToggleAddCredentialSection_isVisible() {
        composeRule.onNodeWithTag(TestTags.CREDENTIAL_DIALOGUE).assertDoesNotExist()

        composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.add_credential)).performClick()
        composeRule.onNodeWithTag(TestTags.CREDENTIAL_DIALOGUE).assertIsDisplayed()
    }

}