package com.mkitsimple.counterboredom.ui.views

import com.mkitsimple.counterboredom.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class FriendsListItems(): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        //viewHolder.itemView.username_textview_new_message.text = "Your Friends Name"
        //Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageview_new_message)
    }

    override fun getLayout(): Int {
        return R.layout.row_friends_list
    }
}