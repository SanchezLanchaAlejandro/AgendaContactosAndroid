package com.example.agendaproyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import com.example.agendaproyecto.adapter.ContactAdapter
import com.example.agendaproyecto.models.Contact
import com.example.agendaproyecto.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val listaContactos = ArrayList<Contact>()
    private lateinit var adaptador: ContactAdapter
    private lateinit var baseDeDatos: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración del RecyclerView
        binding.recyclerViewContacts.layoutManager = LinearLayoutManager(this)
        adaptador = ContactAdapter(
            listaContactos,
            ::eliminarContacto,
            ::cambiarEstadoFavorito,
            ::irAModificarContacto
        )
        binding.recyclerViewContacts.adapter = adaptador

        // Configuración de Firebase
        baseDeDatos = FirebaseDatabase.getInstance().getReference("Contacts")
        cargarContactos()

        // Configuración de la barra de búsqueda
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(consulta: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(textoNuevo: String?): Boolean {
                filtrarContactos(textoNuevo)
                return true
            }
        })

        // Botón flotante para añadir un contacto
        binding.addContact.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cargarContactos() {
        baseDeDatos.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    listaContactos.clear()
                    for (contactoSnapshot in snapshot.children) {
                        val contacto = contactoSnapshot.getValue(Contact::class.java)
                        contacto?.let { listaContactos.add(it) }
                    }
                    // Ordenar contactos alfabéticamente
                    listaContactos.sortBy { it.name.lowercase() }
                    adaptador.actualizarLista(listaContactos) // Actualizar la lista en el adaptador
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error al cargar los contactos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun eliminarContacto(contacto: Contact) {
        val idContacto = contacto.id
        if (idContacto != null) {
            baseDeDatos.child(idContacto).removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this, "Contacto eliminado", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al eliminar el contacto", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun cambiarEstadoFavorito(contacto: Contact) {
        val idContacto = contacto.id
        if (idContacto != null) {
            val referenciaContacto = baseDeDatos.child(idContacto)
            referenciaContacto.child("esFavorito").setValue(!contacto.esFavorito)
                .addOnSuccessListener {
                    Toast.makeText(this, "Estado de favorito actualizado", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al actualizar el estado de favorito", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun filtrarContactos(consulta: String?) {
        val listaFiltrada = if (consulta.isNullOrEmpty()) {
            listaContactos // Mostrar lista completa si no hay búsqueda
        } else {
            listaContactos.filter {
                it.name.contains(consulta, ignoreCase = true) ||
                        it.phone.contains(consulta, ignoreCase = true) ||
                        it.email.contains(consulta, ignoreCase = true)
            }
        }
        adaptador.actualizarLista(listaFiltrada)
    }

    // Mmetodo para abrir la actividad de modificación
    private fun irAModificarContacto(contacto: Contact) {
        if (contacto.id != null) {
            val intent = Intent(this, ModificarContacto::class.java).apply {
                putExtra("CONTACTO_ID", contacto.id)
                putExtra("CONTACTO_NOMBRE", contacto.name)
                putExtra("CONTACTO_TELEFONO", contacto.phone)
                putExtra("CONTACTO_EMAIL", contacto.email)
                putExtra("CONTACTO_FAVORITO", contacto.esFavorito)
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "Datos del contacto inválidos", Toast.LENGTH_SHORT).show()
        }
    }
}