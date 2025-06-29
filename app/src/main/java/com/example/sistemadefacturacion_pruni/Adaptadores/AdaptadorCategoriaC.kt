package com.example.sistemadefacturacion_pruni.Adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sistemadefacturacion_pruni.Cliente.ProductosC.ProductosCatActivity
import com.example.sistemadefacturacion_pruni.Modelos.ModeloCategoria
import com.example.sistemadefacturacion_pruni.R
import com.example.sistemadefacturacion_pruni.databinding.ItemCategoriaCBinding

class AdaptadorCategoriaC(
   private val mContext: Context,
   private val categoriaArrayList: ArrayList<ModeloCategoria>
) : RecyclerView.Adapter<AdaptadorCategoriaC.HolderCategoriaC>() {

   private lateinit var binding: ItemCategoriaCBinding

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategoriaC {
      binding = ItemCategoriaCBinding.inflate(LayoutInflater.from(mContext), parent, false)
      return HolderCategoriaC(binding.root)
   }

   override fun getItemCount(): Int {
      return categoriaArrayList.size
   }

   override fun onBindViewHolder(holder: HolderCategoriaC, position: Int) {
      val modelo = categoriaArrayList[position]
      val categoria = modelo.categoria
      val imagen = modelo.imagenUrl

      holder.item_nombre_c.text = categoria

      Glide.with(mContext)
         .load(imagen)
         .placeholder(R.drawable.categories_icon)
         .into(holder.item_img_cat)

      holder.item_ver_productos.setOnClickListener {
         val intent = Intent(mContext, ProductosCatActivity::class.java)
         intent.putExtra("nombreCat", categoria)
         Toast.makeText(mContext, "Categoría Seleccionada: $categoria", Toast.LENGTH_SHORT).show()
         mContext.startActivity(intent)
      }
   }

   inner class HolderCategoriaC(itemView: View) : RecyclerView.ViewHolder(itemView) {
      var item_nombre_c = binding.itemNombreCC
      var item_img_cat = binding.itemEliminarC
      var item_ver_productos = binding.itemEliminarC
   }
}
