package be.uhasselt.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import be.uhasselt.app.R
import be.uhasselt.app.databinding.LoginFragmentBinding
import com.google.android.material.snackbar.Snackbar
import io.appwrite.Client
import io.appwrite.services.Account
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.login_fragment) {

    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(layoutInflater)

        binding.buttonLogin.setOnClickListener(this::login)
        binding.buttonGoToCreateAccount.setOnClickListener(this::goToCreateAccount)

        return binding.root
    }

    private fun login(view: View) {
        val email = binding.editTextRegisterEmailAddress.text.toString()
        val password = binding.editTextRegisterPassword.text.toString()

        if (email.isEmpty()) {
            msg("email is required", view)
            return
        } else if (password.isEmpty()) {
            msg("password can't be empty", view)
            return
        }

        val client = Client(requireContext())
            .setEndpoint("https://appwrite.lowie.xyz/v1") // Your API Endpoint
            .setProject("627bac3c2508bec32bc2") // Your project ID

        val account = Account(client)

        GlobalScope.launch {
            account.createSession(
                email = email,
                password = password
            )
        }
        findNavController().navigate(R.id.action_login_fragment_to_first_fragment)
    }

    private fun goToCreateAccount(view: View) {
        findNavController().navigate(R.id.action_login_fragment_to_register_fragment)
    }

    private fun msg(text: String, view: View) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }
}
