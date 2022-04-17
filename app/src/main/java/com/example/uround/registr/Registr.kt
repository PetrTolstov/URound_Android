package com.example.uround.registr

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.example.RegistrMutation
import com.example.type.UserInput
import com.example.uround.MainActivity
import com.example.uround.databinding.RegistrBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



class Registr : AppCompatActivity() {

    private lateinit var binding: RegistrBinding

    private lateinit var emailInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var passwordRepeatInput : EditText
    private lateinit var firstName : EditText
    private lateinit var lastName : EditText
    private lateinit var toLoginBut : Button
    private lateinit var registrBut : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            supportActionBar?.hide()
            binding = RegistrBinding.inflate(layoutInflater)
            setContentView(binding.root)

        emailInput = binding.loginEditText
        passwordInput = binding.passwordEditText
        passwordRepeatInput = binding.passwordRepeatEditText
        firstName = binding.firstName
        lastName = binding.lastName
        toLoginBut = binding.toLoginBut
        registrBut = binding.RegistrBut

        val sps: SharedPreferences = getSharedPreferences("login", MODE_PRIVATE)

        val listFields : List<EditText> = listOf<EditText>(emailInput, passwordInput, passwordRepeatInput, firstName, lastName)

        binding.toLoginBut.setOnClickListener {
            val msgIntent = Intent(this@Registr, Registr::class.java)
            startActivity(msgIntent)
            finishAfterTransition()
        }

        binding.RegistrBut.setOnClickListener {
            if (isOnline(applicationContext)) {
                if (checkFields(listFields)) {
                    GlobalScope.launch {
                        val emailStr = emailInput.text.toString()
                        val passwordStr = passwordInput.text.toString()
                        val firstNameStr = firstName.text.toString()
                        val lastNameStr = lastName.text.toString()

                        val user : UserInput = UserInput(emailStr, passwordStr, firstNameStr, lastNameStr)

                        val (isLogged, errorMsg) = isLogin(user)

                        if (isLogged) {
                            val editor = sps.edit()
                            editor.putString("login", emailStr)
                            editor.putString("password", passwordStr)
                            editor.commit()

                            val msgIntent = Intent(this@Registr, MainActivity::class.java)
                            startActivity(msgIntent)
                            finishAfterTransition()
                        } else {
                            this@Registr.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(
                                    this@Registr,
                                    errorMsg,
                                    Toast.LENGTH_LONG
                                ).show()
                                passwordInput.setText("")
                            })
                        }
                    }
                }
            } else {
                Toast.makeText(this@Registr, "No Internet", Toast.LENGTH_LONG).show()
            }
        }

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

    fun checkFields(listFields: List<EditText>): Boolean{
        var toastMessage = ""
        for (field in listFields){
            if(TextUtils.isEmpty(field.text)){
                toastMessage += "Please write your " + field.getHint() + " \n"
                field.setHintTextColor(Color.RED)
            }else{
                field.setHintTextColor(Color.LTGRAY)
            }
        }

        if (!TextUtils.isEmpty(toastMessage)){
            Toast.makeText(this@Registr, toastMessage, Toast.LENGTH_LONG).show()
            return false
        } else if(listFields[1].text != listFields[2].text){
            Toast.makeText(this@Registr, "Passwords doesn't match", Toast.LENGTH_LONG).show()
            listFields[1].setText("")
            listFields[2].setText("")
            listFields[1].setHintTextColor(Color.RED)
            listFields[2].setHintTextColor(Color.RED)
            return false
        }
        return true
    }

    suspend fun isLogin(user : UserInput): Pair<Boolean, String?> {
        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://uround-server.herokuapp.com")
            .build()

        val response = apolloClient.mutation(RegistrMutation(user)).execute()

        return Pair(!response.data!!.addUser.message.isError, response.data!!.addUser.message.description)
    }



}

