package com.example.books.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.books.*
import com.example.books.databinding.FragmentBookListBinding
import com.example.books.util.SpUtil
import timber.log.Timber

class BookListFragment : Fragment(){

    private lateinit var queryArgs: String
    private lateinit var binding: FragmentBookListBinding
    private lateinit var viewModel: BookListViewModel
    private val adapter = BookAdapter()

    companion object {
        private const val LAST_SEARCH_QUERY = "last_search_query"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentBookListBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this,
                Injection.provideViewModelFactory(requireContext())).get(BookListViewModel::class.java)

        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.rvBooks.addItemDecoration(decoration)

        initAdapter()

        val safeArgs: BookListFragmentArgs by navArgs()
        queryArgs = safeArgs.query

        val queryString = queryArgs.split(",")
        val lastSearchString = if (viewModel.lastTitleValue() != null)
            listOf(viewModel.lastTitleValue()) else null
        val query:List<String?> = lastSearchString ?: queryString
        Timber.d("$lastSearchString")
        viewModel.searchBooks(query)

        return binding.root
    }

    private fun initSearch(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as android.widget.SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                try {
                    updateBookListFromInput(query)

                } catch (e: Exception){
                    Timber.d(e);
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
                viewModel.searchBooks(listOf(it.toString()))
                adapter.submitList(null)
            }
        }
    }

    private fun initAdapter() {
        binding.rvBooks.adapter = adapter

        viewModel.books.observe(viewLifecycleOwner, Observer {
            Timber.d("list: ${it?.size}")
            showEmptyList(it?.size == 0)
            adapter.submitList(it)
        })

        viewModel.networkErrors.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "\uD83D\uDE28 Wooops $it", Toast.LENGTH_LONG).show()
        })
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            binding.rvBooks.visibility = View.GONE
            binding.tvError.visibility = View.VISIBLE
        } else {
            binding.rvBooks.visibility = View.VISIBLE
            binding.tvError.visibility = View.GONE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.book_list_menu, menu)
        initSearch(menu)
//        val recentList: ArrayList<String> = SpUtil.getQueryList(requireContext())
//        var recentMenu: MenuItem? = null
//        for (item in recentList){
//            recentMenu = menu.add(Menu.NONE, recentList.indexOf(item), Menu.NONE, item)
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.search_dest -> {
                findNavController().navigate(BookListFragmentDirections.actionBooklistDestToSearchDest())
                return true
            }
            R.id.action_search -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}