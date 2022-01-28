package com.example.kothakhali

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.example.kothakhali.activity.api.ServiceBuilder
import com.example.kothakhali.activity.ui.LoginActivity
import com.example.kothakhali.activity.ui.UpdateDetailsActivity
import com.example.kothakhali.activity.ui.ViewAdActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class UpdateClientDetailsUIInstrumentedTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(UpdateDetailsActivity::class.java)

    @Test
    fun checkUpdate(){

        ServiceBuilder.token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiI2MDNjN2I1YmMwNjM1NTMyZTBjM2JhMjciLCJpYXQiOjE2MTg3MjIxMDJ9.RU0WgefZBR8ke0K5wk15bz_n5I2gwisJ84dRYgwwAoU"
        Espresso.onView(ViewMatchers.withId(R.id.etuname))
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText("Saaz Neupane"))

        Espresso.onView(ViewMatchers.withId(R.id.etumobile))
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeText("9851168289"))

        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.btnupdatedetails))
                .perform(ViewActions.click())

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.bottomnavigation))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}