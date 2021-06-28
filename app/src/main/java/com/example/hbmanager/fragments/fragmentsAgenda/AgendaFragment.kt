package com.example.hbmanager.fragments.fragmentsAgenda

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hbmanager.R
import com.example.hbmanager.clases.Usuario
import com.example.hbmanager.utils.AgendaAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AgendaFragment : Fragment() {
    private lateinit var ivLogout:ImageView
    private lateinit var btnAgregar:Button
    private lateinit var recyclerAgenda:RecyclerView
    private lateinit var progresCargando:ProgressBar
    var agendaList = ArrayList<Usuario>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agenda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivLogout = view.findViewById(R.id.iv_Logout)
        ivLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_agendaFragment_to_loginFragment)
        }
        progresCargando = view.findViewById(R.id.progresCargando)
        btnAgregar = view.findViewById(R.id.btnAgregar)
        recyclerAgenda = view.findViewById(R.id.recycler_agenda)
        recyclerAgenda.layoutManager = LinearLayoutManager(activity)
        recyclerAgenda.setHasFixedSize(true)

        btnAgregar.setOnClickListener{
            findNavController().navigate(R.id.action_agendaFragment_to_nuevaAgendaFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        listarContactos()
    }

    fun listarContactos(){

        progresCargando.visibility = View.VISIBLE
        recyclerAgenda.visibility = View.GONE

        agendaList = ArrayList()

        val shared = requireActivity().getSharedPreferences("SharedHBManager",0)

        val ref = FirebaseDatabase.getInstance().reference.child("usuarios")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    //it.toString()
                    Log.d("RegistroActivity","cuenta:: ${shared.getString("cuenta","")}")
                    Log.d("RegistroActivity","IT: $it")
                    val usuario = it.getValue(Usuario::class.java)
                    if (usuario != null && usuario.cuenta == shared.getString("cuenta","")) {
                        agendaList.add(usuario)
                    }
                    recyclerAgenda.adapter = AgendaAdapter(agendaList)
                    recyclerAgenda.visibility = View.VISIBLE
                    progresCargando.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                recyclerAgenda.visibility = View.GONE
                progresCargando.visibility = View.GONE
            }
        })



    }


    class Usuario2 (val idUsuario:String,val nombres:String,val fechaNacimiento:String,val urlFoto:String){
        constructor() :this("","","","")
    }



}