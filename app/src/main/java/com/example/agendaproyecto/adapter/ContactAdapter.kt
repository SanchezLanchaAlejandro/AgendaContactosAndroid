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
        // Infla el diseño del ítem y crea un ViewHolder para cada elemento.
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactoViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ContactoViewHolder, position: Int) {
        // Vincula los datos del contacto con las vistas del ViewHolder en la posición actual.
        val contacto = listaContactos[position]
        holder.nombre.text = contacto.name
        holder.telefono.text = contacto.phone
        holder.correo.text = contacto.email

        // Muestra u oculta la imagen de favorito según el estado del contacto.
        if (contacto.esFavorito) {
            holder.botonFavorito.visibility = View.VISIBLE
        } else {
            holder.botonFavorito.visibility = View.GONE
        }

        // Configura los clics para eliminar, marcar como favorito o modificar el contacto.
        holder.botonEliminar.setOnClickListener { onEliminarClickListener(contacto) }
        holder.botonFavorito.setOnClickListener { onFavoritoClickListener(contacto) }
        holder.itemView.setOnClickListener { onClickListener(contacto) }
    }

    override fun getItemCount(): Int = listaContactos.size
    // Devuelve el número total de elementos en la lista de contactos.

    // Función para actualizar la lista de contactos
    fun actualizarLista(nuevaListaContactos: List<Contact>) {
        listaContactos = nuevaListaContactos
        notifyDataSetChanged()
    }
}