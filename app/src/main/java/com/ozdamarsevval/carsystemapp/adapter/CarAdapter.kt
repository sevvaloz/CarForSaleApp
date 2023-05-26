package com.ozdamarsevval.carsystemapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ozdamarsevval.carsystemapp.databinding.ItemCarBinding
import com.ozdamarsevval.carsystemapp.model.Car
import java.text.SimpleDateFormat

class CarAdapter(val onItemClicked: (Int, Car) -> Unit
): RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    val sdf = SimpleDateFormat("dd MMM yyyy")
    private var carList: MutableList<Car> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        return CarViewHolder(ItemCarBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(carList[position])
    }

    override fun getItemCount(): Int {
       return carList.size
    }

    fun updateList(list: MutableList<Car>){
        this.carList = list
        notifyDataSetChanged()
    }

    inner class  CarViewHolder(val binding: ItemCarBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(car: Car){
            binding.owner.text =  car.owner
            binding.date.text = sdf.format(car.date)
            binding.desc.text = car.brand + " " + car.model
            binding.price.text = car.price
            binding.carItem.setOnClickListener {
                onItemClicked.invoke(adapterPosition, car)
            }
        }
    }


}