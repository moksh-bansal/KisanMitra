package com.appdev.kisanmitra.data.model

data class Product(
    val id: String = "",
    val name: String = "",
    val price: String = "",
    val description: String = "",
    val contactNumber: String = "",
    val imageBase64: String = "",
    val sellerId: String = "",
    val sellerName: String = "",
    val timestamp: Long = System.currentTimeMillis()
)