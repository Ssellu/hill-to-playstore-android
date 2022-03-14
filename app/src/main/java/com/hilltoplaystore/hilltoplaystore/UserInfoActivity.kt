package com.hilltoplaystore.hilltoplaystore

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hilltoplaystore.hilltoplaystore.ui.theme.HillToPlaystoreTheme

class UserInfoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HillToPlaystoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun Greeting() {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            val textState = remember {
                mutableStateOf(
                    TextFieldValue(
                        getUserTelFromDeviceStorage(context = this@UserInfoActivity)
                    )
                )
            }

            OutlinedTextField(
                value = textState.value,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = getString(R.string.menu1_tel)) },
                visualTransformation = PhoneNumberTransformation(),
                onValueChange = {
                    textState.value = it
                })

            MainMenuButton(
                onClick = {
                    MaterialAlertDialogBuilder(this@UserInfoActivity)
                        .setTitle(getString(R.string.menu1_dialog_title))
                        .setMessage(
                            String.format(
                                getString(
                                    R.string.menu1_dialog_message,
                                    textState.value.text
                                )
                            )
                        )
                        .setPositiveButton(resources.getString(R.string.menu1_dialog_accept)) { _, _ ->
                            this@UserInfoActivity.finish()
                        }
                        .show()
                    saveUserTel(textState.value.text)
                }, "연락처 저장"
            )
        }

    }

    private fun saveUserTel(text: String) {
        getSharedPreferences(
            getString(R.string.preference_file_key),
            MODE_PRIVATE
        ).edit()
            .putString(getString(R.string.preference_user_tel_key), text)
            .apply()
    }

}

class PhoneNumberTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return phoneNumFilter(text)
    }
}

fun phoneNumFilter(text: AnnotatedString): TransformedText {

    val trimmed = text.text
    var out = ""
    for (i in trimmed.indices) {
        out += trimmed[i]
        if (i == 2 || i == 6) out += "-"

    }

    val phoneNumberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return if (offset <= 2)
                offset
            else if (offset <= 6)
                offset + 1
            else
                offset + 2
        }

        override fun transformedToOriginal(offset: Int): Int {

            return if (offset <= 3)
                offset
            else if (offset <= 7)
                offset - 1
            else
                offset - 2
        }
    }

    return TransformedText(AnnotatedString(out), phoneNumberOffsetTranslator)
}

fun getUserTelFromDeviceStorage(context: Context) =
    context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        ComponentActivity.MODE_PRIVATE
    ).getString(
        context.getString(R.string.preference_user_tel_key),
        context.getString(R.string.preference_user_tel_value_default)
    )!!