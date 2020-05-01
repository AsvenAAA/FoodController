package com.example.foodcontroller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.math.BigDecimal
import java.math.BigInteger

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RecyclerView.Adapter<FoodAdapter.FoodHolder>
    var myData: Array<String> = arrayOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutManager = LinearLayoutManager(this)
        adapter = FoodAdapter(getData())

        recyclerView = findViewById<RecyclerView>(R.id.food_recycler).apply {
            setHasFixedSize(true)
            adapter = this.adapter
            layoutManager = this.layoutManager
        }


        val buttonAddProduct: Button = findViewById<Button>(R.id.main_add_button)
        buttonAddProduct.setOnClickListener {
            val intentToNewProduct = Intent(this@MainActivity, NewProductActivity::class.java)//.apply {
                //putExtra("EXTRA_VARIABLE", myData)
            //}
            startActivity(intentToNewProduct)
        }

    }

    fun getData(): Array<Array<String>>{
        return arrayOf(myData)
    }

    fun addNewProduct(){
        TODO()
    }

    companion object {
        const val EXTRA_VARIABLE = "com.example.foodcontroller"
    }

}
