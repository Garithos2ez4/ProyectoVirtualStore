package com.example.sistemadefacturacion_pruni.Cliente.ProductosC

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sistemadefacturacion_pruni.Adaptadores.AdaptadorProductoC
import com.example.sistemadefacturacion_pruni.Modelos.ModeloProducto
import com.example.sistemadefacturacion_pruni.databinding.ActivityProductosCatBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductosCatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductosCatBinding
    private lateinit var productosArrayList: ArrayList<ModeloProducto>
    private lateinit var adaptadorProducto: AdaptadorProductoC
    private var nombreCat = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductosCatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nombreCat = intent.getStringExtra("nombreCat").toString()
        listarProductos()


        }
    private fun listarProductos() {
        productosArrayList= ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.orderByChild("categoria").equalTo(nombreCat)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    productosArrayList.clear()
                    for (ds in snapshot.children) {

                        val modeloProducto = ds.getValue(ModeloProducto::class.java)

                            productosArrayList.add(modeloProducto!!)
                        }
                    adaptadorProducto = AdaptadorProductoC( this@ProductosCatActivity, productosArrayList,false,true,true)
                    binding.productosTV.adapter = adaptadorProducto
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }

}
