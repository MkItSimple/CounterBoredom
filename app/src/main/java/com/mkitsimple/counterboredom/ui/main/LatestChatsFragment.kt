package com.mkitsimple.counterboredom.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mkitsimple.counterboredom.R
import com.mkitsimple.counterboredom.data.models.ChatMessage
import com.mkitsimple.counterboredom.ui.views.LatestChatItems
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_latest_chats.*

class LatestChatsFragment : Fragment() {

    companion object {
        val TAG = "LatestMessages"
        var latestMessagesMap = HashMap<String, ChatMessage>()
        val USER_KEY = "USER_KEY"
    }

    private lateinit var viewModel: LatestChatsViewModel
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_latest_chats, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LatestChatsViewModel::class.java)

        // set item click listener on your adapter
        adapter.setOnItemClickListener { item, view ->
            //Log.d(TAG, "123")
            val row = item as LatestChatItems
            val intent = Intent(context, ChatLogActivity::class.java)
            intent.putExtra(USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }

        //Setup RecyclerView
        listenForLatestMessages()
    }

    private fun listenForLatestMessages() {
        viewModel.listenForLatestMessages()
        viewModel.mLatestMessagesMap.observe(this, Observer { mLatestMessagesMap ->
            latestMessagesMap = mLatestMessagesMap
            //Log.d(TAG, "latestMessagesMap: "+latestMessagesMap)
            refreshRecyclerViewMessages()
        })
    }

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestChatItems(it))
        }
        recyclerviewLatestChats.adapter = adapter
    }
}
