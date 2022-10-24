package com.example.firestore_fun

import android.content.ContentValues
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
        val edtEndereco = findViewById<EditText>(R.id.edtEndereco)
        val edtCEP = findViewById<EditText>(R.id.edtCEP)
        val btnCadastrar = findViewById<Button>(R.id.btnCadastrar)
        val txtConsulta = findViewById<TextView>(R.id.txtLink)

        txtConsulta.setOnClickListener {
            val intent = Intent(this, TableActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnCadastrar.setOnClickListener {
            val txtNome = edtNome.text.toString()
            val txtIdade = edtIdade.text.toString()
            val txtTelefone = edtTelefone.text.toString()
            val txtEndereco = edtEndereco.text.toString()
            val txtCEP = edtCEP.text.toString()

            if (txtNome.isEmpty() || txtIdade.isEmpty() || txtTelefone.isEmpty() || txtEndereco.isEmpty() || txtCEP.isEmpty()) {
                Toast.makeText(
                    baseContext, "Preencha todos os campos!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val pessoa = hashMapOf(
                    "nome" to txtNome,
                    "idade" to txtIdade.toInt(),
                    "telefone" to txtTelefone,
                    "endereco" to txtEndereco,
                    "cep" to txtCEP
                )

                db.collection("pessoas")
                    .add(pessoa)
                    .addOnSuccessListener { documentReference ->
                        Log.d(
                            ContentValues.TAG,
                            "DocumentSnapshot added with ID: ${documentReference.id}"
                        )
                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error adding document", e)
                    }
            }
        }
    }
}