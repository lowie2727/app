package be.uhasselt.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import be.uhasselt.app.R
import be.uhasselt.app.databinding.LoginFragmentBinding

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
        findNavController().navigate(R.id.action_login_fragment_to_first_fragment)
    }

    private fun goToCreateAccount(view: View) {
        findNavController().navigate(R.id.action_login_fragment_to_register_fragment)
    }
}
