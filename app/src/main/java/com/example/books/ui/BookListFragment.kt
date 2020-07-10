package com.example.books.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.onNavDestinationSelected
import com.example.books.*
import com.example.books.databinding.FragmentBookListBinding
import com.example.books.util.SpUtil
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class BookListFragment : Fragment() {

    private val bookListViewModel by viewModel<BookListViewModel>()

    private lateinit var queryArgs: String
    private lateinit var binding: FragmentBookListBinding
    private val adapter = BookAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentBookListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = bookListViewModel
            rvBooks.adapter = adapter
        }
        val safeArgs: BookListFragmentArgs by navArgs()
        queryArgs = safeArgs.query

        val queryString = queryArgs.split(",")
        val lastSearchString = if (bookListViewModel.lastTitleValue() != null)
            listOf(bookListViewModel.lastTitleValue()) else null
        val query: List<String?> = lastSearchString ?: queryString
        Timber.d("$lastSearchString")
        bookListViewModel.searchBooks(query)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeBooks()
        observeNetworkErrors()
    }

    private fun initSearch(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as android.widget.SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                try {
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
    }

    private fun updateBookListFromInput(query: String?) {
        query?.trim().let {
            if (!it.isNullOrEmpty()) {
                binding.rvBooks.scrollToPosition(0)
                bookListViewModel.searchBooks(listOf(it.toString()))
                adapter.submitList(null)
            }
        }
    }

    private fun observeBooks() {
        bookListViewModel.books.observe(viewLifecycleOwner, Observer {
            Timber.d("list: ${it?.size}")
            adapter.submitList(it)
        })
    }

    private fun observeNetworkErrors() {
        bookListViewModel.networkErrors.observe(viewLifecycleOwner, Observer {
            Snackbar.make(requireView(), "\uD83D\uDE28 Wooops $it", Snackbar.LENGTH_LONG).show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.book_list_menu, menu)
        initSearch(menu)
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
}