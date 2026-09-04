package com.unsa.smartflu.login

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.unsa.smartflu.R
import com.unsa.smartflu.models.Device

class RegisterDevice : AppCompatActivity() {

    private lateinit var editTextUid: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editDeviceName: EditText
    private lateinit var editTagDescription: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var buttonReg: Button
    private lateinit var sizeOptions: Spinner
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_device)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        editTextUid = findViewById(R.id.etUid)
        editTextEmail = findViewById(R.id.etEmail)
        editDeviceName = findViewById(R.id.etName)
        editTagDescription = findViewById(R.id.etTag)
        buttonReg = findViewById(R.id.buttonReg)
        progressBar = findViewById(R.id.progressBar)
        sizeOptions = findViewById<Spinner>(R.id.spSize)
        val sizes = arrayOf("1/2", "3/4", "1", "2")
        val arrayAdp = ArrayAdapter(this@RegisterDevice, android.R.layout.simple_spinner_dropdown_item, sizes)
        sizeOptions.adapter = arrayAdp

        sizeOptions?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Toast.makeText(this@RegisterDevice, "item is ${sizes[p2]}, xd: ${p2}", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@RegisterDevice, "No se ha seleccionado nada", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun registerDevice(v: View) {
        db = FirebaseFirestore.getInstance()
        progressBar.setVisibility(View.VISIBLE)
        val uid = editTextUid.text.toString()
        val email = editTextEmail.text.toString()
        val name = editDeviceName.text.toString()
        val tagDescription = editTagDescription.text.toString()
        val selectedSize = sizeOptions.selectedItem.toString()
        val size: Double

        when (selectedSize) {
            "1/2" -> size = 0.5
            "3/4" -> size = 0.75
            "1" -> size = 1.0
            "2" -> size = 2.0
            else -> size = 0.0
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this@RegisterDevice, "Ingresa el correo electrónico", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this@RegisterDevice, "Ingresa el nombre del dispositivo", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(tagDescription)) {
            Toast.makeText(this@RegisterDevice, "Repita el tag", Toast.LENGTH_SHORT).show()
            return
        }

        val device = Device(email, uid, 0, name, tagDescription, size)

        db.collection("devices")
            .document(device.id)
            .set(device)
            .addOnSuccessListener {
                progressBar.visibility = View.GONE
                Toast.makeText(this@RegisterDevice, "Dispositivo registrado correctamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                progressBar.visibility = View.GONE
                Toast.makeText(this@RegisterDevice, "Error al registrar el dispositivo: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}