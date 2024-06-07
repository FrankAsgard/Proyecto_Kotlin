package com.example.shopnow

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginActivity(private val context: Context) {

    fun login(usernameInput: EditText, passwordInput: EditText, errorUsername: TextView, errorPassword: TextView) {
        val username = usernameInput.text.toString()
        val password = passwordInput.text.toString()

        var valid = true

        if (username.isEmpty()) {
            errorUsername.visibility = View.VISIBLE
            valid = false
        } else {
            errorUsername.visibility = View.GONE
        }

        if (password.isEmpty()) {
            errorPassword.visibility = View.VISIBLE
            valid = false
        } else {
            errorPassword.visibility = View.GONE
        }

        if (valid) {
            val intent = Intent(context, WelcomeActivity::class.java)
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}
