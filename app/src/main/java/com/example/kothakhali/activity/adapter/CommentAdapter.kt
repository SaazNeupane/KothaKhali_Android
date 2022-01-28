package com.example.kothakhali.activity.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kothakhali.R
import com.example.kothakhali.activity.api.ServiceBuilder
import com.example.kothakhali.activity.model.Comment
import com.example.kothakhali.activity.model.Wishlist
import com.example.kothakhali.activity.repository.AdRepository
import com.example.kothakhali.activity.repository.ClientRepository
import com.example.kothakhali.activity.ui.ViewAdActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class CommentAdapter(
        private val listcomment: ArrayList<Comment>,
        private val context: Context

): RecyclerView.Adapter<CommentAdapter.CommentHolder>() {
    class CommentHolder(view: View) : RecyclerView.ViewHolder(view) {
        val comment: TextView = view.findViewById(R.id.comment)
        val cuser: TextView = view.findViewById(R.id.cuser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.CommentHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.commentdesign, parent, false)
        return CommentAdapter.CommentHolder(view)
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        val comment = listcomment[position]

        holder.comment.text=comment.comment

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val clientRepository = ClientRepository()
                val response = clientRepository.getclient(comment.clientid!!)
                if (response.success == true) {
                    val client = response.data!!
                    withContext(Dispatchers.Main){
                        holder.cuser.text = "-${client.name}"
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

    override fun getItemCount(): Int {
        return listcomment.size
    }
}