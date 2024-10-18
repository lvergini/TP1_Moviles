package com.definit.tp1patronobs.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.definit.tp1patronobs.databinding.FragmentProfileBinding
import com.definit.tp1patronobs.home.SharedViewModel
import androidx.fragment.app.activityViewModels
import com.definit.tp1patronobs.R

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Configurar la vista con la información del usuario
        sharedViewModel.user.observe(viewLifecycleOwner) { user ->
                binding.tvProfileUsername.text = getString(R.string.profile_username, user.username)
                binding.tvProfileEmail.text = getString(R.string.profile_email, user.email)
                binding.tvProfileGender.text = getString(R.string.profile_gender, user.gender)

        }

        return binding.root
    }
}