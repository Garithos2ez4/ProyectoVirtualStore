package com.example.sistemadefacturacion_pruni.Vendedor.Nav_Fragment_Vendedor

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sistemadefacturacion_pruni.databinding.FragmentCategoriasVBinding
import com.google.firebase.database.FirebaseDatabase


class FragmentCategoriasV : Fragment() {

    private lateinit var binding: FragmentCategoriasVBinding
    private lateinit var mContext : Context
    private lateinit var progressDialog : ProgressDialog

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


        binding.btnAgregarCat.setOnClickListener {
            validarInfo()
        }

        return binding.root
    }
    private var categoria = ""
    private fun validarInfo() {
        categoria = binding.etCategoria.text.toString().trim()
        if (categoria.isEmpty()){
            Toast.makeText(mContext,"Ingrese una categoria", Toast.LENGTH_SHORT).show()
        }else{
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
                progressDialog.dismiss()
                Toast.makeText(mContext,"Se agrego una categoria con éxito", Toast.LENGTH_SHORT).show()
                binding.etCategoria.setText("")
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(mContext,"${e.message}", Toast.LENGTH_SHORT).show()

            }




    }

}