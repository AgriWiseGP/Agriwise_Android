package com.example.agriwise.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

open class BaseViewModel : ViewModel() {
    var loadingFlow =
        MutableSharedFlow<Boolean>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    var loadingState: SharedFlow<Boolean> = loadingFlow

    var errorFlow =
        MutableSharedFlow<String>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    var errorState: SharedFlow<String> = errorFlow

    fun <T> requestApi(flow: Flow<Response<T>>?, onResult: (suspend (T?) -> Unit)) {
            viewModelScope.launch {
                loadingFlow.emit(true)
                flow?.catch {
                    errorFlow.emit(it.message.toString())
                }?.onCompletion {
                    loadingFlow.emit(false)
                }?.collect {
                    if (it.body() != null) {
                        onResult.invoke(it.body())
                    } else {
                        onResult.invoke(null)
                        try {
                            val message =
                                JSONObject(
                                    it.errorBody()?.string().toString()
                                ).getString("error")
                            errorFlow.emit(message)
                        } catch (e: Exception) {
                                errorFlow.emit("Something went wrong. Please try again later")
                        }
                    }
                }
            }


    }
}