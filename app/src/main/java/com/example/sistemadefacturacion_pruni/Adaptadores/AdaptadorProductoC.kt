package com.example.sistemadefacturacion_pruni.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sistemadefacturacion_pruni.Modelos.ModeloProducto
import com.example.sistemadefacturacion_pruni.databinding.ItemProdcutoCBinding
import com.google.firebase.auth.FirebaseAuth
import com.itextpdf.kernel.pdf.PdfWriter
import android.view.View
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class AdaptadorProductoC(
    private val mContext: Context,
    private val productosArrayList: ArrayList<ModeloProducto>,
    private val mostrarBotonAgregar: Boolean,
    private val mostrarBotonPagar: Boolean,
    private val mostrarBotonPDF: Boolean
) : RecyclerView.Adapter<AdaptadorProductoC.HolderProducto>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProducto {
        val binding = ItemProdcutoCBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderProducto(binding)
    }

    override fun getItemCount(): Int = productosArrayList.size

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

        // Control individual de botones
        holder.binding.btnAgregarOrden.visibility = if (mostrarBotonAgregar) View.VISIBLE else View.GONE
        holder.binding.btnPagar.visibility = if (mostrarBotonPagar) View.VISIBLE else View.GONE
        holder.binding.btnDescargarPDF.visibility = if (mostrarBotonPDF) View.VISIBLE else View.GONE

        holder.binding.btnAgregarOrden.setOnClickListener {
            agregarProductoAOrden(modeloProducto)
        }

        holder.binding.btnPagar.setOnClickListener {
            registrarVenta(modeloProducto)
        }

        holder.binding.btnDescargarPDF.setOnClickListener {
            generarPDF(modeloProducto)
        }

        cargarPrimeraImg(modeloProducto, holder)
    }


    inner class HolderProducto(val binding: ItemProdcutoCBinding) : RecyclerView.ViewHolder(binding.root)

    private fun cargarPrimeraImg(modeloProducto: ModeloProducto, holder: HolderProducto) {
        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(modeloProducto.id).child("Imagenes")
            .limitToFirst(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        val imagenUrl = ds.child("imagenUrl").value.toString()
                        try {
                            Glide.with(mContext)
                                .load(imagenUrl)
                                .into(holder.binding.imagenP)
                        } catch (_: Exception) {}
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun registrarVenta(modeloProducto: ModeloProducto) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val refVentas = FirebaseDatabase.getInstance().getReference("Ventas")
        val datosVenta = hashMapOf(
            "id" to modeloProducto.id,
            "nombre" to modeloProducto.nombre,
            "categoria" to modeloProducto.categoria,
            "descripcion" to modeloProducto.descripcion,
            "precio" to modeloProducto.precio,
            "precioDescuento" to modeloProducto.precioDescuento,
            "notaDescuento" to modeloProducto.notaDescuento,
            "idUsuario" to (firebaseAuth.uid ?: "")
        )

        refVentas.push().setValue(datosVenta)
            .addOnSuccessListener {
                Toast.makeText(mContext, "Venta registrada correctamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(mContext, "Error al registrar venta: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun generarPDF(modeloProducto: ModeloProducto) {
        try {
            val nombreArchivo = "${modeloProducto.nombre}_${System.currentTimeMillis()}.pdf"
            val ruta = android.os.Environment.getExternalStoragePublicDirectory(
                android.os.Environment.DIRECTORY_DOWNLOADS
            ).toString() + "/" + nombreArchivo

            val pdfWriter = PdfWriter(ruta)
            val pdfDocument = PdfDocument(pdfWriter)
            val documento = Document(pdfDocument)

            documento.add(Paragraph("DETALLE DE LA VENTA"))
            documento.add(Paragraph("Nombre: ${modeloProducto.nombre}"))
            documento.add(Paragraph("Categoría: ${modeloProducto.categoria}"))
            documento.add(Paragraph("Descripción: ${modeloProducto.descripcion}"))
            documento.add(Paragraph("Precio: ${modeloProducto.precio} USD"))

            if (modeloProducto.precioDescuento.isNotEmpty() && modeloProducto.precioDescuento != "0") {
                documento.add(Paragraph("Precio Descuento: ${modeloProducto.precioDescuento} USD"))
                documento.add(Paragraph("Nota: ${modeloProducto.notaDescuento}"))
            }

            documento.close()
            Toast.makeText(mContext, "PDF generado en: $ruta", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(mContext, "Error al generar PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun agregarProductoAOrden(modeloProducto: ModeloProducto) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val ref = FirebaseDatabase.getInstance().getReference("Ordenes")
        val datosOrden = hashMapOf(
            "id" to modeloProducto.id,
            "nombre" to modeloProducto.nombre,
            "descripcion" to modeloProducto.descripcion,
            "categoria" to modeloProducto.categoria,
            "precio" to modeloProducto.precio,
            "precioDescuento" to modeloProducto.precioDescuento,
            "notaDescuento" to modeloProducto.notaDescuento
        )
        ref.child(firebaseAuth.uid ?: "").child(modeloProducto.id)
            .setValue(datosOrden)
            .addOnSuccessListener {
                Toast.makeText(mContext, "Producto agregado a tu orden", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(mContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
