package com.example.kothakhaliwear

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.example.kothakhaliwear.api.ServiceBuilder
import com.example.kothakhaliwear.repository.ClientRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    private lateinit var etemail: EditText
    private lateinit var etpassword: EditText
    private lateinit var btnlogin: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etemail=findViewById(R.id.etemail)
        etpassword=findViewById(R.id.etpassword)
        btnlogin=findViewById(R.id.btnlogin)

        btnlogin.setOnClickListener {
            if (checkEmpty()){
                login()
            }
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
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            DashboardActivity::class.java
                        )
                    )
                    finish()
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@LoginActivity,
                            "${response.message}", Toast.LENGTH_SHORT
                        ).show()
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
}