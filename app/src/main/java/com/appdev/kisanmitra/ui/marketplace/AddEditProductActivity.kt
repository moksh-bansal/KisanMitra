package com.appdev.kisanmitra.ui.marketplace

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.appdev.kisanmitra.data.model.Product
import com.appdev.kisanmitra.databinding.ActivityAddEditProductBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*

class AddEditProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditProductBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var imageBase64: String = ""
    private var productId: String? = null

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                imageBase64 = convertImageToBase64(it)
                binding.imgProduct.setImageURI(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productId = intent.getStringExtra("productId")

        if (productId != null) {
            loadProductForEdit(productId!!)
            binding.btnSave.text = "Update Product"
        }

        binding.imgProduct.setOnClickListener {
            imagePicker.launch("image/*")
        }

        binding.btnSave.setOnClickListener {
            saveProduct()
        }
    }

    private fun convertImageToBase64(uri: Uri): String {

        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)

        val byteArray = outputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun loadProductForEdit(id: String) {

        firestore.collection("products").document(id)
            .get()
            .addOnSuccessListener { doc ->
                val product = doc.toObject(Product::class.java)
                if (product != null) {

                    binding.etName.setText(product.name)
                    binding.etPrice.setText(product.price)
                    binding.etDescription.setText(product.description)
                    binding.etContact.setText(product.contactNumber)

                    imageBase64 = product.imageBase64

                    if (imageBase64.isNotEmpty()) {
                        val imageBytes =
                            Base64.decode(imageBase64, Base64.DEFAULT)
                        val bitmap =
                            BitmapFactory.decodeByteArray(
                                imageBytes, 0, imageBytes.size)
                        binding.imgProduct.setImageBitmap(bitmap)
                    }
                }
            }
    }

    private fun saveProduct() {

        val name = binding.etName.text.toString().trim()
        val price = binding.etPrice.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val contact = binding.etContact.text.toString().trim()

        if (name.isEmpty() || price.isEmpty() ||
            description.isEmpty() || contact.isEmpty()) {

            Toast.makeText(this, "Fill all fields",
                Toast.LENGTH_SHORT).show()
            return
        }

        if (imageBase64.isEmpty()) {
            Toast.makeText(this,
                "Please select image",
                Toast.LENGTH_SHORT).show()
            return
        }

        val progress = ProgressDialog(this)
        progress.setMessage("Saving product...")
        progress.show()

        val user = auth.currentUser ?: return

        val id = productId ?: firestore.collection("products").document().id

        val product = Product(
            id = id,
            name = name,
            price = price,
            description = description,
            contactNumber = contact,
            imageBase64 = imageBase64,
            sellerId = user.uid,
            sellerName = user.displayName ?: "User"
        )

        firestore.collection("products")
            .document(id)
            .set(product)
            .addOnSuccessListener {
                progress.dismiss()
                Toast.makeText(this,
                    "Product saved",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                progress.dismiss()
                Toast.makeText(this,
                    "Failed to save",
                    Toast.LENGTH_SHORT).show()
            }
    }
}