package com.appdev.kisanmitra.ui.community

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.appdev.kisanmitra.databinding.ActivityCreatePostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream

class CreatePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePostBinding

    private val PICK_IMAGE = 100

    private var imageBase64 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnUploadImage.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"

            startActivityForResult(intent, PICK_IMAGE)
        }

        binding.btnPost.setOnClickListener {

            val description = binding.etDescription.text.toString()

            val user = FirebaseAuth.getInstance().currentUser

            val post = hashMapOf(
                "userId" to user?.uid,
                "username" to (user?.displayName ?: "Farmer"),
                "location" to "India",
                "description" to description,
                "imageBase64" to imageBase64,
                "likes" to 0,
                "commentCount" to 0,
                "timestamp" to System.currentTimeMillis()
            )

            FirebaseFirestore.getInstance()
                .collection("community_posts")
                .add(post)

            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {

            val input = contentResolver.openInputStream(data!!.data!!)
            val bitmap = BitmapFactory.decodeStream(input)

            binding.imgPreview.setImageBitmap(bitmap)

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)

            val bytes = stream.toByteArray()

            imageBase64 = Base64.encodeToString(bytes, Base64.DEFAULT)
        }
    }
}