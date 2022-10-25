package com.example.firestore_fun

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DataActivity : AppCompatActivity() {
    private lateinit var alert: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        val db = Firebase.firestore

        val btnEditar = findViewById<Button>(R.id.btnEditar)
        val btnExcluir = findViewById<Button>(R.id.btnExcluir)
        val btnRefresh = findViewById<Button>(R.id.btnRefresh)

        val txtCadastrar = findViewById<TextView>(R.id.txtLink)

        val display = findViewById<TextView>(R.id.display)

        db.collection("pessoas")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    display.append("Nome: ${document.data["nome"]}\n")
                    display.append("Idade: ${document.data["idade"]}\n")
                    display.append("Telefone: ${document.data["telefone"]}\n")
                    display.append("Email: ${document.data["email"]}\n\n")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)

                Toast.makeText(
                    baseContext, "Não foi possível recuperar os registros",
                    Toast.LENGTH_SHORT
                ).show()
            }

        txtCadastrar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnRefresh.setOnClickListener {
            val intent = Intent(this, DataActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnExcluir.setOnClickListener {
            val build = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_delete, null)
            build.setView(view)

            val btnClose = view.findViewById<Button>(R.id.btnClose)
            val btnExcluirReg = view.findViewById<Button>(R.id.btnExcluirReg)

            val searchEmail = view.findViewById<EditText>(R.id.edtSearchEmail)

            btnClose.setOnClickListener {
                alert.dismiss()
            }

            btnExcluirReg.setOnClickListener {
                val txtSearchEmail = searchEmail.text.toString()

                db.collection("pessoas").whereEqualTo("email", txtSearchEmail).get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            for (document in documents)
                                db.collection("pessoas").document(document.id).delete()
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            baseContext,
                                            "Registro excluído.",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        alert.dismiss()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            baseContext,
                                            "Não foi possível excluir o registro.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                        } else {
                            Toast.makeText(
                                baseContext, "Esse registro não existe.",
                                Toast.LENGTH_SHORT
                            ).show()

                            searchEmail.text.clear()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            baseContext,
                            "Não foi possível encontrar o registro.",
                            Toast.LENGTH_SHORT
                        ).show()

                        searchEmail.text.clear()
                    }
            }

            alert = build.create()
            alert.show()
            alert.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        btnEditar.setOnClickListener {
            val build = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_edit, null)
            build.setView(view)

            val btnClose = view.findViewById<Button>(R.id.btnClose)
            val btnEditarReg = view.findViewById<Button>(R.id.btnEditarReg)

            val searchEmail = view.findViewById<EditText>(R.id.edtSearchEmail)

            btnClose.setOnClickListener {
                alert.dismiss()
            }

            btnEditarReg.setOnClickListener {
                val txtSearchEmail = searchEmail.text.toString()

                db.collection("pessoas").whereEqualTo("email", txtSearchEmail).get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            var docId = ""
                            for (document in documents) {
                                docId = document.id
                            }

                            val intent = Intent(this, EditActivity::class.java)
                            intent.putExtra("DocId", docId)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                baseContext, "Esse registro não existe.",
                                Toast.LENGTH_SHORT
                            ).show()

                            searchEmail.text.clear()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            baseContext,
                            "Não foi possível encontrar o registro.",
                            Toast.LENGTH_SHORT
                        ).show()

                        searchEmail.text.clear()
                    }
            }

            alert = build.create()
            alert.show()
            alert.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}