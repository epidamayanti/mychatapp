package com.poy.mychatapp.model

class ChatData {
    private var mName: String? = null
    private var mId: String? = null
    private var mMessage: String? = null

    fun ChatData(){
        // empty constructor
    }

    fun getName(): String? {
        return mName
    }

    fun setName(name: String) {
        mName = name
    }

    fun getId(): String? {
        return mId
    }

    fun getMessage(): String? {
        return mMessage
    }

    fun setMessage(message: String) {
        mMessage = message
    }

    fun setId(id: String) {
        mId = id
    }
}
