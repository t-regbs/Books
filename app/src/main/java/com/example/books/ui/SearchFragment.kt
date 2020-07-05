package com.example.books.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.books.R
import com.example.books.util.SpUtil
import com.example.books.databinding.FragmentSearchBinding
import androidx.navigation.fragment.findNavController

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

//        viewModel = ViewModelProvider(this,
//                Injection.provideViewModelFactory(requireContext())).get(SearchFragmentViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearch.setOnClickListener {
            val title = binding.etTitle.text.toString().trim { it <= ' ' }
            val author = binding.etAuthor.text.toString().trim { it <= ' ' }
            val publisher = binding.etPublisher.text.toString().trim { it <= ' ' }
            val isbn = binding.etIsbn.text.toString().trim { it <= ' ' }
            if (title.isEmpty() && author.isEmpty() && publisher.isEmpty() && isbn.isEmpty()) {
                val message = getString(R.string.no_search_data)
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            } else {
                val context: Context = requireContext()
                // Shared Preference
                var position = SpUtil.getPreferenceInt(context, SpUtil.POSITION)
                if (position == 0 || position == 5) {
                    position = 1
                } else {
                    position++
                }
                val key = SpUtil.QUERY + position
                val value = "$title,$author,$publisher,$isbn"
                SpUtil.setPreferenceString(context, key, value)
                SpUtil.setPreferenceInt(context, SpUtil.POSITION, position)

                val action = SearchFragmentDirections.actionSearchFragmentToBooklistDest(value)
                findNavController().navigate(action)
            }
        }
    }
}