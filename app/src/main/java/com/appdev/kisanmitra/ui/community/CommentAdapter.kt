package com.appdev.kisanmitra.ui.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appdev.kisanmitra.databinding.ItemCommentBinding
import com.appdev.kisanmitra.data.model.Comment

class CommentAdapter(
    private val comments: List<Comment>
) : RecyclerView.Adapter<CommentAdapter.CommentVH>() {

    class CommentVH(val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentVH {

        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CommentVH(binding)
    }

    override fun getItemCount() = comments.size

    override fun onBindViewHolder(holder: CommentVH, position: Int) {

        val comment = comments[position]

        holder.binding.tvCommentUser.text = comment.username
        holder.binding.tvCommentText.text = comment.comment
    }
}