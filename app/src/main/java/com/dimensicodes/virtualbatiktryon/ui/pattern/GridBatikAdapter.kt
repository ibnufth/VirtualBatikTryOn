package com.dimensicodes.virtualbatiktryon.ui.pattern

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dimensicodes.virtualbatiktryon.databinding.ItemGridBatikBinding

class GridBatikAdapter : RecyclerView.Adapter<GridBatikAdapter.ViewHolder>() {

    var listBatik = ArrayList<String>()
            set(listBatik) {
                if(listBatik.size > 0 ){
                    this.listBatik.clear()
                }
                this.listBatik.addAll(listBatik)
                notifyDataSetChanged()
            }

    class ViewHolder(private val binding: ItemGridBatikBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(batik: String) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGridBatikBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listBatik[position])
    }

    override fun getItemCount(): Int = listBatik.size
}