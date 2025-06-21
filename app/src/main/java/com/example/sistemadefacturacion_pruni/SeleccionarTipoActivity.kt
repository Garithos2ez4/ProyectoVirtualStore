package com.example.sistemadefacturacion_pruni

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sistemadefacturacion_pruni.Cliente.LoginClienteActivity
import com.example.sistemadefacturacion_pruni.Vendedor.LoginVendedorActivity
import com.example.sistemadefacturacion_pruni.databinding.ActivitySeleccionarTipoBinding


class SeleccionarTipoActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeleccionarTipoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionarTipoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.TipoVendedor.setOnClickListener {
            startActivity(Intent(this@SeleccionarTipoActivity, LoginVendedorActivity::class.java))

        }
        binding.tipoCliente.setOnClickListener {
            startActivity(Intent(this@SeleccionarTipoActivity, LoginClienteActivity::class.java))
        }


    }
}