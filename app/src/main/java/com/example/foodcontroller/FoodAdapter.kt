package com.example.foodcontroller

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcontroller.repository.local.ProductEntity


class FoodAdapter(context: Context): RecyclerView.Adapter<FoodAdapter.FoodHolder>(){

    private var productEntity: List<ProductEntity> = emptyList<ProductEntity>()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    class FoodHolder(val linearLayout: LinearLayout) : RecyclerView.ViewHolder(linearLayout) {
        val productTextView: TextView = linearLayout.findViewById(R.id.food_name)
        val proteinTextView: TextView = linearLayout.findViewById(R.id.protein)
        val fatTextView: TextView = linearLayout.findViewById(R.id.fat)
        val carbohydratesTextView: TextView = linearLayout.findViewById(R.id.carbohydrates)
        val calorieTextView: TextView = linearLayout.findViewById(R.id.calories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val viewGroup = inflater
            .inflate(R.layout.eaten_food, parent, false) as LinearLayout
        return FoodHolder(viewGroup)
    }
    //Непонятно как лучше сделать, определить переменные указивающие на TextView в холдере или же просто
    //импортировать вью из активити и забиндить в них значение в onBindViewHolder
    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        holder.productTextView.text = productEntity[position].product.toString()
        holder.proteinTextView.text = productEntity[position].protein.toString()
        holder.fatTextView.text = productEntity[position].fat.toString()
        holder.carbohydratesTextView.text = productEntity[position].carbohydrates.toString()
        holder.calorieTextView.text = productEntity[position].calories.toString()
    }

    override fun getItemCount(): Int = productEntity.size

    internal fun setProduct(product: List<ProductEntity>) {
        this.productEntity = product
        notifyDataSetChanged()
    }

}