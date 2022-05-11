package com.example.chat.fragments.dialogs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chat.R
import com.example.chat.databinding.FragmentDialogsBinding


class DialogsFragment : Fragment(R.layout.fragment_dialogs) {
    private val binding by viewBinding(FragmentDialogsBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}