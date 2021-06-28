package com.example.hbmanager.fragments.fragmentsAgenda

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.hbmanager.R
import com.example.hbmanager.clases.Usuario
import com.example.hbmanager.utils.DatePickerFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class NuevaAgendaFragment : Fragment() {

    private var fechaSeleccionada: String? = null

    lateinit var filePath: Uri
    private lateinit var etNombre: EditText
    private lateinit var ivFoto: ImageView
    private lateinit var etFechaNacimiento: EditText
    private lateinit var btnElegirFoto: Button
    private lateinit var btnPublicar: Button
    private lateinit var ivAtras: ImageView


    private val File = 1
    private val database = Firebase.database
    private val myRef = database.getReference("agenda")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nueva_agenda, container, false)
    }

    fun cargarFoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(Intent.createChooser(intent, "Elija una foto"), File)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == File) {
            if (resultCode == Activity.RESULT_OK && data != null) {

                filePath = data.data!!
                var bitmap =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, filePath)
                ivFoto.setImageBitmap(bitmap)


                /*
                val FileUri = data!!.data
                val Folder: StorageReference = FirebaseStorage.getInstance().getReference().child("usuario")
                val file_name: StorageReference = Folder.child("file"+FileUri!!.lastPathSegment)
                file_name.putFile(FileUri).addOnSuccessListener {
                    taskSnapshot ->
                    file_name.downloadUrl().add
                }*/
            }
        }
    }

    fun mostrarDialogCalendario() {
        val datapicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datapicker.show(requireActivity().supportFragmentManager, "Datepicker")
    }

    fun onDateSelected(day: Int, month: Int, year: Int) {
        val month2 = month + 1
        if (month2 > 9) {
            etFechaNacimiento.setText("$day-$month2-$year")
        } else {
            etFechaNacimiento.setText("$day-0$month2-$year")
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etNombre = view.findViewById(R.id.etNombres)
        etFechaNacimiento = view.findViewById(R.id.etFechaNacimiento)
        etFechaNacimiento.setOnClickListener {
            mostrarDialogCalendario()
        }
        ivFoto = view.findViewById(R.id.ivFoto)
        btnElegirFoto = view.findViewById(R.id.btnElegirFoto)
        btnPublicar = view.findViewById(R.id.btnPublicar)
        ivAtras = view.findViewById(R.id.iv_Atras)
        ivAtras.setOnClickListener {
            findNavController().navigate(R.id.action_nuevaAgendaFragment_to_agendaFragment)
        }

        btnElegirFoto.setOnClickListener {
            cargarFoto()
        }

        btnPublicar.setOnClickListener {
            publicarAgenda()
        }

    }

    fun publicarAgenda() {
        if (filePath != null) {
            var pd = ProgressDialog(activity)
            pd.setTitle("Registrando...")
            pd.show()

            val fileName = UUID.randomUUID()

            var imageRef = FirebaseStorage.getInstance().reference.child("images/$fileName")
            imageRef.putFile(filePath!!)
                .addOnSuccessListener {
                    pd.dismiss()

                    //Toast.makeText(activity,"Publicación exitosa! ${it.metadata?.path}",Toast.LENGTH_SHORT).show()
                    imageRef.downloadUrl.addOnSuccessListener {
                        Log.d("RegistroActivity", "File location: $it")
                        insertarNuevoUsuario(it.toString(), fileName.toString())
                    }
                }
                .addOnFailureListener {
                    pd.dismiss()
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener {
                    var progreso = (100.0 * it.bytesTransferred) / it.totalByteCount
                    //Toast.makeText(activity,"Publicación exitosa!",Toast.LENGTH_SHORT).show()
                    pd.setMessage("Publicando al ${progreso.toInt()}%")
                }
        }
    }

    private fun insertarNuevoUsuario(urlFoto: String, idUsuario: String) {
        //val idUsuario = FirebaseAuth.getInstance().uid?:""
        val shared = requireActivity().getSharedPreferences("SharedHBManager", 0)
        val ref = FirebaseDatabase.getInstance().getReference("/usuarios/$idUsuario")
        val usuario = Usuario(
            shared.getString("cuenta", "").toString(),
            idUsuario,
            etNombre.text.toString(),
            etFechaNacimiento.text.toString(),
            urlFoto
        )

        ref.setValue(usuario)
            .addOnSuccessListener {
                Log.d("RegistroActivity", "Usuario registrado!")
                Toast.makeText(activity, "Usuario registrado!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_nuevaAgendaFragment_to_agendaFragment)
            }
            .addOnFailureListener {
                Log.d("RegistroActivity", "Fallo el registro de usuario.")
                Toast.makeText(
                    activity,
                    "Fallo el registro de usuario, vuelva a intentar",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


}