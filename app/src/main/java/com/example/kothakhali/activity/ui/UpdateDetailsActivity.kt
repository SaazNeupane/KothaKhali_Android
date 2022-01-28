package com.example.kothakhali.activity.ui

import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.kothakhali.R
import com.example.kothakhali.activity.model.Client
import com.example.kothakhali.activity.notificatons.NotificationChannels
import com.example.kothakhali.activity.repository.AdRepository
import com.example.kothakhali.activity.repository.ClientRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class UpdateDetailsActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var etname: EditText
    private lateinit var etmobile: EditText
    private lateinit var btnupdate: Button
    private lateinit var sensorManager: SensorManager
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    private var clientDetails: Client? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_details)

        etname=findViewById(R.id.etuname)
        etmobile=findViewById(R.id.etumobile)
        btnupdate=findViewById(R.id.btnupdatedetails)

        loadclient()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH


        //button
        btnupdate.setOnClickListener {
                update()
        }

    }

    private fun update(){
        val name=etname.text.toString()
        val mobile=etmobile.text.toString()

        val details = Client(name=name,mobile = mobile)

        CoroutineScope(Dispatchers.IO).launch{
            try{
                val clientRepository = ClientRepository()
                val response = clientRepository.update(details)
                if (response.success == true){
                    withContext(Dispatchers.Main){
                        showNotification("${response.message}")
                        startActivity(
                            Intent(
                                this@UpdateDetailsActivity,
                                DashboardActivity::class.java
                            )
                        )
                    }
                }
            }
            catch (ex:Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@UpdateDetailsActivity,
                        ex.toString(),
                        Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun loadclient() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val clientRepository = ClientRepository()
                val response = clientRepository.profile()

                if (response.success == true) {
                    clientDetails = response.data!!
                    withContext(Dispatchers.Main) {
                        etname.setText( "${clientDetails!!.name}")
                        etmobile.setText( "${clientDetails!!.mobile}")
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@UpdateDetailsActivity,
                        "Error : $ex", Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun showNotification(message: String) {
        val notificationManager = NotificationManagerCompat.from(this)

        val notificationChannels = NotificationChannels(this)
        notificationChannels.createNotificationChannels()

        val notification = NotificationCompat.Builder(this, notificationChannels.CHANNEL_1)
            .setSmallIcon(R.drawable.notifications)
            .setContentTitle("KothaKhali")
            .setContentText("$message")
            .setColor(Color.BLUE)
            .build()

        notificationManager.notify(1, notification)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values
        val xAxis = values[0]
        val yAxis = values[1]
        val zAxis = values[2]

        lastAcceleration = currentAcceleration
        currentAcceleration =
                kotlin.math.sqrt((xAxis * xAxis + yAxis * yAxis + zAxis * zAxis).toDouble()).toFloat()
        val delta: Float = currentAcceleration - lastAcceleration
        acceleration = acceleration * 0.9f + delta
        if (acceleration > 12) {
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        sensorManager.unregisterListener(this)
        super.onPause()
        finish()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}