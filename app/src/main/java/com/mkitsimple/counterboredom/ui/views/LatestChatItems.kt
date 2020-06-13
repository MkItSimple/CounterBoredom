package com.mkitsimple.counterboredom.ui.views

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mkitsimple.counterboredom.R
import com.mkitsimple.counterboredom.data.models.ChatMessage
import com.mkitsimple.counterboredom.data.models.MessageType
import com.mkitsimple.counterboredom.data.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.row_latest_chats.view.*

class LatestChatItems (val chatMessage: ChatMessage): Item<ViewHolder>() {
    var chatPartnerUser: User? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {
        if (chatMessage.type == MessageType.TEXT){
            viewHolder.itemView.textViewLatestMessage.text = chatMessage.text
        } else {
            viewHolder.itemView.textViewLatestMessage.text = "You sent a photo"
        }

        val chatPartnerId: String
        if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
            chatPartnerId = chatMessage.toId
        } else {
            chatPartnerId = chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val chatPartnerUser = p0.getValue(User::class.java)
                viewHolder.itemView.latestMessageTextView.text = chatPartnerUser?.username

                val uri = chatPartnerUser?.profileImageUrl
                val targetImageView = viewHolder.itemView.latestMessageCircleImageView
                //Picasso.get().load(chatPartnerUser?.profileImageUrl).into(targetImageView)
                if(uri == "null") {
                    Picasso.get().load(R.drawable.profile_black).into(targetImageView)
                } else {
                    Picasso.get().load(uri).into(targetImageView)
                }


            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    override fun getLayout(): Int {
        return R.layout.row_latest_chats
    }
}