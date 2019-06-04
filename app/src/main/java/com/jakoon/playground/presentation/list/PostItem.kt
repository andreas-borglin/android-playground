package com.jakoon.playground.presentation.list

import com.jakoon.playground.R
import com.jakoon.playground.model.Post
import com.jakoon.playground.databinding.ListPostItemBinding
import com.xwray.groupie.databinding.BindableItem

class PostItem(val post: Post) : BindableItem<ListPostItemBinding>() {
    override fun getLayout() = R.layout.list_post_item

    override fun bind(viewBinding: ListPostItemBinding, position: Int) {
        viewBinding.title.text = post.title
        viewBinding.body.text = post.body
    }
}