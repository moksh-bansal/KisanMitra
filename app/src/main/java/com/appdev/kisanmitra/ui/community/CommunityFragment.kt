package com.appdev.kisanmitra.ui.community

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.appdev.kisanmitra.R
import com.appdev.kisanmitra.databinding.FragmentCommunityBinding
import com.appdev.kisanmitra.data.model.CommunityPost
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.jvm.java

class CommunityFragment:Fragment(R.layout.fragment_community){

    private lateinit var binding:FragmentCommunityBinding

    private val posts= mutableListOf<CommunityPost>()

    private lateinit var adapter:PostAdapter

    private val db=FirebaseFirestore.getInstance()

    override fun onViewCreated(view:View,savedInstanceState:Bundle?){

        binding=FragmentCommunityBinding.bind(view)

        adapter=PostAdapter(posts){
            CommentBottomSheet(it).show(parentFragmentManager,"comments")
        }

        binding.recyclerPosts.layoutManager=LinearLayoutManager(requireContext())
        binding.recyclerPosts.adapter=adapter

        loadPosts()

        binding.fabCreatePost.setOnClickListener{

            startActivity(
                Intent(requireContext(),CreatePostActivity::class.java)
            )
        }
    }

    private fun loadPosts(){

        db.collection("community_posts")
            .addSnapshotListener{snapshot,_ ->

                posts.clear()

                snapshot?.forEach{

                    val post=it.toObject(CommunityPost::class.java)
                    post.postId=it.id

                    posts.add(post)
                }

                adapter.notifyDataSetChanged()
            }
    }
}