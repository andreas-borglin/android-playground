package com.jakoon.playground.presentation.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakoon.playground.model.Post
import com.jakoon.playground.repository.DataRetrievalOutcome
import com.jakoon.playground.repository.Repository
import kotlinx.coroutines.launch


class ListPostsViewModel(val repository: Repository) : ViewModel() {

    private val posts = MutableLiveData<List<Post>>()
    val errors = MutableLiveData<Throwable>()

    fun getPosts(): LiveData<List<Post>> {
        if (posts.value == null) {
            refreshPosts()
        }
        return posts
    }

    fun refreshPosts() {
        viewModelScope.launch {
            when (val getPostsOutcome: DataRetrievalOutcome<Post> = repository.getPosts()) {
                is DataRetrievalOutcome.Success -> posts.value = getPostsOutcome.list
                is DataRetrievalOutcome.Failure -> errors.value = getPostsOutcome.error
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("bla", "onCleared")
    }
}