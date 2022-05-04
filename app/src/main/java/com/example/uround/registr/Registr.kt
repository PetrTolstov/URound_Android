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
import com.example.RegistrMutation
import com.example.type.UserInput
import com.example.uround.MainActivity
import com.example.uround.auth.Auth
import com.example.uround.databinding.RegistrBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



class Registr : AppCompatActivity() {

    private lateinit var binding: RegistrBinding

    private lateinit var usernameInput: EditText
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

        usernameInput = binding.usernameEditText
        emailInput = binding.loginEditText
        passwordInput = binding.passwordEditText
        passwordRepeatInput = binding.passwordRepeatEditText
        firstName = binding.firstName
        lastName = binding.lastName
        toLoginBut = binding.toLoginBut
        registrBut = binding.RegistrBut

        val sps: SharedPreferences = getSharedPreferences("login", MODE_PRIVATE)

        val listFields : List<EditText> = listOf(usernameInput,emailInput, passwordInput, passwordRepeatInput, firstName, lastName)

        binding.toLoginBut.setOnClickListener {
            val msgIntent = Intent(this@Registr, Auth::class.java)
            startActivity(msgIntent)
            finishAfterTransition()
        }

        binding.RegistrBut.setOnClickListener {
            if (isOnline(applicationContext)) {
                if (checkFields(listFields)) {
                    GlobalScope.launch {
                        val username = usernameInput.text.toString()
                        val emailStr = emailInput.text.toString()
                        val passwordStr = passwordInput.text.toString()
                        val firstNameStr = firstName.text.toString()
                        val lastNameStr = lastName.text.toString()

                        val user : UserInput = UserInput(username, emailStr, passwordStr, firstNameStr, lastNameStr)

                        val response = isLogin(user)

                        if (!response.message.isError) {
                            val editor = sps.edit()
                            editor.putString("login", username)
                            editor.putString("password", passwordStr)
                            editor.commit()

                            val b = Bundle()
                            b.putString("id", response.userInfo!!._id)
                            b.putString("username", response.userInfo.username)
                            b.putString("email", response.userInfo.email)
                            b.putString("password", passwordStr)
                            b.putString("hashedPassword", response.userInfo.hashedPassword,)
                            b.putString("firstName", response.userInfo.firstName!!)
                            b.putString("lastName",  response.userInfo.lastName!!)

                            val msgIntent = Intent(this@Registr, MainActivity::class.java)

                            msgIntent.putExtras(b)
                            msgIntent.putExtra(MainActivity.USER_L, b)
                            startActivity(msgIntent)
                        } else {
                            this@Registr.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(
                                    this@Registr,
                                    response.message.description,
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
                toastMessage += "Please write your " + field.hint + " \n"
                field.setHintTextColor(Color.RED)
            }else{
                field.setHintTextColor(Color.LTGRAY)
            }
        }

        if (!TextUtils.isEmpty(toastMessage)){
            Toast.makeText(this@Registr, toastMessage, Toast.LENGTH_LONG).show()
            return false
        } else if(listFields[2].text.toString() != listFields[3].text.toString()){
            Toast.makeText(this@Registr, "Passwords doesn't match", Toast.LENGTH_LONG).show()
            listFields[2].setText("")
            listFields[3].setText("")
            listFields[2].setHintTextColor(Color.RED)
            listFields[3].setHintTextColor(Color.RED)
            return false
        }
        return true
    }

    suspend fun isLogin(user : UserInput): RegistrMutation.AddUser {
        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://uround-server.herokuapp.com")
            .build()

        val response = apolloClient.mutation(RegistrMutation(user)).execute()

        return response.data!!.addUser
    }



}

