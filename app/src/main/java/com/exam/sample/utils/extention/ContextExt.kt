package com.exam.sample.utils.extention

import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AlertDialog.Builder
import com.exam.sample.R


fun Context.alertDialog(title: String = getString(R.string.dialog_default_title),
                           msg: String,
                           ok: String = getString(R.string.dialog_default_ok),
                           cancel: String = getString(R.string.dialog_default_cancel),
                           onClickOK: () -> Unit,
                           onClickCancel: () -> Unit
): AlertDialog
{
    val builder: AlertDialog.Builder = Builder(this)
    builder.setTitle(title).setMessage(msg)
    builder.setPositiveButton(ok,
        DialogInterface.OnClickListener { _, _ ->
            onClickOK()
        })
    builder.setNegativeButton(cancel,
        DialogInterface.OnClickListener { _, _ ->
            onClickCancel()
        })
    val alertDialog: AlertDialog = builder.create()
    alertDialog.show()
    return alertDialog
}