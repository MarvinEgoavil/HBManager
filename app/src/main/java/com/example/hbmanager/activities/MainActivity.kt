package com.example.hbmanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hbmanager.R
import com.example.hbmanager.databinding.ActivityMainBinding
import com.example.hbmanager.fragments.fragmentsAgenda.AgendaFragment
import com.example.hbmanager.fragments.fragmentsEntrada.LoginFragment
// Marvin Egoavil Samaniego

class MainActivity : AppCompatActivity() {

    lateinit var loginFragment: LoginFragment
    lateinit var agendaFragment: AgendaFragment

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

      //  loginFragment = LoginFragment()

        agendaFragment = AgendaFragment()


    }



}