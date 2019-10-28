package com.poy.mychatapp.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.poy.mychatapp.ActivityCallback
import com.poy.mychatapp.R
import com.poy.mychatapp.adapter.ChatAdapter
import com.poy.mychatapp.model.ChatData
import com.poy.mychatapp.utils.Constants
import com.poy.mychatapp.utils.Utils
import java.util.Date

/**
 * Class responsible to be the chat screen of the app
 */
class ChatFragment : Fragment() {

    /** Activity callback  */
    private var mCallback: ActivityCallback? = null

    /** Database instance  */
    private var mReference: DatabaseReference? = null

    /** UI Components  */
    private var mChatInput: EditText? = null
    private var mAdapter: ChatAdapter? = null

    /** Class variables  */
    private var mUsername: String? = null
    private var mUserId: String? = null

    /// Lifecycle methods

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_chat, container, false)

        mChatInput = root.findViewById(R.id.chat_input)
        mChatInput!!.setOnEditorActionListener { textView, i, keyEvent ->
            val data = ChatData()
            data.setMessage(mChatInput!!.text.toString())
            data.setId(mUserId!!)
            data.setName(mUsername!!)

            mReference!!.child(Date().time.toString()).setValue(data)

            closeAndClean()
            true
        }

        val chat = root.findViewById(R.id.chat_message) as RecyclerView
        chat.layoutManager = LinearLayoutManager(getContext())

        mAdapter = ChatAdapter()
        chat.adapter = mAdapter

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        mUsername = Utils.getLocalUsername(getContext()!!)
        mUserId = Utils.getLocalUserId(getContext()!!)

        setupConnection()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mCallback = context as ActivityCallback
    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_chat, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut()
            mCallback!!.logout()
        }

        return super.onOptionsItemSelected(item)
    }

    /// Private methods

    private fun closeAndClean() {
        Utils.closeKeyboard(getContext()!!, mChatInput!!)
        mChatInput!!.setText("")
    }

    private fun setupConnection() {
        val database = FirebaseDatabase.getInstance()
        mReference = database.getReference(Constants.DATABASE_NAME)

        mReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(Constants.LOG_TAG, "SUCCESS!")
                handleReturn(dataSnapshot)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(Constants.LOG_TAG, "ERROR: " + databaseError.message)
                Toast.makeText(getContext(), R.string.chat_init_error, Toast.LENGTH_SHORT).show()
                mCallback!!.logout()
            }
        })
    }

    private fun handleReturn(dataSnapshot: DataSnapshot) {
        mAdapter!!.clearData()

        for (item in dataSnapshot.children) {
            val data = item.getValue(ChatData::class.java)
            mAdapter!!.addData(data)
        }

        mAdapter!!.notifyDataSetChanged()
    }

    companion object {

        /**
         * Create a instance of this fragment
         *
         * @return fragment instance
         */
        fun newInstance(): ChatFragment {
            return ChatFragment()
        }
    }
}
