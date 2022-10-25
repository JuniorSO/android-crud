package com.example.firestore_fun

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val db = Firebase.firestore

        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtIdade = findViewById<EditText>(R.id.edtIdade)
        val edtTelefone = findViewById<EditText>(R.id.edtTelefone)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)

        val btnSalvar = findViewById<Button>(R.id.btnSalvar)

        val docId = intent.getStringExtra("DocId").toString()

        db.collection("pessoas").document(docId).get()
            .addOnSuccessListener { document ->
                edtNome.setText(document.data!!["nome"].toString())
                edtIdade.setText(document.data!!["idade"].toString())
                edtTelefone.setText(document.data!!["telefone"].toString())
                edtEmail.setText(document.data!!["email"].toString())
            }

        btnSalvar.setOnClickListener {
            btnSalvar.isEnabled = false

            val txtNomeNovo = edtNome.text.toString()
            val txtIdadeNova = edtIdade.text.toString()
            val txtTelefoneNovo = edtTelefone.text.toString()
            val txtEmailNovo = edtEmail.text.toString()

            db.collection("pessoas").document(docId)
                .update(
                    mapOf(
                        "nome" to txtNomeNovo,
                        "idade" to txtIdadeNova,
                        "telefone" to txtTelefoneNovo,
                        "email" to txtEmailNovo,
                    )
                )
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated!")

                    Toast.makeText(baseContext, "Registro atualizado.", Toast.LENGTH_SHORT).show()

                    btnSalvar.isEnabled = true

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error updating document", e)

                    Toast.makeText(
                        baseContext,
                        "Não foi possível atualizar o registro, tente novamente",
                        Toast.LENGTH_SHORT
                    ).show()

                    btnSalvar.isEnabled = true
                }

        }
    }
}