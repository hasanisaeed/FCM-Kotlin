package ir.ryca.fcm.notification

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import org.json.JSONException
import android.text.TextUtils
import ir.ryca.fcm.activity.MessageShowActivity
import android.content.Intent

class MyFirebaseMessagingService : FirebaseMessagingService() {

    val TAG = "MessagingService"
    private var notificationUtils: NotificationUtils? = null

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.e("FCM Token", token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.e(TAG, "From: " + remoteMessage.from)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.e(TAG, "Data Payload: " + remoteMessage.data.toString())

            try {
                val json = JSONObject(remoteMessage.data.toString())
                handleDataMessage(json)
            } catch (e: Exception) {
                Log.e(TAG, "Exception: " + e.message)
            }

        }
    }

    private fun handleDataMessage(json: JSONObject) {
        Log.e(TAG, "push json: $json")

        try {
            val wrapper = json.getJSONObject("wrapper")
            val title = wrapper.getString("title")
            val message = wrapper.getString("message")
            val imageUrl = wrapper.getString("image")
            val timestamp = wrapper.getString("timestamp")
            val payload = wrapper.getJSONObject("payload")

            val content = payload.getString("content")
            val company = payload.getString("company")

            //Send notification data to MessageShowActivity class for showing
            val resultIntent = Intent(applicationContext, MessageShowActivity::class.java)
            resultIntent.putExtra("title", title)
            resultIntent.putExtra("timestamp", timestamp)
            resultIntent.putExtra("content", content)
            resultIntent.putExtra("company", company)
            resultIntent.putExtra("image", imageUrl)

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(applicationContext, title, message, timestamp, resultIntent)
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(
                    applicationContext,
                    title,
                    message,
                    timestamp,
                    resultIntent,
                    imageUrl
                )
            }

        } catch (e: JSONException) {
            Log.e(TAG, "Json Exception: " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }

    }

    /**
     * Showing notification with text only
     */
    private fun showNotificationMessage(
        context: Context,
        title: String,
        message: String,
        timeStamp: String,
        intent: Intent
    ) {
        notificationUtils = NotificationUtils(context)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        notificationUtils?.showNotificationMessage(title, message, timeStamp, intent)
    }

    /**
     * Showing notification with text and image
     */
    private fun showNotificationMessageWithBigImage(
        context: Context,
        title: String,
        message: String,
        timeStamp: String,
        intent: Intent,
        imageUrl: String
    ) {
        notificationUtils = NotificationUtils(context)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        notificationUtils?.showNotificationMessage(title, message, timeStamp, intent, imageUrl)
    }
}