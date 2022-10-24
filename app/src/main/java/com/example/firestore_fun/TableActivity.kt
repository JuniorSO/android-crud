package com.example.firestore_fun

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.marginBottom
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TableActivity : AppCompatActivity() {
    private lateinit var persRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table)

        val db = Firebase.firestore
        val entryList = findViewById<LinearLayout>(R.id.entry_list)

        val txtCadastro = findViewById<TextView>(R.id.txtLink)

        db.collection("pessoas")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    val card = CardView(this)
                    (card.layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin = 2
                    (card.layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin = 25

                    card.cardElevation = 8f
                    card.radius = 8f


                    val dynamicTextview = TextView(this)

                    dynamicTextview.text = document.data["nome"].toString()
                    dynamicTextview.textSize = 30f

                    card.addView(dynamicTextview)
                    card.setPadding(24, 24, 24, 24)

                    entryList.addView(card)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        txtCadastro.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}