package com.example.kothakhali.activity.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kothakhali.R
import com.example.kothakhali.activity.api.ServiceBuilder
import com.example.kothakhali.activity.repository.ClientRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.lang.Exception

class SplashActivity : AppCompatActivity() {

    var email: String? = null
    var password:String? =null
    var remember: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (!checkInternetConnection()) {
            Toast.makeText(
                    this,
                    "No Internet connection , please turn on the WIFI",
                    Toast.LENGTH_SHORT
            ).show()
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                getSharedPref()
                if (remember == true) {
                    if (email != "") {
                        login()
                    }
                    else {
                        loadLoginPage()
                    }
                }
                else {
                    loadLoginPage()
                }
            }
        }
    }
    private fun login() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = ClientRepository()
                val response = repository.checkclient(email!!, password!!)
                if (response.success == true) {
                    ServiceBuilder.token = "Bearer " + response.token
                    startActivity(
                            Intent(
                                    this@SplashActivity,
                                    PermissionActivity::class.java
                            )
                    )
                    finish()
                } else {
                    withContext(Dispatchers.Main) {
                        loadLoginPage()
                    }
                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                            this@SplashActivity,
                            "$ex", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("Preferences", MODE_PRIVATE)
        email = sharedPref.getString("email","")
        password = sharedPref.getString("password","")
        remember = sharedPref.getBoolean("remember", false)
    }

    private fun loadLoginPage() {
        startActivity(
                Intent(
                        this@SplashActivity,
                        LoginActivity::class.java
                )
        )
        finish()
    }

    private fun checkInternetConnection(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}