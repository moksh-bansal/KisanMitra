package com.appdev.kisanmitra.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.appdev.kisanmitra.R
import com.appdev.kisanmitra.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        setupListeners()

        return binding.root
    }

    private fun setupListeners() {

        binding.btnSignup.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(),
                    "Please fill all fields",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signupUser(email, password)
        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signup_to_login)
        }
    }

    private fun signupUser(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    Toast.makeText(requireContext(),
                        "Signup successful. Please login.",
                        Toast.LENGTH_SHORT).show()

                    findNavController().navigate(R.id.action_signup_to_login)

                } else {
                    Toast.makeText(
                        requireContext(),
                        task.exception?.message ?: "Signup failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}