package com.example.foodcontroller


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

        //Вызов активити для добавления нового продукта в общую базу хранения
        val buttonAddProduct = findViewById<FloatingActionButton>(R.id.fabAddNewProduct)
        buttonAddProduct.setOnClickListener {
            val intentToNewProduct = Intent(this@MainActivity, NewProductActivity::class.java)
            startActivityForResult(intentToNewProduct, newProductActivityRequestCode)
        }

        // Реакция на нажатие на область с описанием продукта
        adapter.setOnProductClickListener(object : FoodAdapter.UpdateProductClickListener {
            override fun onProductClick(view: View, position: Int) {
                val product = adapter.getProductAtPosition(position)
                launchUpdateNewProductActivity(product)
            }
        })

        //Вызывает небольшое окно, можно придумать в нем какое-либо действие
        val buttonDeleteTargetProduct = findViewById<FloatingActionButton>(R.id.fabDeleteTargetProduct)
        buttonDeleteTargetProduct.setOnClickListener {
            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView : View = inflater.inflate(R.layout.popup_delete_target_small_window, null)
            val width : Int = LinearLayout.LayoutParams.WRAP_CONTENT
            val height : Int = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable : Boolean = true
            val popupWindow = PopupWindow(popupView, width, height, focusable)
            popupWindow.showAtLocation(it, Gravity.CENTER, 0, 0)
        }

        //Удаление всех продуктов из общей базы хранения
        val buttonDeleteAllProducts = findViewById<FloatingActionButton>(R.id.fabDeleteAllProduct)
        buttonDeleteAllProducts.setOnClickListener {
            productViewModel.deleteAll()
        }

    }

    // Передает id нужного объекта в базе в активити с редактированием данных
    fun launchUpdateNewProductActivity(product: ProductEntity) {
        val intent = Intent(this, NewProductActivity::class.java)
        val dataList = arrayOf<String>(product.product, product.protein.toString(),
                                        product.fat.toString(), product.carbohydrates.toString(),
                                        product.calories.toString())
        intent.putExtra(EXTRA_VARIABLE_UPDATE_PRODUCT, dataList)
        intent.putExtra(EXTRA_VARIABLE, product.id)
        startActivityForResult(intent, updateProductInNewProductActivity)
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
        } else if (requestCode == updateProductInNewProductActivity && resultCode == Activity.RESULT_OK) {
            //Тут он не получает данные от NewProductActivity, из=за этого пропускается весь этот блок кода и ничего не выводится
            data?.getStringArrayExtra(NewProductActivity.EXTRA_RESPONSE_FROM_NEW_PRODUCT_ACTIVITY)?.let {
                val id = data.getIntExtra(NewProductActivity.EXTRA_RESPONSE_FROM_NEW_PRODUCT_ACTIVITY_AFTER_UPDATE, -1)
                // -1 это id по умолчанию, значит что ни какого id продукта передано не было, передается из NewProductActivity
                // id важно, так как по нему выбирается какой имменно обьект меняется в базе
                if (id != -1) {
                    val product = ProductEntity(id = id,
                        product = it[0],
                        protein = it[1].toFloat(),
                        fat = it[2].toFloat(),
                        carbohydrates = it[3].toFloat(),
                        calories = it[4].toFloat())
                    productViewModel.update(product)

                    Toast.makeText(
                        applicationContext,
                        R.string.update_product_in_database_success,
                        Toast.LENGTH_LONG
                    ).show()

                } else {
                    Toast.makeText(
                        applicationContext,
                        R.string.update_product_in_database_declined,
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        const val EXTRA_VARIABLE = "com.example.foodcontroller.add_new_product"
        const val EXTRA_VARIABLE_UPDATE_PRODUCT = "com.example.foodcontroller.product_updated"
    }

}
