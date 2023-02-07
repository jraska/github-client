package com.jraska.github.client.identity.google

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jraska.github.client.core.android.HasViewModelFactory
import com.jraska.github.client.identity.R


class GoogleSignInFragment : Fragment() {
  private val viewModel: GoogleSignInViewModel by lazy { viewModel() }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    val view = inflater.inflate(R.layout.identity_google_login, container, false)

    val signInButton = view.findViewById<View>(R.id.google_sign_in_button)
    signInButton.setOnClickListener { viewModel.onLoginButtonClick() }

    val signOutButton = view.findViewById<View>(R.id.google_sign_out_button)
    signOutButton.setOnClickListener { viewModel.onLogoutButtonClick() }

    val disconnectButton = view.findViewById<View>(R.id.google_disconnect_button)
    disconnectButton.setOnClickListener { viewModel.onDisconnectButtonClick() }

    viewModel.viewState().observe(viewLifecycleOwner) {
      signOutButton.isVisible = it.signOutButtonVisible
      signInButton.isVisible = it.signInButtonVisible
      disconnectButton.isVisible = it.disconnectButtonVisible
    }

    return view
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
    if (data != null && requestCode == GoogleSignInRepository.REQUEST_CODE_SIGN_IN) {
      viewModel.onGoogleSignInResultReceived(data)
    }
  }

  private fun viewModel(): GoogleSignInViewModel {
    val factory = (requireActivity().application as HasViewModelFactory).factory()
    return ViewModelProvider(this, factory)[GoogleSignInViewModel::class.java]
  }

  object Factory : GoogleSignInFactory {
    override fun signInFragment(): Fragment {
      return GoogleSignInFragment()
    }
  }
}
