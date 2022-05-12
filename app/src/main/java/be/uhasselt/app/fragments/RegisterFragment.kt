package be.uhasselt.app.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.RestrictTo
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import be.uhasselt.app.R
import be.uhasselt.app.databinding.RegisterFragmentBinding
import com.google.android.material.snackbar.Snackbar
import be.uhasselt.app.net.Appwrite
import io.appwrite.exceptions.AppwriteException
import kotlinx.coroutines.launch

class RegisterFragment : Fragment(R.layout.register_fragment) {

    private lateinit var binding: RegisterFragmentBinding
    private lateinit var appwrite: Appwrite

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterFragmentBinding.inflate(layoutInflater)

        appwrite = Appwrite()
        appwrite.createClient(this.requireContext())

        binding.registerButtonCreateAccount.setOnClickListener(this::createAccount)

        return binding.root
    }

    private fun createAccount(view: View) {
        val userId = binding.editTextRegisterUserName.text.toString()
        val email = binding.editTextRegisterEmailAddress.text.toString()
        val password = binding.editTextRegisterPassword.text.toString()
        val name = binding.editTextRegisterFullName.text.toString()

        binding.editTextRegisterUserName.onEditorAction(EditorInfo.IME_ACTION_DONE)
        binding.editTextRegisterEmailAddress.onEditorAction(EditorInfo.IME_ACTION_DONE)
        binding.editTextRegisterPassword.onEditorAction(EditorInfo.IME_ACTION_DONE)
        binding.editTextRegisterFullName.onEditorAction(EditorInfo.IME_ACTION_DONE)

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
        } else {
            msg("creating account", view)
        }

        createAccount(userId, email, password, name)
    }

    private fun msg(text: String, view: View) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    private fun createAccount(userId: String, email: String, password: String, name: String) {
        lifecycleScope.launch {
            try {
                appwrite.account.create(
                    userId = userId,
                    email = email,
                    password = password,
                    name = name
                )
                findNavController().navigate(R.id.action_register_fragment_to_login_fragment)
            } catch (e: AppwriteException) {
                msg(e.message.toString(), requireView())
            }
        }
    }
}
