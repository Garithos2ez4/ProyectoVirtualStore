package com.example.sistemadefacturacion_pruni.Cliente

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sistemadefacturacion_pruni.Vendedor.MainActivityVendedor
import com.example.sistemadefacturacion_pruni.databinding.ActivityLoginClienteBinding
import com.google.firebase.auth.FirebaseAuth

class LoginClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginClienteBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.btnoLginC.setOnClickListener {
            validarInfo()
        }
        binding.tvRegistrarC.setOnClickListener {
            startActivity(Intent(applicationContext,RegistroClienteActivity::class.java))
        }

    }
    private var email = ""
    private var password = ""
    private fun validarInfo(){
        email = binding.etEmailC.text.toString().trim()
        password = binding.etPassword.text.toString().trim()

        if (email.isEmpty()){
            binding.etEmailC.error="Ingrese su email"
            binding.etEmailC.requestFocus()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmailC.error="Email invalido"
            binding.etEmailC.requestFocus()
        }else if (password.isEmpty()){
            binding.etPassword.error="Ingrese su contraseña"
            binding.etPassword.requestFocus()
        }else{
            loginCliente()

        }

    }

    private fun loginCliente() {
        progressDialog.setMessage("Ingresando")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityCliente::class.java))
                finish()
                Toast.makeText(
                    this,
                    "Bienvenido(a)",
                    Toast.LENGTH_SHORT

                ).show()
            }
            .addOnFailureListener { e->
                Toast.makeText(this,"Fallo el login debido a ${e.message}",
                    Toast.LENGTH_SHORT).show()
            }
    }
}