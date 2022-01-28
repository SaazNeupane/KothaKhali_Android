package com.example.kothakhali.activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.kothakhali.R
import com.example.kothakhali.activity.adapter.AdAdapter
import com.example.kothakhali.activity.db.KothaKhaliDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private val imageList = ArrayList<SlideModel>()
    private lateinit var rvroom: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_home, container, false)

        //Image Slider
        val imageSlider = view.findViewById<ImageSlider>(R.id.image_slider)
        imageadd()
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        loadad()
        //Recyler View
        rvroom=view.findViewById(R.id.rvroom)


        return view
    }

    private fun imageadd(){
        imageList.add(SlideModel(R.drawable.main, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.room, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.hostel, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.house, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.shop, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.apartment, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.office, ScaleTypes.FIT))
    }

    private fun loadad(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val adlist = KothaKhaliDB.getInstance(requireContext()).getAdDao().getallAds()
                withContext(Main) {
                    rvroom.adapter = AdAdapter(requireContext(), adlist!!)
                    rvroom.layoutManager= LinearLayoutManager(
                        context,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                }
            }
            catch (ex: Exception){
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        ex.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }
}