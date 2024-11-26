package com.my.search.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.my.search.MainActivity
import com.my.search.R
import java.lang.ref.WeakReference


class SearchBarFragment : Fragment() {

    private lateinit var backIButton: ImageButton
    private lateinit var searchIButton: ImageButton
    private lateinit var searchEditText: EditText
    private lateinit var clearIButton: ImageButton
    private lateinit var searchTextView: TextView
    private val viewModel by lazy { ViewModelProvider(this).get(SearchPageViewModel::class.java) }
    private lateinit var mainActivity: WeakReference<MainActivity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.search_bar_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backIButton = view.findViewById(R.id.bar_back_image_button)
        searchIButton = view.findViewById(R.id.bar_search_image_button)
        searchEditText = view.findViewById(R.id.bar_search_edit_text)
        clearIButton = view.findViewById(R.id.bar_clear_image_button)
        searchTextView = view.findViewById(R.id.bar_search_text_view)

        backIButton.setOnClickListener { back() }
        searchIButton.setOnClickListener { searchEditTextFocusWithSoftInput() }
        clearIButton.setOnClickListener { searchEditText.text.clear() }
        searchTextView.setOnClickListener { search() }

        mainActivity = WeakReference(activity as? MainActivity)
    }

    private fun back() {
        activity?.finish()
    }

    private fun search() {
        mainActivity.get()?.search()
    }

    private fun searchEditTextFocusWithSoftInput() {
        if(activity == null) return
        searchEditText.requestFocus()
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(searchEditText, 0)
    }
}