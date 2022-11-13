package com.dobashi.todolist_jetpack.other

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dobashi.todolist_jetpack.R
import com.dobashi.todolist_jetpack.TodoApplication
import com.dobashi.todolist_jetpack.model.ToDoModel

class Notification {
    companion object {

        private const val NOTIFICATION_CHANNEL_ID = "com.example.todolist"
        private const val NOTIFICATION_CHANNEL_NAME = "todolist"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION = "期限がきたら通知を表示します"
        private const val NOTIFICATION_TITLE = "期限切れのTodoがあります"

        fun createChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    importance
                ).apply {
                    description = NOTIFICATION_CHANNEL_DESCRIPTION
                }
                val manager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(channel)
            }
        }

        fun sendNotification(context: Context, message: String, createTime: String) {
            if (createTime.isEmpty()) return
            val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_launcher_foreground)
                setContentTitle(NOTIFICATION_TITLE)
                setContentText(message)

                val intent = Intent().apply {
                    this.data =
                        Uri.parse("todolist_jetpack://${DetailDestination.route}/${createTime}")
                }

                val pending: PendingIntent = PendingIntent.getActivity(
                    context,
                    createTime.toInt(),
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
                setContentIntent(pending)
                setAutoCancel(true)
                priority = NotificationCompat.PRIORITY_DEFAULT
            }

            NotificationManagerCompat.from(context).notify(createTime.toInt(), builder.build())
        }


        /**
         * ローカル通知を設定する
         * @param context
         * @param model
         */
        fun setNotification(
            context: Context,
            model: ToDoModel,
        ) {
            val dateList = model.todoDate.split("/")
            val timeList = model.todoTime.split(":")

            val month = dateList[1].toInt()
            val day = dateList[2].toInt()
            val hour = timeList[0].toInt()
            val min = timeList[1].toInt()

            val calender = Calendar.getInstance()

            val intent = Intent(context, Receiver::class.java).apply {
                putExtra("title", model.toDoName)
                putExtra("createTime", model.createTime)
            }

            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    model.createTime.toInt(),
                    intent,
                    PendingIntent.FLAG_MUTABLE
                )
            calender.set(Calendar.MONTH, month - 1)
            calender.set(Calendar.DAY_OF_MONTH, day)
            calender.set(Calendar.HOUR_OF_DAY, hour)
            calender.set(Calendar.MINUTE, min)
            calender.set(Calendar.SECOND, 0)

            val alarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC, calender.timeInMillis, pendingIntent)
        }


        /**
         * ModelからTodoを全件取得してから、ローカル通知を全件キャンセルする
         * @param context
         */
        suspend fun cancelAllNotification(context: Context) {
            val intent = Intent(context, Receiver::class.java)
            val model = TodoApplication.database.todoDao().getAll()
            model.forEach {
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    it.createTime.toInt(),
                    intent,
                    PendingIntent.FLAG_MUTABLE
                )
                val alarmManager =
                    context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(pendingIntent)
            }
        }


        /**
         * ローカル通知をキャンセルする
         * @param context
         * @param createTime キャンセル通知設定の識別値
         */
        fun cancelNotification(context: Context, createTime: String) {
            val intent = Intent(context, Receiver::class.java)
            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    createTime.toInt(),
                    intent,
                    PendingIntent.FLAG_MUTABLE
                )

            val alarmManager =
                context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }
    }
}