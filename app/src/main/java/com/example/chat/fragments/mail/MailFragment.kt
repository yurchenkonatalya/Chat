package com.example.chat.fragments.mail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chat.R
import com.example.chat.databinding.FragmentMailBinding
import com.example.chat.databinding.FragmentOwnersBinding

class MailFragment : Fragment(R.layout.fragment_mail) {
    private val binding by viewBinding(FragmentMailBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}