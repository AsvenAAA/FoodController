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
        val saveButton = findViewById<Button>(R.id.save_button)
        //Не работает, тут попытка принять и отобразить уже существующие данные
        val intentExtra = intent.extras
        if(intentExtra != null) {
            val product = intentExtra.getStringArray(MainActivity.EXTRA_VARIABLE_UPDATE_PRODUCT)
            product?.let {
                productView.text = product[0]
                proteinView.text = product[1]
                fatView.text = product[2]
                carbohydratesView.text = product[3]
                calorieView.text = product[4]
            }
        }

/*        intent.extras?.let {
            val product = it.getStringArray(MainActivity.EXTRA_VARIABLE_UPDATE_PRODUCT)
            product?.let {
                productView.text = product[0]
                proteinView.text = product[1]
                fatView.text = product[2]
                carbohydratesView.text = product[3]
                calorieView.text = product[4]
            }
        }*/

        saveButton.setOnClickListener{
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
                //Не работает, тут попытка отослать в main обновленные данные
                replyIntent.putExtra(EXTRA_RESPONSE_FROM_NEW_PRODUCT_ACTIVITY, dataList)
                if(intentExtra != null && intentExtra.containsKey(EXTRA_RESPONSE_FROM_NEW_PRODUCT_ACTIVITY_AFTER_UPDATE)){
                    val id = intentExtra.getInt(
                        EXTRA_RESPONSE_FROM_NEW_PRODUCT_ACTIVITY_AFTER_UPDATE, -1)
                    if (id != -1) {
                        replyIntent.putExtra(EXTRA_RESPONSE_FROM_NEW_PRODUCT_ACTIVITY_AFTER_UPDATE, id)
                    }
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
