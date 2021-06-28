package com.example.hbmanager.clases

class Usuario {
    lateinit var cuenta:String
    lateinit var idUsuario:String
    lateinit var nombres:String
    lateinit var fechaNacimiento:String
    lateinit var urlFoto:String

    constructor() :this("","","","","")

    constructor(cuenta:String,idUsuario: String,nombres: String, fechaNacimiento: String, urlFoto: String) {
        this.cuenta = cuenta
        this.idUsuario = idUsuario
        this.nombres = nombres
        this.fechaNacimiento = fechaNacimiento
        this.urlFoto = urlFoto
    }
}