package com.example.foodcontroller

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
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
    private lateinit var adapter: FoodAdapter
    private lateinit var productViewModel: FoodViewModel

    //Коды ответов.
    //С помощью них можно точно определить от каких активити пришли ответы.
    private val newProductActivityRequestCode = 1
    private val updateProductInNewProductActivity = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutManager = LinearLayoutManager(this)
        adapter = FoodAdapter(this)

        //Тут не создавался recyclerview почему - не понятно, по идее apply должен делать тоже самое, что и
        //приравнивание параметров ниже
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

        // Удаление определенного продукта через swipe
        val touchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val productPosition = viewHolder.adapterPosition
                    val productEntity : ProductEntity = adapter.getProductAtPosition(productPosition)
                    productViewModel.deleteTarget(productEntity)
                }
            }
        val touchHelper = ItemTouchHelper(touchHelperCallback)
        touchHelper.attachToRecyclerView(recyclerView)

        //Попытался сделать реакцию на нажатие пункта в recycler, не работает, думаю в корне не верно
        /*val test =
            object:
                RecyclerView.SimpleOnItemTouchListener() {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    if(e.action == MotionEvent.ACTION_BUTTON_PRESS)
                    {
                        Toast.makeText(
                            applicationContext,
                            "You touch me tum tum tum!",
                            Toast.LENGTH_LONG
                        ).show()
                        return true
                    }
                    return false
                }
            }
        test.onTouchEvent(recyclerView, MotionEvent.obtain(10L))*/

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

        adapter.setOnProductClickListener(object : FoodAdapter.UpdateProductClickListener{
            override fun onProductClick(view: View, position: Int) {
                val product = adapter.getProductAtPosition(position)
                launchUpdateNewProductActivity(product)
            }
        })

/*        val test = findViewById<Button>(R.id.button_in_eaten_food)
        test.setOnClickListener{
            Toast.makeText(
                this,
                "Oy you did it!",
                Toast.LENGTH_LONG
            )
        }*/

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

    fun launchUpdateNewProductActivity(product: ProductEntity) {
        val intent = Intent(this, NewProductActivity::class.java)
        val dataList = arrayOf(product.protein, product.fat, product.carbohydrates, product.calories)
        intent.putExtra(EXTRA_VARIABLE_UPDATE_PRODUCT, dataList)
        startActivityForResult(intent, updateProductInNewProductActivity)
    }

    companion object {
        const val EXTRA_VARIABLE = "com.example.foodcontroller"
        const val EXTRA_VARIABLE_UPDATE_PRODUCT = "product_updated"
    }

}
