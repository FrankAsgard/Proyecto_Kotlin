package com.example.shopnow

import android.content.Context
import android.content.Intent
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.shopnow.utils.ProgressDialogFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.mindrot.jbcrypt.BCrypt

class LoginActivity(private val context: Context) {

    private val db = Firebase.firestore
    private var progressDialog: DialogFragment = ProgressDialogFragment.newInstance()

    fun login(usernameInput: EditText, passwordInput: EditText) {
        val username = usernameInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            showErrorDialog("Por favor completa todos los campos.")
            return
        }

        // Mostrar el ProgressDialog después de la validación de los campos
        progressDialog.show((context as AppCompatActivity).supportFragmentManager, "progress_dialog")

        db.collection("users").whereEqualTo("username", username).get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    progressDialog.dismiss()
                    showErrorDialog("Nombre de usuario no encontrado.")
                } else {
                    for (document in result) {
                        val storedPassword = document.getString("password")
                        if (storedPassword != null && BCrypt.checkpw(password, storedPassword)) {
                            progressDialog.dismiss()
                            showSuccessDialog()
                        } else {
                            progressDialog.dismiss()
                            showErrorDialog("Contraseña incorrecta.")
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                showErrorDialog("Error al verificar el nombre de usuario: ${exception.message}")
            }
    }

    private fun showErrorDialog(message: String) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Error")
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("OK") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        alertDialog.show()
    }

    private fun showSuccessDialog() {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Login Exitoso")
        alertDialog.setMessage("¡Has iniciado sesión exitosamente!")
        alertDialog.setPositiveButton("OK") { dialogInterface, _ ->
            dialogInterface.dismiss()
            val intent = Intent(context, WelcomeActivity::class.java)
            context.startActivity(intent)
        }
        alertDialog.show()
    }
}
