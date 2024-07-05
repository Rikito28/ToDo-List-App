package com.dicoding.todoapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import com.dicoding.todoapp.ui.detail.DetailTaskActivity
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.NOTIFICATION_CHANNEL_ID
import com.dicoding.todoapp.utils.TASK_ID

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val channelName = inputData.getString(NOTIFICATION_CHANNEL_ID)
    private val NOTIFY = 100

    private fun getPendingIntent(task: Task): PendingIntent? {
        val intent = Intent(applicationContext, DetailTaskActivity::class.java).apply {
            putExtra(TASK_ID, task.id)
        }
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            } else {
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
    }

    override fun doWork(): Result {
        //TODO 14 : If notification preference on, get nearest active task from repository and show notification with pending intent
        val preference = TaskRepository.getInstance(applicationContext)
        val task = preference.getNearestActiveTask()
        showActiveTaskNotification(applicationContext, task)
        return Result.success()
    }

    private fun showActiveTaskNotification(context: Context, notif: Task) {
        val channel = context.getString(R.string.channel)
        val setMessageShow = context.resources.getString(R.string.notify_content, DateConverter.convertMillisToString(notif.dueDateMillis))

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifBuilder = NotificationCompat.Builder(context, channel)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentText(notif.title)
            .setContentText(setMessageShow)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getPendingIntent(notif))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notif = NotificationChannel(channel, "channelName", NotificationManager.IMPORTANCE_DEFAULT)
            notifBuilder.setChannelId(channel)
            notificationManagerCompat.createNotificationChannel(notif)
        }
        val setNotification = notifBuilder.build()
        notificationManagerCompat.notify(NOTIFY, setNotification)
    }
}
