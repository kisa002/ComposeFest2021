package com.example.compose.rally

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Rule
import org.junit.Test

/**
 * Created by GwangMoo You on 2021/12/10.
 */
class RallyNavHostTest {
    // 3.4.0 AOS 12 오류 발생
    // androidTestImplementation "androidx.test.espresso:espresso-core:3.5.0-alpha03"

    @get:Rule
    val composeTestRule = createComposeRule()
    lateinit var navController: NavHostController

//    @Test
//    fun test() {
//        assertEquals(1, 1)
//    }
//    @Test
//    fun rallyNavHost() {
//        composeTestRule.setContent {
//            navController = rememberNavController()
//            RallyNavHost(navController = navController)
//        }
//        fail()
//    }

//    @Test
//    fun rallyNavHost_navigateToAllAccounts_callingNavigate() {
//        runBlocking {
//            withContext(Dispatchers.Main) {
//                navController.navigate(RallyScreen.Accounts.name)
//            }
//        }
//        composeTestRule
//            .onNodeWithContentDescription("Accounts Screen")
//            .assertIsDisplayed()
//    }

//    @Test
//    fun rallyNavHost_navigateToAllAccounts_viaUI() {
//        // When click on "All Bills"
//        composeTestRule.onNodeWithContentDescription("All Bills").apply {
//            performScrollTo()
//            performClick()
//        }
//        // Then the route is "Bills"
//        val route = navController.currentBackStackEntry?.destination?.route
//        assertEquals(route, "Bills")
//    }
}