package com.emreakpinar.filmeet.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emreakpinar.filmeet.arkadasekle.Message

class ChatViewModel : ViewModel() {
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    fun updateMessages(newMessages: List<Message>) {
        _messages.value = newMessages
    }
}
