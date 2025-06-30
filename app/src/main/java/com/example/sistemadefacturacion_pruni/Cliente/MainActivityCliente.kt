package com.example.sistemadefacturacion_pruni.Cliente

import android.content.Intent
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
import com.example.sistemadefacturacion_pruni.SeleccionarTipoActivity
import com.example.sistemadefacturacion_pruni.databinding.ActivityMainClienteBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivityCliente : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainClienteBinding
    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        firebaseAuth = FirebaseAuth.getInstance()
        comprobarSesion()

        binding.navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,binding.drawerLayout,
            toolbar,R.string.open_drawer,
            R.string.close_drawer

        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        replaceFragment(FragmentInicioCliente())
        binding.navigationView.setCheckedItem(R.id.op_inicio_c)

    }
    private fun comprobarSesion() {
        val usuarioActual = firebaseAuth!!.currentUser
        if (usuarioActual == null) {
            startActivity(Intent(applicationContext, SeleccionarTipoActivity::class.java))
            finish()
        } else {
            val ref = com.google.firebase.database.FirebaseDatabase.getInstance().getReference("Usuarios")
            ref.child(usuarioActual.uid).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val tipoUsuario = snapshot.child("tipoUsuario").value.toString()
                        if (tipoUsuario == "cliente") {
                            Toast.makeText(this, "Cliente en línea", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Acceso denegado para este tipo de usuario", Toast.LENGTH_SHORT).show()
                            firebaseAuth!!.signOut()
                            startActivity(Intent(this, SeleccionarTipoActivity::class.java))
                            finish()
                        }
                    } else {
                        Toast.makeText(this, "Datos de usuario no encontrados", Toast.LENGTH_SHORT).show()
                        firebaseAuth!!.signOut()
                        startActivity(Intent(this, SeleccionarTipoActivity::class.java))
                        finish()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al obtener datos: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun cerrarSession(){
        firebaseAuth!!.signOut()
        startActivity(Intent(applicationContext, SeleccionarTipoActivity::class.java))
        finish()
        Toast.makeText(this,"Cerrando Sesión",Toast.LENGTH_SHORT).show()
    }

    private fun replaceFragment(fragment: Fragment) {
      supportFragmentManager
          .beginTransaction()
          .replace(R.id.navFragment,fragment)
          .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
       when(item.itemId){
           R.id.op_inicio_c ->{
               replaceFragment(FragmentInicioCliente())
           }
           R.id.op_mi_perfil_c ->{
               replaceFragment(FragmentMiPerfilCliente())
           }
           R.id.op_cerrar_sesion_c ->{
            cerrarSession()
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

