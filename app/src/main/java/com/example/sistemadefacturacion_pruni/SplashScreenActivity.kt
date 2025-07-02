package com.example.sistemadefacturacion_pruni

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sistemadefacturacion_pruni.Cliente.MainActivityCliente
import com.example.sistemadefacturacion_pruni.Vendedor.MainActivityVendedor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        firebaseAuth = FirebaseAuth.getInstance()

        verBienvenido() // ✅ ahora la puedes llamar
    }

    private fun verBienvenido() {
        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Puedes mostrar una cuenta regresiva si lo deseas
            }

            override fun onFinish() {
                comprobarTipoUsuario()
            }
        }.start()
    }
    private fun comprobarTipoUsuario(){
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser ==null){
            startActivity(Intent(this, SeleccionarTipoActivity::class.java))

        }else{
            val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
            reference.child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                    val tipoU = snapshot.child("tipoUsuario").value
                        if (tipoU =="vendedor"){
                            startActivity(Intent(this@SplashScreenActivity, MainActivityVendedor::class.java))
                            finishAffinity()
                        }else if (tipoU =="cliente"){
                            startActivity(Intent(this@SplashScreenActivity, MainActivityCliente::class.java))
                            finishAffinity()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }

    }

}
