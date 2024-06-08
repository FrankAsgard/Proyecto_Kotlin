package com.example.shopnow.auth

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.shopnow.models.Role
import com.example.shopnow.models.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.mindrot.jbcrypt.BCrypt

class RegisterHelper(private val context: Context, private val progressDialog: DialogFragment) {

    private val db = Firebase.firestore

    fun registerUser(user: User, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        checkUserExists(user, onSuccess, onFailure)
    }

    private fun checkUserExists(user: User, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        db.collection("users").whereEqualTo("username", user.username).get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    progressDialog.dismiss()
                    onFailure("El nombre de usuario ya está registrado.")
                } else {
                    checkEmailExists(user, onSuccess, onFailure)
                }
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                onFailure("Error al verificar el nombre de usuario: ${exception.message}")
            }
    }

    private fun checkEmailExists(user: User, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        db.collection("users").whereEqualTo("email", user.email).get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    progressDialog.dismiss()
                    onFailure("El email ya está registrado.")
                } else {
                    saveUser(user, onSuccess, onFailure)
                }
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                onFailure("Error al verificar el email: ${exception.message}")
            }
    }

    private fun saveUser(user: User, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val hashedPassword = BCrypt.hashpw(user.password, BCrypt.gensalt())
        val userId = db.collection("users").document().id
        user.userId
        user.password = hashedPassword

        db.collection("users").document(userId).set(user)
            .addOnSuccessListener {
                progressDialog.dismiss()
                onSuccess()
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                onFailure("Error al registrar el usuario: ${exception.message}")
            }
    }
}
