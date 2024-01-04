package email.phone.signinwithphonedemo.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


// TAG - classname prints in main method only but can't prints in retrofit inner methods or others
val Any.TAG: String
    get() {
//        val tag = javaClass.simpleName
//        return if (tag.length <= 23) tag else tag.substring(0, 23)
        return if (!javaClass.isAnonymousClass) {
            val name = javaClass.simpleName
            if (name.length <= 23) name else name.substring(0, 23)// first 23 chars
        } else {
            val name = javaClass.name
            if (name.length <= 23) name else name.substring(name.length - 23, name.length)// last 23 chars
        }
    }

fun RecyclerView.setManager(
    isItHorizontal: Boolean = false,
    isItGrid: Boolean = false,
    spanCount: Int = 2
) {
    if (isItGrid)
        this.layoutManager = GridLayoutManager(this.context, spanCount)
    else {
        if (isItHorizontal)
            this.layoutManager = LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
        else
            this.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
    }


}

/**
 * Set an onclick listener
 */
fun View.click(block: () -> Unit) = setOnClickListener { block.invoke() }


fun View.isVisible() = this.visibility == View.VISIBLE

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, duration).show()

fun Fragment.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this.context, message, duration).show()


fun Context.toast(message: () -> String) {
    Toast.makeText(this, message(), Toast.LENGTH_LONG).show()
}

fun Fragment.toast(message: () -> String) {
    Toast.makeText(this.context, message(), Toast.LENGTH_LONG).show()
}


fun AppCompatActivity.toast(message: () -> String, duration: () -> Int = { Toast.LENGTH_LONG }) {
    Toast.makeText(this.applicationContext, message(), duration()).show()
}

fun String.isValidEmail(): Boolean = this.isNotEmpty() &&
        Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}



inline fun <reified T : Any> Activity.launchActivity(
    options: Bundle? = null,
    finishAffinity: Boolean = false,
    noinline init: Intent.() -> Unit = {}
) {

    val intent = newIntent<T>(this)
    intent.init()
    if (finishAffinity) {
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        finishAffinity()
    }
    startActivity(intent)

}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)


fun Drawable.tint(context: Context, @ColorRes color: Int) {
    DrawableCompat.setTint(this, ContextCompat.getColor(context, color))
}

fun TextView.setCustomTypeface(context: Context, @FontRes font: Int) {
    val typefaceCustom = ResourcesCompat.getFont(context, font)
    this.typeface = typefaceCustom
}

/*
* Convert in String for EditText value
*/
fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun TextView.underline() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

/*Fragment*/
inline fun FragmentManager.addFragmentTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, tag: String) {
    supportFragmentManager.addFragmentTransaction { add(frameId, fragment, tag) }
}

inline fun FragmentManager.replaceFragmentTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, tag: String) {
    supportFragmentManager.replaceFragmentTransaction { replace(frameId, fragment, tag) }
}

fun Fragment.replaceFragment(fragment: Fragment, frameId: Int, tag: String) {
    childFragmentManager.replaceFragmentTransaction { replace(frameId, fragment, tag) }
}

fun TextView.drawUnderLine() {
    val text = SpannableString(this.text.toString())
    text.setSpan(UnderlineSpan(), 0, text.length, 0)
    this.text = text
}

/**
 * Wrapping try/catch to ignore catch block
 */
inline fun <T> justTry(block: () -> T) = try {
    block()
} catch (e: Throwable) {
}