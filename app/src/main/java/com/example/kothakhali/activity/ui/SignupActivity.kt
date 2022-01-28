package com.example.kothakhali.activity.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.kothakhali.R
//import com.example.kothakhali.activity.db.KothaKhaliDB
import com.example.kothakhali.activity.model.Client
import com.example.kothakhali.activity.repository.ClientRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SignupActivity : AppCompatActivity() {
    private lateinit var etname: EditText
    private lateinit var etmobile: EditText
    private lateinit var etemail: EditText
    private lateinit var etpassword: EditText
    private lateinit var etrepassword: EditText
    private lateinit var btnsignup: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        etname = findViewById(R.id.etname)
        etmobile = findViewById(R.id.etmobile)
        etemail = findViewById(R.id.etemail)
        etpassword = findViewById(R.id.etpassword)
        etrepassword = findViewById(R.id.etrepassword)
        btnsignup = findViewById(R.id.btnsignup)

        btnsignup.setOnClickListener {
            if (checkEmpty()) {
                signup()
            }
        }
    }
    private fun signup(){
        val name = etname.text.toString()
        val mobile = etmobile.text.toString()
        val email = etemail.text.toString()
        val password = etpassword.text.toString()
        val repassword = etrepassword.text.toString()

        if(password != repassword){
            etrepassword.error = "Password does not match"
            etrepassword.requestFocus()
        }
        else{
            val client = Client(name = name, mobile = mobile, email = email, password = password)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val clientrepository = ClientRepository()
                    val response = clientrepository.clientRegister(client)
                    if (response.success == true){
                        withContext(Main){
                            Toast.makeText(this@SignupActivity, "${response.message}", Toast.LENGTH_SHORT).show()
                            reset()
                            startActivity(
                                    Intent(
                                            this@SignupActivity,
                                            LoginActivity::class.java
                                    )
                            )
                            finish()
                        }
                    }

                } catch (e:Exception){
                    withContext(Main){
                        Toast.makeText(this@SignupActivity, "$e", Toast.LENGTH_SHORT).show()
                    }
                }

                //ROOMDATABASE
//                KothaKhaliDB
//                        .getInstance(this@SignupActivity)
//                        .getClientDao()
//                        .registerClient(client)
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(
//                            this@SignupActivity,
//                            "Client Registered", Toast.LENGTH_SHORT
//                    ).show()
//                    reset()
//                }
            }
        }
    }
    private fun checkEmpty(): Boolean{
        var flag=true

        if(TextUtils.isEmpty(etname.text)){
            etname.setError("Please enter your Company Name")
            etname.requestFocus()
            flag = false
        }
        else if(TextUtils.isEmpty(etmobile.text)){
            etmobile.setError("Please enter your Mobile Number")
            etmobile.requestFocus()
            flag = false
        }
        else if(TextUtils.isEmpty(etemail.text)){
            etemail.setError("Please enter your Email Address")
            etemail.requestFocus()
            flag = false
        }
        else if(TextUtils.isEmpty(etpassword.text)){
            etpassword.setError("Please Enter a valid password")
            etpassword.requestFocus()
            flag = false
        }
        return flag
    }
    private fun reset(){
        etname.text.clear()
        etmobile.text.clear()
        etemail.text.clear()
        etpassword.text.clear()
        etrepassword.text.clear()
    }
}