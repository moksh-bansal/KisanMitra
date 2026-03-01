package com.appdev.kisanmitra.ui.marketplace

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appdev.kisanmitra.data.model.Product
import com.appdev.kisanmitra.databinding.ItemProductBinding
import com.bumptech.glide.Glide
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.firebase.firestore.FirebaseFirestore

class ProductAdapter(
    private var list: List<Product>,
    private val isMyProducts: Boolean = false
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private val firestore = FirebaseFirestore.getInstance()

    inner class ViewHolder(val binding: ItemProductBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val product = list[position]
        val context = holder.itemView.context

        holder.binding.tvName.text = product.name
        holder.binding.tvPrice.text = "₹ ${product.price}"
        holder.binding.tvDescription.text = product.description

        if (product.imageBase64.isNotEmpty()) {
            val imageBytes =
                Base64.decode(product.imageBase64, Base64.DEFAULT)
            val bitmap =
                BitmapFactory.decodeByteArray(
                    imageBytes, 0, imageBytes.size)
            holder.binding.imgProduct.setImageBitmap(bitmap)
        }

        holder.binding.btnContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://wa.me/${product.contactNumber}")
            context.startActivity(intent)
        }

        if (isMyProducts) {
            holder.binding.btnEdit.visibility = android.view.View.VISIBLE
            holder.binding.btnDelete.visibility = android.view.View.VISIBLE

            holder.binding.btnEdit.setOnClickListener {
                val intent = Intent(context, AddEditProductActivity::class.java)
                intent.putExtra("productId", product.id)
                context.startActivity(intent)
            }

            holder.binding.btnDelete.setOnClickListener {

                AlertDialog.Builder(context)
                    .setTitle("Delete Product")
                    .setMessage("Are you sure you want to delete this product?")
                    .setPositiveButton("Yes") { _, _ ->
                        firestore.collection("products")
                            .document(product.id)
                            .delete()
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        }
    }

    fun updateList(newList: List<Product>) {
        list = newList
        notifyDataSetChanged()
    }
}