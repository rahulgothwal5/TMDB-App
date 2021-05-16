package com.warlock.tmdb.util

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Toast.makeText
import com.google.android.material.snackbar.Snackbar


object Toast {
    fun showToast(activity: Activity, message: String) {
        val toast =
            makeText(activity, message, android.widget.Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM, 0, 0)
        toast.show()
        //        android.widget.Toast.makeText(activity, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    fun showToastLong(activity: Activity, message: String) {
        makeText(activity, message, android.widget.Toast.LENGTH_LONG).show()
    }

    /*********************
     * show toast message
     */
    fun showToast(context: Context, message: String) {
        val toast =
            makeText(context, message, android.widget.Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM, 0, 100)
        toast.show()
        //        android.widget.Toast.makeText(context, "" + message, android.widget.Toast.LENGTH_SHORT).show();
    }

    /*********************
     * show SnackBar message
     */
    fun showSnackBar(view: Activity, message: String) {

        Snackbar.make(view.findViewById<View>(android.R.id.content), message, Snackbar.LENGTH_SHORT)
            .show()
    }
}
