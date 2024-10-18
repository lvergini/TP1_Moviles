package com.definit.tp1patronobs.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.definit.tp1patronobs.R
import com.definit.tp1patronobs.databinding.FragmentPendientesBinding

class PendientesFragment : Fragment() {
    private lateinit var binding: FragmentPendientesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentPendientesBinding.inflate(inflater,container,false)
        return binding.root
    }

}