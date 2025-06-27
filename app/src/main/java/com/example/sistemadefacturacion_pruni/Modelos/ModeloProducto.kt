package com.example.sistemadefacturacion_pruni.Modelos

class ModeloProducto {
    var id : String =""
    var nombre : String =""
    var descripcion : String =""
    var categoria : String =""
    var precio : String =""
    var precioDescuento : String =""
    var notaDescuento : String =""

    constructor()

    constructor( id : String,
                 nombre : String,
                 descripcion : String,
                 categoria : String,
                 precio : String,
                 precioDescuento : String,
                 notaDescuento : String
    ){
        this.id = id
        this.nombre = nombre
        this.descripcion = descripcion
        this.categoria = categoria
        this.precio = precio
        this.precioDescuento = precioDescuento
        this.notaDescuento = notaDescuento
    }


}