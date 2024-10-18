package com.definit.tp1patronobs.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.definit.tp1patronobs.databinding.FragmentLeidosBinding
import com.definit.tp1patronobs.models.Book
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LeidosFragment : Fragment() {
    private lateinit var binding: FragmentLeidosBinding
    private var bookList= mutableListOf<Book>()
    private lateinit var bookAdapter: BookAdapter
    private val gson=Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentLeidosBinding.inflate(inflater,container,false)

        //config the RecyclerView
        bookAdapter=BookAdapter(bookList)
        binding.rvBooks.layoutManager=LinearLayoutManager(requireContext())
        binding.rvBooks.adapter=bookAdapter

        //add books to the list
        binding.ibleidos

        return binding.root
    }

}