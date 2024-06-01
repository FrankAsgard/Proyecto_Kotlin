package com.example.shopnow

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var loginManager: LoginManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usernameInput: EditText = findViewById(R.id.username_input)
        val passwordInput: EditText = findViewById(R.id.password_input)
        val errorUsername: TextView = findViewById(R.id.error_username)
        val errorPassword: TextView = findViewById(R.id.error_password)
        val loginButton: Button = findViewById(R.id.login_btn)
        val createAccountLink: TextView = findViewById(R.id.create_account_link)

        loginManager = LoginManager(this)

        loginButton.setOnClickListener {
            loginManager.login(usernameInput, passwordInput, errorUsername, errorPassword)
        }

        createAccountLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
