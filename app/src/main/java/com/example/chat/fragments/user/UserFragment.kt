package com.example.chat.fragments.user

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.chat.*
import com.example.chat.Constants.BASE_IMAGE_URL
import com.example.chat.databinding.FragmentUserBinding
import com.example.chat.locale.LanguageEn
import com.example.chat.locale.LanguageRu
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user.*

@AndroidEntryPoint
class UserFragment : Fragment(R.layout.fragment_user) {
    private val binding by viewBinding(FragmentUserBinding::bind)
    private val viewModel: UserViewModel by viewModels()
    private var userId: Long = -1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = arguments?.getLong("userId") ?: -1
        bindViewsWithLoadingStatus()
        viewModel.onViewAttached(savedInstanceState != null)
    }

    override fun onStart() {
        super.onStart()
        val locale = if (currentLocale == 0) LanguageRu else LanguageEn
        binding.buttonWriteMessage.text = locale.frUserWrite
    }

    private fun bindViewsWithLoadingStatus() {
        lifecycleScope.launchWhenStarted {
            viewModel.loadingStatus.collect { loadingStatus ->
                val user = viewModel.userInfo
                if (loadingStatus == 0) {
                    with(binding) {
                        Glide
                            .with(context ?: return@collect)
                            .load(BASE_IMAGE_URL + user?.user_photo ?: "a")
                            .placeholder(R.drawable.people)
                            .error(R.drawable.people)
                            .fitCenter()
                            .centerCrop()
                            .into(binding.imageCard)
                        val nameText =
                            user?.user_name.toString() + " " + user?.user_surname.toString()
                        mainName.text = nameText
                        textView_email.text = user?.user_mail
                        textView_phone.text = user?.user_phone
                        user?.user_birthday?.let {
                            textView_birthday.text =
                                InfoHelper.systemDateTimeToDate(it)
                        }
                        user?.let {
                            buttonWriteMessage.setOnClickListener {
                                findNavController().navigate(
                                    UserFragmentDirections.actionNavUserFragmentToNavDialogFragment(
                                        user.id,
                                        user.user_photo,
                                        user.user_name,
                                        user.user_surname
                                    )
                                )
                            }
                            buttonWriteMessage.isVisible = user.id != AUTHORIZED_USER_ID
                        }
                    }
                }
            }
        }
    }
}