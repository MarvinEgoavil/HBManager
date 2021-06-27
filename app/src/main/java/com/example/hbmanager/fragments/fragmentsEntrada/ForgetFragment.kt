package com.example.hbmanager.fragments.fragmentsEntrada

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.hbmanager.R
import com.example.hbmanager.databinding.FragmentForgetBinding
import com.google.firebase.auth.FirebaseAuth


class ForgetFragment : Fragment() {

    private var _binding: FragmentForgetBinding? = null
    private val binding get() = _binding!!

    private lateinit var textUser: EditText

    private lateinit var btnSend: Button
    private lateinit var mAuth: FirebaseAuth

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentForgetBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textUser = binding.etForgetEmail
        btnSend = binding.btnEnviar

        progressBar = binding.progressBar3
        mAuth = FirebaseAuth.getInstance()

        btnSend.setOnClickListener {
            send()
        }
    }

    fun send() {
        val email = textUser.text.toString()

        if (!TextUtils.isEmpty(email)) {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressBar.visibility = View.VISIBLE
                    findNavController().navigate(R.id.action_forgetFragment_to_loginFragment)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error al enviar el Email",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }


        }

    }
}