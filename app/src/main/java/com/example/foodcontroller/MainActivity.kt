package com.example.foodcontroller

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

/*        recyclerView = findViewById<RecyclerView>(R.id.food_recycler).apply {
            setHasFixedSize(true)
            adapter = this.adapter
            layoutManager = this.layoutManager
        }*/
        recyclerView = findViewById(R.id.food_recycler)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        productViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)
        productViewModel.allProducts.observe(this, Observer { products ->
            products?.let { adapter.setProduct(it) }
        })

        //Вызов активити для добавления нового продукта в общую базу хранения
        val buttonAddProduct = findViewById<FloatingActionButton>(R.id.fabAddNewProduct)
        buttonAddProduct.setOnClickListener {
            val intentToNewProduct = Intent(this@MainActivity, NewProductActivity::class.java)
            startActivityForResult(intentToNewProduct, newProductActivityRequestCode)
        }

        //Вызов активити для обновления существующего продукта в общей базе хранения
        //Надо подумать над повторным использованием существующего активити
        val buttonEditProduct = findViewById<FloatingActionButton>(R.id.fabUpdateProduct)
        buttonEditProduct.setOnClickListener {

        }

        //Удаление опрделенного продукта из общей базы хранения
        val buttonDeleteTargetProduct = findViewById<FloatingActionButton>(R.id.fabDeleteTargetProduct)
        buttonDeleteTargetProduct.setOnClickListener {
            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView : View = inflater.inflate(R.layout.popup_delete_target_small_window, null)
            val width : Int = LinearLayout.LayoutParams.WRAP_CONTENT
            val height : Int = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable : Boolean = true
            val popupWindow = PopupWindow(popupView, width, height, focusable)
            popupWindow.showAtLocation(it, Gravity.CENTER, 0, 0)

            val buttonDeleteInPopupWindow = findViewById<Button>(R.id.popupDeleteButton)
            buttonDeleteInPopupWindow.setOnClickListener {
                val target = findViewById<EditText>(R.id.target_to_delete).text.toString()
               // productViewModel.deleteTarget(productEntity = ProductEntity(target))
            }
            //Вызов текущего корневого viewgroup
            //val currentMainView = window.decorView.rootView as ViewGroup
            //val inflater : LayoutInflater = layoutInflater
            //val popupDeleteTargetView = inflater.inflate(R.layout.popup_delete_target_small_window, currentMainView, false)
            //val popUpWindow: PopupWindow = PopupWindow(this)



        }

        //Удаление все продуктов из общей базы хранения
        val buttonDeleteAllProducts = findViewById<FloatingActionButton>(R.id.fabDeleteAllProduct)
        buttonDeleteAllProducts.setOnClickListener {
            productViewModel.deleteAll()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == newProductActivityRequestCode &&
                resultCode == Activity.RESULT_OK) {
            data?.getStringArrayExtra(NewProductActivity.EXTRA_RESPONSE_FROM_NEW_PRODUCT_ACTIVITY)?.let {
                val product = ProductEntity(product = it[0],
                                            protein = it[1].toFloat(),
                                            fat = it[2].toFloat(),
                                            carbohydrates = it[3].toFloat(),
                                            calories = it[4].toFloat())
                productViewModel.insert(product)
            }
            // Унифицировать сообщение, либо добавить еще несколько, чтобы зависили от нажатой кнопки
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

    companion object {
        const val EXTRA_VARIABLE = "com.example.foodcontroller"
    }

}
