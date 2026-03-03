package com.appdev.kisanmitra.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appdev.kisanmitra.databinding.ItemTipBinding

class TipsAdapter(private val tips: List<String>) :
    RecyclerView.Adapter<TipsAdapter.TipViewHolder>() {

    inner class TipViewHolder(val binding: ItemTipBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder {
        val binding = ItemTipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        holder.binding.tvTip.text = tips[position]
    }

    override fun getItemCount(): Int {
        return tips.size
    }
}