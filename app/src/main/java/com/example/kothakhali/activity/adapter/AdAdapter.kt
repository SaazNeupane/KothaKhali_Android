package com.example.kothakhali.activity.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kothakhali.R
import com.example.kothakhali.activity.api.ServiceBuilder
import com.example.kothakhali.activity.model.AdList
import com.example.kothakhali.activity.ui.ViewAdActivity

class AdAdapter(
    private val context: Context,
    private val listAdList: MutableList<AdList>

):
        RecyclerView.Adapter<AdAdapter.AdViewHolder>(){
    class AdViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val tvadtitle: TextView = view.findViewById(R.id.tvadtitile)
        val ivimage: ImageView = view.findViewById(R.id.ivimage)
        val tvrent: TextView = view.findViewById(R.id.tvrent)
        val llbutton: LinearLayout = view.findViewById(R.id.llbutton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.bigaddesign,parent,false)
        return AdViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        val ad = listAdList[position]
        holder.tvadtitle.text=ad.adtitle
        holder.tvrent.text="Rs. ${ad.rent}"

        holder.llbutton.setOnClickListener {
            val intent = Intent(context, ViewAdActivity::class.java)
                .putExtra("ad",ad)
            context.startActivity(intent)
        }

        val imagepath=ServiceBuilder.loadImagepath()+ad.photo
        Glide.with(context)
                .load(imagepath)
                .apply( RequestOptions()
                        .placeholder(R.drawable.blogo)
                        .fitCenter())
                .into(holder.ivimage)
    }

    override fun getItemCount(): Int {
        return listAdList.size
    }

}