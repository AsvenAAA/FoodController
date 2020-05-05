package com.example.foodcontroller

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcontroller.repository.local.ProductEntity
import com.example.foodcontroller.viewmodel.FoodViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.math.BigDecimal
import java.math.BigInteger

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    //private lateinit var adapter: RecyclerView.Adapter<FoodAdapter.FoodHolder>
    private lateinit var adapter: FoodAdapter
    private lateinit var productViewModel: FoodViewModel

    //Коды ответов.
    //С помощью них можно точно определить от каких активити пришли ответы.
    private val newProductActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutManager = LinearLayoutManager(this)
        adapter = FoodAdapter(this)

        recyclerView = findViewById<RecyclerView>(R.id.food_recycler).apply {
            setHasFixedSize(true)
            adapter = this.adapter
            layoutManager = this.layoutManager
        }

        productViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)
        productViewModel.allProducts.observe(this, Observer { products ->
            products?.let { adapter.setProduct(it) }
        })


        val buttonAddProduct = findViewById<FloatingActionButton>(R.id.fabAddNewProduct)
        buttonAddProduct.setOnClickListener {
            val intentToNewProduct = Intent(this@MainActivity, NewProductActivity::class.java)
            startActivityForResult(intentToNewProduct, newProductActivityRequestCode)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == newProductActivityRequestCode &&
                resultCode == Activity.RESULT_OK) {
            data?.getStringArrayExtra(NewProductActivity.EXTRA_RESPONSE_FROM_NEWPRODUCTACTIVITY)?.let {
                val product = ProductEntity(product = it[0],
                                            protein = it[1].toFloat(),
                                            fat = it[2].toFloat(),
                                            carbohydrates = it[3].toFloat(),
                                            calories = it[4].toFloat()) }
            Toast.makeText(
                applicationContext,
                R.string.added_to_database_success,
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun addNewProduct(){
        TODO()
    }

    companion object {
        const val EXTRA_VARIABLE = "com.example.foodcontroller"
    }

}
