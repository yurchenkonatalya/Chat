package com.example.chat.fragments.signin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chat.R
import com.example.chat.databinding.FragmentSigninBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SigninFragment : Fragment(R.layout.fragment_signin) {
    private val binding by viewBinding(FragmentSigninBinding::bind)
    lateinit var regLink: Button
    private val viewModel: SigninViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        regLink = binding.linkReg
        regLink.setOnClickListener {
            findNavController().navigate(
                SigninFragmentDirections.actionNavSigninFragmentToRegistrationFragment()
            )
        }
        binding.buttonLogin.setOnClickListener {
            if (binding.editTextLogin.text.isEmpty() || binding.editTextPassword.text.isEmpty())
                return@setOnClickListener
            viewModel.authorizeUser(
                binding.editTextLogin.text.toString(),
                binding.editTextPassword.text.toString()
            )
        }

        lifecycleScope.launch {
            viewModel.authorizedResultFlow.collectLatest {
                Log.e(
                    "CURDEST",
                    id.toString() + " " + it.toString() + " " + findNavController().currentDestination.toString()
                )
                if (it == SigninViewModel.States.SUCCESS) {
                    if (findNavController().currentDestination?.id != R.id.nav_signin_fragment)
                        return@collectLatest
                    findNavController().navigate(
                        SigninFragmentDirections.actionNavSigninFragmentToDialogsFragment()
                    )
                } else {
                    binding.buttonLogin.isEnabled = it != SigninViewModel.States.LOADING
                    binding.progress.isVisible = it == SigninViewModel.States.LOADING
                    binding.editTextLogin.isVisible = it != SigninViewModel.States.LOADING
                    binding.editTextPassword.isVisible = it != SigninViewModel.States.LOADING
                    binding.chatImage.isVisible = it != SigninViewModel.States.LOADING
                    binding.buttonLogin.isVisible = it != SigninViewModel.States.LOADING
                    binding.textview.isVisible = it != SigninViewModel.States.LOADING
                    binding.linkReg.isVisible = it != SigninViewModel.States.LOADING
                    if (it == SigninViewModel.States.ERROR) {
                        Toast.makeText(
                            this@SigninFragment.context,
                            "произошла ошибка",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}