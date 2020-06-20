package com.mkitsimple.counterboredom.ui.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.mkitsimple.counterboredom.data.models.Profile
import com.mkitsimple.counterboredom.data.models.User
import java.util.*

class ProfileViewModel : ViewModel() {

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                _currentUser.value = p0.getValue(User::class.java)
                //Log.d("LatestMessages", "Current user ${cUser.profileImageUrl}")
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    fun uid() : String? {
        return FirebaseAuth.getInstance().uid
    }




    fun updateProfile(profileImageUrl: String, username: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = Profile(uid, username, profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                _isSuccessful.value = true

            }
            .addOnFailureListener{
                _isSuccessful.value = false
            }
    }

    // Upload Image
    private val _isUploadImageSuccessful = MutableLiveData<Boolean>()
    val isUploadImageSuccessful: LiveData<Boolean>
        get() = _isUploadImageSuccessful

    private val _uploadedImageUri = MutableLiveData<String>()
    val uploadedImageUri: LiveData<String>
        get() = _uploadedImageUri

    private val _uploadImageErrorMessage = MutableLiveData<String>()
    val uploadImageErrorMessage: LiveData<String>
        get() = _uploadImageErrorMessage

    fun uploadImageToFirebaseStorage(selectedPhotoUri: Uri) {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri)
            .addOnSuccessListener {
                _isUploadImageSuccessful.value = true

                ref.downloadUrl.addOnSuccessListener {
                    _uploadedImageUri.value = it.toString()
                }
            }
            .addOnFailureListener {
                _isUploadImageSuccessful.value = false
                _uploadImageErrorMessage.value = it.message
            }
    }
}
