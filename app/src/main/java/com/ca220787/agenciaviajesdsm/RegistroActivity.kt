package com.ca220787.agenciaviajesdsm

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class RegistroActivity : AppCompatActivity() {

    private lateinit var auti: FirebaseAuth
    private lateinit var etMail: EditText
    private lateinit var etPass: EditText
    private lateinit var etPassConf: EditText
    private lateinit var btnRegi: Button
    private lateinit var btnIrLogi: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val barSys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(barSys.left, barSys.top, barSys.right, barSys.bottom)
            insets
        }

        auti = FirebaseAuth.getInstance()

        etMail = findViewById(R.id.etMail)
        etPass = findViewById(R.id.etPass)
        etPassConf = findViewById(R.id.etPassConf)
        btnRegi = findViewById(R.id.btnRegi)
        btnIrLogi = findViewById(R.id.btnIrLogi)

        btnRegi.setOnClickListener {
            registrarUsuario()
        }

        btnIrLogi.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registrarUsuario() {
        val mail = etMail.text.toString().trim()
        val pass = etPass.text.toString().trim()
        val passConf = etPassConf.text.toString().trim()

        if (!validarRegistro(mail, pass, passConf)) {
            return
        }

        btnRegi.isEnabled = false

        auti.createUserWithEmailAndPassword(mail, pass)
            .addOnCompleteListener(this) { tarea ->
                btnRegi.isEnabled = true

                if (tarea.isSuccessful) {
                    Toast.makeText(this, getString(R.string.msg_ok_regi), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        tarea.exception?.message ?: getString(R.string.msg_err_gene),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun validarRegistro(mail: String, pass: String, passConf: String): Boolean {
        var esVali = true

        if (mail.isEmpty()) {
            etMail.error = getString(R.string.msg_req)
            esVali = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            etMail.error = getString(R.string.msg_mail_inv)
            esVali = false
        } else {
            etMail.error = null
        }

        if (pass.isEmpty()) {
            etPass.error = getString(R.string.msg_req)
            esVali = false
        } else if (pass.length < 6) {
            etPass.error = getString(R.string.msg_pass_min)
            esVali = false
        } else {
            etPass.error = null
        }

        if (passConf.isEmpty()) {
            etPassConf.error = getString(R.string.msg_req)
            esVali = false
        } else if (pass != passConf) {
            etPassConf.error = getString(R.string.msg_pass_diff)
            esVali = false
        } else {
            etPassConf.error = null
        }

        return esVali
    }
}