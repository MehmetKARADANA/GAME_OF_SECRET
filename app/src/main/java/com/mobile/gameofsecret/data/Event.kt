package com.mobile.gameofsecret.data

open class Event<out T>(private val content: T) {
    private var haasBeenHandled = false

    fun getContentIfNotHandled() : T? {
        return if (haasBeenHandled){
            null
        }else{
            haasBeenHandled=true
            content
        }
    }
}