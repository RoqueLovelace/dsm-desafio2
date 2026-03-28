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

class LoginActivity : AppCompatActivity() {

    private lateinit var auti: FirebaseAuth
    private lateinit var etMail: EditText
    private lateinit var etPass: EditText
    private lateinit var btnLogi: Button
    private lateinit var btnIrRegi: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val barSys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(barSys.left, barSys.top, barSys.right, barSys.bottom)
            insets
        }

        auti = FirebaseAuth.getInstance()

        etMail = findViewById(R.id.etMail)
        etPass = findViewById(R.id.etPass)
        btnLogi = findViewById(R.id.btnLogi)
        btnIrRegi = findViewById(R.id.btnIrRegi)

        btnLogi.setOnClickListener {
            iniciarSesion()
        }

        btnIrRegi.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if (auti.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun iniciarSesion() {
        val mail = etMail.text.toString().trim()
        val pass = etPass.text.toString().trim()

        if (!validarLogin(mail, pass)) {
            return
        }

        btnLogi.isEnabled = false

        auti.signInWithEmailAndPassword(mail, pass)
            .addOnCompleteListener(this) { tarea ->
                btnLogi.isEnabled = true

                if (tarea.isSuccessful) {
                    Toast.makeText(this, getString(R.string.msg_ok_logi), Toast.LENGTH_SHORT).show()
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

    private fun validarLogin(mail: String, pass: String): Boolean {
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

        return esVali
    }
}