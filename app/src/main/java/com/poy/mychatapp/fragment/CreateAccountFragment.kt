package com.poy.mychatapp.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.poy.mychatapp.R
import android.widget.Toast
import com.poy.mychatapp.ActivityCallback
import com.google.firebase.auth.FirebaseAuth
import android.widget.EditText
import com.poy.mychatapp.utils.Utils


class CreateAccountFragment : Fragment() {

    /** UI Components  */
    private var mUsername:EditText? = null
    private var mPassword:EditText? = null
    private var mEmail:EditText? = null
    private var mProgressView:View? = null
    private var mCreateForm:View? = null

    /** Activity callback  */
    private var mCallback:ActivityCallback? = null

    /** Firebase objects  */
    private var mAuth:FirebaseAuth? = null
//    private FirebaseAuth.AuthStateListener mAuthListener;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_account, container, false)

        mUsername = root.findViewById(R.id.create_account_username) as EditText
        mPassword = root.findViewById(R.id.create_account_password) as EditText
        mEmail = root.findViewById(R.id.create_account_email) as EditText

        mCreateForm = root.findViewById(R.id.create_account_form)
        mProgressView = root.findViewById(R.id.create_account_progress)

        val createButton = root.findViewById(R.id.create_account_button) as Button
        createButton.setOnClickListener(View.OnClickListener { createAccount() })

        mAuth = FirebaseAuth.getInstance()

        return root
    }



    /**
 * Create a instance of this fragment
 *
 * @return fragment instance
 */
    companion object {
        fun newInstance():CreateAccountFragment {
            return CreateAccountFragment()
        }
    }
     /// Lifecycle methods


    override fun onAttach(context:Context?) {
        super.onAttach(context)
        mCallback = context as ActivityCallback?
    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
    }

    /// Private methods

    private fun createAccount() {
        showProgress(true)

        val email = mEmail!!.text.toString()
        val password = mPassword!!.text.toString()

        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity!!
        ) { task ->
            if (!task.isSuccessful) {
                Toast.makeText(context, R.string.error_create_account, Toast.LENGTH_SHORT).show()
            } else {
                Utils.saveLocalUser(
                    context!!,
                    mUsername!!.text.toString(),
                    mEmail!!.text.toString(),
                    task.result!!.user.uid
                )

                mCallback!!.openChat()
            }

            showProgress(false)
            Utils.closeKeyboard(context!!, mEmail!!)
        }
            }

            private fun showProgress(show:Boolean) {
                mProgressView!!.visibility = if (show) View.VISIBLE else View.GONE
                mCreateForm!!.visibility = if (show) View.GONE else View.VISIBLE
        }
}
