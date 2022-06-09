package com.example.uround.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider

import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController


import androidx.navigation.ui.setupWithNavController
import com.example.uround.R
import com.example.uround.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

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

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = (activity as FragmentActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController


        navView.setupWithNavController(navController)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}