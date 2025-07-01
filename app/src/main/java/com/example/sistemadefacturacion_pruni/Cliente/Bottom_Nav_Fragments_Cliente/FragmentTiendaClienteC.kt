package com.example.sistemadefacturacion_pruni.Cliente.Bottom_Nav_Fragments_Cliente

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sistemadefacturacion_pruni.Adaptadores.AdaptadorProductoC
import com.example.sistemadefacturacion_pruni.Modelos.ModeloProducto
import com.example.sistemadefacturacion_pruni.databinding.FragmentTiendaClienteBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FragmentTiendaClienteC : Fragment() {

    private lateinit var binding : FragmentTiendaClienteBinding
    private lateinit var mContext : Context

    private lateinit var productoArrayList : ArrayList<ModeloProducto>
    private lateinit var adapterProducto : AdaptadorProductoC

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTiendaClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listarProductos()
    }

    private fun listarProductos() {
        productoArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Productos")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productoArrayList.clear()
                for (ds in snapshot.children){
                    val modeloProducto = ds.getValue(ModeloProducto::class.java)
                    productoArrayList.add(modeloProducto!!)
                }
                adapterProducto = AdaptadorProductoC(mContext, productoArrayList)
                binding.ProductoCliente.adapter = adapterProducto

            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error adecuadamente
            }
        })
    }
}
