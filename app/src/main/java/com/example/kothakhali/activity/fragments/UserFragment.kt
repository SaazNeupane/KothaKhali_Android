package com.example.kothakhali.activity.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kothakhali.R
import com.example.kothakhali.activity.adapter.OwnAdAdapter
import com.example.kothakhali.activity.adapter.WishlistAdapter
import com.example.kothakhali.activity.api.ServiceBuilder
import com.example.kothakhali.activity.model.Client
import com.example.kothakhali.activity.notificatons.NotificationChannels
import com.example.kothakhali.activity.repository.AdRepository
import com.example.kothakhali.activity.repository.ClientRepository
import com.example.kothakhali.activity.ui.AddRoomActivity
import com.example.kothakhali.activity.ui.LoginActivity
import com.example.kothakhali.activity.ui.PermissionActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class UserFragment : Fragment() {
    private lateinit var btnaddroom: Button
    private lateinit var btnlogout: Button
    private lateinit var rvmyroom: RecyclerView
    private lateinit var rvmywishlist: RecyclerView
    private lateinit var name: TextView
    private lateinit var mobile: TextView
    private lateinit var email: TextView
    private lateinit var profileimage: ImageView

    private var clientDetails: Client? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_user, container, false)

        btnaddroom = view.findViewById(R.id.btnaddroom)
        btnlogout = view.findViewById(R.id.btnlogout)
        rvmyroom = view.findViewById(R.id.rvmyroom)
        rvmywishlist = view.findViewById(R.id.rvmywishlist)
        name = view.findViewById(R.id.name)
        mobile = view.findViewById(R.id.mobile)
        email = view.findViewById(R.id.email)
        profileimage=view.findViewById(R.id.profileimage)

        loadclient()
        loadmyads()
        loadmywishlist()
        btnaddroom.setOnClickListener {
            startActivity(
                    Intent(
                            context,
                            AddRoomActivity::class.java
                    )
            )
        }

        btnlogout.setOnClickListener {
            val sharedPref = activity?.getSharedPreferences("Preferences", AppCompatActivity.MODE_PRIVATE)
            val editor = sharedPref?.edit()
            editor?.clear()
            editor?.apply()
            ServiceBuilder.token.equals("")
            showNotification("Logged Out Successfully")
            startActivity(
                    Intent(
                            context,
                            LoginActivity::class.java
                    )
            )
            activity?.finish()
        }

        return view
    }

    private fun loadclient() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val clientRepository = ClientRepository()
                val response = clientRepository.profile()

                if (response.success == true) {
                    clientDetails = response.data!!
                    withContext(Dispatchers.Main) {
                        name.text = "Welcome, ${clientDetails!!.name}"
                        mobile.text = "Mobile: ${clientDetails!!.mobile}"
                        email.text = "Email: ${clientDetails!!.email}"

                        val imagepath = ServiceBuilder.loadImagepath() + clientDetails!!.image
                        Glide.with(context!!)
                                .load(imagepath)
                                .apply( RequestOptions()
                                        .placeholder(R.drawable.no)
                                        .fitCenter())
                                .into(profileimage)
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Error : $ex", Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun loadmyads(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val adRepository = AdRepository()
                val response = adRepository.getmyads()
                if (response.success == true) {
                    val adlist = response.data!!
                    withContext(Dispatchers.Main) {
                        val adAdapter = OwnAdAdapter(adlist, context!!)
                        rvmyroom.adapter = adAdapter
                        rvmyroom.layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                    }
                }

            }
            catch (ex:Exception){
                withContext(Dispatchers.Main) {
                    Toast.makeText(context,
                            ex.localizedMessage,
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

    private fun loadmywishlist(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val adRepository = AdRepository()
                val response = adRepository.getmywishlist()
                if (response.success == true) {
                    val wishlist = response.data!!
                    withContext(Dispatchers.Main) {
                        val adAdapter = WishlistAdapter(wishlist, context!!)
                        rvmywishlist.adapter = adAdapter
                        rvmywishlist.layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                    }
                }

            }
            catch (ex:Exception){
                withContext(Dispatchers.Main) {
                    Toast.makeText(context,
                            ex.localizedMessage,
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

    private fun showNotification(message: String) {
        val notificationManager = NotificationManagerCompat.from(context!!)

        val notificationChannels = NotificationChannels(context!!)
        notificationChannels.createNotificationChannels()

        val notification = NotificationCompat.Builder(context!!, notificationChannels.CHANNEL_1)
                .setSmallIcon(R.drawable.notifications)
                .setContentTitle("KothaKhali")
                .setContentText("$message")
                .setColor(Color.BLUE)
                .build()

        notificationManager.notify(1, notification)
    }


}