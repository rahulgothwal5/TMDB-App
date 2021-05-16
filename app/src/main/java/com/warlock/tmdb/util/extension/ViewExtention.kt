package com.warlock.tmdb.util.extension

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.IntegerRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar


/**
 * Shows the system software keyboard.
 * @param requestFocus whether to request the focus on the specified [View].
 */
internal fun View.showKeyboard(requestFocus: Boolean = true) {
    if (requestFocus) {
        requestFocus()
    }

    this.context.showKeyboard(this)
}

/**
 * Hides the system software keyboard.
 *
 * @param clearFocus whether to remove the focus from the input field
 */
internal fun View.hideKeyboard(clearFocus: Boolean = true) {
    if (clearFocus) {
        clearFocus()
    }

    this.context.hideKeyboard(this)
}


/**
 * Cancels all the currently active animations associated with the specified [View].
 */
internal fun View.cancelActiveAnimations() {
    clearAnimation()
    animate().cancel()
}

/**
 * Used to show snack message
 */
@SuppressLint("ResourceType")
inline fun View.snack(
    @IntegerRes messageRes: Int, length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit
) {
    snack(resources.getString(messageRes), length, f)
}

/**
 * Used to show snack message
 */
inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}


/**
 * Used to show snack message
 */
@SuppressLint("ResourceType")
fun Snackbar.action(@IntegerRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

/**
 * Used to show snack message
 */
fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}

/**
 * Notify the widget that refresh state is refreshing. Do not call this when
 * refresh is triggered by a swipe gesture.
 */
fun SwipeRefreshLayout.startRefreshing() {
    isRefreshing = true
}

/**
 * Notify the widget that refresh state is not refreshing. Do not call this when
 * refresh is triggered by a swipe gesture.
 */
fun SwipeRefreshLayout.stopRefreshing() {
    isRefreshing = false
}


/**
 * * Inflate a new view hierarchy from the specified xml resource.
 * @receiver ViewGroup
 * @param layoutId  for an XML layout resource to load (e.g.,
 *        <code>R.layout.main_page</code>)
 * @param attachToRoot Whether the inflated hierarchy should be attached to
 *        the root parameter? If false, root is only used to create the
 *        correct subclass of LayoutParams for the root view in the XML.
 * @return The root View of the inflated hierarchy. If root was supplied and
 *         attachToRoot is true, this is root; otherwise it is the root of
 *         the inflated XML file.
 */
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)


/**
 * Used to set image from url
 * @receiver ImageView
 * @param url String
 * @return ViewTarget<(android.widget.ImageView..android.widget.ImageView?), (android.graphics.drawable.Drawable..android.graphics.drawable.Drawable?)>
 */
fun ImageView.loadImage(url: String) = Glide.with(this).load(url).into(this)

/**
 * used to set image with circle transformation
 * @receiver ImageView
 * @param  url to be set on imageView
 * @return ViewTarget<(android.widget.ImageView..android.widget.ImageView?), (android.graphics.drawable.Drawable..android.graphics.drawable.Drawable?)>
 */
fun ImageView.loadImageRound(url: String) =
    Glide.with(this).load(url).apply(RequestOptions.circleCropTransform()).into(this)

