package com.example.kothakhali

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.example.kothakhali.activity.ui.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class LoginUIInstrumentedTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun checkLogin(){
        onView(withId(R.id.etlemail))
            .perform(ViewActions.typeText("saazneu789@gmail.com"))

        onView(withId(R.id.etlpassword))
            .perform(ViewActions.typeText("12345"))

        closeSoftKeyboard()

        onView(withId(R.id.btnlogin))
            .perform(ViewActions.click())

        Thread.sleep(2000)

        onView(withId(R.id.bottomnavigation))
            .check(ViewAssertions.matches(isDisplayed()))
    }
}