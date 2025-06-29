package com.example.sistemadefacturacion_pruni.Cliente

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sistemadefacturacion_pruni.databinding.ActivityLoginTelBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class LoginTelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginTelBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    private var storedVerificationId: String? = null
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginTelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnEnviarCodigo.setOnClickListener {
            val telefono = binding.etTelefonoC.text.toString().trim()
            val codPais = binding.telCodePicker.selectedCountryCodeWithPlus

            if (telefono.isEmpty()) {
                Toast.makeText(this, "Ingrese su número", Toast.LENGTH_SHORT).show()
            } else {
                val numeroCompleto = codPais + telefono
                enviarCodigoVerificacion(numeroCompleto)
            }
        }

        binding.btnVerificarCod.setOnClickListener {
            val codigo = binding.etCodVer.text.toString().trim()
            if (codigo.isEmpty()) {
                Toast.makeText(this, "Ingrese el código recibido", Toast.LENGTH_SHORT).show()
            } else {
                verificarCodigo(codigo)
            }
        }

        binding.tvReenviarCod.setOnClickListener {
            val telefono = binding.etTelefonoC.text.toString().trim()
            val codPais = binding.telCodePicker.selectedCountryCodeWithPlus
            val numeroCompleto = codPais + telefono
            reenviarCodigo(numeroCompleto)
        }
    }

    private fun enviarCodigoVerificacion(telefono: String) {
        progressDialog.setMessage("Enviando código a $telefono")
        progressDialog.show()

        val opciones = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(telefono)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(opciones)
    }

    private fun verificarCodigo(codigo: String) {
        if (storedVerificationId == null) {
            Toast.makeText(this, "Primero debe solicitar el código", Toast.LENGTH_SHORT).show()
            return
        }

        progressDialog.setMessage("Verificando código")
        progressDialog.show()

        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, codigo)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Verificado correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun reenviarCodigo(telefono: String) {
        progressDialog.setMessage("Reenviando código")
        progressDialog.show()

        val opciones = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(telefono)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendToken)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(opciones)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Verificación automática
            firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(this@LoginTelActivity, "Verificado correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(this@LoginTelActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            progressDialog.dismiss()
            Toast.makeText(this@LoginTelActivity, "Falló: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("FIREBASE", "Error: ", e)
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            progressDialog.dismiss()
            storedVerificationId = verificationId
            resendToken = token
            Toast.makeText(this@LoginTelActivity, "Código enviado", Toast.LENGTH_SHORT).show()
        }
    }
}
