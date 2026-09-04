package com.unsa.smartflu.components

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsa.smartflu.R
import com.unsa.smartflu.login.RegisterUser

class OptionsDevice : AppCompatActivity() {
    private lateinit var devName: String
    private lateinit var devId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_options_device)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = intent
        devName = intent.getStringExtra("nombre") ?: ""
        devId = intent.getStringExtra("id") ?: ""

    }

    fun goToLive(v: View) {
        val intent = Intent(this, DeviceLiveView::class.java)
        intent.putExtra("nombre", devName)
        intent.putExtra("id", devId)
        startActivity(intent)
        //finish()
    }

    fun goToForecasted(v: View) {
        val intent = Intent(this, DeviceForecastedData::class.java)
        intent.putExtra("nombre", devName)
        intent.putExtra("id", devId)
        startActivity(intent)
        //finish()
    }

    fun showConsumptionModal(v: View) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_consumption)

        val editTextInput = dialog.findViewById<EditText>(R.id.editTextInput)
        val textViewResult = dialog.findViewById<TextView>(R.id.textViewResult)
        val btnCalculate = dialog.findViewById<Button>(R.id.btnCalculate)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btnClose)

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btnCalculate.setOnClickListener {
            val inputValue = editTextInput.text.toString()
            if (inputValue.isNotEmpty()) {
                val result = calculateConsumption(inputValue.toDouble())
                textViewResult.text = result.toString()
            } else {
                Toast.makeText(this, "Por favor ingresa un valor", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    fun calculateConsumption(value: Double): Double {
        return value * 1.25
    }


}