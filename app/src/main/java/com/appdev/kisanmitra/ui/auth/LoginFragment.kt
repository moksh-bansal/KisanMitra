package com.appdev.kisanmitra.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.appdev.kisanmitra.R
import com.appdev.kisanmitra.databinding.FragmentLoginBinding
import com.appdev.kisanmitra.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        setupListeners()

        return binding.root
    }

    private fun setupListeners() {

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(),
                    "Please fill all fields",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        binding.tvSignup.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signup)
        }

        binding.tvReset.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_reset)
        }
    }

    private fun loginUser(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    startActivity(
                        Intent(requireContext(), MainActivity::class.java)
                    )
                    requireActivity().finish()
                } else {
                    Toast.makeText(
                        requireContext(),
                        task.exception?.message ?: "Login failed",
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