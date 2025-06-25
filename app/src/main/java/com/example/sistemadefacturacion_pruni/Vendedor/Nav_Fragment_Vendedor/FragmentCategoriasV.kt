package com.example.sistemadefacturacion_pruni.Vendedor.Nav_Fragment_Vendedor

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sistemadefacturacion_pruni.Adaptadores.AdaptadorCategoriaV
import com.example.sistemadefacturacion_pruni.Modelos.ModeloCategoria
import com.example.sistemadefacturacion_pruni.R
import com.example.sistemadefacturacion_pruni.databinding.FragmentCategoriasVBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class FragmentCategoriasV : Fragment() {
    private lateinit var categoriaArrayList: ArrayList<ModeloCategoria>
    private lateinit var adaptadorCategoriaV: AdaptadorCategoriaV
    private lateinit var binding: FragmentCategoriasVBinding
    private lateinit var mContext : Context
    private lateinit var progressDialog : ProgressDialog
    private var imageUri: Uri? = null

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriasVBinding.inflate(inflater,container,false)

       progressDialog = ProgressDialog(mContext)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.imgCategorias.setOnClickListener {
            seleccionarIMG()
        }

        binding.btnAgregarCat.setOnClickListener {
            validarInfo()
        }
        categoriaArrayList = ArrayList()
        adaptadorCategoriaV = AdaptadorCategoriaV(mContext, categoriaArrayList)
        binding.Vcategorias.adapter = adaptadorCategoriaV

        cargarCategorias()

        return binding.root
    }

    private fun seleccionarIMG() {
           ImagePicker.with(requireActivity())
               .crop()
               .compress(1024)
               .maxResultSize(1080,1080)
               .createIntent { intent ->
                   resultadoImg.launch(intent)
               }
    }
    private val resultadoImg = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data
            imageUri = data!!.data
            binding.imgCategorias.setImageURI(imageUri)
            }else{
            Toast.makeText(mContext,"Accion Cancelada", Toast.LENGTH_SHORT).show()

        }
    }
    private fun cargarCategorias() {
        val ref = FirebaseDatabase.getInstance().getReference("Categorias")
        ref.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                categoriaArrayList.clear()
                for (ds in snapshot.children) {
                    val id = ds.child("id").value.toString()
                    val categoria = ds.child("categoria").value.toString()
                    val imagenUrl = ds.child("imagenUrl").value.toString()

                    val modelo = ModeloCategoria(id, categoria, imagenUrl)
                    categoriaArrayList.add(modelo)
                }
                adaptadorCategoriaV.notifyDataSetChanged()
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                Toast.makeText(mContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private var categoria = ""
    private fun validarInfo() {
        categoria = binding.etCategoria.text.toString().trim()
        if (categoria.isEmpty()){
            Toast.makeText(mContext,"Ingrese una categoria", Toast.LENGTH_SHORT).show()
        } else if (imageUri == null){
            Toast.makeText(mContext,"Seleccione una imagen", Toast.LENGTH_SHORT).show()
        }
        else{
            agregarCatBD()
        }
    }

    private fun agregarCatBD() {
        progressDialog.setMessage("Agregando categoria")
        progressDialog.show()

        val ref = FirebaseDatabase.getInstance().getReference("Categorias")
        val KeyId = ref.push().key

        val hashMap = HashMap<String,Any>()

        hashMap["id"] = KeyId!!
        hashMap["categoria"] = "${categoria}"

        ref.child(KeyId!!)
            .setValue(hashMap)
            .addOnSuccessListener {
           //     progressDialog.dismiss()
              //  Toast.makeText(mContext,"Se agrego una categoria con éxito", Toast.LENGTH_SHORT).show()
             //   binding.etCategoria.setText("")
                subirImgStorage(KeyId)
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(mContext,"${e.message}", Toast.LENGTH_SHORT).show()

            }




    }

    private fun subirImgStorage(keyId: String) {
        progressDialog.setMessage("Subiendo imagen")
        progressDialog.show()

        val nombreImagen = keyId
        val nombreCarpeta = "Categorias/$nombreImagen"
        val storageReference = FirebaseStorage.getInstance().getReference(nombreCarpeta)
        storageReference.putFile(imageUri!!)
            .addOnSuccessListener {taskSnapshot->
            progressDialog.dismiss()
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val urlImgCargada = uriTask.result

                if (uriTask.isSuccessful){
                    val hashMap = HashMap<String,Any>()
                    hashMap["imagenUrl"] = "${urlImgCargada}"
                    val ref = FirebaseDatabase.getInstance().getReference("Categorias")
                    ref.child(nombreImagen).updateChildren(hashMap)
                    Toast.makeText(context,"Se agrego una categoria con éxito", Toast.LENGTH_SHORT).show()
                    binding.etCategoria.setText("")
                    imageUri = null
                    binding.imgCategorias.setImageURI(imageUri)
                    binding.imgCategorias.setImageResource(R.drawable.categories_icon)

                }

            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(context,"${e.message}", Toast.LENGTH_SHORT).show()
            }


    }

}