package com.mkitsimple.counterboredom.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mkitsimple.counterboredom.R
import com.mkitsimple.counterboredom.ui.main.MainActivity
import com.mkitsimple.counterboredom.util.longToast
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders.of(this)[AuthViewModel::class.java]

        buttonLogin.setOnClickListener {
            performLogin()
        }

        textViewBackToRegister.setOnClickListener{
            finish()
        }
    }

    private fun performLogin() {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out email/pw.", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.performLogin(email, password)
        viewModel.isPerformLoginSuccessful.observe(this, Observer {isPerformLoginSuccessful ->
            if (isPerformLoginSuccessful) {
                startActivity(intentFor<MainActivity>().clearTask().newTask())
            } else {
                viewModel.loginErrorMessage.observe(this, Observer { loginErrorMessage ->
                    longToast("Failed to log in: " + loginErrorMessage)
                })
            }
        })

//        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener {
//                if (!it.isSuccessful) return@addOnCompleteListener
//                //Log.d("Login", "Successfully logged in: ${it.result.user.uid}")
//                Log.d("Login", "Successfully logged in: ")
//
//                //val intent = Intent(this, MainActivity::class.java)
//                //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//                //startActivity(intent)
//                startActivity(intentFor<MainActivity>().clearTask().newTask())
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
//            }
    }
}
