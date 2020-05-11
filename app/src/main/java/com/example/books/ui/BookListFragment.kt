package com.example.books.ui

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.books.*
import com.example.books.databinding.FragmentBookListBinding
import java.io.IOException
import java.net.URL

class BookListFragment : Fragment(){

    private val TAG = BookListFragment::class.simpleName
    private lateinit var bookUrl: URL
    private lateinit var binding: FragmentBookListBinding
    private lateinit var viewModel: BookListViewModel
    private val adapter = BookAdapter()

    companion object {
        private const val LAST_SEARCH_QUERY = "last_search_query"
        private const val DEFAULT_QUERY = "cooking"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentBookListBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this,
                Injection.provideViewModelFactory(requireContext())).get(BookListViewModel::class.java)

        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.rvBooks.addItemDecoration(decoration)

        initAdapter()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
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
                    Log.d("Error", e.message);
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
                viewModel.searchBooks(it.toString())
                adapter.submitList(null)
            }
        }
    }

    private fun initAdapter() {
        binding.rvBooks.adapter = adapter

        viewModel.books.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "list: ${it?.size}")
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val safeArgs: BookListFragmentArgs by navArgs()
//        val query = safeArgs.query
//        try {
//            bookUrl = if (query.isEmpty()) {
//                Log.d(TAG, "no query")
//                ApiUtil.buildUrl("cooking")
//            } else {
//                Log.d(TAG, query)
//                URL(query)
//            }
//            BooksQueryTask().execute(bookUrl)
//        }
//        catch (e: Exception) {
//            Log.d("error", e.message)
//        }
    }


    inner class BooksQueryTask : AsyncTask<URL?, Void?, String?>() {
        override fun doInBackground(vararg urls: URL?): String? {
            val searchUrl = urls[0]
            var result: String? = null
            try {
                result = ApiUtil.getJson(searchUrl)
            } catch (e: IOException) {
                Log.e("Error", e.message)
            }
            return result
        }

        override fun onPostExecute(result: String?) {
//            mLoading.setVisibility(View.INVISIBLE)
            if (result == null) {
                binding.rvBooks.visibility = View.INVISIBLE
                binding.tvError.visibility = View.VISIBLE
            } else {
                binding.rvBooks.visibility = View.VISIBLE
                binding.tvError.visibility = View.INVISIBLE
                val books = ApiUtil.getBooksFromJson(result)
                val resultString = ""
                val adapter = BookAdapter()
                binding.rvBooks.adapter = adapter
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.book_list_menu, menu)
        initSearch(menu)
        val recentList: ArrayList<String> = SpUtil.getQueryList(requireContext())
        var recentMenu: MenuItem? = null
        for (item in recentList){
            recentMenu = menu.add(Menu.NONE, recentList.indexOf(item), Menu.NONE, item)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.search_dest -> {
                findNavController().navigate(BookListFragmentDirections.actionBooklistDestToSearchDest())
            }
            R.id.action_search -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
}