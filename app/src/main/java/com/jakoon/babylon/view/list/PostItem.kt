package com.jakoon.babylon.view.list

import com.jakoon.babylon.R
import com.jakoon.babylon.api.Post
import com.jakoon.babylon.databinding.ListPostItemBinding
import com.xwray.groupie.databinding.BindableItem

class PostItem(val post: Post) : BindableItem<ListPostItemBinding>() {
    override fun getLayout() = R.layout.list_post_item

    override fun bind(viewBinding: ListPostItemBinding, position: Int) {
        viewBinding.title.text = post.title
        viewBinding.body.text = post.body
    }
}