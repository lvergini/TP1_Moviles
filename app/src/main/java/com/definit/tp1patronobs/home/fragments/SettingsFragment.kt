package com.definit.tp1patronobs.home.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.definit.tp1patronobs.databinding.FragmentSettingsBinding
import com.definit.tp1patronobs.home.SharedViewModel
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val themeSwitch: SwitchMaterial = binding.switchDarkMode

        // Obtener el estado del modo oscuro directamente desde SharedPreferences para inicializar el Switch
        val sharedPreferences = requireContext().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        themeSwitch.isChecked = isDarkMode

        // Configurar el observador para actualizar el Switch si hay un cambio real
        sharedViewModel.isDarkMode.observe(viewLifecycleOwner) { isDarkMode ->
            if (themeSwitch.isChecked != isDarkMode) {
                themeSwitch.isChecked = isDarkMode
            }
        }

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Cambiar el tema solo si hay una diferencia entre el estado del switch y el estado almacenado
            if (sharedViewModel.isDarkMode.value != isChecked) {
                sharedViewModel.setDarkMode(isChecked)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
