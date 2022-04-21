package com.example.uround.ui.profile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.uround.MainActivity
import com.example.uround.auth.Auth
import com.example.uround.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                ProfileViewModel::class.java
            )

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sps: SharedPreferences = activity!!.getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)

        val textView: TextView = binding.textProfile

        val email = binding.emailTextView
        val name = binding.nameTextView
        val hashedPassword = binding.hashedPasswordTextView
        val fullName = MainActivity.USER_INFO.getString("firstName") + " " + MainActivity.USER_INFO.getString("lastName")
        email.text = MainActivity.USER_INFO.getString("email")
        name.text = fullName
        hashedPassword.text =  MainActivity.USER_INFO.getString("hashedPassword")


        binding.logOutBut.setOnClickListener {
            val editor = sps.edit()
            editor.putString("login", null)
            editor.putString("password", null)
            editor.commit()

            val msgIntent = Intent(activity, Auth::class.java)
            startActivity(msgIntent)

        }

        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}