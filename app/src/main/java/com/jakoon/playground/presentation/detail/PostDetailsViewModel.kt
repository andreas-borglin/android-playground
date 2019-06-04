package com.jakoon.playground.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakoon.playground.model.Post
import com.jakoon.playground.repository.DataResult
import com.jakoon.playground.repository.Repository
import kotlinx.coroutines.launch

class PostDetailsViewModel(private val repository: Repository) : ViewModel() {

    private val postDetails = MutableLiveData<DataResult<Post>>()

    fun getPostDetails(post: Post): LiveData<DataResult<Post>> {
        if (postDetails.value == null || postDetails.value is DataResult.Failure) {
            fetchDetails(post)
        }
        return postDetails
    }

    private fun fetchDetails(post: Post) {
        viewModelScope.launch {
            postDetails.value = repository.getPostDetails(post)
        }
    }

}