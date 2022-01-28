package com.example.kothakhali.activity.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.kothakhali.R
class SearchFragment : Fragment() {

    private lateinit var btnsearch:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view =  inflater.inflate(R.layout.fragment_search, container, false)

        btnsearch = view.findViewById(R.id.btnsearch)

        btnsearch.setOnClickListener {
            Toast.makeText(context, "Still On Development Phase", Toast.LENGTH_LONG).show()
        }
        return view;

    }
}