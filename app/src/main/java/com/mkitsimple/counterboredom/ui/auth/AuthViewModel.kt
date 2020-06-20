package com.mkitsimple.counterboredom.ui.auth

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.mkitsimple.counterboredom.data.models.User
import java.util.*

class AuthViewModel : ViewModel() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _isRegisterSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isRegisterSuccessful

    private val _isUploadImageSuccessful = MutableLiveData<Boolean>()
    val isUploadImageSuccessful: LiveData<Boolean>
        get() = _isUploadImageSuccessful

    private val _isPerformRegisterSuccessful = MutableLiveData<Boolean>()
    val isPerformRegisterSuccessful: LiveData<Boolean>
        get() = _isPerformRegisterSuccessful

    private val _isPerformLoginSuccessful = MutableLiveData<Boolean>()
    val isPerformLoginSuccessful: LiveData<Boolean>
        get() = _isPerformLoginSuccessful


    private val _loginErrorMessage = MutableLiveData<String>()
    val loginErrorMessage: LiveData<String>
        get() = _loginErrorMessage

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _downloadedPhotoUri = MutableLiveData<Uri>()
    val downloadedPhotoUri: LiveData<Uri>
        get() = _downloadedPhotoUri


    fun uploadImageToFirebaseStorage(selectedPhotoUri: Uri) {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri)
            .addOnSuccessListener {
                //Log.d(RegisterActivity.TAG, "Successfully uploaded image: ${it.metadata?.path}")
                _isUploadImageSuccessful.value = true

                ref.downloadUrl.addOnSuccessListener {
                    //Log.d(RegisterActivity.TAG, "File Location: $it")
                    //downloadedPhotoUri = it.toString()
                    _downloadedPhotoUri.value = it
                }
            }
            .addOnFailureListener {
                //Log.d(RegisterActivity.TAG, "Failed to upload image to storage: ${it.message}")
                _isUploadImageSuccessful.value = false
            }
    }

    fun performRegister(email: String, password: String) {

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                _isPerformRegisterSuccessful.value = true
            }
            .addOnFailureListener{
                _isPerformRegisterSuccessful.value = false
            }
    }

    fun saveUserToFirebaseDatabase(
        username: String,
        downloadedPhotoUri: String,
        token: String
    ) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, username, downloadedPhotoUri, token)
//        ref.setValue(user)
//            .addOnSuccessListener {
//                _isRegisterSuccessful.value = true
//            }
//            .addOnFailureListener {
//                _isRegisterSuccessful.value = false
//                _errorMessage.value = it.message
//            }
    }

    fun performLogin(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                _isPerformLoginSuccessful.value = true
            }
            .addOnFailureListener {
                _isPerformLoginSuccessful.value = false
                _loginErrorMessage.value = it.message
            }
    }


}