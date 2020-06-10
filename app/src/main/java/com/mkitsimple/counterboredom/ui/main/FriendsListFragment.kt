package com.mkitsimple.counterboredom.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mkitsimple.counterboredom.R
import com.mkitsimple.counterboredom.ui.views.FriendsListItems
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.friends_list_fragment.*

class FriendsListFragment : Fragment() {

    companion object {
        fun newInstance() = FriendsListFragment()
    }

    private lateinit var viewModel: FriendsListViewModel
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.friends_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FriendsListViewModel::class.java)
        setDummyData()
    }

    private fun setDummyData() {
        adapter.clear()
        adapter.add(FriendsListItems())
        adapter.add(FriendsListItems())
        adapter.add(FriendsListItems())
        recyclerviewFriendsList.adapter = adapter
    }

}
