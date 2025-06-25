package com.example.sistemadefacturacion_pruni.Vendedor.Productos

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sistemadefacturacion_pruni.Adaptadores.AdaptadorImagenSeleccionada
import com.example.sistemadefacturacion_pruni.Constantes
import com.example.sistemadefacturacion_pruni.Modelos.ModeloCategoria
import com.example.sistemadefacturacion_pruni.Modelos.ModeloImagenSeleccionada
import com.example.sistemadefacturacion_pruni.databinding.ActivityAgregarProductoBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AgregarProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoBinding
    private var imageUri: Uri? = null

    private lateinit var imagenSelectArrayList: ArrayList<ModeloImagenSeleccionada>
    private lateinit var adaptadorImagenSeleccionada: AdaptadorImagenSeleccionada
    private lateinit var categoriaArrayList: ArrayList<ModeloCategoria>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cargarCategorias()
        imagenSelectArrayList = ArrayList()

        binding.imgAgregarProducto.setOnClickListener {
            seleccionarImg()
        }
        binding.Categoria.setOnClickListener {
            selecCategorias()
        }
        cargarImagenes()
    }

    private fun cargarCategorias() {
       categoriaArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Categorias").orderByChild("categoria")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriaArrayList.clear()
                for (ds in snapshot.children) {
                    val modelo = ds.getValue(ModeloCategoria::class.java)
                    categoriaArrayList.add(modelo!!)

                }

        }
                override fun onCancelled(error: DatabaseError) {


                }
    })
        }

    private var idCet =""
    private var tituloCat = ""
    private fun selecCategorias(){
        val categoriasArray = arrayOfNulls<String>(categoriaArrayList.size)
        for (i in categoriaArrayList.indices){
            categoriasArray[i] = categoriaArrayList[i].categoria
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccione una categoria")
            .setItems(categoriasArray){dialog, which ->
                idCet = categoriaArrayList[which].id
                tituloCat = categoriaArrayList[which].categoria
                binding.Categoria.text = tituloCat
    }
            .show()

    }


                private fun cargarImagenes() {
        adaptadorImagenSeleccionada = AdaptadorImagenSeleccionada(this, imagenSelectArrayList)
        binding.RVImagenesProducto.adapter = adaptadorImagenSeleccionada
    }

    private fun seleccionarImg(){
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent { intent ->
                resultadoImg.launch(intent)
            }

    }
    private val resultadoImg =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data = result.data
                imageUri = data!!.data
                val tiempo = "${Constantes().obtenerTiempoD()}"
                val modeloImgSel =  ModeloImagenSeleccionada(tiempo,imageUri,null,false)
                   imagenSelectArrayList.add(modeloImgSel)
                cargarImagenes()
            } else {
                Toast.makeText(this, "Accion cancelada,No selecciono ninguna imagen", Toast.LENGTH_SHORT).show()
            }
        }

}