package com.example.sistemadefacturacion_pruni.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sistemadefacturacion_pruni.Modelos.ModeloProducto
import com.example.sistemadefacturacion_pruni.R
import com.example.sistemadefacturacion_pruni.databinding.ItemProductoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdaptadorProducto  : RecyclerView.Adapter<AdaptadorProducto.HolderProducto> {

    private lateinit  var binding : ItemProductoBinding
    private var mContext : Context
    private var productosArrayList = ArrayList<ModeloProducto>()

    constructor(productosArrayList: ArrayList<ModeloProducto>, mContext: Context) {
        this.productosArrayList = productosArrayList
        this.mContext = mContext
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProducto {
       binding = ItemProductoBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return HolderProducto(binding.root)
    }

    override fun getItemCount(): Int {
      return productosArrayList.size

    }

    override fun onBindViewHolder(holder: HolderProducto, position: Int) {
        val modeloProducto = productosArrayList[position]

        val nombre = modeloProducto.nombre
        val precio = modeloProducto.precio
        val precioDescuento = modeloProducto.precioDescuento
        val notaDescuento = modeloProducto.notaDescuento

        cargarPrimeraImg(modeloProducto, holder)

        holder.item_nombre_p.text = nombre
        holder.item_precio_p.text = "$precio USD"

        // Por defecto los ocultamos
        holder.item_precio_p_desc.visibility = View.GONE
        holder.item_nota_p.visibility = View.GONE

        // Mostramos si corresponde
        if (precioDescuento.isNotEmpty() && precioDescuento != "0" && notaDescuento.isNotEmpty() && notaDescuento != "0") {
            holder.item_precio_p_desc.text = "$precioDescuento USD"
            holder.item_nota_p.text = notaDescuento
            holder.item_precio_p_desc.visibility = View.VISIBLE
            holder.item_nota_p.visibility = View.VISIBLE

    }



}


    private fun cargarPrimeraImg(modeloProducto: ModeloProducto, holder: AdaptadorProducto.HolderProducto) {
        val idProducto = modeloProducto.id
        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto).child("Imagenes")
            .limitToFirst(1)
            .addValueEventListener(object  : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children){
                        val imagenUrl = "${ds.child("imagenUrl").value}"

                        try {
                            Glide.with(mContext)
                                .load(imagenUrl)
                                .placeholder(R.drawable.item)
                                .into(holder.imagenP)
                        } catch (e: Exception) {
                           // holder.imagenP.setImageResource(R.drawable.item)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


    }

    inner class HolderProducto(itemView : View) : RecyclerView.ViewHolder(itemView){
        var imagenP = binding.imagenP
        var item_nombre_p = binding.itemNombreP
        var item_precio_p = binding.itemPrecioP
        var item_precio_p_desc = binding.itemPrecioPDesc
        var item_nota_p = binding.itemNotaP



    }


}