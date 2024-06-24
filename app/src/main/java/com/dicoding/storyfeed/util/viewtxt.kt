package com.dicoding.storyfeed.util

import android.widget.EditText

fun EditText.showError(message: String) {
    error = message
    requestFocus()
}