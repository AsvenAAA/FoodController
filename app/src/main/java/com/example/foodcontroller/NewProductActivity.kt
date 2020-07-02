package com.example.foodcontroller

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView

class NewProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_product)

        val productView = findViewById<TextView>(R.id.edit_product_name)
        val proteinView = findViewById<TextView>(R.id.edit_protein_number)
        val fatView = findViewById<TextView>(R.id.edit_fat_number)
        val carbohydratesView = findViewById<TextView>(R.id.edit_carbohydrates_number)
        val calorieView = findViewById<TextView>(R.id.edit_calorie_number)
        val button = findViewById<Button>(R.id.save_button)

        intent.extras?.let {
            val product = it.getStringArray(MainActivity.EXTRA_VARIABLE_UPDATE_PRODUCT)
            product?.let {
                productView.text = product[0]
                proteinView.text = product[1]
                fatView.text = product[2]
                carbohydratesView.text = product[3]
                calorieView.text = product[4]
            }
        }

        button.setOnClickListener{
            val replyIntent = Intent()
            if(TextUtils.isEmpty(proteinView.text) and
               TextUtils.isEmpty(fatView.text) and
               TextUtils.isEmpty(carbohydratesView.text) and
               TextUtils.isEmpty(calorieView.text)) {

                setResult(Activity.RESULT_CANCELED, replyIntent)

            } else {
                val product = productView.text.toString()
                val protein = proteinView.text.toString()
                val fat = fatView.text.toString()
                val carbohydrates = carbohydratesView.text.toString()
                val calorie = calorieView.text.toString()
                val dataList = arrayOf(product, protein, fat, carbohydrates, calorie)

                replyIntent.putExtra(EXTRA_RESPONSE_FROM_NEW_PRODUCT_ACTIVITY, dataList)
                if(intent.extras != null && intent.extras!!.containsKey(EXTRA_RESPONSE_FROM_NEW_PRODUCT_ACTIVITY_AFTER_UPDATE)){
                    //val id = intent.extras.getInt(
                    //    EXTRA_RESPONSE_FROM_NEW_PRODUCT_ACTIVITY_AFTER_UPDATE, -1)
                }
                setResult(Activity.RESULT_OK, replyIntent)
            }
            //Эта команда завершает работу текущего активити
            finish()
        }
    }


    companion object {
        const val EXTRA_RESPONSE_FROM_NEW_PRODUCT_ACTIVITY = "com.example.foodcontroller"
        const val EXTRA_RESPONSE_FROM_NEW_PRODUCT_ACTIVITY_AFTER_UPDATE = "com.example.foodcontroller.REPLY_AFTER_UPDATE"
    }
}
