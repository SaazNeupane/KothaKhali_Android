package com.example.kothakhali.activity.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.kothakhali.R
import com.example.kothakhali.activity.db.KothaKhaliDB
import com.example.kothakhali.activity.fragments.HomeFragment
import com.example.kothakhali.activity.fragments.OthersFragment
import com.example.kothakhali.activity.fragments.SearchFragment
import com.example.kothakhali.activity.fragments.UserFragment
import com.example.kothakhali.activity.repository.AdRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class DashboardActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var bottomnavigation: BottomNavigationView
    private var db:KothaKhaliDB? = null
    private lateinit var sensorManager: SensorManager
    private var prosensor: Sensor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        bottomnavigation = findViewById(R.id.bottomnavigation)
        menuitem()

        //Retrieve Data
        initialize()
        loadAds()


        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, HomeFragment())
            addToBackStack(null)
            commit()
        }

        //Proximity
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if(!checkproSensor())
            return
        else{
            prosensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            sensorManager.registerListener(this,prosensor,SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun loadAds() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val adRepository = AdRepository()
                val response = adRepository.getallads()

                if (response.success == true) {

                    // Insert all the ads in room database
                    adRepository.insertBulkAds(this@DashboardActivity, response.data!!)

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


    private fun menuitem(){
        bottomnavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.container, HomeFragment())
                        addToBackStack(null)
                        commit()
                    }
                    true
                }
                R.id.search -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.container, SearchFragment())
                        addToBackStack(null)
                        commit()
                    }
                    true
                }
                R.id.user -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.container, UserFragment())
                        addToBackStack(null)
                        commit()
                    }
                    true
                }
                R.id.others -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.container, OthersFragment())
                        addToBackStack(null)
                        commit()
                    }
                    true
                }
                else -> false
            }
        }
    }


    private fun initialize() {
        db = KothaKhaliDB.getInstance(application)
    }

    private fun checkproSensor(): Boolean {
        var flag = true
        if(sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == null){
            flag = false
        }
        return flag
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                val values = event!!.values[0]

                if (values < 0.01) {
                    this.finishAffinity()
                }
            }
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}