package com.mkitsimple.counterboredom.ui.main

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth

import com.mkitsimple.counterboredom.R
import com.mkitsimple.counterboredom.ui.views.UserItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_friends_list.*

class FriendsListFragment : Fragment() {

    companion object {
        fun newInstance() = FriendsListFragment()
        val USER_KEY = "USER_KEY"
        val TAG = "FriendsListFragment"
    }

    private lateinit var viewModel: FriendsListViewModel
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friends_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FriendsListViewModel::class.java)
        //setDummyData()
        viewModel.fetchUsers()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {

        viewModel.users.observe(this, Observer { users ->
            for (user in users){
                //Log.d(TAG, "User: "+ user.username)
                val uid = FirebaseAuth.getInstance().uid
                if (user.uid != uid) {
                    adapter.add(
                        UserItem(user)
                    )
                }
            }

            adapter.setOnItemClickListener { item, view ->
                val userItem = item as UserItem
                val intent = Intent(view.context, ChatLogActivity::class.java)
                intent.putExtra(USER_KEY, userItem.user)
                startActivity(intent)
            }

            recyclerviewFriendsList.adapter = adapter
        })
    }

}
