package com.example.shopnow

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.shopnow.models.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var fullNameInput: EditText
    private lateinit var registerButton: Button
    private lateinit var loginLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        emailInput = findViewById(R.id.email_input)
        fullNameInput = findViewById(R.id.full_name_input)
        registerButton = findViewById(R.id.register_btn)
        loginLink = findViewById(R.id.login_link)

        registerButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            val email = emailInput.text.toString()
            val fullName = fullNameInput.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty() && fullName.isNotEmpty()) {
                val newUser = User(username, password, email, fullName)
                println(newUser)
                showSuccessDialog()
            } else {
                showErrorDialog()
            }
        }

        loginLink.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showSuccessDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Registro Exitoso")
        alertDialog.setMessage("¡Tu cuenta ha sido registrada exitosamente!")
        alertDialog.setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
            // Redirige a WelcomeActivity
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }
        alertDialog.show()
    }

    private fun showErrorDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Error")
        alertDialog.setMessage("Por favor completa todos los campos.")
        alertDialog.setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }
        alertDialog.show()
    }
}
