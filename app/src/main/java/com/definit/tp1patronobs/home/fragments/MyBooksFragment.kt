package com.definit.tp1patronobs.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.definit.tp1patronobs.R
import com.definit.tp1patronobs.databinding.FragmentMyBooksBinding

class MyBooksFragment : Fragment() {
    private lateinit var binding: FragmentMyBooksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentMyBooksBinding.inflate(inflater,container,false)

        binding.ibleidos.setOnClickListener{
            replaceFragment(FormBookFragment())
        }
        binding.tvleidos.setOnClickListener{
            replaceFragment(LeidosFragment())
        }

        binding.ibenprogreso.setOnClickListener{
            replaceFragment(FormBookFragment())
        }
        binding.tvenprogreso.setOnClickListener {
            replaceFragment(LeyendoFragment())
        }

        binding.ibpendientes.setOnClickListener{
            replaceFragment(FormBookFragment())
        }
        binding.tvpendientes.setOnClickListener {
            replaceFragment(PendientesFragment())
        }

        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment).addToBackStack(null).commit()

    }

}