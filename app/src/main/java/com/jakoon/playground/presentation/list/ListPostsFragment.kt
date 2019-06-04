package com.jakoon.playground.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakoon.playground.R
import com.jakoon.playground.databinding.FragmentListPostsBinding
import com.jakoon.playground.json.toJson
import com.jakoon.playground.repository.DataResult
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import org.koin.android.viewmodel.ext.android.viewModel


class ListPostsFragment : Fragment() {

    private lateinit var binding: FragmentListPostsBinding
    private val viewModel: ListPostsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_posts, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val section = setupRecyclerView(view)

        viewModel.getPosts().observe(this, Observer { result ->
            when (result) {
                is DataResult.Success -> section.update(result.list.map { PostItem(it) })
                is DataResult.Failure -> binding.errorMessage.isVisible = true
            }
            binding.swipeToRefresh.isRefreshing = false
        })

        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.refreshPosts()
        }
    }

    private fun setupRecyclerView(view: View): Section {
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.postsList.layoutManager = layoutManager
        binding.postsList.addItemDecoration(DividerItemDecoration(view.context, layoutManager.orientation))

        val adapter = GroupAdapter<ViewHolder>()
        val section = Section()
        adapter.add(section)
        binding.postsList.adapter = adapter

        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimary)

        adapter.setOnItemClickListener { item, _ ->
            val postItem = item as PostItem
            val action = ListPostsFragmentDirections.actionListToDetails(postItem.post.toJson())
            view.findNavController().navigate(action)
        }
        return section
    }
}