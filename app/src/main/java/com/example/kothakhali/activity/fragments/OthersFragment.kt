package com.example.kothakhali.activity.fragments

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.example.kothakhali.R
import com.example.kothakhali.activity.ui.ContactUsActivity
import com.example.kothakhali.activity.ui.PermissionActivity
import com.example.kothakhali.activity.ui.UpdateDetailsActivity

class OthersFragment : Fragment() {

    private lateinit var maps: LinearLayout
    private lateinit var update: LinearLayout
    private lateinit var tempeture: LinearLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_others, container, false)

        maps=view.findViewById(R.id.maps)
        update=view.findViewById(R.id.update)

        maps.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    ContactUsActivity::class.java
                )
            )
        }
        update.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    UpdateDetailsActivity::class.java
                )
            )
        }


        return view
    }
}