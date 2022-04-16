package com.example.uround.auth

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.apollographql.apollo3.ApolloClient
import com.example.LoginQuery
import com.example.uround.MainActivity
import com.example.uround.databinding.AuthBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Auth : AppCompatActivity() {

    private lateinit var binding: AuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        binding = AuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginInput = binding.loginEditText
        val passwordInput = binding.passwordEditText
        val loginBut = binding.loginBut
        val logo = binding.logoImageView
        val progressBar = binding.progressBar

        loginInput.visibility = View.INVISIBLE
        passwordInput.visibility = View.INVISIBLE
        loginBut.visibility = View.INVISIBLE

        val sps = getSharedPreferences("login", MODE_PRIVATE)
        val login = sps.getString("login", null)
        val password = sps.getString("password", null)
        println(login)
        if(isOnline(applicationContext)){
            GlobalScope.launch {
                if (login is String && password is String) {
                    val (isLogged, errorMsg) = isLogin(login, password)
                    if (isLogged){
                        val msgIntent = Intent(this@Auth, MainActivity::class.java)
                        startActivity(msgIntent)
                        finishAfterTransition()
                    }else{
                        loginInput.visibility = View.VISIBLE
                        passwordInput.visibility = View.VISIBLE
                        loginBut.visibility = View.VISIBLE
                        logo.visibility = View.INVISIBLE
                        progressBar.visibility = View.INVISIBLE


                        this@Auth.runOnUiThread(java.lang.Runnable {
                            Toast.makeText(
                                this@Auth,
                                errorMsg,
                                Toast.LENGTH_LONG
                            ).show()
                            passwordInput.setText("")
                            if(errorMsg == "No such user"){
                                loginInput.setText("")
                            }
                        })
                    }
                }else{
                    loginInput.visibility = View.VISIBLE
                    passwordInput.visibility = View.VISIBLE
                    loginBut.visibility = View.VISIBLE
                    logo.visibility = View.INVISIBLE
                    progressBar.visibility = View.INVISIBLE
                }
        }}else {
            Toast.makeText(this@Auth, "No Internet", Toast.LENGTH_LONG).show()
        }


        binding.loginBut.setOnClickListener() {
            if (isOnline(applicationContext)) {
                var toastMessage = ""

                if (TextUtils.isEmpty(loginInput.text)) {
                    toastMessage += "Please write your Login\n"
                    loginInput.setHintTextColor(Color.RED)
                } else {
                    passwordInput.setHintTextColor(Color.LTGRAY)
                }

                if (TextUtils.isEmpty(passwordInput.text)) {
                    toastMessage += "Please write your Password\n"
                    passwordInput.setHintTextColor(Color.RED)
                } else {
                    passwordInput.setHintTextColor(Color.LTGRAY)
                }

                if (!TextUtils.isEmpty(toastMessage)) {
                    Toast.makeText(this@Auth, toastMessage, Toast.LENGTH_LONG).show()

                } else {
                    GlobalScope.launch {
                        val loginStr = loginInput.text.toString()
                        val passwordStr = passwordInput.text.toString()

                        val (isLogged, errorMsg) = isLogin(loginStr, passwordStr)
                        if (isLogged) {
                            val editor = sps.edit()
                            editor.putString("login", loginStr)
                            editor.putString("password", passwordStr)
                            editor.commit()

                            val msgIntent = Intent(this@Auth, MainActivity::class.java)
                            startActivity(msgIntent)
                            finishAfterTransition()
                        } else {
                            this@Auth.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(
                                    this@Auth,
                                    errorMsg,
                                    Toast.LENGTH_LONG
                                ).show()
                                passwordInput.setText("")
                                if(errorMsg == "No such user"){
                                    loginInput.setText("")
                                }
                            })
                        }
                    }
                }
            } else {
                Toast.makeText(this@Auth, "No Internet", Toast.LENGTH_LONG).show()
            }
        }


    }

    suspend fun isLogin(login: String, password: String): Pair<Boolean, String?> {
            val apolloClient = ApolloClient.Builder()
                .serverUrl("https://uround-server.herokuapp.com")
                .build()

            val response = apolloClient.query(LoginQuery(login, password)).execute()

            return Pair(response.data!!.login.isLoggedIn, response.data!!.login.message.description)
    }


    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
}
