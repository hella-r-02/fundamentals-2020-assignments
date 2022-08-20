/*
 * Copyright (C) 2020 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.people.data

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.core.app.*
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import com.example.android.people.MainActivity
import com.example.android.people.R
import com.example.android.people.ReplyReceiver

/**
 * Handles all operations related to [Notification].
 */
interface Notifications {
    fun initialize()
    fun showNotification(chat: Chat)
    fun dismissNotification(chatId: Long)
}

class AndroidNotifications(private val context: Context) : Notifications {

    companion object {
        /**
         * The notification channel for messages. This is used for showing Bubbles.
         */
        private const val CHANNEL_NEW_MESSAGES = "new_messages"

        private const val REQUEST_CONTENT = 1

        private const val CHAT_TAG = "chat"
    }

    private val notificationManagerCompat: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    override fun initialize() {
        if (notificationManagerCompat.getNotificationChannel(CHANNEL_NEW_MESSAGES) == null) {

/*
                #1 Create The Notification Channel

                a) Create a `NotificationChannel` object:
                   Create `NotificationChannelCompat.Builder`
                   with `CHANNEL_NEW_MESSAGES` as channel ID and `IMPORTANCE_HIGH` as
                        a channel importance
                   use `R.string.channel_new_messages` as channel name
                   use `R.string.channel_new_messages_description` as channel description

                b) Create a notification channel in the Notification manager:
                   Pass built `NotificationChannelCompat` to the
                     `notificationManagerCompat.createNotificationChannel`

                Note: Don't forget we have context in the class already
*/
            notificationManagerCompat.createNotificationChannel(
                NotificationChannelCompat.Builder(
                    CHANNEL_NEW_MESSAGES,
                    NotificationManagerCompat.IMPORTANCE_HIGH
                )
                    .setName(context.getString(R.string.channel_new_messages))
                    .setDescription(context.getString(R.string.channel_new_messages_description))
                    .build()
            )
        }
    }

    @WorkerThread
    override fun showNotification(chat: Chat) {
        /*
                #4 Open Chat From The Notification

                a) Form deep link (`contentUri`):
                   Use string pattern `https://android.example.com/chat/$chat.contact.id`
                   Use Android KTX method `String.toUri()` to convert it to Uri

                b) Create Chat Intent (`chatIntent`):
                   Create explicit intent for `MainActivity` class
                   Set action view `Intent.ACTION_VIEW`
                   Set `contentUri` as data

                c) Create a Pending Intent (`pendingIntent`):
                   Use `PendingIntent.getActivity()`
                   Use `REQUEST_CONTENT` as Request Code
                   Use `chatIntent` as Intent
                   Use `PendingIntent.FLAG_UPDATE_CURRENT` as Flags

                d) Pass pending intent to the notification builder:
                   Pass `pendingIntent` to builder with `setContentIntent()` setter
*/
        val contentUrl = "https://android.example.com/chat/${chat.contact.id}".toUri()
        val icon = IconCompat.createWithContentUri(chat.contact.iconUri)
        val person = Person.Builder()
            .setName(chat.contact.name)
            .setIcon(icon)
            .build()
/*

                #2 Show The Notification
                
                a) Create the notification:
                   Create `NotificationCompat.Builder`
                   with `CHANNEL_NEW_MESSAGES` as channel ID
                   use `chat.contact.name` as Content Title
                   use last message text from chat.messages as Content Text
                   use `R.drawable.ic_message` as Small Icon
                   use `IMPORTANCE_HIGH` as Priority
                   use last message timestamp as When
                   
                b) Show notification:
                   Pass built notification to `notificationManagerCompat.notify`
                   use `CHAT_TAG` as notification Tag
                   use `chat.contact.id` as notification ID

*/
        val builder = NotificationCompat.Builder(context, CHANNEL_NEW_MESSAGES)
            .setContentTitle(chat.contact.name)
            .setContentText(chat.messages.last().text)
            .setSmallIcon(R.drawable.ic_message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    REQUEST_CONTENT,
                    Intent(context, MainActivity::class.java)
                        .setAction(Intent.ACTION_VIEW)
                        .setData(contentUrl),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .addReplyAction(context, contentUrl)
            .setStyle(
                NotificationCompat.MessagingStyle(person)
                    .run {
                        for (message in chat.messages) {
                            val m = NotificationCompat.MessagingStyle.Message(
                                message.text,
                                message.timestamp,
                                if (message.isIncoming) person else null
                            ).apply {
                                if (message.photoUri != null) {
                                    setData(message.photoMimeType, message.photoUri)
                                }
                            }
                            if (!message.isNew) {
                                addHistoricMessage(m)
                            } else {
                                addMessage(m)
                            }
                        }
                        this
                    }
                    .setGroupConversation(false)
            )
            .setWhen(chat.messages.last().timestamp)

        notificationManagerCompat.notify(CHAT_TAG, chat.contact.id.toInt(), builder.build())

// TODO #1 from Workshop #3: Call on notification builder setReplyAction extension
/*
                #5 Style Notification as Chat (Optional)

                a) Create a Person (person):
                   With `Person.Builder`
                   Use `chat.contact.name` as Name
                   Use `IconCompat.createWithContentUri()` with `chat.contact.iconUri` as Icon
                   Call `Builder.build()` to create person

                b) Create a Messaging Style (style):
                   Create `NotificationCompat.MessagingStyle` with `person`
                   For every message in the `chat.message` create `Message` (message)
                       Create `NotificationCompat.MessagingStyle.Message`
                       Use `message.text` as Text
                       Use `message.timestamp` as Timestamp
                       Use `person` as Person if `message.isIncoming` otherwise null
                       Call `setData` on `message` if `message.photoUri` is not null
                         Use `message.photoMimeType` as Data Mime Type
                         Use `message.photoUri` as Data Uri
                       Call `addMessage` on `style` with `message` if `message.isNew`
                         otherwise `addHistoricMessage`

                c) Pass style to notification builder:
                   Pass `style` to builder with `setStyle()` setter

*/


    }

    override fun dismissNotification(chatId: Long) {
/*

                #3 Cancel Chat Notification(s)
                
                Simply call `notificationManagerCompat.cancel` with `CHAT_TAG` as tag and `chatId` as 
                    notification ID
*/
        notificationManagerCompat.cancel(CHAT_TAG, chatId.toInt())
    }

    private fun NotificationCompat.Builder.addReplyAction(
        context: Context,
        contentUri: Uri
    ): NotificationCompat.Builder {
        addAction(
            NotificationCompat.Action
                .Builder(
                    IconCompat.createWithResource(context, R.drawable.ic_send),
                    context.getString(R.string.label_reply),
                    PendingIntent.getBroadcast(
                        context,
                        REQUEST_CONTENT,
                        Intent(context, ReplyReceiver::class.java).setData(contentUri),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                .addRemoteInput(
                    RemoteInput.Builder(ReplyReceiver.KEY_TEXT_REPLY)
                        .setLabel(context.getString(R.string.hint_input))
                        .build()
                )
                .setAllowGeneratedReplies(true)
                .build()
        )
        return this
    }
}