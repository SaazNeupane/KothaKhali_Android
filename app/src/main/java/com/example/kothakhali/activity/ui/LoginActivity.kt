package com.example.kothakhali.activity.ui

import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.WearableExtender
import androidx.core.app.NotificationManagerCompat
import com.example.kothakhali.R
import com.example.kothakhali.activity.api.ServiceBuilder
import com.example.kothakhali.activity.notificatons.NotificationChannels
import com.example.kothakhali.activity.repository.ClientRepository
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text


class LoginActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var etemail:EditText
    private lateinit var textemail:TextInputLayout
    private lateinit var etpassword:EditText
    private lateinit var textpassword:TextInputLayout
    private lateinit var btnlogin: Button
    private lateinit var tvcreate: TextView
    private lateinit var cbremember: CheckBox
    private lateinit var lllogin: LinearLayout
    private lateinit var sensorManager: SensorManager
    private var lightsensor: Sensor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etemail=findViewById(R.id.etlemail)
        textemail=findViewById(R.id.textemail)
        etpassword=findViewById(R.id.etlpassword)
        textpassword=findViewById(R.id.textpassword)
        btnlogin=findViewById(R.id.btnlogin)
        tvcreate=findViewById(R.id.tvcreate)
        cbremember=findViewById(R.id.cbremember)
        lllogin=findViewById(R.id.lllogin)

        btnlogin.setOnClickListener {
            if (checkEmpty()){
                login()
            }
        }

        tvcreate.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        //Light Sensor
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if(!checklightSensor())
            return
        else{
            lightsensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            sensorManager.registerListener(this,lightsensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun login() {
        val email = etemail.text.toString()
        val password = etpassword.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = ClientRepository()
                val response = repository.checkclient(email, password)
                if (response.success == true) {
                    ServiceBuilder.token = "Bearer " + response.token
                    saveSharedPref()
                    showNotification("${response.message}")
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            PermissionActivity::class.java
                        )
                    )
                    finish()
                } else {
                    withContext(Dispatchers.Main) {
                        val snack =
                                Snackbar.make(
                                    lllogin,
                                    "${response.message}",
                                    Snackbar.LENGTH_LONG
                                )
                        snack.setAction("OK", View.OnClickListener {
                            snack.dismiss()
                        })
                        snack.show()
                    }
                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@LoginActivity,
                        "$ex", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun checkEmpty(): Boolean {
        var flag = true

        if (TextUtils.isEmpty(etemail.text)) {
            etemail.setError("Please enter valid Email")
            etemail.requestFocus()
            flag = false
        } else if (TextUtils.isEmpty(etpassword.text)) {
            etpassword.setError("Please enter valid Password")
            etpassword.requestFocus()
            flag = false
        }
        return flag
    }

    private fun saveSharedPref() {
        val email = etemail.text.toString()
        val password = etpassword.text.toString()
        var remember = false
        if (cbremember.isChecked){
            remember = true;
        }
        val sharedPref = getSharedPreferences("Preferences", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.putBoolean("remember", remember)
        editor.apply()
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

    private fun checklightSensor(): Boolean {
        var flag = true
        if(sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == null){
            flag = false
        }
        return flag
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values[0]

        if(values>1000){
            lllogin.setBackgroundColor(resources.getColor(R.color.black))
            textemail.boxBackgroundColor = resources.getColor(R.color.black)
            textpassword.boxBackgroundColor = resources.getColor(R.color.black)
            btnlogin.setTextColor(resources.getColor(R.color.black))
        }
        else{
            lllogin.setBackgroundColor(resources.getColor(R.color.brandcolor))
            textemail.boxBackgroundColor = resources.getColor(R.color.brandcolor)
            textpassword.boxBackgroundColor = resources.getColor(R.color.brandcolor)
            btnlogin.setTextColor(resources.getColor(R.color.brandcolor))
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

}