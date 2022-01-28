package com.example.kothakhali

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.example.kothakhali.activity.api.ServiceBuilder
import com.example.kothakhali.activity.model.AdList
import com.example.kothakhali.activity.ui.ViewAdActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class AddWishlistUIInstrumentedTesting {

    @get:Rule
    val testRule = ActivityScenarioRule(ViewAdActivity::class.java)

    @Test
    fun checkWishlist(){
        ServiceBuilder.token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiI2MDNjN2I1YmMwNjM1NTMyZTBjM2JhMjciLCJpYXQiOjE2MTg3MjIxMDJ9.RU0WgefZBR8ke0K5wk15bz_n5I2gwisJ84dRYgwwAoU"

        val ad= AdList("605f4ae49a593a2bfcbc060f","603c7b5bc0635532e0c3ba27","Koteshwor Hostel","3","Kathmandu","Koteshwor","Mahadevstan","16168578282422.jpg","Hostel","15000","No","Available","Clean & Healty Hostel with 24 hr water and 3 time food.","2021-03-27T15:10:28.256+00:00")
        Intent().putExtra("ad",ad)

        Espresso.onView(ViewMatchers.withId(R.id.btnwishlist))
                .perform(ViewActions.click())

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.bottomnavigation))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }
}