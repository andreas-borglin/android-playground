package com.jakoon.playground.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakoon.playground.api.Post
import com.jakoon.playground.api.TypicodeJsonService
import kotlinx.coroutines.launch


class ListPostsViewModel(val apiService: TypicodeJsonService) : ViewModel() {

    private val posts = MutableLiveData<List<Post>>()

    fun getPosts(): LiveData<List<Post>> {
        if (posts.value == null) {
            refreshPosts()
        }
        return posts
    }

    fun refreshPosts() {
        viewModelScope.launch {
            posts.value = apiService.getPosts()
        }
    }

}