package com.example.chat.fragments.dialog

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chat.*
import com.example.chat.databinding.FragmentDialogBinding
import com.example.chat.locale.LanguageEn
import com.example.chat.locale.LanguageRu
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dialog.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DialogFragment : Fragment(R.layout.fragment_dialog) {
    private val binding by viewBinding(FragmentDialogBinding::bind)
    private val viewModel: DialogViewModel by viewModels()
    private var adapter: DialogAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    var selectedImage: Uri? = null
    var selectedBitmap: Bitmap? = null

    @Inject
    lateinit var imageHelper: ImageHelper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userId: Long? = null
        var userPhoto: String? = null
        with(binding) {
            arguments?.let {
                val name = it.getString("userName")
                val surname = it.getString("userSurname")
                userId = it.getLong("userId")
                userPhoto = it.getString("userPhoto")
                val res = "$name $surname"
                tvName.text = res
            }
            buttonBack.setOnClickListener {
                (activity as MainActivity).onBackPressed()
            }
        }
        viewModel.onViewAttach(savedInstanceState != null)
        bindLoadingStatus()
        initAdapter(userId ?: return, userPhoto ?: return)
        initRecycler()
        initList()
        binding.buttonSend.setOnClickListener {
            if (binding.editTextExplore.text.isNotEmpty()) {
                viewModel.sendMessage(binding.editTextExplore.text.toString())
                binding.editTextExplore.text.clear()
            }
        }
        binding.buttonAttachment.setOnClickListener {
            select_image(it)
        }
        lifecycleScope.launch {
            viewModel.lastActiveFlow.collectLatest {
                if (it == null)
                    binding.tvOnline.isVisible = false
                it?.let {
                    binding.tvOnline.isVisible = InfoHelper.getCurrentTimeIsOnline(it)
                }
            }
        }
    }

    private fun bindLoadingStatus() {
        lifecycleScope.launch {
            viewModel.loadingStatus.collectLatest { loading ->
                binding.run {
                    progressBar.isVisible = loading == 3
                }
            }
        }
    }

    private fun initAdapter(userId: Long, userPhoto: String) {
        adapter = DialogAdapter(userId, userPhoto, AUTHORIZED_USER_PHOTO ?: return)
        layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, true)
        adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            var restored = false
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if (!restored && adapter?.itemCount ?: 0 >= viewModel.settings.position) {
                    restored = true
                    layoutManager?.scrollToPositionWithOffset(
                        viewModel.settings.position,
                        viewModel.settings.offset
                    )
                }
            }
        })

        adapter?.let { dialogAdapter ->
            lifecycleScope.launch {
                dialogAdapter.loadStateFlow.collect {
                    if (layoutManager?.findFirstVisibleItemPosition() == 0)
                        rvList.smoothScrollToPosition(0)
                }
            }
        }
    }

    private fun initRecycler() {
        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (layoutManager?.findLastVisibleItemPosition() == (adapter?.itemCount ?: 0) - 1)
                    viewModel.onListEnded()
            }
        })
        binding.rvList.layoutManager = layoutManager
        binding.rvList.adapter = adapter
    }

    private fun initList() {
        lifecycleScope.launch {
            viewModel.data.collectLatest {
                adapter?.submitData(it)
            }
        }
    }

    override fun onStop() {
        viewModel.onViewStop(
            layoutManager?.findFirstVisibleItemPosition() ?: 0,
            layoutManager?.findViewByPosition(
                layoutManager?.findFirstVisibleItemPosition() ?: 0
            )?.top ?: 0
        )
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onViewStart()
        val locale = if (currentLocale == 0) LanguageRu else LanguageEn
        binding.editTextExplore.hint = locale.frDialogWrite
        binding.tvOnline.text = locale.frDialogOnline
    }

    override fun onDestroyView() {
        adapter = null
        layoutManager = null
        super.onDestroyView()
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
                viewModel.sendMessagePhoto(
                    imageHelper.bitmapToBase64(
                        selectedBitmap ?: return@let,
                        true
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}