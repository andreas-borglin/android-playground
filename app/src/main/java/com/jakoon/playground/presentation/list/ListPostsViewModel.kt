package com.jakoon.playground.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakoon.playground.model.Post
import com.jakoon.playground.repository.DataResult
import com.jakoon.playground.repository.Repository
import kotlinx.coroutines.launch


internal class ListPostsViewModel(private val repository: Repository) : ViewModel() {

    private val postResults = MutableLiveData<DataResult<List<Post>>>()

    fun getPosts(): LiveData<DataResult<List<Post>>> {
        if (postResults.value == null || postResults.value is DataResult.Failure) {
            fetchPosts()
        }
        return postResults
    }

    private fun fetchPosts() {
        viewModelScope.launch {
            postResults.value = repository.getPosts()
        }
    }

    fun refreshPosts() {
        repository.clearCache()
        fetchPosts()
    }
}