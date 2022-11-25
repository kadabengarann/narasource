package id.co.mka.narasource.core.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.math.roundToInt

fun CircleImageView.loadImage(urls: String?) {
    Glide.with(this.context)
        .load(urls)
        .circleCrop()
        .into(this)
}

fun ImageView.loadImage(urls: String?) {
    Glide.with(this.context)
        .load(urls)
        .into(this)
}

fun getTimeStringFromDouble(time: Double): String {
    val resultInt = time.roundToInt()
    val hours = resultInt % 86400 / 3600
    val minutes = resultInt % 86400 % 3600 / 60
    val seconds = resultInt % 86400 % 3600 % 60

    return makeTimeString(hours, minutes, seconds)
}
private fun makeTimeString(hour: Int, min: Int, sec: Int): String = String.format("%02d:%02d:%02d", hour, min, sec)
