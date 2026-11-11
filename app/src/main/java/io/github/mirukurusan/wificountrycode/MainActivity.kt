package io.github.mirukurusan.wificountrycode

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.mirukurusan.wificountrycode.ui.GlobalSnackbarHost
import io.github.mirukurusan.wificountrycode.ui.route.NavigationHost
import io.github.mirukurusan.wificountrycode.ui.theme.WifiCountryCodeTheme


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun App() {
    WifiCountryCodeTheme {
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = GlobalSnackbarHost.state,
                    modifier = Modifier.systemBarsPadding(),
                ) { data ->
                    val containerStateColor =
                        if (GlobalSnackbarHost.onError.value) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primary
                    Snackbar(
                        modifier = Modifier.systemBarsPadding(),
                        snackbarData = data,
                        containerColor = containerStateColor,
                        shape = MaterialTheme.shapes.medium,
                    )
                }
            }
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                NavigationHost()
            }
        }
    }
}