package com.example.smclone2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.smclone2.models.UserDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    // Views
    private lateinit var edtTxtName: EditText
    private lateinit var edtTxtEmail: EditText
    private lateinit var edtTxtPassword: EditText
    private lateinit var edtTxtConfPassword: EditText
    private lateinit var btnSignup: Button

    // Firebase
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Binding views
        edtTxtName = findViewById(R.id.SignUpActivity_edtText_name)
        edtTxtEmail = findViewById(R.id.SignUpActivity_edtText_email)
        edtTxtPassword = findViewById(R.id.SignUpActivity_edtText_password)
        edtTxtConfPassword = findViewById(R.id.SignUpActivity_edtText_confPassword)
        btnSignup = findViewById(R.id.SignUpActivity_btn_signup)

        // Setting up firebase
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        // Implementing signup button
        btnSignup.setOnClickListener {
            createUser()
        }
    }

    private fun createUser(){
        val name = edtTxtName.text.toString()
        val email = edtTxtEmail.text.toString()
        val password = edtTxtPassword.text.toString()
        val confPassword = edtTxtConfPassword.text.toString()

        if (password != confPassword) return

        // Creating user authentication
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Toast.makeText(this, "User Created", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show()
            }

        // Adding user details to database
        val uid = mAuth.currentUser?.uid!!

        val userDetails = UserDetails(uid, name, email)

        mDbRef
            .child("users")
            .child(uid)
            .setValue(userDetails)
    }
}