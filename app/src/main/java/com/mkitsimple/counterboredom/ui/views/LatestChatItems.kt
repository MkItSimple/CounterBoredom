package com.mkitsimple.counterboredom.ui.views

import com.mkitsimple.counterboredom.R
import com.mkitsimple.counterboredom.data.models.User
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.row_latest_chats.view.*

class LatestChatItems (): Item<ViewHolder>() {
    var chatPartnerUser: User? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {
        //viewHolder.itemView.textViewLatestMessage.text = "Some new message"
        //Picasso.get().load(chatPartnerUser?.profileImageUrl).into(targetImageView)

//        val chatPartnerId: String
//        if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
//            chatPartnerId = chatMessage.toId
//        } else {
//            chatPartnerId = chatMessage.fromId
//        }
//
//        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
//        ref.addListenerForSingleValueEvent(object: ValueEventListener {
//            override fun onDataChange(p0: DataSnapshot) {
//                chatPartnerUser = p0.getValue(User::class.java)
//                viewHolder.itemView.username_textview_latest_message.text = chatPartnerUser?.username
//
//                val targetImageView = viewHolder.itemView.imageview_latest_message
//                Picasso.get().load(chatPartnerUser?.profileImageUrl).into(targetImageView)
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//        })
    }

    override fun getLayout(): Int {
        return R.layout.row_latest_chats
    }
}