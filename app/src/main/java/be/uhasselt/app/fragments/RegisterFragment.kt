package be.uhasselt.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import be.uhasselt.app.R
import be.uhasselt.app.databinding.RegisterFragmentBinding
import com.google.android.material.snackbar.Snackbar
import io.appwrite.Client
import io.appwrite.services.Account
import kotlinx.coroutines.*

class RegisterFragment : Fragment(R.layout.register_fragment) {

    private lateinit var binding: RegisterFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterFragmentBinding.inflate(layoutInflater)

        binding.registerButtonCreateAccount.setOnClickListener(this::createAccount)

        return binding.root
    }

    private fun createAccount(view: View) {
        val userId = binding.editTextRegisterUserName.text.toString()
        val email = binding.editTextRegisterEmailAddress.text.toString()
        val password = binding.editTextRegisterPassword.text.toString()
        val name = binding.editTextRegisterFullName.text.toString()

        if (name.isEmpty()) {
            msg("please fill in your full name", view)
            return
        } else if (userId.isEmpty()) {
            msg("name is required", view)
            return
        } else if (email.isEmpty()) {
            msg("email is required", view)
            return
        } else if (password.isEmpty()) {
            msg("password can't be empty", view)
            return
        }

        val client = Client(requireContext())
            .setEndpoint("https://appwrite.lowie.xyz/v1") // Your API Endpoint
            .setProject("627bac3c2508bec32bc2") // Your project ID
            .setSelfSigned(true)

        val account = Account(client)

        GlobalScope.launch {
            account.create(
                userId = userId,
                email = email,
                password = password,
            )
        }
        findNavController().navigate(R.id.action_register_fragment_to_login_fragment)
    }

    private fun msg(text: String, view: View) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }
}
