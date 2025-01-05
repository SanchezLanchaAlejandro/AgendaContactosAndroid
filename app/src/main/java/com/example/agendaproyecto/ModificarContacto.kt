package com.example.agendaproyecto

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agendaproyecto.databinding.ActivityModificarContactoBinding
import com.example.agendaproyecto.models.Contact
import com.google.firebase.database.*

class ModificarContacto : AppCompatActivity() {

    private lateinit var binding: ActivityModificarContactoBinding
    private lateinit var baseDeDatos: DatabaseReference
    private var contactoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModificarContactoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar la base de datos
        baseDeDatos = FirebaseDatabase.getInstance().getReference("Contacts")

        // Obtener los datos del Intent
        contactoId = intent.getStringExtra("CONTACTO_ID")
        val contactoNombre = intent.getStringExtra("CONTACTO_NOMBRE")
        val contactoTelefono = intent.getStringExtra("CONTACTO_TELEFONO")
        val contactoEmail = intent.getStringExtra("CONTACTO_EMAIL")
        val contactoFavorito = intent.getBooleanExtra("CONTACTO_FAVORITO", false)

        // Rellenar los campos con los datos
        binding.etName.setText(contactoNombre)
        binding.etPhone.setText(contactoTelefono)
        binding.etEmail.setText(contactoEmail)
        binding.switchFavorite.isChecked = contactoFavorito

        // Configurar el botón de guardar
        binding.saveButton.setOnClickListener {
            if (contactoId != null) {
                // Obtener los nuevos valores de los campos
                val nombre = binding.etName.text.toString()
                val telefono = binding.etPhone.text.toString()
                val email = binding.etEmail.text.toString()
                val esFavorito = binding.switchFavorite.isChecked

                // Crear un nuevo objeto Contact con los nuevos datos
                val contactoModificado = Contact(contactoId!!, nombre, telefono, email, esFavorito)

                // Actualizar los datos en la base de datos
                baseDeDatos.child(contactoId!!).setValue(contactoModificado)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Contacto modificado", Toast.LENGTH_SHORT).show()
                        finish() // Volver a la actividad principal
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al modificar el contacto", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}