package com.appdev.kisanmitra.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.kisanmitra.databinding.LayoutCommentsBinding
import com.appdev.kisanmitra.data.model.Comment
import com.appdev.kisanmitra.data.model.CommunityPost
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CommentBottomSheet(private val post: CommunityPost) : BottomSheetDialogFragment() {

    private lateinit var binding: LayoutCommentsBinding
    private val db = FirebaseFirestore.getInstance()
    private val comments = mutableListOf<Comment>()
    private lateinit var adapter: CommentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = LayoutCommentsBinding.inflate(inflater, container, false)

        adapter = CommentAdapter(comments)
        binding.recyclerComments.adapter = adapter

        loadComments()

        binding.btnSendComment.setOnClickListener {

            val text = binding.etComment.text.toString()

            if(text.isEmpty()) return@setOnClickListener

            val user = FirebaseAuth.getInstance().currentUser

            val comment = hashMapOf(
                "username" to (user?.displayName ?: "Farmer"),
                "comment" to text,
                "timestamp" to System.currentTimeMillis()
            )

            db.collection("community_posts")
                .document(post.postId)
                .collection("comments")
                .add(comment)
                .addOnSuccessListener {

                    db.collection("community_posts")
                        .document(post.postId)
                        .update("commentCount", post.commentCount + 1)

                }

            binding.etComment.setText("")
        }

        return binding.root
    }

    private fun loadComments() {

        db.collection("community_posts")
            .document(post.postId)
            .collection("comments")
            .addSnapshotListener { snapshot, _ ->

                comments.clear()

                snapshot?.forEach {

                    val c = it.toObject(Comment::class.java)
                    comments.add(c)
                }

                adapter.notifyDataSetChanged()
            }
    }
}