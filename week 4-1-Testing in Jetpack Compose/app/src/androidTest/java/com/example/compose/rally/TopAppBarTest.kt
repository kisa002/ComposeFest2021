package com.example.compose.rally

import android.util.Log
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.compose.rally.ui.components.RallyTopAppBar
import com.example.compose.rally.ui.overview.OverviewBody
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

/**
 * Created by GwangMoo You on 2021/12/10.
 */
class TopAppBarTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun rallyTopAppBarTest_currentTabSelected() {
        val allScreens = RallyScreen.values().toList()
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = { },
                currentScreen = RallyScreen.Accounts
            )
        }

//        composeTestRule
//            .onNodeWithContentDescription(RallyScreen.Accounts.name)
//            .assertIsSelected()

        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")

        // toUpperCase depreacted
        composeTestRule
            .onNode(
                hasText(RallyScreen.Accounts.name.uppercase()) and hasParent(
                    hasContentDescription(
                        RallyScreen.Accounts.name
                    )
                ), useUnmergedTree = true
            )
            .assertExists()
    }

    @Test
    fun optionalExercise() {
        var currentScreen = RallyScreen.Overview
        var passCount = 0

        composeTestRule.setContent {
            RallyApp(currentScreen) {
                print("changeScreen: $it")
                currentScreen = it
            }
        }

        RallyScreen.values().forEach {
            composeTestRule
                .onNodeWithContentDescription(it.name)
                .performClick()

//            assert(currentScreen.name == it.name)
            print("currentScreen.name: ${currentScreen.name} == ${it.name}")
            if (currentScreen.name == it.name)
                passCount++
        }
        print("assert -> $passCount == ${RallyScreen.values().size} ${passCount == RallyScreen.values().size}")

    }

    @Test
    fun overviewScreen_alertsDisplayed() {
        composeTestRule.setContent {
            OverviewBody()
        }

        composeTestRule
            .onNodeWithText("Alerts")
            .assertIsDisplayed()
    }

    fun print(msg: String) = Log.d("Kawai-PangMoo", msg)
}