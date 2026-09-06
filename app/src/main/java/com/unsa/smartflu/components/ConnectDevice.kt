package com.unsa.smartflu.components

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.unsa.smartflu.MainActivity
import com.unsa.smartflu.R
import java.util.ArrayList

class ConnectDevice : AppCompatActivity() {

    private lateinit var editTextId: EditText
    private lateinit var buttonConnect: Button
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_device)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editTextId = findViewById(R.id.etIdDevice)
        buttonConnect = findViewById(R.id.buttonConnect)

        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
    }

    fun connect(v: View) {
        val id = editTextId.text.toString()

        if (TextUtils.isEmpty(id)) {
            Toast.makeText(this@ConnectDevice, "Ingresa el id del dispositivo", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            val userEmail = currentUser.email
            if (userEmail != null) {
                val userDocRef = db.collection("users").document(userEmail)

                db.runTransaction { transaction ->
                    val snapshot = transaction.get(userDocRef)
                    val devs = if (snapshot.exists()) {
                        snapshot.get("devs") as? ArrayList<String> ?: arrayListOf()
                    } else {
                        arrayListOf()
                    }
                    
                    if (!devs.contains(id)) {
                        devs.add(id)
                    }
                    
                    transaction.set(userDocRef, hashMapOf("devs" to devs), SetOptions.merge())
                }
                    .addOnSuccessListener {
                        Toast.makeText(this@ConnectDevice, "Dispositivo vinculado correctamente", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this@ConnectDevice, "Error al vincular el dispositivo: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

            } else {
                Toast.makeText(this@ConnectDevice, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}