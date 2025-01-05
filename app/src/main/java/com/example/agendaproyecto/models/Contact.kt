package com.example.agendaproyecto.models

data class Contact(
    var id: String? = null,
    var name: String = "",
    var phone: String = "",
    var email: String = "",
    var esFavorito: Boolean = false
)