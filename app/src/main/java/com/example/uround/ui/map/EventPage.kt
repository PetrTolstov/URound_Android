package com.example.uround.ui.map

import android.app.PendingIntent.getActivity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.metrics.Event
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.apollographql.apollo3.ApolloClient
import com.example.GetAllUsersQuery
import com.example.uround.MainActivity.Companion.USER_INFO
import com.example.uround.R
import com.example.uround.databinding.ActivityMainBinding
import com.example.uround.databinding.AuthBinding
import com.example.uround.databinding.FragmentAllnewsBinding
import com.example.uround.databinding.FragmentEventPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EventPage  : AppCompatActivity(){

    private lateinit var binding: FragmentEventPageBinding
    companion object {
        const val DATA_L = "User info"
        lateinit var DATA : Bundle
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentEventPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val b = intent.extras
        if (b != null) {
            DATA = b
            binding.eventPageTEST.setText(DATA.getString("id"))
        }



    }





}