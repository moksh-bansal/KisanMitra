package com.appdev.kisanmitra.ui.marketplace

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.appdev.kisanmitra.data.model.Product
import com.appdev.kisanmitra.databinding.FragmentMarketplaceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MarketplaceFragment : Fragment() {

    private lateinit var binding: FragmentMarketplaceBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val allProducts = mutableListOf<Product>()
    private var showingMyProducts = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMarketplaceBinding.inflate(inflater, container, false)

        binding.rvProducts.layoutManager =
            LinearLayoutManager(requireContext())

        loadProducts()

        binding.btnAddProduct.setOnClickListener {
            startActivity(Intent(requireContext(),
                AddEditProductActivity::class.java))
        }

        binding.btnMyProducts.setOnClickListener {
            showingMyProducts = !showingMyProducts
            updateList()
        }

        setupSearch()

        return binding.root
    }

    private fun loadProducts() {

        firestore.collection("products")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {

                    allProducts.clear()

                    for (doc in snapshot.documents) {
                        val product = doc.toObject(Product::class.java)
                        if (product != null) {
                            allProducts.add(product)
                        }
                    }

                    updateList()
                }
            }
    }

    private fun updateList() {

        val currentUserId = auth.currentUser?.uid

        val displayList = if (showingMyProducts) {
            allProducts.filter { it.sellerId == currentUserId }
        } else {
            allProducts
        }

        binding.rvProducts.adapter =
            ProductAdapter(displayList, showingMyProducts)
    }

    private fun setupSearch() {

        binding.etSearch.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val query = s.toString().lowercase()

                val filtered = allProducts.filter {
                    it.name.lowercase().contains(query) ||
                            it.description.lowercase().contains(query)
                }

                binding.rvProducts.adapter =
                    ProductAdapter(filtered, showingMyProducts)
            }
        })
    }
}