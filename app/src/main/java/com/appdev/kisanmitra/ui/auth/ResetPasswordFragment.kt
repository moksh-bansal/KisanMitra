package com.appdev.kisanmitra.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.appdev.kisanmitra.databinding.FragmentResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        binding.btnReset.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(requireContext(),
                    "Enter your email",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(),
                            "Reset email sent",
                            Toast.LENGTH_SHORT).show()

                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(requireContext(),
                            task.exception?.message ?: "Error occurred",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}