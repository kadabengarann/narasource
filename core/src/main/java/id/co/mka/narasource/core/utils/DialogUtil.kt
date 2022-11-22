package id.co.mka.narasource.core.utils

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import id.co.mka.narasource.core.R

object DialogUtil {
    fun showDialog(context: Context, title: String?, message: String, positiveButton: String, negativeButton: String?, positiveButtonClick: DialogInterface.OnClickListener?) {
        MaterialAlertDialogBuilder(context, R.style.Theme_NaraSource_AlertDialog).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(positiveButton, positiveButtonClick)
            setNegativeButton(negativeButton, null)
            create()
            show()
        }
    }
}
