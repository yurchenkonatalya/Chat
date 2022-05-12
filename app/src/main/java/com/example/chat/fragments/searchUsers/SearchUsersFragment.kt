package com.example.chat.fragments.searchUsers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chat.R
import com.example.chat.databinding.FragmentSearchUsersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchUsersFragment : Fragment(R.layout.fragment_search_users) {
    private val binding by viewBinding(FragmentSearchUsersBinding::bind)
    private var adapter: SearchUserAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private val viewModel: SearchUserViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onViewAttach(savedInstanceState != null)
        bindLoadingStatus()
        initAdapter()
        initRecycler()
        initSwiper()
        initSearchView()
        initList()
    }

    private fun bindLoadingStatus() {
        lifecycleScope.launch {
            viewModel.loadingStatus.collectLatest { loading ->
                binding.let {
                    it.swiper.post { it.swiper.isRefreshing = loading == 3 }
                }
            }
        }
    }

    private fun initAdapter() {
        adapter = SearchUserAdapter(
            object : SearchUserAdapter.ItemClickListener {
                override fun onClick(userId: Long) {
                    val currentDestinationId = findNavController().currentDestination?.id ?: return
                    if (currentDestinationId == R.id.nav_search_users_fragment) {
                        findNavController().navigate(
                            SearchUsersFragmentDirections.actionNavSearchUsersFragmentToNavUserFragment(
                                userId
                            )
                        )
                        return
                    }
                }
            })

        layoutManager = LinearLayoutManager(this.context)
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

    private fun initSearchView() {
        binding.editTextExplore.setText(viewModel.settings.search)
        binding.buttonExplore.setOnClickListener {
            viewModel.onSearchStateChanged(binding.editTextExplore.text.toString())
        }
    }

    private fun initList() {
        lifecycleScope.launch {
            viewModel.data.collectLatest {
                adapter?.submitData(it)
            }
        }
    }

    private fun initSwiper() {
        binding.swiper.setOnRefreshListener {
            viewModel.onRefresh()
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

    override fun onDestroyView() {
        adapter = null
        layoutManager = null
        super.onDestroyView()
    }
}
