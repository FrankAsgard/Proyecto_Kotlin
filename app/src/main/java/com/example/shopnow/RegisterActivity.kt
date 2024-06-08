package com.example.shopnow

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.shopnow.auth.RegisterHelper
import com.example.shopnow.models.User
import com.example.shopnow.utils.ProgressDialogFragment

class RegisterActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var fullNameInput: EditText
    private lateinit var registerButton: Button
    private lateinit var loginLink: TextView
    private lateinit var progressDialog: DialogFragment
    private lateinit var registerHelper: RegisterHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initializeViews()
        registerHelper = RegisterHelper(this, progressDialog)

        registerButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val fullName = fullNameInput.text.toString().trim()

            if (fieldsAreEmpty(username, password, email, fullName)) {
                showErrorDialog("Por favor completa todos los campos.")
            } else {
                progressDialog.show(supportFragmentManager, "progress_dialog")
                registerHelper.registerUser(User(null, username, password, email, fullName),
                    { showSuccessDialog() },
                    { message -> showErrorDialog(message) }
                )
            }
        }

        loginLink.setOnClickListener { onBackPressed() }
    }

    private fun initializeViews() {
        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        emailInput = findViewById(R.id.email_input)
        fullNameInput = findViewById(R.id.full_name_input)
        registerButton = findViewById(R.id.register_btn)
        loginLink = findViewById(R.id.login_link)
        progressDialog = ProgressDialogFragment.newInstance()
    }

    private fun fieldsAreEmpty(vararg fields: String): Boolean {
        return fields.any { it.isEmpty() }
    }

    private fun showSuccessDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Registro Exitoso")
        alertDialog.setMessage("¡Tu cuenta ha sido registrada exitosamente!")
        alertDialog.setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }
        alertDialog.show()
    }

    private fun showErrorDialog(message: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Error")
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }
        alertDialog.show()
    }
}
