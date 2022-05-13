package com.example.chat.fragments.settings

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.chat.Constants.BASE_IMAGE_URL
import com.example.chat.ImageHelper
import com.example.chat.InfoHelper
import com.example.chat.R
import com.example.chat.currentLocale
import com.example.chat.databinding.FragmentSettingsBinding
import com.example.chat.locale.LanguageEn
import com.example.chat.locale.LanguageRu
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val binding by viewBinding(FragmentSettingsBinding::bind)
    var selectedImage: Uri? = null
    var selectedBitmap: Bitmap? = null
    lateinit var imageView: ImageView
    private val viewModel: SettingsViewModel by viewModels()

    @Inject
    lateinit var imageHelper: ImageHelper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = binding.imageCard
        imageView.setOnClickListener {
            select_image(it)
        }
        lifecycleScope.launch {
            viewModel.userFlow.collectLatest {
                if (it != null) {
                    binding.tvName.setText(it.user_name)
                    binding.tvMail.setText(it.user_mail)
                    binding.tvSurname.setText(it.user_surname)
                    binding.tvPhone.setText(it.user_phone)
                    context?.let { context ->
                        Glide
                            .with(context)
                            .load(BASE_IMAGE_URL + it.user_photo)
                            .placeholder(R.drawable.image_gallery)
                            .error(R.drawable.image_gallery)
                            .fitCenter()
                            .centerCrop()
                            .into(binding.imageCard)
                    }
                    binding.tvBithday.text =
                        InfoHelper.systemDateTimeToDate(it.user_birthday)
                }
            }
        }

        binding.buttonLogout.setOnClickListener {
            try {
                viewModel.logout()
            } catch (e: java.lang.Exception) {
                Log.e("111111111", e.toString())
            }
            findNavController().popBackStack(R.id.nav_dialogs_fragment, false)
            findNavController().navigate(
                SettingsFragmentDirections.actionNavSettingsFragmentToSigninFragment()
            )
        }
        binding.buttonChangeLanguage.setOnClickListener {
            currentLocale = 1 - currentLocale
            viewModel.updateLocale()
            val locale = if (currentLocale == 0) LanguageRu else LanguageEn
            binding.a.text = locale.frSettingsMail
            binding.b.text = locale.frSettingsPhone
            binding.c.text = locale.frSettingsBirthday
            binding.buttonChangeLanguage.text = locale.frSettingsChangeLanguage
            binding.buttonLogout.text = locale.frSettingsLogoutExit
        }
    }

    override fun onStart() {
        super.onStart()
        val locale = if (currentLocale == 0) LanguageRu else LanguageEn
        binding.a.text = locale.frSettingsMail
        binding.b.text = locale.frSettingsPhone
        binding.c.text = locale.frSettingsBirthday
        binding.buttonChangeLanguage.text = locale.frSettingsChangeLanguage
        binding.buttonLogout.text = locale.frSettingsLogoutExit
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
                if (selectedImage != null) {
                    if (Build.VERSION.SDK_INT >= 28) {
                        val source = ImageDecoder.createSource(it.contentResolver, selectedImage!!)
                        selectedBitmap = ImageDecoder.decodeBitmap(source)
                        viewModel.changeUserPhoto(
                            imageHelper.bitmapToBase64(
                                selectedBitmap ?: return, true
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}