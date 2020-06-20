package com.mkitsimple.counterboredom.ui.auth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.mkitsimple.counterboredom.R
import com.mkitsimple.counterboredom.ui.main.MainActivity
import com.mkitsimple.counterboredom.util.longToast
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    companion object {
        val TAG = "RegisterActivity"
        val CHANNEL_ID = "MainActivity"
        val CHANNEL_NAME = "Simplified Coding"
        val CHANNEL_DESC = "Simplified Coding Notifications"
    }

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var token: String? = null

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        viewModel = ViewModelProviders.of(this)[AuthViewModel::class.java]

        // generate registration token for this device
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    token = task.result!!.token
                    //saveToken(token)
                    Log.d(TAG, token!!)
                }
            }

        textViewAlreadyHaveAccount.setOnClickListener {
            Log.d(TAG, "Try to show login activity")

            // launch the login activity somehow
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        buttonSelectPhoto.setOnClickListener {
            Log.d(TAG, "Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        buttonRegister.setOnClickListener {
            performRegister()
        }
    }

    var selectedPhotoUri: Uri? = null
    var downloadedPhotoUri: String? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            Log.d(TAG, "Photo was selected")

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            imageViewSelectPhoto.setImageBitmap(bitmap)
            buttonSelectPhoto.alpha = 0f
        }
    }

    private fun performRegister(){
        //uploadImageToFirebaseStorage()
        val username = editTextUsername.text.toString()
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if (username.isEmpty()) {
            longToast("Please fill out username")
            return
        }

        if (email.isEmpty()) {
            longToast("Please fill out email")
            return
        }

        if (password.isEmpty()) {
            longToast("Please fill out password")
            return
        }

        viewModel.performRegister(email, password)
        viewModel.isPerformRegisterSuccessful.observe(this, Observer {
            if (it) {
                uploadImageToFirebaseStorage()
            } else {
                viewModel.registerErrorMessage.observe(this, Observer { registerErrorMessage ->
                    longToast("Failed to create user: $registerErrorMessage")
                })
            }
        })

//        mAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener {
//                if (!it.isSuccessful) return@addOnCompleteListener
//                Log.d(TAG, "Successfully created user with uid: ${it.result?.user?.uid}")
//                uploadImageToFirebaseStorage()
//            }
//            .addOnFailureListener{
//                Log.d(TAG, "Failed to create user: ${it.message}")
//                toast("Failed to create user: ${it.message}")
//            }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) {
            saveUserToFirebaseDatabase(token)
        } else {
            viewModel.uploadImageToFirebaseStorage(selectedPhotoUri!!)
            viewModel.mSelectedPhotoUri.observe(this, Observer {
                saveUserToFirebaseDatabaseWithProfileImage(it.toString(), token)
            })
        }

//        val filename = UUID.randomUUID().toString()
//        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
//
//        ref.putFile(selectedPhotoUri!!)
//            .addOnSuccessListener {
//                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")
//
//                ref.downloadUrl.addOnSuccessListener {
//                    Log.d(TAG, "File Location: $it")
//                    //downloadedPhotoUri = it.toString()
//                    //saveUserToFirebaseDatabase(it.toString(), token)
//                    //saveUserToFirebaseDatabase(token)
//                    selectedPhotoUri = it
//
//                }
//            }
//            .addOnFailureListener {
//                Log.d(TAG, "Failed to upload image to storage: ${it.message}")
//            }
    }

    private fun saveUserToFirebaseDatabase(token: String?) {

        viewModel.saveUserToFirebaseDatabase(editTextUsername.text.toString(), "null", token!!)
        viewModel.isSaveUserToFirebaseDatabaseSuccessful.observe(this, Observer {
            if (it)
                loginUser()
        })

//        val uid = FirebaseAuth.getInstance().uid ?: ""
//        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
//
//        val user = User(uid, editTextUsername.text.toString(), "null", token!!)
//
//        ref.setValue(user)
//            .addOnSuccessListener {
//                Log.d(TAG, "Finally we saved the user to Firebase Database")
//                loginUser()
//            }
//            .addOnFailureListener {
//                Log.d(TAG, "Failed to set value to database: ${it.message}")
//            }
    }

    private fun saveUserToFirebaseDatabaseWithProfileImage(profileImageUrl: String?, token: String?) {

        viewModel.saveUserToFirebaseDatabase(editTextUsername.text.toString(), profileImageUrl!!, token!!)
        viewModel.isSaveUserToFirebaseDatabaseSuccessful.observe(this, Observer {
            if (it)
                loginUser()
        })

//        val uid = FirebaseAuth.getInstance().uid ?: ""
//        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
//
//        val user = User(uid, editTextUsername.text.toString(), profileImageUrl!!, token!!)
//
//        ref.setValue(user)
//            .addOnSuccessListener {
//                Log.d(TAG, "Finally we saved the user to Firebase Database")
//                loginUser()
//            }
//            .addOnFailureListener {
//                Log.d(TAG, "Failed to set value to database: ${it.message}")
//            }
    }

    private fun loginUser() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}
