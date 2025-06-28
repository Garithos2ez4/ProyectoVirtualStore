package com.example.sistemadefacturacion_pruni.Cliente

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.sistemadefacturacion_pruni.Constantes
import com.example.sistemadefacturacion_pruni.R
import com.example.sistemadefacturacion_pruni.databinding.ActivityLoginClienteBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class LoginClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginClienteBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mGoogleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)



        binding.btnoLginC.setOnClickListener {
            validarInfo()
        }
        binding.btnLoginGoogle.setOnClickListener {
            googleLogin()
        }
        binding.tvRegistrarC.setOnClickListener {
            startActivity(Intent(applicationContext,RegistroClienteActivity::class.java))
        }

    }

    private fun googleLogin() {
        val googleSignInIntent = mGoogleSignInClient.signInIntent
        googleSignInARL.launch(googleSignInIntent)

    }
private val googleSignInARL = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ resultado ->
        if (resultado.resultCode == RESULT_OK){
            val data = resultado.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val cuenta = task.getResult(ApiException::class.java)
                autenticacionGoogle(cuenta.idToken)

            }catch (e:Exception){
                Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()
            }

    }else{
        Toast.makeText(this,"La operacion ha sido cancelado",Toast.LENGTH_SHORT).show()
        }
}

    private fun autenticacionGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { resultadoAuth->
                if (resultadoAuth.additionalUserInfo!!.isNewUser){

                    llenarInfoBD()
                    Toast.makeText(this,"Cuenta nueva",Toast.LENGTH_SHORT).show()
                }else{
                    startActivity(Intent(this,MainActivityCliente::class.java))
                    finishAffinity()
                }
            }
            .addOnFailureListener { e->
                Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()

            }

    }

    private fun llenarInfoBD() {
        progressDialog.setMessage("Guardando Información")

        val uid = firebaseAuth.uid
        val nombreC = firebaseAuth.currentUser?.displayName
        val emailC = firebaseAuth.currentUser?.email
        val tiempoRegistro= Constantes().obtenerTiempoD()

        val datosCliente = HashMap<String,Any>()
        datosCliente["uid"]="$uid"
        datosCliente["nombreC"]="$nombreC"
        datosCliente["emailC"]="$emailC"
        datosCliente["tiempoRegistro"]="$tiempoRegistro"
        datosCliente["imagen"]=""
        datosCliente["tipoUsuario"]="cliente"

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(uid!!)
            .setValue(datosCliente)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this,MainActivityCliente::class.java))
                finishAffinity()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()
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