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
import com.example.shopnow.models.Role
import com.example.shopnow.models.User
import com.example.shopnow.utils.ProgressDialogFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.mindrot.jbcrypt.BCrypt

class RegisterActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var fullNameInput: EditText
    private lateinit var registerButton: Button
    private lateinit var loginLink: TextView
    private lateinit var progressDialog: DialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initializeViews()

        registerButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val fullName = fullNameInput.text.toString().trim()

            if (fieldsAreEmpty(username, password, email, fullName)) {
                showErrorDialog("Por favor completa todos los campos.")
            } else {
                progressDialog.show(supportFragmentManager, "progress_dialog")
                checkUserExists(User(null, username, password, email, fullName))
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

    private fun checkUserExists(user: User) {
        db.collection("users").whereEqualTo("username", user.username).get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    progressDialog.dismiss()
                    showErrorDialog("El nombre de usuario ya está registrado.")
                } else {
                    checkEmailExists(user)
                }
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                showErrorDialog("Error al verificar el nombre de usuario: ${exception.message}")
            }
    }

    private fun checkEmailExists(user: User) {
        db.collection("users").whereEqualTo("email", user.email).get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    progressDialog.dismiss()
                    showErrorDialog("El email ya está registrado.")
                } else {
                    saveUser(user)
                }
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                showErrorDialog("Error al verificar el email: ${exception.message}")
            }
    }

    private fun saveUser(user: User) {
        val hashedPassword = BCrypt.hashpw(user.password, BCrypt.gensalt())
        val userId = db.collection("users").document().id
        val newUser = hashMapOf(
            "userId" to userId,
            "username" to user.username,
            "password" to hashedPassword,
            "email" to user.email,
            "fullName" to user.fullName,
            "role" to Role.CLIENT
        )

        db.collection("users").document(userId).set(newUser)
            .addOnSuccessListener {
                progressDialog.dismiss()
                showSuccessDialog()
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                showErrorDialog("Error al registrar el usuario: ${exception.message}")
            }
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
