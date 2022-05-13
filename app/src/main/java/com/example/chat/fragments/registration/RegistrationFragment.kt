package com.example.chat.fragments.registration

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chat.*
import com.example.chat.databinding.FragmentRegistrationBinding
import com.example.chat.locale.LanguageEn
import com.example.chat.locale.LanguageRu
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private val binding by viewBinding(FragmentRegistrationBinding::bind)
    var selectedImage: Uri? = null
    var selectedBitmap: Bitmap? = null
    lateinit var loadPhoto: Button
    private val viewModel: RegistrationViewModel by viewModels()

    @Inject
    lateinit var imageHelper: ImageHelper
    private var currentBirthday = "2002-09-08"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPhoto = binding.buttonLoadPhoto
        loadPhoto.setOnClickListener {
            select_image(it)
        }
        binding.buttonReg.setOnClickListener {
            if (binding.etLogin.text.isEmpty() || binding.etMail.text.isEmpty() || binding.etName.text.isEmpty() || binding.etPassword.text.isEmpty() || binding.etPhone.text.isEmpty() || binding.etSurname.text.isEmpty() || selectedBitmap == null)
                return@setOnClickListener
            var login = binding.etLogin.text.toString().removeSpaces()
            var password = binding.etPassword.text.toString().removeSpaces()
            var name = binding.etName.text.toString().removeSpaces()
            var surname = binding.etSurname.text.toString().removeSpaces()
            var phone = binding.etPhone.text.toString().removeSpaces()
            var mail = binding.etMail.text.toString().removeSpaces()
            var toastText: String? = null
            if (login.contains(" "))
                toastText = "логин не должен содержать пробелов"
            if (password.contains(" "))
                toastText = "пароль не должен содержать пробелов"
            if (name.contains(" "))
                toastText = "имя не должно содержать пробелов"
            if (surname.contains(" "))
                toastText = "фамилия не должна содержать пробелов"
            if (phone.contains(" "))
                toastText = "телефон не должен содержать пробелов"
            if (mail.contains(" "))
                toastText = "почта не должна содержать пробелов"
            toastText?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.registerUser(
                login,
                password,
                name,
                surname,
                phone,
                mail,
                "1",
                currentBirthday,
                selectedBitmap ?: return@setOnClickListener
            )
        }

        lifecycleScope.launch {
            viewModel.registratedResultFlow.collectLatest {
                if (it == RegistrationViewModel.States.SUCCESS) {
                    (activity as MainActivity).onBackPressed()
                } else {
                    if (it == RegistrationViewModel.States.ERROR) {
                        Toast.makeText(
                            this@RegistrationFragment.context,
                            "Произошла ошибка",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (it == RegistrationViewModel.States.LOGIN_EXIST) {
                        Toast.makeText(
                            this@RegistrationFragment.context,
                            "ошибка: логин уже занят",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.birthday.init(
            2002,
            8,
            8
        ) { day, year, month, dateOfMonth -> currentBirthday = "$year-${month + 1}-${dateOfMonth}" }
    }

    fun select_image(view: View) {
        activity?.let {
            if (ContextCompat.checkSelfPermission(
                    it.applicationContext,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            } else {
                val Intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(Intent, 2)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val Intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(Intent, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImage = data.data
        }
        try {
            context?.let {
                selectedBitmap = imageHelper.uriToBitmap(data?.data ?: return)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        val locale = if (currentLocale == 0) LanguageRu else LanguageEn
        binding.etName.hint = locale.frRegistrationName
        binding.etSurname.hint = locale.frRegistrationSurname
        binding.etMail.hint = locale.frRegistrationMail
        binding.birthdayLabel.text = locale.frRegistrationBirthday
        binding.etPhone.hint = locale.frRegistrationPhone
        binding.etLogin.hint = locale.frRegistrationLogin
        binding.etPassword.hint = locale.frRegistrationPassword
        binding.buttonLoadPhoto.text = locale.frRegistrationLoadPhoto
        binding.buttonReg.text = locale.frSigninRegister
    }
}