package com.ca220787.agenciaviajesdsm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ca220787.agenciaviajesdsm.data.Destino
import com.ca220787.agenciaviajesdsm.util.ImgUtil
import com.google.firebase.database.FirebaseDatabase
import java.text.DecimalFormat

class DetalleActivity : AppCompatActivity() {

    private lateinit var imgDest: ImageView
    private lateinit var tvNomb: TextView
    private lateinit var tvPais: TextView
    private lateinit var tvPrec: TextView
    private lateinit var tvDesc: TextView
    private lateinit var btnEdit: Button
    private lateinit var btnElim: Button
    private lateinit var btnVolv: Button

    private var idDest: String = ""
    private val dbRef = FirebaseDatabase.getInstance().getReference("destinos")

    private val decFor = DecimalFormat("$0.00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val barSys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(barSys.left, barSys.top, barSys.right, barSys.bottom)
            insets
        }

        imgDest = findViewById(R.id.imgDest)
        tvNomb = findViewById(R.id.tvNomb)
        tvPais = findViewById(R.id.tvPais)
        tvPrec = findViewById(R.id.tvPrec)
        tvDesc = findViewById(R.id.tvDesc)
        btnEdit = findViewById(R.id.btnEdit)
        btnElim = findViewById(R.id.btnElim)
        btnVolv = findViewById(R.id.btnVolv)

        idDest = intent.getStringExtra("idDest") ?: ""

        cargarDetalle()

        btnEdit.setOnClickListener {
            val inte = Intent(this, DestinoFormActivity::class.java)
            inte.putExtra("idDest", idDest)
            startActivity(inte)
        }

        btnElim.setOnClickListener {
            mostrarAlertaEliminar()
        }

        btnVolv.setOnClickListener {
            finish()
        }
    }

    private fun cargarDetalle() {
        dbRef.child(idDest).get().addOnSuccessListener { snap ->
            val dest = snap.getValue(Destino::class.java)

            if (dest != null) {
                tvNomb.text = dest.nombDest
                tvPais.text = dest.paisDest
                tvPrec.text = decFor.format(dest.precDest)
                tvDesc.text = dest.descDest

                val bit = ImgUtil.base64ABitmap(dest.imagBase)
                if (bit != null) {
                    imgDest.setImageBitmap(bit)
                }
            }
        }
    }

    private fun mostrarAlertaEliminar() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.msg_elim_conf))
            .setPositiveButton(getString(R.string.btn_elim)) { _, _ ->
                eliminarDestino()
            }
            .setNegativeButton(getString(R.string.btn_canc), null)
            .show()
    }

    private fun eliminarDestino() {
        dbRef.child(idDest).removeValue().addOnSuccessListener {
            Toast.makeText(this, getString(R.string.msg_elim_ok), Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, getString(R.string.msg_err_gene), Toast.LENGTH_SHORT).show()
        }
    }
}