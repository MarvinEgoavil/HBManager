package com.example.hbmanager.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hbmanager.R
import com.example.hbmanager.clases.Usuario
import com.example.hbmanager.fragments.fragmentsAgenda.AgendaFragment
import com.squareup.picasso.Picasso

class AgendaAdapter(private val agendaList:List<Usuario>):RecyclerView.Adapter<AgendaAdapter.ViewHolderAgenda>() {

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolderAgenda {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.item_card_view_agenda,parent,false)
        return ViewHolderAgenda(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderAgenda,position: Int) {
        val agenda = agendaList[position]
        holder.txNombre.text = ""+agenda.nombres
        holder.txFechaCumpleaños.text = ""+agenda.fechaNacimiento

        Picasso.with(holder.itemView.context).load(agenda.urlFoto).into(holder.ivFoto)

    }

    override fun getItemCount(): Int {
        return agendaList.size
    }

    class ViewHolderAgenda(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txNombre :TextView = itemView.findViewById(R.id.txNombre)
        val txFechaCumpleaños:TextView = itemView.findViewById(R.id.txFechaCumpleaños)
        val ivFoto:ImageView = itemView.findViewById(R.id.ivFoto);
    }
}