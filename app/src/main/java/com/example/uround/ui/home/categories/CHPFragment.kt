package com.example.uround.ui.home.categories

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.uround.MainActivity
import com.example.uround.databinding.FragmentChpBinding
import com.example.uround.databinding.FragmentHomeBinding
import com.example.uround.ui.home.HomeViewModel

class CHPFragment : Fragment() {

    private var _binding: FragmentChpBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(HomeViewModel::class.java)

        _binding = FragmentChpBinding.inflate(inflater, container, false)
        val root: View = binding.root

        println("\n\n\\n\n\n\nn\n\n\n\n\n\n")

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}