package com.definit.tp1patronobs.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.definit.tp1patronobs.R
import com.definit.tp1patronobs.databinding.FragmentLeyendoBinding

class LeyendoFragment : Fragment() {

    private lateinit var binding: FragmentLeyendoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentLeyendoBinding.inflate(inflater,container,false)
        return binding.root
    }

}