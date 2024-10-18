package com.definit.tp1patronobs.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.definit.tp1patronobs.R
import com.definit.tp1patronobs.databinding.FragmentHomeBinding
import com.definit.tp1patronobs.home.SharedViewModel


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val sharedViewModel: SharedViewModel by activityViewModels() // Usar el ViewModel compartido

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.tvBooksRead.text = getString(R.string.books_read, 5)
        binding.tvBooksReading.text = getString(R.string.books_reading, 6)
        binding.tvBooksPending.text = getString(R.string.books_pending, 3)
        // Observar los libros del usuario
        val userBooks = sharedViewModel.getUserBooks()
        //binding.tvWelcomeHome.text =
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //observar los cambios del usuario en el ViewModel

        sharedViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.tvWelcomeMessage.text = getString(R.string.welcome_msg, user.username)

        }

        // obtener libros de muestra para el usuario actual
        //sharedViewModel.getSampleUserBooks()

        /*// Observar los libros del usuario para actualizar la interfaz
        sharedViewModel.userBooks.observe(viewLifecycleOwner) { books ->
            //val readBooksCount = books.count { it.status == ReadingStatus.READ }
            //val readingBooksCount = books.count { it.status == ReadingStatus.READING }
            //val pendingBooksCount = books.count { it.status == ReadingStatus.PENDING }

            // Actualizar los textos de las tarjetas con las cantidades correspondientes
            binding.tvBooksRead.text = getString(R.string.books_read, 5)
            binding.tvBooksReading.text = getString(R.string.books_reading, 6)
            binding.tvBooksPending.text = getString(R.string.books_pending, 3)
        }*/

    }

}