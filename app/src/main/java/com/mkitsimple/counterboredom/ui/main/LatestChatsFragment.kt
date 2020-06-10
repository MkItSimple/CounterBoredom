package com.mkitsimple.counterboredom.ui.main

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mkitsimple.counterboredom.R
import com.mkitsimple.counterboredom.ui.views.LatestChatItems
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_chats_fragment.*

class LatestChatsFragment : Fragment() {

    companion object {
        fun newInstance() = LatestChatsFragment()
    }

    private lateinit var viewModel: LatestChatsViewModel
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.latest_chats_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LatestChatsViewModel::class.java)
        setDummyData()
    }

    private fun setDummyData() {
        adapter.clear()
        adapter.add(LatestChatItems())
        adapter.add(LatestChatItems())
        adapter.add(LatestChatItems())

        adapter.setOnItemClickListener { item, view ->

            //val userItem = item as UserItem

            val intent = Intent(view.context, ChatLogActivity::class.java)
            //intent.putExtra(USER_KEY, userItem.user)
            startActivity(intent)
        }

        recyclerview_latest_chats.adapter = adapter
    }

}
