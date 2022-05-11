package com.example.chat.fragments.registration

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chat.ImageHelper
import com.example.chat.MainActivity
import com.example.chat.R
import com.example.chat.databinding.FragmentRegistrationBinding
import com.example.chat.fragments.signin.SigninFragmentDirections
import com.example.chat.fragments.signin.SigninViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
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
//            getContentLauncher.launch(imageHelper.getImageIntent())
            select_image(it)

        }
        binding.buttonReg.setOnClickListener {

            if(binding.etLogin.text.isEmpty() || binding.etMail.text.isEmpty()|| binding.etName.text.isEmpty()||binding.etPassword.text.isEmpty()||binding.etPhone.text.isEmpty()||binding.etSurname.text.isEmpty() || selectedBitmap == null)
                return@setOnClickListener

            viewModel.registerUser(
                binding.etLogin.text.toString(),
                binding.etPassword.text.toString(),
                binding.etName.text.toString(),
                binding.etSurname.text.toString(),
                binding.etPhone.text.toString(),
                binding.etMail.text.toString(),
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
                    if(it == RegistrationViewModel.States.ERROR){
                        Toast.makeText(this@RegistrationFragment.context, "Произошла ошибка", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        binding.birthday.init(
            2002,
            8,
            8
        ) { day, year, month, dateOfMonth -> currentBirthday = "$year-${month+1}-${dateOfMonth}" }
    }

//    private val getContentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//
//            lifecycleScope.launch(Dispatchers.Default) {
//                viewModel.loadingStatus.value = 1
//                try {
//                    val uri = result.data?.data ?: return@launch
//
//                    viewModel.bitmap.value = imageHelper.uriToBitmap(uri)
//
//                } catch (e: Exception) { }
//
//                viewModel.onBitmapChanged()
//
//                viewModel.loadingStatus.value = 0
//            }
//        }
//    }

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

//                if (selectedImage != null) {
//                    if (Build.VERSION.SDK_INT >= 28) {
//                        val source = ImageDecoder.createSource(it.contentResolver, selectedImage!!)
//                        selectedBitmap = ImageDecoder.decodeBitmap(source)
//                    }
//                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
