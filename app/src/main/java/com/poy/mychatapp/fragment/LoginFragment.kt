package com.poy.mychatapp.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.poy.mychatapp.R
import com.google.firebase.auth.FirebaseAuth
import android.widget.EditText
import android.widget.AutoCompleteTextView
import android.widget.Toast
import android.text.TextUtils
import android.widget.Button
import com.poy.mychatapp.ActivityCallback
import com.poy.mychatapp.utils.Constants
import com.poy.mychatapp.utils.Utils


class LoginFragment : Fragment() {

    /** UI Components  */
    private var mEmail: AutoCompleteTextView? = null
    private var mPassword: EditText? = null
    private var mProgressView: View? = null
    private var mLoginFormView: View? = null

    /** Activity callback  */
    private var mCallback: ActivityCallback? = null

    /** Firebase objects  */
    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_login, container, false)

        mEmail = root.findViewById(R.id.username) as AutoCompleteTextView
        mPassword = root.findViewById(R.id.password) as EditText

        val signInButton = root.findViewById(R.id.sign_in_button) as Button
        signInButton.setOnClickListener {
            Utils.closeKeyboard(context!!, signInButton)
            attemptLogin()
        }

        val createAccount = root.findViewById(R.id.create_account_button) as Button
        createAccount.setOnClickListener {
            Utils.closeKeyboard(context!!, createAccount)
            mCallback?.openCreateAccount()
        }

        mLoginFormView = root.findViewById(R.id.login_form)
        mProgressView = root.findViewById(R.id.login_progress)

        mAuth = FirebaseAuth.getInstance()
        Utils.closeKeyboard(context!!, mEmail!!)

        return root
    }

    companion object {
        fun newInstance():LoginFragment {
            return LoginFragment()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCallback = context as ActivityCallback?
    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
    }

    /// Private methods

    private fun attemptLogin() {

        // Reset errors.
        mEmail?.error = null
        mPassword?.error = null

        // Store values at the time of the login attempt.
        val username = mEmail?.text.toString()
        val password = mPassword?.text.toString()

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mEmail?.error = getString(R.string.error_empty)
            mEmail?.requestFocus()
            return
        }

        if (TextUtils.isEmpty(password)) {
            mPassword?.error = getString(R.string.error_password)
            mPassword?.requestFocus()
            return
        }

        login()
    }

    private fun login() {
        showProgress(true)

        val email = mEmail?.text.toString()
        val password = mPassword?.text.toString()

        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnSuccessListener { authResult ->
                if (mCallback != null) {
                    Utils.saveLocalUser(
                        context!!, Constants.DEFAULT_USER,
                        mEmail?.text.toString(),
                        authResult.user.uid
                    )

                    mCallback?.openChat()
                }
            }?.addOnFailureListener { e ->
                showProgress(false)
                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    private fun showProgress(show: Boolean) {
        mProgressView?.visibility = if (show) View.VISIBLE else View.GONE
        mLoginFormView?.visibility = if (show) View.GONE else View.VISIBLE
    }
}
