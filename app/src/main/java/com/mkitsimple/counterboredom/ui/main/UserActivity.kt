package com.mkitsimple.counterboredom.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.mkitsimple.counterboredom.R
import com.mkitsimple.counterboredom.data.models.User
import com.mkitsimple.counterboredom.util.toast
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        val uid = mAuth.uid!!

        button.setOnClickListener {
            if (currentUser !== null){
                toast("not null")
            } else {
                toast("null")
            }
        }


        getCurrentUser(uid)
       // toast(currentUser!!.username)
//        if (currentUser !== null){
//            toast("not null")
//        } else {
//            toast("null")
//        }


    }

    private fun getCurrentUser(uid: String) {
        viewModel.fetchFilteredUsers(uid)

        viewModel.user.observe(this, Observer {
            currentUser = it
        })
    }

//    private fun fetchFilteredUsers(uid: String){
//        val dbUsers = FirebaseDatabase.getInstance().getReference()
//            .child(uid)
//
//        dbUsers.addListenerForSingleValueEvent(object: ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()){
//                    //Log.d(TAG, "From User: " + snapshot.getValue(User::class.java))
//                }
//            }
//        })
//    }
}
