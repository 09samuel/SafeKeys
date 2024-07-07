package com.example.safekeys

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.safekeys.di.DatabaseModule
import com.example.safekeys.main.HiltTestActivity
import com.example.safekeys.navigation.Screen
import com.example.safekeys.ui.screens.HomeScreen
import com.example.safekeys.ui.theme.SafeKeysTheme
import com.example.safekeys.utils.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
class CredentialEndToEndTest {
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
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home
                ) {
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
    fun  saveNewNote_deleteAfterwards(){
        //Click on FAB to get to Add Credential Dialogue
        composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.add_credential)).performClick()

        // Enter texts in text fields
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).performTextInput("title-text")
        composeRule.onNodeWithTag(TestTags.WEBSITE_TEXT_FIELD).performTextInput("website-text")
        composeRule.onNodeWithTag(TestTags.PASSWORD_TEXT_FIELD).performTextInput("password-text")
        composeRule.onNodeWithTag(TestTags.USERNAME_TEXT_FIELD).performTextInput("username-text")
        //Save new credential
        composeRule.onNodeWithContentDescription("Save Credential").performClick()

        // Make sure there is a note in the list with our title and password
        composeRule.onNodeWithText("title-text").assertIsDisplayed()
        composeRule.onNodeWithText("password-text").assertIsDisplayed()
        composeRule.onNodeWithText("username-text").assertIsDisplayed()
        //delete the credential
        composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.delete_credential))
        // Make sure the update was applied to the list
        composeRule.onNodeWithText("title-text").assertIsDisplayed()
    }

    @Test
    fun saveNewCredentials_orderByTitle() {
        for (i in 3 downTo 1) {
            //Click on FAB to get to Add Credential Dialogue
            composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.add_credential))
                .performClick()

            // Enter texts in title and content text fields
            // Enter texts in text fields
            composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).performTextInput("title$i")
            composeRule.onNodeWithTag(TestTags.WEBSITE_TEXT_FIELD).performTextInput("website$i")
            composeRule.onNodeWithTag(TestTags.PASSWORD_TEXT_FIELD).performTextInput("password$i")
            composeRule.onNodeWithTag(TestTags.USERNAME_TEXT_FIELD).performTextInput("username$i")
            //Save new credential
            composeRule.onNodeWithContentDescription("Save Credential").performClick()
        }

        composeRule.onNodeWithText("title3").assertIsDisplayed()
        composeRule.onNodeWithText("title2").assertIsDisplayed()
        composeRule.onNodeWithText("title1").assertIsDisplayed()

        composeRule.onNodeWithText("TITLE").performClick()

        composeRule.onAllNodesWithTag(TestTags.CREDENTIAL_ITEM)[0]
            .assertTextContains("title1")
        composeRule.onAllNodesWithTag(TestTags.CREDENTIAL_ITEM)[1]
            .assertTextContains("title2")
        composeRule.onAllNodesWithTag(TestTags.CREDENTIAL_ITEM)[2]
            .assertTextContains("title3")
    }

        @Test
        fun saveNewCredentials_orderByDate() {
            for (i in 3 downTo 1) {
                //Click on FAB to get to Add Credential Dialogue
                composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.add_credential))
                    .performClick()

                // Enter texts in title and content text fields
                // Enter texts in text fields
                composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).performTextInput("title$i")
                composeRule.onNodeWithTag(TestTags.WEBSITE_TEXT_FIELD).performTextInput("website$i")
                composeRule.onNodeWithTag(TestTags.PASSWORD_TEXT_FIELD)
                    .performTextInput("password$i")
                composeRule.onNodeWithTag(TestTags.USERNAME_TEXT_FIELD)
                    .performTextInput("username$i")
                //Save new credential
                composeRule.onNodeWithContentDescription("Save Credential").performClick()

                runBlocking { delay(1000) }
            }

            composeRule.onNodeWithText("title3").assertIsDisplayed()
            composeRule.onNodeWithText("title2").assertIsDisplayed()
            composeRule.onNodeWithText("title1").assertIsDisplayed()

            composeRule.onNodeWithText("DATE_CREATED").performClick()

            composeRule.onAllNodesWithTag(TestTags.CREDENTIAL_ITEM)[0]
                .assertTextContains("title3")
            composeRule.onAllNodesWithTag(TestTags.CREDENTIAL_ITEM)[1]
                .assertTextContains("title2")
            composeRule.onAllNodesWithTag(TestTags.CREDENTIAL_ITEM)[2]
                .assertTextContains("title1")
        }

}