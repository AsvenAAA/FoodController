package com.example.foodcontroller

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.eaten_food.view.*
import org.json.JSONObject

class FoodAdapter(private val dataSet: Array<Array<String>>): RecyclerView.Adapter<FoodAdapter.FoodHolder>(){

    class FoodHolder(val linearLayout: LinearLayout) : RecyclerView.ViewHolder(linearLayout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val viewGroup = LayoutInflater.from(parent.context).inflate(R.layout.eaten_food, parent, false) as LinearLayout
        return FoodHolder(viewGroup)
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        holder.itemView.protein.text = dataSet[position][0]
        holder.itemView.fat.text = dataSet[position][1]
        holder.itemView.carbohydrates.text = dataSet[position][2]
        holder.itemView.calories.text = dataSet[position][3]
    }

    override fun getItemCount(): Int = dataSet.size

}