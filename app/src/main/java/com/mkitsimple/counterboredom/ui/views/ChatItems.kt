package com.mkitsimple.counterboredom.ui.views

import com.mkitsimple.counterboredom.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*


class ChatFromItem(): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
//        viewHolder.itemView.textViewFromRow.text = "Hello there"
//
//        val uri = R.drawable.pi
//        val targetImageView = viewHolder.itemView.imageViewFromRow
//        Picasso.get().load(uri).into(targetImageView)
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
//        viewHolder.itemView.textViewToRow.text = "Where are you now?"
//
//        val uri = R.drawable.pi
//        val targetImageView = viewHolder.itemView.imageViewToRow
//        Picasso.get().load(uri).into(targetImageView)
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}