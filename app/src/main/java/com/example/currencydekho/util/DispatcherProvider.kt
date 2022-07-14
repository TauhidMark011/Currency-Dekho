package com.example.currencydekho.util

import kotlinx.coroutines.CoroutineDispatcher

//it defines our coroutines dispatcher
interface DispatcherProvider {

    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher

    //view model should choose the test dispatcher for all test cases and just the normal dispatcher for our real app
}