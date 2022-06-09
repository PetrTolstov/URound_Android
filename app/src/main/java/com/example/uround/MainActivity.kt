package com.example.uround

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.apollographql.apollo3.ApolloClient
import com.example.GetAllUsersQuery
import com.example.uround.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        const val USER_L = "User info"
        lateinit var USER_INFO : Bundle
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        val actionBar: ActionBar? = supportActionBar
        val colorDrawable =  ColorDrawable(Color.parseColor("#FFFFFF"));
        if (actionBar != null) {
            actionBar.setCustomView(R.layout.custom_action_bar)
            actionBar.setBackgroundDrawable(colorDrawable)
        }

        //getSupportActionBar().setElevation(0);
        //getSupportActionBar().setElevation(0);
        val view = supportActionBar!!.customView


        val b = intent.extras
        if (b != null) {
            USER_INFO = b
        }

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_map, R.id.navigation_messages, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        GlobalScope.launch {
            printGraphQLData()
        }
    }



    suspend fun printGraphQLData() {
        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://uround-server.herokuapp.com")
            .build()

        val response = apolloClient.query(GetAllUsersQuery()).execute()
        println(response.data!!.getAllUsers)


    }


}