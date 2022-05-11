package com.example.chat.fragments.searchUsers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chat.R
import com.example.chat.databinding.FragmentSearchUsersBinding

class SearchUsersFragment : Fragment(R.layout.fragment_search_users) {
    private val binding by viewBinding(FragmentSearchUsersBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}