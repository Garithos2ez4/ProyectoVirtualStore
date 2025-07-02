package com.example.sistemadefacturacion_pruni.Cliente.Bottom_Nav_Fragments_Cliente


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sistemadefacturacion_pruni.Adaptadores.AdaptadorProductoC
import com.example.sistemadefacturacion_pruni.Modelos.ModeloProducto
import com.example.sistemadefacturacion_pruni.databinding.FragmentMisOrdenesCBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FragmentMisOrdenesC : Fragment() {

    private lateinit var binding: FragmentMisOrdenesCBinding
    private lateinit var mContext: Context

    private lateinit var productoArrayList: ArrayList<ModeloProducto>
    private lateinit var adapterProducto: AdaptadorProductoC
    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMisOrdenesCBinding.inflate(LayoutInflater.from(mContext), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listarMisOrdenes()
    }

    private fun listarMisOrdenes() {
        val firebaseAuth = FirebaseAuth.getInstance()
        productoArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Ordenes").child(firebaseAuth.uid!!)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productoArrayList.clear()
                for (ds in snapshot.children) {
                    val modeloProducto = ds.getValue(ModeloProducto::class.java)
                    productoArrayList.add(modeloProducto!!)
                }
                adapterProducto = AdaptadorProductoC(mContext, productoArrayList, false, true, true)
                binding.ordenesCRV.adapter = adapterProducto

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(mContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
