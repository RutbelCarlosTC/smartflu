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
import com.unsa.smartflu.MainActivity
import com.unsa.smartflu.R

class LoginUser : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            goToPrincipal(currentUser)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        editTextEmail = findViewById(R.id.etCorreo)
        editTextPassword = findViewById(R.id.etPassword)
        progressBar = findViewById(R.id.progressBar)
        buttonLogin = findViewById(R.id.buttonLogin)
        mAuth = FirebaseAuth.getInstance()
    }

    fun login(v: View) {
        progressBar.setVisibility(View.VISIBLE)
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this@LoginUser, "Ingresa el correo electrónico", Toast.LENGTH_SHORT).show()
            progressBar.setVisibility(View.GONE)
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this@LoginUser, "Ingresa la contraseña", Toast.LENGTH_SHORT).show()
            progressBar.setVisibility(View.GONE)
            return
        }

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressBar.setVisibility(View.GONE)
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    goToPrincipal(user)
                } else {
                    Toast.makeText(
                        baseContext,
                        "Autenticacion fallida.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun goToPrincipal(user: FirebaseUser?) {
        val intent = Intent(this,   MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun goToRegister(v: View) {
        val intent = Intent(this, RegisterUser::class.java)
        startActivity(intent)
        //finish()
    }
}