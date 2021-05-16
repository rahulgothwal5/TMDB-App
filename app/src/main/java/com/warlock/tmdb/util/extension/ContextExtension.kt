package com.warlock.tmdb.util.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

/**
 * Shows the software keyboard (if possible).
 */
fun Context.showKeyboard(view: View) {
    (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.showSoftInput(
        view,
        0
    )
}


/**
 * Hides the software keyboard (if possible)
 */
fun Context.hideKeyboard(view: View) {
    (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.hideSoftInputFromWindow(
        view.windowToken,
        0
    )
}

/**
 * Make a standard toast that just contains a text view. it will display a message for a LENGTH_SHORT
 * @receiver context  The context to use.
 * @param message    The text to show.  Can be formatted text.
 */
fun Context.shortToast(message: String) {
    toast(message, Toast.LENGTH_SHORT)
}

/**
 * Make a standard toast that just contains a text view. it will display a message for a LENGTH_LONG
 * @receiver context  The context to use.
 * @param message     The text to show.  Can be formatted text.
 */
fun Context.longToast(message: String) {
    toast(message, Toast.LENGTH_LONG)
}

/**
 * Make a standard toast that just contains a text view.
 * @receiver context  The context to use.
 * @param message     The text to show.  Can be formatted text.
 * @param duration   How long to display the message.  Either {@link #LENGTH_SHORT} or
 *                   {@link #LENGTH_LONG}
 */
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(
        this,
        message,
        duration
    ).show()
}