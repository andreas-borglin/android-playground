package com.jakoon.babylon.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakoon.babylon.R
import com.jakoon.babylon.databinding.FragmentListPostsBinding
import com.jakoon.babylon.vm.ListPostsViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import org.koin.android.viewmodel.ext.android.viewModel

class ListPostsFragment : Fragment() {

    lateinit var binding: FragmentListPostsBinding
    val viewModel: ListPostsViewModel by viewModel()
    val adapter = GroupAdapter<ViewHolder>()
    val section = Section()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_posts, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.postsList.layoutManager = layoutManager
        binding.postsList.adapter = adapter

        adapter.add(section)
        viewModel.getPosts().observe(this, Observer { list ->
            section.update(list.map { PostItem(it) })
        })
    }
}