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

    // Login
    private val _isPerformLoginSuccessful = MutableLiveData<Boolean>()
    val isPerformLoginSuccessful: LiveData<Boolean>
        get() = _isPerformLoginSuccessful

    private val _loginErrorMessage = MutableLiveData<String>()
    val loginErrorMessage: LiveData<String>
        get() = _loginErrorMessage

    // Register
    private val _isSaveUserToFirebaseDatabaseSuccessful = MutableLiveData<Boolean>()
    val isSaveUserToFirebaseDatabaseSuccessful: LiveData<Boolean>
        get() = _isSaveUserToFirebaseDatabaseSuccessful

    private val _isPerformRegisterSuccessful = MutableLiveData<Boolean>()
    val isPerformRegisterSuccessful: LiveData<Boolean>
        get() = _isPerformRegisterSuccessful

    private val _registerErrorMessage = MutableLiveData<String>()
    val registerErrorMessage: LiveData<String>
        get() = _registerErrorMessage

    private val _isUploadImageSuccessful = MutableLiveData<Boolean>()
    val isUploadImageSuccessful: LiveData<Boolean>
        get() = _isUploadImageSuccessful





    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _mSelectedPhotoUri = MutableLiveData<String>()
    val mSelectedPhotoUri: LiveData<String>
        get() = _mSelectedPhotoUri


    fun uploadImageToFirebaseStorage(selectedPhotoUri: Uri) {

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    _mSelectedPhotoUri.value = it.toString()
                    _isUploadImageSuccessful.value = true
                }
            }
            .addOnFailureListener {
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
                _registerErrorMessage.value = it.message
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
        ref.setValue(user)
            .addOnSuccessListener {
                _isSaveUserToFirebaseDatabaseSuccessful.value = true
            }
            .addOnFailureListener {
                _isSaveUserToFirebaseDatabaseSuccessful.value = false
                _errorMessage.value = it.message
            }
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