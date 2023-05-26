package com.ozdamarsevval.carsystemapp.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ozdamarsevval.carsystemapp.databinding.ItemImageBinding

class ImageAdapter(): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private var imageList: MutableList<Uri> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageList[position], position)
    }

    inner class ImageViewHolder(val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Uri, pos: Int){
            binding.img.setImageURI(item)
        }
    }

    fun updateList(list: MutableList<Uri>){
        this.imageList = list
        notifyDataSetChanged()
    }
}