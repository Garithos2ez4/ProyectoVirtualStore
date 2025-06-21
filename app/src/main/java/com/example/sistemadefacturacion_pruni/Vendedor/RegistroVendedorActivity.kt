package com.example.sistemadefacturacion_pruni.Vendedor

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sistemadefacturacion_pruni.Constantes
import com.example.sistemadefacturacion_pruni.R
import com.example.sistemadefacturacion_pruni.databinding.ActivityRegistroVendedorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class   RegistroVendedorActivity : AppCompatActivity() {
        private lateinit var binding: ActivityRegistroVendedorBinding
        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCancelable(false)

        binding.btnRegistrarV.setOnClickListener {
            validarInformacion()
        }

        }
    private  var nombres=""
    private  var email=""
    private  var password=""
    private  var cpassword=""

    private fun validarInformacion() {
    nombres=binding.etNombresV.text.toString().trim()
    email=binding.etEmailV.text.toString().trim()

    password=binding.etPassword.text.toString().trim()
    cpassword=binding.etCpassword.text.toString().trim()
        if (nombres.isEmpty()){
            binding.etNombresV.error="Ingrese sus nombres"
            binding.etNombresV.requestFocus()

        }else if (email.isEmpty()){
            binding.etEmailV.error="Ingrese su email"
            binding.etEmailV.requestFocus()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmailV.error="Ingrese un email valido"
            binding.etEmailV.requestFocus()
        } else if (password.isEmpty()){
            binding.etPassword.error="Ingrese su contraseña"
            binding.etPassword.requestFocus()
        }else if (password.length<6){
            binding.etPassword.error="La contraseña debe tener al menos 6 caracteres"
        } else if (cpassword.isEmpty()){
            binding.etCpassword.error="Confirme su contraseña"
            binding.etCpassword.requestFocus()
            }else if (password!=cpassword){
            binding.etCpassword.error="Las contraseñas no coinciden"
            binding.etCpassword.requestFocus()
        }else{
            registrarVendedor()
        }
    }

    private fun registrarVendedor() {
        progressDialog.setMessage("creando cuenta")
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                insertarInfoDB()

            }
            .addOnFailureListener{ e->
                Toast.makeText(this,"Fallo el registro debido a ${e.message}",Toast.LENGTH_SHORT).show() }
    }

    private fun insertarInfoDB(){
        progressDialog.setMessage("Guardando informacion")

        val uidBD = firebaseAuth.uid
        val nombresBD = nombres
        val emailBD = email
       // val tipoUsuario ="vendedor"
        val TiempoBD = Constantes().obtenerTiempoD()

        val datosVendedor = HashMap<String,Any>()
        datosVendedor["uid"]="$uidBD"
        datosVendedor["nombres"]="$nombresBD"
        datosVendedor["email"]="$emailBD"
        datosVendedor["tipoUsuario"]="vendedor"
        datosVendedor["Tiempo"]=TiempoBD

        val references = FirebaseDatabase.getInstance().getReference("Usuarios")
        references.child(uidBD!!)
            .setValue(datosVendedor)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this,MainActivityVendedor::class.java))

                finish()

            }
            .addOnFailureListener{e->
             progressDialog.dismiss()
                Toast.makeText(this,"Fallo el registro  en la BD debido a ${e.message}",Toast.LENGTH_SHORT).show() }



    }

}