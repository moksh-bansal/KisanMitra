package com.appdev.kisanmitra.ui.community

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appdev.kisanmitra.databinding.ItemPostBinding
import com.appdev.kisanmitra.data.model.CommunityPost

class PostAdapter(
    private val posts:List<CommunityPost>,
    private val commentClick:(CommunityPost)->Unit
):RecyclerView.Adapter<PostAdapter.PostVH>(){

    class PostVH(val binding:ItemPostBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):PostVH{
        val binding=ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return PostVH(binding)
    }

    override fun getItemCount()=posts.size

    override fun onBindViewHolder(holder:PostVH,position:Int){

        val post=posts[position]

        holder.binding.tvUsername.text=post.username
        holder.binding.tvLocation.text=post.location
        holder.binding.tvDescription.text=post.description
        holder.binding.tvLikes.text = post.likes.toString()
        holder.binding.tvComments.text = post.commentCount.toString()

        holder.binding.tvDescription.setOnClickListener{

            if(holder.binding.tvDescription.maxLines==3)
                holder.binding.tvDescription.maxLines=100
            else
                holder.binding.tvDescription.maxLines=3
        }

        if(post.imageBase64.isNotEmpty()){

            val bytes=Base64.decode(post.imageBase64,Base64.DEFAULT)
            val bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.size)

            holder.binding.imgPost.setImageBitmap(bitmap)
            holder.binding.imgPost.visibility=View.VISIBLE
        }

        holder.binding.layoutComment.setOnClickListener {
            commentClick(post)
        }
    }
}