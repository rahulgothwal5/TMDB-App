package com.warlock.tmdb.util

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.warlock.tmdb.R
import java.io.File
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*



object CommonUtils {

    @SuppressLint("all")
    fun getDeviceId(context: Context): String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)


    fun isEmailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()


    fun getCache(activity: AppCompatActivity): File {
        createRootDirs(activity)
        return activity.cacheDir
    }

    private fun createRootDirs(activity: AppCompatActivity) {
        val filePath =
            File("${activity.cacheDir}/${activity.getString(R.string.app_name)}/${AppConstants.APP_FILE_FOLDER}")
        if (filePath.exists().not()) {
            filePath.mkdirs()
        }
    }

    @Throws(IOException::class)
    fun loadJSONFromAsset(context: Context, jsonFileName: String): String {
        val manager = context.assets
        val inputStream = manager.open(jsonFileName)

        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        return String(buffer)
    }


    @SuppressLint("StringFormatMatches", "StringFormatInvalid")
    fun getProperTimeFromMilli(context: Context, neededTimeMilis: Long): String {

        val minutes = neededTimeMilis / 1000 / 60
        val seconds = neededTimeMilis / 1000 % 60

        return when {
            minutes.toString().toInt() == 1 -> if (seconds < 10) {
                context.getString(R.string.minute, minutes, "0$seconds")
            } else {
                context.getString(R.string.minute, minutes, seconds.toString())
            }
            minutes.toString().toInt() > 1 -> if (seconds < 10) {
                context.getString(R.string.minutes, minutes, "0$seconds")
            } else {
                context.getString(R.string.minutes, minutes, seconds)
            }
            else -> context.getString(R.string.seconds, seconds)
        }
    }


    /**
     * Method checks if the app is in background or not
     */
    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess.equals(context.packageName)) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo.get(0).topActivity
            if (componentInfo?.packageName.equals(context.packageName)) {
                isInBackground = false
            }
        }

        return isInBackground
    }

    private fun getFormattedDateForUI(
        inputFormat: String,
        outputFormat: String,
        time: String
    ): String {
        try {
            val serverFormat =
                SimpleDateFormat(inputFormat, Locale.getDefault())
            serverFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = serverFormat.parse(time)
            val dateFormatForUi = SimpleDateFormat(outputFormat, Locale.getDefault())
            dateFormatForUi.timeZone = TimeZone.getDefault()
            return dateFormatForUi.format(date)
        } catch (pe: ParseException) {
            pe.printStackTrace()
        }
        return time
    }


    fun deleteCache(context: Context) {
        try {
            val dir = context.cacheDir
            deleteDir(dir)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }


}