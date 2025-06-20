package com.example.sistemadefacturacion_pruni.Cliente

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.sistemadefacturacion_pruni.Cliente.Bottom_Nav_Fragments_Cliente.FragmentMisOrdenesC
import com.example.sistemadefacturacion_pruni.Cliente.Bottom_Nav_Fragments_Cliente.FragmentTiendaClienteC
import com.example.sistemadefacturacion_pruni.Cliente.Nav_Fragments_Cliente.FragmentInicioCliente
import com.example.sistemadefacturacion_pruni.Cliente.Nav_Fragments_Cliente.FragmentMiPerfilCliente
import com.example.sistemadefacturacion_pruni.R
import com.example.sistemadefacturacion_pruni.databinding.ActivityMainClienteBinding
import com.google.android.material.navigation.NavigationView

class MainActivityCliente : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainClienteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,binding.drawerLayout,
            toolbar,R.string.open_drawer,
            R.string.close_drawer

        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        replaceFragment(FragmentInicioCliente())

    }

    private fun replaceFragment(fragment: Fragment) {
      supportFragmentManager
          .beginTransaction()
          .replace(R.id.navFragment,fragment)
          .commit()
    }

    override fun onNavigationItemSelected(selectedNavigationItem: MenuItem): Boolean {
       when(selectedNavigationItem.itemId){
           R.id.op_inicio_c ->{
               replaceFragment(FragmentInicioCliente())
           }
           R.id.op_mi_perfil_c ->{
               replaceFragment(FragmentMiPerfilCliente())
           }
           R.id.op_cerrar_sesion_c ->{
               Toast.makeText(applicationContext,"Cerrando Sesión",Toast.LENGTH_SHORT).show()

           }
           R.id.op_tienda_c ->{
               replaceFragment(FragmentTiendaClienteC())
           }
           R.id.op_mis_ordenes_c ->{
               replaceFragment(FragmentMisOrdenesC())
        }

       }
    binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}

