package com.example.sistemadefacturacion_pruni.Vendedor.Productos

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.sistemadefacturacion_pruni.databinding.ActivityAgregarProductoBinding
import com.github.dhaval2404.imagepicker.ImagePicker

class AgregarProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoBinding
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgAgregarProducto.setOnClickListener {
            seleccionarImg()
        }

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
                binding.imgAgregarProducto.setImageURI(imageUri)
            } else {
                Toast.makeText(this, "Accion cancelada,No selecciono ninguna imagen", Toast.LENGTH_SHORT).show()
            }
        }

}