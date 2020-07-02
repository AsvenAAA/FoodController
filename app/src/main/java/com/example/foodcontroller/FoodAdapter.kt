package com.example.foodcontroller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcontroller.repository.local.ProductEntity

//, View.OnClickListener
class FoodAdapter(context: Context): RecyclerView.Adapter<FoodAdapter.FoodHolder>(){

    private var productEntity: List<ProductEntity> = emptyList()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    //Попытки добавить редактирование слова
    companion object{
        private lateinit var clickListener: UpdateProductClickListener
    }


    class FoodHolder(private val linearLayout: LinearLayout) : RecyclerView.ViewHolder(linearLayout) {
        var productTextView: TextView = linearLayout.findViewById(R.id.food_name)
        var proteinTextView: TextView = linearLayout.findViewById(R.id.protein)
        var fatTextView: TextView = linearLayout.findViewById(R.id.fat)
        var carbohydratesTextView: TextView = linearLayout.findViewById(R.id.carbohydrates)
        var calorieTextView: TextView = linearLayout.findViewById(R.id.calories)

        //Попытки добавить редактирование слова
        init {
            super.itemView
            productTextView = itemView.findViewById(R.id.food_name)
            proteinTextView = itemView.findViewById(R.id.protein)
            fatTextView = itemView.findViewById(R.id.fat)
            carbohydratesTextView = itemView.findViewById(R.id.carbohydrates)
            calorieTextView = itemView.findViewById(R.id.calories)

            itemView.setOnClickListener(View.OnClickListener {
                clickListener.onProductClick(it, adapterPosition)
            })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val viewGroup = inflater
            .inflate(R.layout.eaten_food, parent, false) as LinearLayout

        return FoodHolder(viewGroup)
    }
    //Непонятно как лучше сделать, определить переменные указивающие на TextView в холдере или же просто
    //импортировать вью из активити и забиндить в них значение в onBindViewHolder
    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        holder.productTextView.text = productEntity[position].product
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

    fun getProductAtPosition(position: Int) : ProductEntity {
        return productEntity[position]
    }

    //Попытки добавить редактирование слова
    fun setOnProductClickListener(clickListener: UpdateProductClickListener) {
        FoodAdapter.clickListener = clickListener
    }

    interface UpdateProductClickListener {
        fun onProductClick(view: View, position: Int)
    }

}