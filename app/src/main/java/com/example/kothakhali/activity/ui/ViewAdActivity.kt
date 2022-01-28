package com.example.kothakhali.activity.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kothakhali.R
import com.example.kothakhali.activity.adapter.CommentAdapter
import com.example.kothakhali.activity.adapter.WishlistAdapter
import com.example.kothakhali.activity.api.ServiceBuilder
import com.example.kothakhali.activity.model.AdList
import com.example.kothakhali.activity.model.Comment
import com.example.kothakhali.activity.model.Wishlist
import com.example.kothakhali.activity.notificatons.NotificationChannels
import com.example.kothakhali.activity.repository.AdRepository
import com.example.kothakhali.activity.repository.ClientRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ViewAdActivity : AppCompatActivity() {
    private lateinit var adimage: ImageView
    private lateinit var adtitle: TextView
    private lateinit var adrent: TextView
    private lateinit var adlocation: TextView
    private lateinit var adcategory: TextView
    private lateinit var addescription: TextView
    private lateinit var username: TextView
    private lateinit var useremail: TextView
    private lateinit var usermobile: TextView
    private lateinit var etcomment: TextView
    private lateinit var btnwishlist: Button
    private lateinit var btncomment: Button
    private lateinit var rvcomment: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_ad)

        adimage=findViewById(R.id.adimage)
        adtitle=findViewById(R.id.adtitle)
        adrent=findViewById(R.id.adrent)
        adlocation=findViewById(R.id.adlocation)
        adcategory=findViewById(R.id.adcategory)
        addescription=findViewById(R.id.addescription)
        etcomment=findViewById(R.id.etcomment)
        btnwishlist=findViewById(R.id.btnwishlist)
        btncomment=findViewById(R.id.btncomment)
        rvcomment=findViewById(R.id.rvcomment)
        username=findViewById(R.id.username)
        useremail=findViewById(R.id.useremail)
        usermobile=findViewById(R.id.usermobile)

        val intent = intent.getParcelableExtra<AdList>("ad")!!

        if(intent !=null){
            val imagepath = ServiceBuilder.loadImagepath() + intent.photo
            Glide.with(this)
                .load(imagepath)
                .into(adimage)

            adtitle.setText(intent.adtitle)
            if (intent.negotiable == "Yes"){
                adrent.setText("Rent: ${intent.rent}, Negotiable")
            }
            else if(intent.negotiable == "No"){
                adrent.setText("Rent: ${intent.rent}, Non-negotiable")
            }
            adlocation.setText("Location: ${intent.street}, ${intent.city}, ${intent.district}")
            adcategory.setText("Category: ${intent.category}")
            addescription.setText("Description: ${intent.description}")

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val clientRepository = ClientRepository()
                    val response = clientRepository.getclient(intent.clientid!!)
                    if (response.success == true) {
                        val client = response.data!!
                        withContext(Dispatchers.Main){
                            username.text = "Name: ${client.name}"
                            useremail.text="Email: ${client.email}"
                            usermobile.text="Mobile: ${client.mobile}"
                        }

                    }

                }
                catch (ex: Exception){
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ViewAdActivity,
                                ex.localizedMessage,
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }

        btnwishlist.setOnClickListener {
            val wishlist = Wishlist( adid = intent._id)

            CoroutineScope(Dispatchers.IO).launch{
                try{
                    val adRepository = AdRepository()
                    val response = adRepository.wishlistadd(wishlist)
                    if (response.success == true){
                        withContext(Dispatchers.Main){
                            showNotification("${response.message}")
                            startActivity(
                                    Intent(
                                            this@ViewAdActivity,
                                            DashboardActivity::class.java
                                    )
                            )
                        }
                    }
                }
                catch (ex: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@ViewAdActivity,
                                ex.toString(),
                                Toast.LENGTH_SHORT)
                                .show()
                    }
                }
            }

        }

        btncomment.setOnClickListener {
            val com = etcomment.text.toString()
            val comment = Comment(adid = intent._id,comment = com)

            CoroutineScope(Dispatchers.IO).launch{
                try{
                    val adRepository = AdRepository()
                    val response = adRepository.commentadd(comment)
                    if (response.success == true){
                        withContext(Dispatchers.Main){
                            showNotification("${response.message}")
                            startActivity(
                                Intent(
                                    this@ViewAdActivity,
                                    PermissionActivity::class.java
                                )
                            )
                        }
                    }
                }
                catch (ex: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@ViewAdActivity,
                                ex.toString(),
                                Toast.LENGTH_SHORT)
                                .show()
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val adRepository = AdRepository()
                val response = adRepository.getcomment(intent._id)
                if (response.success == true) {
                    val comment = response.data!!
                    withContext(Dispatchers.Main) {
                        val commentAdapter = CommentAdapter(comment, this@ViewAdActivity)
                        rvcomment.adapter = commentAdapter
                        rvcomment.layoutManager= LinearLayoutManager(this@ViewAdActivity, LinearLayoutManager.VERTICAL,false)
                    }
                }

            }
            catch (ex:Exception){
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ViewAdActivity,
                            ex.localizedMessage,
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun checkEmpty(): Boolean{
        var flag=true

        if(TextUtils.isEmpty(etcomment.text)){
            etcomment.setError("Please enter Comment")
            etcomment.requestFocus()
            flag = false
        }
        return flag
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
}