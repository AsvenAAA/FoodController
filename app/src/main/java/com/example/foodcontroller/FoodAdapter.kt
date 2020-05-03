package com.example.foodcontroller

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcontroller.repository.local.ProductEntity
import kotlinx.android.synthetic.main.eaten_food.view.*

class FoodAdapter(private val dataSet: List<ProductEntity>): RecyclerView.Adapter<FoodAdapter.FoodHolder>(){

    private var productEntity: List<ProductEntity> = emptyList<ProductEntity>()

    class FoodHolder(val linearLayout: LinearLayout) : RecyclerView.ViewHolder(linearLayout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val viewGroup = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.eaten_food, parent, false) as LinearLayout
        return FoodHolder(viewGroup)
    }
    //Непонятно как лучше сделать, определить переменные указивающие на TextView в холдере или же просто
    //импортировать вью из активити и забиндить в них значение в onBindViewHolder
    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        holder.itemView.protein.text = dataSet[position].protein.toString()
        holder.itemView.fat.text = dataSet[position].fat.toString()
        holder.itemView.carbohydrates.text = dataSet[position].carbohydrates.toString()
        holder.itemView.calories.text = dataSet[position].calories.toString()
    }

    override fun getItemCount(): Int = dataSet.size

    internal fun setProduct(product: List<ProductEntity>) {
        this.productEntity = product
        notifyDataSetChanged()
    }

}