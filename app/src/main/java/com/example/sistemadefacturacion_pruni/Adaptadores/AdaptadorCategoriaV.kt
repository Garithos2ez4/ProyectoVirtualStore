package com.example.sistemadefacturacion_pruni.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemadefacturacion_pruni.Modelos.ModeloCategoria
import com.example.sistemadefacturacion_pruni.databinding.ItemCategoriaVBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AdaptadorCategoriaV : RecyclerView.Adapter<AdaptadorCategoriaV.HolderCategoriaV>{
     private lateinit var binding : ItemCategoriaVBinding
     private val mContext : Context
     private val categoriaArraylist : ArrayList<ModeloCategoria>

         constructor(mContext: Context, categoriaArrayList: ArrayList<ModeloCategoria>)  {
             this.mContext = mContext
             this.categoriaArraylist = categoriaArrayList


     }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategoriaV {
        binding = ItemCategoriaVBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return HolderCategoriaV(binding.root)

    }
    override fun getItemCount(): Int {
        return categoriaArraylist.size
    }
    override fun onBindViewHolder(holder: HolderCategoriaV, position: Int) {
        val modelo = categoriaArraylist[position]
        val id = modelo.id
        val categoria = modelo.categoria

        holder.item_nombre_c_v.text = categoria

        holder.item_eliminar_c.setOnClickListener {
            //Toast.makeText(mContext,"Eliminar",Toast.LENGTH_SHORT).show()

        val builer = AlertDialog.Builder(mContext)

            builer.setTitle("Eliminar categoria")
            builer.setMessage("Estas seguro de eliminar la categoria?")
            .setPositiveButton("Si"){a,d->
                eliminarCategoria(modelo,holder)
            }
            .setNegativeButton("No"){a,d->
             a.dismiss()
            }
            builer.show()
        }

    }

    private fun eliminarCategoria(modelo: ModeloCategoria, holder: AdaptadorCategoriaV.HolderCategoriaV) {
        val idCat = modelo.id
        val ref = FirebaseDatabase.getInstance().getReference("Categorias")
        ref.child(idCat)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(mContext,"Categoria eliminada",Toast.LENGTH_SHORT).show()
                eliminarImgCat(idCat)

    }
            .addOnFailureListener {e->
                Toast.makeText(mContext,"No se pudo eliminar debido a ${e.message}",Toast.LENGTH_SHORT).show()

            }
    }

    private fun eliminarImgCat(idCat: String){
        val nombreImg = idCat
        val rutaImagen = "Categorias/$nombreImg"
        val storageRef = FirebaseStorage.getInstance().getReference(rutaImagen)
        storageRef.delete()
            .addOnSuccessListener {
                Toast.makeText(mContext,"imagen eliminada de la categoria",Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e->
                Toast.makeText(mContext," ${e.message}",Toast.LENGTH_SHORT).show()


            }

    }

    inner class HolderCategoriaV(itemView: View) : RecyclerView.ViewHolder(itemView){
        var item_nombre_c_v = binding.itemNombreCV
        var item_eliminar_c = binding.itemEliminarC
    }
}