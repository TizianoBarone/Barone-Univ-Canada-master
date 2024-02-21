package com.tba.universidades

import androidx.recyclerview.widget.RecyclerView
import com.tba.universidades.databinding.ItemUniBinding

class UniViewHolder(private val binding: ItemUniBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(universidad: Universidad) {
        binding.txUni.text = universidad.name
    }
}