package com.example.books.ui

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.books.ApiUtil
import com.example.books.BooksAdapter
import com.example.books.R
import com.example.books.SpUtil
import com.example.books.databinding.FragmentBookListBinding
import java.io.IOException
import java.net.URL

class BookListFragment : Fragment(){

    private val TAG = BookListFragment::class.simpleName
    private lateinit var bookUrl: URL
    private lateinit var binding: FragmentBookListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentBookListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val safeArgs: BookListFragmentArgs by navArgs()
        val query = safeArgs.query
        try {
            bookUrl = if (query.isEmpty()) {
                Log.d(TAG, "no query")
                ApiUtil.buildUrl("cooking")
            } else {
                Log.d(TAG, query)
                URL(query)
            }
            BooksQueryTask().execute(bookUrl)
        }
        catch (e: Exception) {
            Log.d("error", e.message)
        }
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
                val adapter = BooksAdapter(books)
                binding.rvBooks.adapter = adapter
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.book_list_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as android.widget.SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                try {
                    bookUrl = ApiUtil.buildUrl(query);
                    BooksQueryTask().execute(bookUrl);

                } catch (e: Exception){
                    Log.d("Error", e.message);
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
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