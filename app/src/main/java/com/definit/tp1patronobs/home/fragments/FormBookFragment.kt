package com.definit.tp1patronobs.home.fragments
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.definit.tp1patronobs.R
import com.definit.tp1patronobs.databinding.FragmentFormBookBinding
import com.definit.tp1patronobs.home.SharedViewModel
import com.definit.tp1patronobs.models.Book

class FormBookFragment : Fragment() {
    private lateinit var binding: FragmentFormBookBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentFormBookBinding.inflate(inflater,container,false)

        binding.bSendBook.setOnClickListener {
            val resourceId = resources.getIdentifier(binding.etCoverUrl.text.toString(), "drawable", requireContext().packageName)

            val book=Book(
                id=binding.etBookId.text.toString(),
                title = binding.etBookTitle.text.toString(),
                author = binding.etBookWriter.text.toString(),
                genre = binding.etGender.text.toString(),
                coverUrl = resourceId
            )
            /*val bundle = Bundle().apply {
                putParcelable("book", book)
            }*/
            sharedViewModel.addBook(book)
            //findNavController().navigate(R.id.action_formBookFragment_to_leidosFragment, bundle)
            findNavController().navigate(R.id.action_formBookFragment_to_leidosFragment)

        }
        return binding.root
    }
}
