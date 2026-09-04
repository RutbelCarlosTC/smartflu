package com.unsa.smartflu.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.unsa.smartflu.MainActivity
import com.unsa.smartflu.R

class WelcomeView : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun goToLoginUser(v: View) {
        val intent = Intent(this, LoginUser::class.java)
        startActivity(intent)
        //finish()
    }

    fun goToRegisterUser(v: View) {
        val intent = Intent(this, RegisterUser::class.java)
        startActivity(intent)
        //finish()
    }

    fun goToRegisterDevice(v: View) {
        val intent = Intent(this, RegisterDevice::class.java)
        startActivity(intent)
        //finish()
    }

    private fun goToMain(user: FirebaseUser?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        //finish()
    }
}