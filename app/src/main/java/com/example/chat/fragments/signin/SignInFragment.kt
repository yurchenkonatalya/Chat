package com.example.chat.fragments.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chat.R
import com.example.chat.databinding.FragmentSettingsBinding
import com.example.chat.databinding.FragmentSignInBinding

class SignInFragment  : Fragment(R.layout.fragment_mail) {
    private val binding by viewBinding(FragmentSignInBinding::bind)
    lateinit var regLink: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        regLink = binding.linkReg
        regLink.setOnClickListener {

        }
    }
}