package com.example.foodcontroller

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.example.foodcontroller.MainActivity.Companion.EXTRA_VARIABLE
import kotlinx.android.synthetic.main.activity_new_product.*

class NewProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_product)

        var myData = intent.getStringArrayExtra(EXTRA_VARIABLE)

        val protein: String = findViewById<EditText>(R.id.edit_protein_number).toString()
        val carbohydrates: String = findViewById<EditText>(R.id.edit_carbohydrates_number).toString()
        val fat: String = findViewById<EditText>(R.id.edit_fat_number).toString()
        val calories: String = findViewById<EditText>(R.id.edit_calorie_number).toString()

        val button: Button = findViewById<Button>(R.id.add_button)
        button.setOnClickListener{
            myData = arrayOf<String>(protein, fat, carbohydrates, calories)
        }
    }

    companion object {
        const val EXTRA_RESPONSE_FROM_NEWPRODUCTACTIVITY = "com.example.foodcontroller"
    }
}
