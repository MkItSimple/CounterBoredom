package com.mkitsimple.counterboredom.data.repositories

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mkitsimple.counterboredom.data.models.User
import com.mkitsimple.counterboredom.util.NODE_USERS

class UserRepository {

    private val dbUsers = FirebaseDatabase.getInstance().getReference(NODE_USERS)

    var mUser : User? = null

    suspend fun getUser(uid: String): User {

        val dbUsers = dbUsers.child(uid)

        dbUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    mUser = snapshot.getValue(User::class.java)
                }
            }
        })

        return mUser!!

    }
}