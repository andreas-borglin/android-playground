package com.jakoon.babylon.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.jakoon.babylon.R
import com.jakoon.babylon.databinding.FragmentPostDetailsBinding
import com.jakoon.babylon.json.postFromJson


class PostDetailsFragment : Fragment() {

    lateinit var binding: FragmentPostDetailsBinding
    val args: PostDetailsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val post = postFromJson(args.post)
        binding.title.text = post?.title ?: "No post found"
    }
}