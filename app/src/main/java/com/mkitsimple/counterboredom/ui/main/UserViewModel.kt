package com.mkitsimple.counterboredom.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mkitsimple.counterboredom.data.models.User
import com.mkitsimple.counterboredom.util.NODE_USERS

class UserViewModel : ViewModel(){

    private val dbUsers = FirebaseDatabase.getInstance().getReference(NODE_USERS)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun fetchFilteredUsers(uid: String) {
        val dbUsers = dbUsers.child(uid)

        dbUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    _user.value = snapshot.getValue(User::class.java)
                }
            }
        })
    }
}