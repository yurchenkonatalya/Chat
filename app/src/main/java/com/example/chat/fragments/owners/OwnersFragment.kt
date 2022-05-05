package com.example.chat.fragments.owners

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chat.R
import com.example.chat.databinding.FragmentOwnersBinding
import com.example.chat.databinding.FragmentSignInBinding

class OwnersFragment : Fragment(R.layout.fragment_owners) {
    private val binding by viewBinding(FragmentOwnersBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}