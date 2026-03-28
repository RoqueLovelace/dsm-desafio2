package com.ca220787.agenciaviajesdsm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ca220787.agenciaviajesdsm.data.Destino
import com.ca220787.agenciaviajesdsm.ui.DestinoAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var auti: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    private lateinit var btnAgre: Button
    private lateinit var btnCerr: Button
    private lateinit var rvDest: RecyclerView
    private lateinit var tvVaci: TextView

    private lateinit var desAdp: DestinoAdapter
    private val lisDest = mutableListOf<Destino>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val barSys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(barSys.left, barSys.top, barSys.right, barSys.bottom)
            insets
        }

        auti = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("destinos")

        btnAgre = findViewById(R.id.btnAgre)
        btnCerr = findViewById(R.id.btnCerr)
        rvDest = findViewById(R.id.rvDest)
        tvVaci = findViewById(R.id.tvVaci)

        desAdp = DestinoAdapter(lisDest) { dest ->
            val inte = Intent(this, DetalleActivity::class.java)
            inte.putExtra("idDest", dest.idDest)
            startActivity(inte)
        }

        rvDest.layoutManager = LinearLayoutManager(this)
        rvDest.adapter = desAdp

        btnAgre.setOnClickListener {
            startActivity(Intent(this, DestinoFormActivity::class.java))
        }

        btnCerr.setOnClickListener {
            auti.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        if (auti.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        cargarDestinos()
    }

    private fun cargarDestinos() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nueLis = mutableListOf<Destino>()

                for (hijo in snapshot.children) {
                    val dest = hijo.getValue(Destino::class.java)
                    if (dest != null) {
                        nueLis.add(dest)
                    }
                }

                desAdp.actualizarDatos(nueLis)
                tvVaci.visibility = if (nueLis.isEmpty()) TextView.VISIBLE else TextView.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                tvVaci.visibility = TextView.VISIBLE
            }
        })
    }
}