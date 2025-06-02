package com.example.reflect.data.repository.friendship

import android.content.Context
import com.example.reflect.common.prefs.AccountPrefs
import com.example.reflect.domain.model.NotificationUserModel
import com.example.reflect.domain.repository.friendship.WebSocketFriendshipRepository
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class WebSocketFriendshipRepositoryImpl @Inject constructor(
    private val client: OkHttpClient,
    @ApplicationContext private val context: Context
) : WebSocketFriendshipRepository {
    private var webSocket: WebSocket? = null

    override fun notifications(): Flow<NotificationUserModel> = callbackFlow {
        val listener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val notificationModel = Gson().fromJson(text, NotificationUserModel::class.java)
                    trySend(notificationModel)
                } catch (e: Exception) {
                    close(e)
                }
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                close()
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                close(t)
            }
        }

        val request = Request.Builder()
            .url("ws://185.185.71.233/ws/notifications/?token=${AccountPrefs.getAuthToken(context)}")
            .build()

        webSocket = client.newWebSocket(request, listener)

        awaitClose {
            webSocket?.close(1000, "Closing")
        }
    }

    override suspend fun close() {
        webSocket?.close(1000, "Normal closure")
        webSocket = null
    }
}