package com.ca220787.agenciaviajesdsm

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ca220787.agenciaviajesdsm.data.Destino
import com.ca220787.agenciaviajesdsm.util.ImgUtil
import com.google.firebase.database.FirebaseDatabase

class DestinoFormActivity : AppCompatActivity() {

    private lateinit var tvTitu: TextView
    private lateinit var etNomb: EditText
    private lateinit var spnPais: Spinner
    private lateinit var etPrec: EditText
    private lateinit var etDesc: EditText
    private lateinit var imgDest: ImageView
    private lateinit var btnGale: Button
    private lateinit var btnGuar: Button
    private lateinit var btnCanc: Button

    private var uriImag: Uri? = null
    private var txtBase: String = ""
    private var idDestEdit: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_destino_form)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val barSys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(barSys.left, barSys.top, barSys.right, barSys.bottom)
            insets
        }

        tvTitu = findViewById(R.id.tvTitu)
        etNomb = findViewById(R.id.etNomb)
        spnPais = findViewById(R.id.spnPais)
        etPrec = findViewById(R.id.etPrec)
        etDesc = findViewById(R.id.etDesc)
        imgDest = findViewById(R.id.imgDest)
        btnGale = findViewById(R.id.btnGale)
        btnGuar = findViewById(R.id.btnGuar)
        btnCanc = findViewById(R.id.btnCanc)

        cargarSpinner()

        btnGale.setOnClickListener {
            abrirGaleria()
        }

        btnGuar.setOnClickListener {
            guardarDestino()
        }

        btnCanc.setOnClickListener {
            finish()
        }

        idDestEdit = intent.getStringExtra("idDest")

        if (idDestEdit != null) {
            cargarDatosEdicion()
        }
    }

    private fun cargarDatosEdicion() {
        FirebaseDatabase.getInstance().getReference("destinos")
            .child(idDestEdit!!)
            .get()
            .addOnSuccessListener { snap ->
                val dest = snap.getValue(Destino::class.java)

                if (dest != null) {
                    etNomb.setText(dest.nombDest)
                    etPrec.setText(dest.precDest.toString())
                    etDesc.setText(dest.descDest)
                    txtBase = dest.imagBase

                    val arrPais = resources.getStringArray(R.array.arr_pais)
                    val index = arrPais.indexOf(dest.paisDest)
                    if (index >= 0) {
                        spnPais.setSelection(index)
                    }

                    val bit = ImgUtil.base64ABitmap(dest.imagBase)
                    if (bit != null) {
                        imgDest.setImageBitmap(bit)
                    }

                    tvTitu.text = getString(R.string.btn_edit)
                }
            }
    }

    private fun cargarSpinner() {
        val arrPais = resources.getStringArray(R.array.arr_pais)
        val adaPai = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrPais)
        adaPai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnPais.adapter = adaPai
    }

    private fun abrirGaleria() {
        val inte = Intent(Intent.ACTION_PICK)
        inte.type = "image/*"
        startActivityForResult(inte, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            uriImag = data?.data
            if (uriImag != null) {
                imgDest.setImageURI(uriImag)
                txtBase = ImgUtil.uriABase64(contentResolver, uriImag!!)
                Toast.makeText(this, getString(R.string.msg_ok_img), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun guardarDestino() {
        val nomb = etNomb.text.toString().trim()
        val pais = spnPais.selectedItem.toString()
        val precTxt = etPrec.text.toString().trim()
        val desc = etDesc.text.toString().trim()

        if (!validarFormulario(nomb, pais, precTxt, desc)) {
            return
        }

        val prec = precTxt.toDouble()
        val idDest = idDestEdit ?: FirebaseDatabase.getInstance().getReference("destinos").push().key ?: return
        val dest = Destino(
            idDest = idDest,
            nombDest = nomb,
            paisDest = pais,
            precDest = prec,
            descDest = desc,
            imagBase = txtBase
        )

        btnGuar.isEnabled = false

        FirebaseDatabase.getInstance()
            .getReference("destinos")
            .child(idDest)
            .setValue(dest)
            .addOnSuccessListener {
                btnGuar.isEnabled = true
                Toast.makeText(this, getString(R.string.msg_ok_guar), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                btnGuar.isEnabled = true
                Toast.makeText(this, getString(R.string.msg_err_gene), Toast.LENGTH_SHORT).show()
            }
    }

    private fun validarFormulario(nomb: String, pais: String, precTxt: String, desc: String): Boolean {
        var esVali = true

        if (nomb.isEmpty()) {
            etNomb.error = getString(R.string.msg_req)
            esVali = false
        } else {
            etNomb.error = null
        }

        if (pais == resources.getStringArray(R.array.arr_pais)[0]) {
            Toast.makeText(this, getString(R.string.lbl_pais_hint), Toast.LENGTH_SHORT).show()
            esVali = false
        }

        val prec = precTxt.toDoubleOrNull()
        if (precTxt.isEmpty() || prec == null || prec <= 0.0) {
            etPrec.error = getString(R.string.msg_prec_inv)
            esVali = false
        } else {
            etPrec.error = null
        }

        if (desc.isEmpty()) {
            etDesc.error = getString(R.string.msg_req)
            esVali = false
        } else if (desc.length < 20) {
            etDesc.error = getString(R.string.msg_desc_min)
            esVali = false
        } else {
            etDesc.error = null
        }

        if (txtBase.isEmpty()) {
            Toast.makeText(this, getString(R.string.msg_img_req), Toast.LENGTH_SHORT).show()
            esVali = false
        }

        return esVali
    }
}