package com.unsa.smartflu.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.unsa.smartflu.MainActivity
import com.unsa.smartflu.R

class RegisterUser : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextRepeatPassword: EditText
    private lateinit var buttonReg: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editTextEmail = findViewById(R.id.etCorreo)
        editTextPassword = findViewById(R.id.etPassword)
        editTextRepeatPassword = findViewById(R.id.etRepeatPassword)
        buttonReg = findViewById(R.id.buttonReg)
        progressBar = findViewById(R.id.progressBar)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    fun register(v: View) {
        progressBar.setVisibility(View.VISIBLE)
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        val repeatPassword = editTextRepeatPassword.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this@RegisterUser, "Ingresa el correo electrónico", Toast.LENGTH_SHORT).show()
            progressBar.setVisibility(View.GONE)
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this@RegisterUser, "Ingresa la contraseña", Toast.LENGTH_SHORT).show()
            progressBar.setVisibility(View.GONE)
            return
        }
        if (TextUtils.isEmpty(repeatPassword)) {
            Toast.makeText(this@RegisterUser, "Repita la contraseña", Toast.LENGTH_SHORT).show()
            progressBar.setVisibility(View.GONE)
            return
        }

        if (password != repeatPassword) {
            Toast.makeText(this@RegisterUser, "Repita la contrasena correctamente.", Toast.LENGTH_SHORT).show()
            progressBar.setVisibility(View.GONE)
            return
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = mAuth.currentUser
                    val userData = hashMapOf(
                        "devs" to arrayListOf<String>()
                    )
                    
                    if (user?.email != null) {
                        db.collection("users").document(user.email!!)
                            .set(userData)
                            .addOnSuccessListener {
                                progressBar.setVisibility(View.GONE)
                                Toast.makeText(
                                    this@RegisterUser, "Cuenta creada y configurada.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                goToPrincipal(user)
                            }
                            .addOnFailureListener {
                                progressBar.setVisibility(View.GONE)
                                Toast.makeText(
                                    this@RegisterUser, "Cuenta creada, pero hubo un error al configurar Firestore.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                goToPrincipal(user)
                            }
                    } else {
                        progressBar.setVisibility(View.GONE)
                        goToPrincipal(user)
                    }
                } else {
                    progressBar.setVisibility(View.GONE)
                    Toast.makeText(
                        this@RegisterUser, "Autenticación fallida.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun goToPrincipal(user: FirebaseUser?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun goToLogin(v: View) {
        val intent = Intent(this, LoginUser::class.java)
        startActivity(intent)
        //finish()
    }
}