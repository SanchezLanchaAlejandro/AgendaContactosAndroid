package com.example.agendaproyecto

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agendaproyecto.databinding.ActivityAddContactBinding
import com.example.agendaproyecto.models.Contact
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddContactBinding
    private lateinit var database: DatabaseReference
    private val toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar la base de datos de Firebase
        database = FirebaseDatabase.getInstance().getReference("Contacts")

        binding.saveButton.setOnClickListener {
            // Crear un nuevo contacto
            val name = binding.etName.text.toString()
            val phone = binding.etPhone.text.toString()
            val email = binding.etEmail.text.toString()
            val isFavorite = binding.switchFavorite.isChecked

            if (name.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty()) {
                val contactId = database.push().key
                val contact = Contact(contactId, name, phone, email, isFavorite)

                // Guardar el contacto en la base de datos
                if (contactId != null) {
                    database.child(contactId).setValue(contact)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Contacto guardado", Toast.LENGTH_SHORT).show()
                            finish()  // Cerrar la actividad y volver atrás
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al guardar el contacto", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                // Reproducir tono de error
                toneGenerator.startTone(ToneGenerator.TONE_PROP_NACK, 150)
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Liberar recursos del ToneGenerator
        toneGenerator.release()
    }
}