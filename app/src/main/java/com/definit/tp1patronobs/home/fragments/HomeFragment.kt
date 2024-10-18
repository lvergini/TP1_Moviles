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
    }

}