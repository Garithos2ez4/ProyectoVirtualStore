package com.example.sistemadefacturacion_pruni.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.sistemadefacturacion_pruni.Modelos.ModeloImagenSeleccionada
import com.example.sistemadefacturacion_pruni.R
import com.example.sistemadefacturacion_pruni.databinding.ItemImagenesSeleccionadasBinding

class AdaptadorImagenSeleccionada (private val context : Context,
                                   private val imagenesSelectArrayList : ArrayList<ModeloImagenSeleccionada>
): Adapter<AdaptadorImagenSeleccionada.HolderImagenSeleccionada>(){

    private lateinit var binding : ItemImagenesSeleccionadasBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderImagenSeleccionada {
         binding = ItemImagenesSeleccionadasBinding.inflate(LayoutInflater.from(context),parent,false)
         return HolderImagenSeleccionada(binding.root)
    }

    override fun getItemCount(): Int {
        return imagenesSelectArrayList.size

    }

    override fun onBindViewHolder(holder: HolderImagenSeleccionada, position: Int) {
      var modelo = imagenesSelectArrayList[position]

        val imagenUri = modelo.imageUri
          //Leyendo imagen
        try{
            Glide.with(context)
                .load(imagenUri)
                .placeholder(R.drawable.item)
                .into(holder.item_imagen)
        }catch (e: Exception){

        }
        //Evento para eliminar una imagen de la lista
            holder.btn_borrar.setOnClickListener {
                imagenesSelectArrayList.remove(modelo)
                notifyDataSetChanged()
            }

    }
    inner class HolderImagenSeleccionada(itemView : View): ViewHolder(itemView){
        var item_imagen = binding.itemImagen
        var btn_borrar = binding.borrarItem

    }
}