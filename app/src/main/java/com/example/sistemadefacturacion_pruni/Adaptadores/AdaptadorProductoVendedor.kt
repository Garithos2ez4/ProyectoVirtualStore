package com.example.sistemadefacturacion_pruni.Adaptadores
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sistemadefacturacion_pruni.Modelos.ModeloProducto
import com.example.sistemadefacturacion_pruni.R
import com.example.sistemadefacturacion_pruni.databinding.ItemProdcutoVBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
class AdaptadorProductoVendedor(
    private val productosArrayList: ArrayList<ModeloProducto>,
    private val mContext: Context
) : RecyclerView.Adapter<AdaptadorProductoVendedor.HolderProducto>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProducto {
            val binding = ItemProdcutoVBinding.inflate(LayoutInflater.from(mContext), parent, false)
            return HolderProducto(binding)
        }

        override fun getItemCount(): Int {
            return productosArrayList.size
        }

        override fun onBindViewHolder(holder: HolderProducto, position: Int) {
            val modeloProducto = productosArrayList[position]

            holder.binding.itemNombreP.text = modeloProducto.nombre
            holder.binding.itemPrecioP.text = "${modeloProducto.precio} USD"

            holder.binding.itemPrecioPDesc.visibility = View.GONE
            holder.binding.itemNotaP.visibility = View.GONE

            if (modeloProducto.precioDescuento.isNotEmpty() && modeloProducto.precioDescuento != "0"
                && modeloProducto.notaDescuento.isNotEmpty() && modeloProducto.notaDescuento != "0") {
                holder.binding.itemPrecioPDesc.text = "${modeloProducto.precioDescuento} USD"
                holder.binding.itemNotaP.text = modeloProducto.notaDescuento
                holder.binding.itemPrecioPDesc.visibility = View.VISIBLE
                holder.binding.itemNotaP.visibility = View.VISIBLE
            }

            cargarPrimeraImg(modeloProducto, holder)
        }

        private fun cargarPrimeraImg(modeloProducto: ModeloProducto, holder: HolderProducto) {
            val ref = FirebaseDatabase.getInstance().getReference("Productos")
            ref.child(modeloProducto.id).child("Imagenes")
                .limitToFirst(1)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (ds in snapshot.children) {
                            val imagenUrl = "${ds.child("imagenUrl").value}"
                            try {
                                Glide.with(mContext)
                                    .load(imagenUrl)
                                    .placeholder(R.drawable.item)
                                    .into(holder.binding.imagenP)
                            } catch (_: Exception) {}
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
        }

        inner class HolderProducto(val binding: ItemProdcutoVBinding) : RecyclerView.ViewHolder(binding.root)
    }
