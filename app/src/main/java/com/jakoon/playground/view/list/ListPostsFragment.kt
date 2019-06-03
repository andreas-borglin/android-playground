package com.jakoon.playground.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakoon.playground.R
import com.jakoon.playground.databinding.FragmentListPostsBinding
import com.jakoon.playground.json.toJson
import com.jakoon.playground.vm.ListPostsViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import org.koin.android.viewmodel.ext.android.viewModel

class ListPostsFragment : Fragment() {

    lateinit var binding: FragmentListPostsBinding
    val viewModel: ListPostsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_posts, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.postsList.layoutManager = layoutManager

        val adapter = GroupAdapter<ViewHolder>()
        val section = Section()
        adapter.add(section)
        binding.postsList.adapter = adapter

        viewModel.getPosts().observe(this, Observer { list ->
            section.update(list.map { PostItem(it) })
        })

        adapter.setOnItemClickListener { item, _ ->
            val postItem = item as PostItem
            val action =
                ListPostsFragmentDirections.actionListPostsFragmentToPostDetailsFragment(postItem.post.toJson())
            view.findNavController().navigate(action)
        }
    }
}