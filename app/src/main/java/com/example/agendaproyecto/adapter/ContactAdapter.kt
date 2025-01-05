package com.example.agendaproyecto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agendaproyecto.R
import com.example.agendaproyecto.models.Contact

class ContactAdapter(
    private var listaContactos: List<Contact>,
    private val onEliminarClickListener: (Contact) -> Unit,
    private val onFavoritoClickListener: (Contact) -> Unit,
    private val onClickListener: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactoViewHolder>() {

    // ViewHolder para los elementos del RecyclerView
    class ContactoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.tvContactName)
        val telefono: TextView = itemView.findViewById(R.id.tvContactPhone)
        val correo: TextView = itemView.findViewById(R.id.tvContactEmail)
        val botonEliminar: ImageButton = itemView.findViewById(R.id.btnDelete)
        val botonFavorito: ImageButton = itemView.findViewById(R.id.btnFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactoViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactoViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ContactoViewHolder, position: Int) {
        val contacto = listaContactos[position]
        holder.nombre.text = contacto.name
        holder.telefono.text = contacto.phone
        holder.correo.text = contacto.email

        // Mostrar o no el ícono de favoritos
        if (contacto.esFavorito) {
            holder.botonFavorito.visibility = View.VISIBLE
        } else {
            holder.botonFavorito.visibility = View.GONE
        }

        // Configuración de los botones
        holder.botonEliminar.setOnClickListener { onEliminarClickListener(contacto) }
        holder.botonFavorito.setOnClickListener { onFavoritoClickListener(contacto) }
        holder.itemView.setOnClickListener { onClickListener(contacto) }
    }

    override fun getItemCount(): Int = listaContactos.size

    // Función para actualizar la lista de contactos
    fun actualizarLista(nuevaListaContactos: List<Contact>) {
        listaContactos = nuevaListaContactos
        notifyDataSetChanged()
    }
}