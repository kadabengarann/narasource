package id.co.mka.narasource.core.utils

import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

fun CircleImageView.loadImage(urls: String?) {
    Glide.with(this.context)
        .load(urls)
        .circleCrop()
        .into(this)
}
