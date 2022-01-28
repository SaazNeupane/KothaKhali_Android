package com.example.kothakhali.activity.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kothakhali.R
import com.example.kothakhali.activity.api.ServiceBuilder
import com.example.kothakhali.activity.model.AdList
import com.example.kothakhali.activity.model.Wishlist
import com.example.kothakhali.activity.repository.AdRepository
import com.example.kothakhali.activity.ui.DashboardActivity
import com.example.kothakhali.activity.ui.ViewAdActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class WishlistAdapter(
        private val listmyWishlist: ArrayList<Wishlist>,
        private val context: Context

): RecyclerView.Adapter<WishlistAdapter.WishlistHolder>() {
    class WishlistHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvadtitle: TextView = view.findViewById(R.id.tvadtitile)
        val ivimage: ImageView = view.findViewById(R.id.ivimage)
        val tvrent: TextView = view.findViewById(R.id.tvrent)
        val llbutton: LinearLayout = view.findViewById(R.id.llbutton)
        val tvdelete: TextView = view.findViewById(R.id.tvdelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistAdapter.WishlistHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.smalladdesign,parent,false)
        return WishlistAdapter.WishlistHolder(view)
    }

    override fun onBindViewHolder(holder: WishlistHolder, position: Int) {
        val wishlist = listmyWishlist[position]

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val adRepository = AdRepository()
                val response = adRepository.getad(wishlist.adid!!)
                if (response.success == true) {
                    val ad = response.data!!
                    withContext(Dispatchers.Main){
                        holder.tvadtitle.text=ad.adtitle
                        holder.tvrent.text="Rs. ${ad.rent}"

                        holder.llbutton.setOnClickListener {
                            val intent = Intent(context, ViewAdActivity::class.java)
                                    .putExtra("ad",ad)
                            context.startActivity(intent)
                        }

                        val imagepath= ServiceBuilder.loadImagepath()+ad.photo
                        Glide.with(context)
                                .load(imagepath)
                                .apply( RequestOptions()
                                        .placeholder(R.drawable.blogo)
                                        .fitCenter())
                                .into(holder.ivimage)
                    }

                }

            }
            catch (ex: Exception){
                withContext(Dispatchers.Main) {
                    Toast.makeText(context,
                            ex.localizedMessage,
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        holder.tvdelete.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            //set title for alert dialog
            builder.setTitle("Are you sure?")

            //set message for alert dialog
            builder.setMessage("You cannot undo it.")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val adRepository = AdRepository()
                        val response = adRepository.deletewishlist(wishlist._id!!)
                        if (response.success == true) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Wishlist Deleted", Toast.LENGTH_SHORT).show()
                                val intent = Intent(context, DashboardActivity::class.java)
                                context.startActivity(intent)
                            }
                        }

                    }
                    catch (ex: Exception){
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context,
                                ex.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
            }
            //performing cancel action
            builder.setNeutralButton("Cancel"){dialogInterface , which ->
            }
            //performing negative action
            builder.setNegativeButton("No"){dialogInterface, which ->
            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()

        }
    }

    override fun getItemCount(): Int {
        return listmyWishlist.size
    }
}