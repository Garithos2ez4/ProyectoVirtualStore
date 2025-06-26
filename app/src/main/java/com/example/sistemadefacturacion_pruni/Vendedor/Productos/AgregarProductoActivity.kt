package com.example.sistemadefacturacion_pruni.Vendedor.Productos

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
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
import com.google.firebase.storage.FirebaseStorage

class AgregarProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoBinding
    private var imageUri: Uri? = null

    private lateinit var imagenSelectArrayList: ArrayList<ModeloImagenSeleccionada>
    private lateinit var adaptadorImagenSeleccionada: AdaptadorImagenSeleccionada
    private lateinit var categoriaArrayList: ArrayList<ModeloCategoria>

    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cargarCategorias()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.etPrecioConDescuentoP.visibility= View.GONE
        binding.etNotaDescuentop.visibility= View.GONE

        binding.descuentoSwitch.setOnCheckedChangeListener {
                buttonView, isChecked ->
            if (isChecked){
                binding.etPrecioConDescuentoP.visibility= View.VISIBLE
                binding.etNotaDescuentop.visibility= View.VISIBLE

            } else {
                binding.etPrecioConDescuentoP.visibility= View.GONE
                binding.etNotaDescuentop.visibility= View.GONE
            }
        }

        imagenSelectArrayList = ArrayList()

        binding.imgAgregarProducto.setOnClickListener {
            seleccionarImg()
        }
        binding.Categoria.setOnClickListener {
            selecCategorias()
        }
        binding.btnAgregarProducto.setOnClickListener {
            validarInfo()
        }
        cargarImagenes()
    }

    private var nombreP = ""
    private var descripcionP = ""
    private var categoriaP = ""
    private var precioP = ""
    private var descuentoHab = false
    private var precioDescuento = ""
    private var notaDescuento = ""

    private fun validarInfo() {
        nombreP = binding.etNombresP.text.toString().trim()
        descripcionP = binding.etDescripcionP.text.toString().trim()
        categoriaP = binding.Categoria.text.toString().trim()
        precioP = binding.etPrecioP.text.toString().trim()
        descuentoHab = binding.descuentoSwitch.isChecked
       if (nombreP.isEmpty()){
           binding.etNombresP.error = "Ingrese el nombre del producto"
           binding.etNombresP.requestFocus()
       }
        else if (descripcionP.isEmpty()){
           binding.etDescripcionP.error = "Ingrese la descripcion del producto"
           binding.etDescripcionP.requestFocus()
       }
       else if (categoriaP.isEmpty()){
            binding.Categoria.error = "Seleccione una categoria"
            binding.Categoria.requestFocus()
        }
         else if (precioP.isEmpty()){
            binding.etPrecioP.error = "Ingrese el precio del producto"
            binding.etPrecioP.requestFocus()
        }
        else if (imageUri == null){
            Toast.makeText(this, "Seleccione al menos una imagen", Toast.LENGTH_SHORT).show()
        }
       else {
           if (descuentoHab){
               precioDescuento = binding.etPrecioConDescuentoP.text.toString().trim()
               notaDescuento = binding.etNotaDescuentop.text.toString().trim()
               if (precioDescuento.isEmpty()){
                   binding.etPrecioConDescuentoP.error = "Ingrese el precio con descuento"
                   binding.etPrecioConDescuentoP.requestFocus()
               } else if (notaDescuento.isEmpty()){
                   binding.etNotaDescuentop.error = "Ingrese la nota del descuento"
                   binding.etNotaDescuentop.requestFocus()
               } else {
                   agregarProducto()

               }

           }
               else {
                   precioDescuento= "0"
                   notaDescuento = "0"
                   agregarProducto()
           }

       }


    }

    private fun agregarProducto() {
        progressDialog.setMessage("Agregando producto")
        progressDialog.show()

        var ref = FirebaseDatabase.getInstance().getReference("Productos")
        val keyId = ref.push().key

        val hashMap = HashMap<String,Any>()
        hashMap["id"] = "${keyId}"
        hashMap["nombre"] = "${nombreP}"
        hashMap["descripcion"] = "${descripcionP}"
        hashMap["categoria"] = "${categoriaP}"
        hashMap["precio"] = "${precioP}"
        hashMap["precioDescuento"] = "${precioDescuento}"
        hashMap["notaDescuento"] = "${notaDescuento}"

        ref.child(keyId!!).setValue(hashMap)
            .addOnSuccessListener {
                subirImgStorage(keyId)   
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this, "No se pudo agregar el producto debido a ${e.message}", Toast.LENGTH_SHORT).show()

            }



    }

    private fun subirImgStorage(keyId: String) {
        for (i in imagenSelectArrayList.indices){
            val modeloImagenSel = imagenSelectArrayList[i]
            val nombreImagen = modeloImagenSel.id
            val rutaImagen = "Productos/$nombreImagen"

            val storageRef = FirebaseStorage.getInstance().getReference(rutaImagen)
            storageRef.putFile(modeloImagenSel.imageUri!!)
                .addOnSuccessListener {taskSnapshot->
                    val uriTask = taskSnapshot.storage.downloadUrl
                    while (!uriTask.isSuccessful);
                    val uriImgCargada = uriTask.result

                    if (uriTask.isSuccessful){
                      //  val urlImg = uriImgCargada.toString()
                        val hashMap = HashMap<String,Any>()
                        hashMap["imagen"] = "${modeloImagenSel.id}"
                        hashMap["imagenUrl"] = "${uriImgCargada}"

                        val ref = FirebaseDatabase.getInstance().getReference("Productos")
                        ref.child(keyId).child("Imagenes")
                            .child(nombreImagen)
                            .updateChildren(hashMap)
                        progressDialog.dismiss()
                        Toast.makeText(this, "Se agrego el producto", Toast.LENGTH_SHORT).show()
                         limpiarCampos()

                    }

                }
                .addOnFailureListener { e->
                    progressDialog.dismiss()
                    Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()

                }

        }

    }

    private fun limpiarCampos() {
        imagenSelectArrayList.clear()
        adaptadorImagenSeleccionada.notifyDataSetChanged()
        binding.etNombresP.setText("")
        binding.etDescripcionP.setText("")
        binding.Categoria.setText("")
        binding.etPrecioP.setText("")
        binding.descuentoSwitch.isChecked = false
        binding.etPrecioConDescuentoP.setText("")
        binding.etNotaDescuentop.setText("")
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