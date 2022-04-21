package com.example.uround.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo3.ApolloClient
import com.example.LoginQuery
import com.example.uround.MainActivity
import com.example.uround.databinding.AuthBinding
import com.example.uround.registr.Registr
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Auth : AppCompatActivity() {

    private lateinit var binding: AuthBinding

    private lateinit var loginInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var loginBut : Button
    private lateinit var logo  : ImageView
    private lateinit var progressBar : ProgressBar
    private lateinit var toRegistrBut : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        binding = AuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sps: SharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        val login = sps.getString("login", null)
        val password = sps.getString("password", null)


        loginInput = binding.loginEditText
        passwordInput = binding.passwordEditText
        loginBut = binding.loginBut
        logo = binding.logoImageView
        progressBar = binding.progressBar
        toRegistrBut = binding.toRegistrBut
        toInvisible()

        if(isOnline(applicationContext)){
            GlobalScope.launch {
                if (login is String && password is String) {
                    val response = isLogin(login, password)

                    if (response.isLoggedIn){
                        val b = Bundle()
                        b.putString("id", response.userInfo!!._id)
                        b.putString("email", response.userInfo.email)
                        b.putString("password", password)
                        b.putString("hashedPassword", response.userInfo.hashedPassword,)
                        b.putString("firstName", response.userInfo.firstName!!)
                        b.putString("lastName",  response.userInfo.firstName!!)

                        val msgIntent = Intent(this@Auth, MainActivity::class.java)

                        msgIntent.putExtras(b)
                        msgIntent.putExtra(MainActivity.USER_L, b)
                        startActivity(msgIntent)

                        finishAfterTransition()
                    }else{

                        this@Auth.runOnUiThread(java.lang.Runnable {

                            toVisible()

                            Toast.makeText(
                                this@Auth,
                                response.message.description,
                                Toast.LENGTH_LONG
                            ).show()
                            passwordInput.setText("")
                            if(response.message.description == "No such user"){
                                loginInput.setText("")
                            }
                        })
                    }
                }else{
                    this@Auth.runOnUiThread(java.lang.Runnable {
                        toVisible()
                    })
                }
        }}else {
            Toast.makeText(this@Auth, "No Internet", Toast.LENGTH_LONG).show()
        }


        binding.toRegistrBut.setOnClickListener {
            val msgIntent = Intent(this@Auth, Registr::class.java)
            startActivity(msgIntent)
            finishAfterTransition()
        }

        binding.loginBut.setOnClickListener {
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
                    toInvisible()
                    GlobalScope.launch {
                        val loginStr = loginInput.text.toString()
                        val passwordStr = passwordInput.text.toString()

                        val response = isLogin(loginStr, passwordStr)

                        if (response.isLoggedIn) {
                            val editor = sps.edit()
                            editor.putString("login", loginStr)
                            editor.putString("password", passwordStr)
                            editor.commit()

                            val b = Bundle()
                            b.putString("id", response.userInfo!!._id)
                            b.putString("email", response.userInfo.email)
                            b.putString("password", passwordStr)
                            b.putString("hashedPassword", response.userInfo.hashedPassword,)
                            b.putString("firstName", response.userInfo.firstName!!)
                            b.putString("lastName",  response.userInfo.lastName!!)

                            val msgIntent = Intent(this@Auth, MainActivity::class.java)

                            msgIntent.putExtras(b)
                            msgIntent.putExtra(MainActivity.USER_L, b)
                            startActivity(msgIntent)

                            finishAfterTransition()
                        } else {
                            this@Auth.runOnUiThread(java.lang.Runnable {
                                toVisible()
                                Toast.makeText(
                                    this@Auth,
                                    response.message.description,
                                    Toast.LENGTH_LONG
                                ).show()
                                passwordInput.setText("")
                                if(response.message.description == "No such user"){
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

    suspend fun isLogin(login: String, password: String): LoginQuery.Login {
            val apolloClient = ApolloClient.Builder()
                .serverUrl("https://uround-server.herokuapp.com")
                .build()

            val response = apolloClient.query(LoginQuery(login, password)).execute()

            return  response.data!!.login
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

    fun toVisible(){
        loginInput.visibility = View.VISIBLE
        passwordInput.visibility = View.VISIBLE
        loginBut.visibility = View.VISIBLE
        toRegistrBut.visibility = View.VISIBLE

        logo.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    fun toInvisible(){
        loginInput.visibility = View.INVISIBLE
        passwordInput.visibility = View.INVISIBLE
        loginBut.visibility = View.INVISIBLE
        toRegistrBut.visibility = View.INVISIBLE

        logo.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
    }
}
