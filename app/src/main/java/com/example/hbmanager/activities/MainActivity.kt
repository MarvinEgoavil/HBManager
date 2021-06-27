package com.example.hbmanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hbmanager.R
import com.example.hbmanager.databinding.ActivityMainBinding
import com.example.hbmanager.fragments.fragmentsEntrada.LoginFragment

class MainActivity : AppCompatActivity() {

    lateinit var loginFragment: LoginFragment

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        loginFragment = LoginFragment()


    }



}