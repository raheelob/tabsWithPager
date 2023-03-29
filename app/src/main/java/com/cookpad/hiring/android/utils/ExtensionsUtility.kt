package com.cookpad.hiring.android.utils

import android.content.Context
import android.widget.Toast

fun showToast(context: Context, messageToShow: String) {
    Toast.makeText(context, messageToShow, Toast.LENGTH_SHORT).show()
}

fun showDialog(mDialog: ProgressDialog){
    mDialog.showDialog()
}

fun hideDialog(mDialog: ProgressDialog){
    mDialog.hideDialog()
}
