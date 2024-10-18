package com.definit.tp1patronobs.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.definit.tp1patronobs.R
import com.definit.tp1patronobs.databinding.FragmentLeidosBinding
import com.definit.tp1patronobs.home.SharedViewModel
import com.definit.tp1patronobs.models.Book
import com.google.gson.Gson
class LeidosFragment : Fragment() {
    private lateinit var binding: FragmentLeidosBinding
    private lateinit var bookAdapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeidosBinding.inflate(inflater, container, false)

        // Configurar el RecyclerView
        bookAdapter = BookAdapter(mutableListOf())
        binding.rvBooks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBooks.adapter = bookAdapter

        // Agregar libros de prueba a la lista
        val sampleBooks = listOf(
            Book(id = "1", title = "Fahrenheit 451", author = "Ray Bradbury", genre = "Ciencia Ficción", coverUrl = R.drawable.cover_book_bradbury_fahrenheit),
            Book(id = "2", title = "1984", author = "George Orwell", genre = "Ciencia Ficción", coverUrl = R.drawable.cover_book_orwell_1984),
            Book(id = "3", title = "Cien años de soledad", author = "Gabriel García Márquez", genre = "Realismo mágico", coverUrl = R.drawable.cover_book_marquez_soledad)
        )

        // Actualizar el adaptador con los libros de prueba
        bookAdapter.updateBooks(sampleBooks.toMutableList())

        return binding.root
    }
}