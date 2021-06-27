package com.example.hbmanager.fragments.fragmentsEntrada

import android.graphics.ColorSpace
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.example.hbmanager.R
import com.example.hbmanager.activities.MainActivity
import com.example.hbmanager.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    lateinit var mainActivity: MainActivity
    lateinit var loginFragment: LoginFragment

    //Vista
    private lateinit var nameRegister: EditText
    private lateinit var lastnameRegister: EditText
    private lateinit var emailRegister: EditText
    private lateinit var passwordRegister: EditText
    private lateinit var rePasswordRegister: EditText
    private lateinit var btnRegister: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvLogin: TextView


    // Firebase
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase


    companion object {
        const val TAG = "RegisterFragment"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        instancias()
        tvLogin = binding.tvLogin
        //Obtiene el contexto
        val appContext = requireContext().applicationContext
        progressBar = ProgressBar(appContext)

        mAuth = FirebaseAuth.getInstance() //Autenticación de firebase
        database = FirebaseDatabase.getInstance()
        dbReference = database.reference.child("User")



        tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.btnRegister.setOnClickListener {
            perfomRegister()
        }
    }

    private fun perfomRegister() {
        //VALIDACION
        val name = nameRegister.text.toString().trim()
        val lastName = lastnameRegister.text.toString().trim()
        val email = emailRegister.text.toString().trim()
        val password = passwordRegister.text.toString().trim()
        val rePassword = rePasswordRegister.text.toString().trim()


        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(
                password
            )
        ) {

            if (rePassword != password) {

                Toast.makeText(requireContext(), "Claves diferentes", Toast.LENGTH_SHORT).show()
            } else {
                progressBar.visibility = View.VISIBLE
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        if (task.isComplete) {
                            val user: FirebaseUser? = mAuth.currentUser
                            verifyEmail(user) // Correo enviado al usuario

                            // damos de alta los demás datos de base de datos
                            val userBD = dbReference.child(user?.uid.toString())
                            userBD.child("Name").setValue(name)
                            userBD.child("Lastname").setValue(lastName)
                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                        }
                    }

            }

        } else {
            Toast.makeText(requireContext(), "Campos vacíos", Toast.LENGTH_SHORT).show()
        }


    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        //updateUI(currentUser)
    }

    private fun verifyEmail(user: FirebaseUser?) {
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isComplete) {
                    Toast.makeText(requireContext(), "Email enviado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error al enviar el correo",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun instancias() {
        nameRegister = binding.etRegisterNonbre
        lastnameRegister = binding.etRegisterApellido
        emailRegister = binding.etRegisterEmail
        passwordRegister = binding.etRegisterPassword
        btnRegister = binding.btnRegister
        progressBar = binding.progressBar
        rePasswordRegister = binding.etRegisterRepassword

    }


}