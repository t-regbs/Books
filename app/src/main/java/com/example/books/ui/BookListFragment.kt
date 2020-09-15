package com.example.books.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.onNavDestinationSelected
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.example.books.*
import com.example.books.databinding.FragmentBookListBinding
import com.example.books.util.SpUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class BookListFragment : Fragment() {

    private val bookListViewModel by viewModel<BookListViewModel>()

    private lateinit var queryArgs: String
    private lateinit var binding: FragmentBookListBinding
    private lateinit var latest: String
    private val adapter = BookAdapter()
    private var searchJob: Job? = null
    private lateinit var _menu: Menu

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentBookListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = bookListViewModel
        }
        initAdapter()

        val safeArgs: BookListFragmentArgs by navArgs()
        queryArgs = safeArgs.query

        val queryString = queryArgs.split(",")

//        val query: List<String?> = lastSearchString ?: queryString

        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        search(query)
//        initSearch(query)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.retryButton.setOnClickListener { adapter.retry() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, latest)
    }

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            bookListViewModel.searchRepo(query).collectLatest{
                adapter.submitData(it)
            }
        }
    }

    private fun initAdapter() {
        binding.rvBooks.adapter = adapter.withLoadStateHeaderAndFooter(
            header = BooksLoadStateAdapter { adapter.retry() },
            footer = BooksLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.rvBooks.isVisible = loadState.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.pbLoading.isVisible = loadState.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    context,
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun initSearch(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as android.widget.SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                try {
                    latest = query!!
                    updateBookListFromInput(query)
                } catch (e: Exception) {
                    Timber.d(e)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.rvBooks.scrollToPosition(0) }
        }
    }

    private fun updateBookListFromInput(query: String?) {
        query?.trim().let {
            if (!it.isNullOrEmpty()) {
                search(it.toString())
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.book_list_menu, menu)
        initSearch(menu)
        _menu = menu
        val recentList: ArrayList<String> = SpUtil.getQueryList(requireContext())
        var recentMenu: MenuItem? = null
        for (item in recentList) {
            recentMenu = menu.add(Menu.NONE, recentList.indexOf(item), Menu.NONE, item)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController()) ||
                super.onOptionsItemSelected(item)
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Fishing"
    }
}