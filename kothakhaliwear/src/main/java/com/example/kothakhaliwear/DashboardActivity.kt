package com.example.kothakhaliwear

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.TextView
import android.widget.Toast
import com.example.kothakhaliwear.repository.ClientRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class DashboardActivity : WearableActivity() {

    private lateinit var tvcount:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Enables Always-on
        setAmbientEnabled()

        tvcount=findViewById(R.id.tvcount)
        loadAds()


    }

    private fun loadAds() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val adRepository = ClientRepository()
                val response = adRepository.getallads()

                if (response.success == true) {
                    val count = response.count.toString()

                    tvcount.text = "The number of Ads are: $count"
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                            this@DashboardActivity,
                            "Error : $ex", Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}