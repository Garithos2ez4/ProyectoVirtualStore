package com.example.sistemadefacturacion_pruni.Cliente.Nav_Fragments_Cliente

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sistemadefacturacion_pruni.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FragmentMiPerfilCliente : Fragment() {

    private lateinit var txtNombrePerfil: TextView
    private lateinit var txtCorreoPerfil: TextView
    private lateinit var txtTipoUsuarioPerfil: TextView

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mi_perfil_cliente, container, false)

        txtNombrePerfil = view.findViewById(R.id.txtNombrePerfil)
        txtCorreoPerfil = view.findViewById(R.id.txtCorreoPerfil)
        txtTipoUsuarioPerfil = view.findViewById(R.id.txtTipoUsuarioPerfil)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios")

        obtenerDatosUsuario()

        return view
    }

    private fun obtenerDatosUsuario() {
        val uid = firebaseAuth.currentUser?.uid

        if (uid != null) {
            databaseReference.child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val nombre = snapshot.child("nombres").value.toString()
                        val correo = snapshot.child("email").value.toString()
                        val tipoUsuario = snapshot.child("tipoUsuario").value.toString()

                        txtNombrePerfil.text = "Nombre: $nombre"
                        txtCorreoPerfil.text = "Correo: $correo"
                        txtTipoUsuarioPerfil.text = "Tipo de usuario: $tipoUsuario"
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
