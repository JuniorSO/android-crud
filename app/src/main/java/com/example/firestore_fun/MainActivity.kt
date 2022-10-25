package com.example.firestore_fun

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Firebase.firestore

        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtIdade = findViewById<EditText>(R.id.edtIdade)
        val edtTelefone = findViewById<EditText>(R.id.edtTelefone)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)

        val btnCadastrar = findViewById<Button>(R.id.btnCadastrar)

        val txtConsulta = findViewById<TextView>(R.id.txtLink)

        txtConsulta.setOnClickListener {
            val intent = Intent(this, DataActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnCadastrar.setOnClickListener {
            val pessoas = db.collection("pessoas")

            btnCadastrar.isEnabled = false

            val txtNome = edtNome.text.toString()
            val txtIdade = edtIdade.text.toString()
            val txtTelefone = edtTelefone.text.toString()
            val txtEmail = edtEmail.text.toString()

            if (txtNome.isEmpty() || txtIdade.isEmpty() || txtTelefone.isEmpty() || txtEmail.isEmpty()) {
                Toast.makeText(
                    baseContext, "Preencha todos os campos!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                pessoas
                    .whereEqualTo("email", txtEmail)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            Toast.makeText(
                                baseContext, "Esse email está em uso.",
                                Toast.LENGTH_SHORT
                            ).show()

                            edtEmail.text.clear()

                            btnCadastrar.isEnabled = true
                        } else {
                            val pessoa = hashMapOf(
                                "nome" to txtNome,
                                "idade" to txtIdade.toInt(),
                                "telefone" to txtTelefone,
                                "email" to txtEmail,
                            )

                            pessoas
                                .add(pessoa)
                                .addOnSuccessListener { documentReference ->
                                    Log.d(
                                        TAG,
                                        "DocumentSnapshot added with ID: ${documentReference.id}"
                                    )

                                    Toast.makeText(
                                        baseContext, "Registro adicionado com sucesso.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    btnCadastrar.isEnabled = true

                                    edtNome.text.clear()
                                    edtIdade.text.clear()
                                    edtTelefone.text.clear()
                                    edtEmail.text.clear()
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error adding document", e)

                                    Toast.makeText(
                                        baseContext, "Não foi possível adicionar o registro.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    edtNome.text.clear()
                                    edtIdade.text.clear()
                                    edtTelefone.text.clear()
                                    edtEmail.text.clear()

                                    btnCadastrar.isEnabled = true
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents: ", exception)

                        Toast.makeText(
                            baseContext, "Tente novamente mais tarde.",
                            Toast.LENGTH_SHORT
                        ).show()

                        btnCadastrar.isEnabled = true
                    }

            }
        }
    }
}