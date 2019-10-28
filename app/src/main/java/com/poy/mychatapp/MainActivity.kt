package com.poy.mychatapp

import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.poy.mychatapp.fragment.ChatFragment
import com.poy.mychatapp.fragment.CreateAccountFragment
import com.poy.mychatapp.fragment.LoginFragment




class MainActivity : AppCompatActivity(), ActivityCallback {

    /// Lifecycle methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.container, LoginFragment.newInstance())
            .commit()
    }

    /// Callback methods

    override fun openChat() {
        replaceFragment(ChatFragment.newInstance());

    }

    override fun openCreateAccount() {
        replaceFragment(CreateAccountFragment.newInstance());
    }

    override fun logout() {
        replaceFragment(LoginFragment.newInstance())

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}
