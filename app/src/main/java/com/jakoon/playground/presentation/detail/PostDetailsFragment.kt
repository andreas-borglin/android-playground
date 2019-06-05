package com.jakoon.playground.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.jakoon.playground.R
import com.jakoon.playground.databinding.FragmentPostDetailsBinding
import com.jakoon.playground.json.postFromJson
import com.jakoon.playground.model.Post
import com.jakoon.playground.repository.DataResult
import org.koin.android.viewmodel.ext.android.viewModel


class PostDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPostDetailsBinding
    private val args: PostDetailsFragmentArgs by navArgs()
    private val viewModel: PostDetailsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val post = postFromJson(args.post)

        viewModel.getPostDetails(post).observe(this, Observer {
            when (it) {
                is DataResult.Success -> populatePostDetails(it.data)
                is DataResult.Failure -> binding.title.text = getString(R.string.post_details_failed)
            }
        })
    }

    private fun populatePostDetails(post: Post) {
        binding.title.text = post.title
        binding.author.text = post.user?.name
        binding.body.text = post.body
        binding.comments.text = post.comments?.size.toString()
    }
}