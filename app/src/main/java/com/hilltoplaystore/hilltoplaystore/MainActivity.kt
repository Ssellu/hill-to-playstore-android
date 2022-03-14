package com.hilltoplaystore.hilltoplaystore

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilltoplaystore.hilltoplaystore.ui.theme.HillToPlaystoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HillToPlaystoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainMenu()
                }
            }
        }
    }

    @Preview
    @Composable
    fun MainMenu() {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            MainMenuButton(onClick = {
                startActivity(
                    Intent(this@MainActivity, UserInfoActivity::class.java)
                )
            }, getString(R.string.menu_title_menu1))
            MainMenuButton(onClick = {
                startActivity(
                    Intent(this@MainActivity, PrescriptionScanningActivity::class.java)
                )
            }, getString(R.string.menu_title_menu2))
            MainMenuButton(onClick = {
                startActivity(
                    Intent(this@MainActivity, PrescriptionListActivity::class.java)
                )
            }, getString(R.string.menu_title_menu3))
            MainMenuButton(onClick = {
                startActivity(
                    Intent(this@MainActivity, RiderLocationInfoActivity::class.java)
                )
            }, getString(R.string.menu_title_menu4))
            MainMenuButton(onClick = {
                startActivity(
                    Intent(this@MainActivity, LocationActivity::class.java)
                )
            }, getString(R.string.menu_title_menu5))
        }

    }
}

@Composable
fun MainMenuButton(onClick: () -> Unit, text: String) {
    return OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text)
    }
}
