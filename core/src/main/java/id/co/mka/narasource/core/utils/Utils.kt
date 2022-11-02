package id.co.mka.narasource.core.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

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
