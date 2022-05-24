package be.uhasselt.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import be.uhasselt.app.R
import be.uhasselt.app.databinding.LoginFragmentBinding
import be.uhasselt.app.net.Appwrite
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment(R.layout.login_fragment) {

    private lateinit var binding: LoginFragmentBinding
    private lateinit var appwrite: Appwrite

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(layoutInflater)

        appwriteSetup()

        binding.buttonLogin.setOnClickListener(this::login)
        binding.buttonGoToCreateAccount.setOnClickListener(this::goToCreateAccount)
        binding.buttonBypassLogin.setOnClickListener(this::byPassLogin)

        return binding.root
    }

    private fun login(view: View) {
        val email = binding.editTextRegisterEmailAddress.text.toString()
        val password = binding.editTextRegisterPassword.text.toString()

        binding.editTextRegisterEmailAddress.onEditorAction(EditorInfo.IME_ACTION_DONE)
        binding.editTextRegisterPassword.onEditorAction(EditorInfo.IME_ACTION_DONE)

        if (email.isEmpty()) {
            msg("email is required", view)
            return
        } else if (password.isEmpty()) {
            msg("password can't be empty", view)
            return
        } else {
            msg("loading...", view)
        }
        appwrite.createSession(email, password)
    }

    private fun appwriteSetup() {
        appwrite = Appwrite { isSuccess, error, _ ->
            if (isSuccess) {
                msg("success", requireView())
                findNavController().navigate(R.id.action_login_fragment_to_main_fragment)
            } else {
                msg(error!!, requireView())
            }
        }
        appwrite.createClient(requireContext())
    }

    private fun goToCreateAccount(view: View) {
        findNavController().navigate(R.id.action_login_fragment_to_register_fragment)
    }

    private fun byPassLogin(view: View) {
        findNavController().navigate(R.id.action_login_fragment_to_main_fragment)
    }

    private fun msg(text: String, view: View) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }
}
