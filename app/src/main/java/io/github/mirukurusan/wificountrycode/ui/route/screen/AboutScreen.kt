package io.github.mirukurusan.wificountrycode.ui.route.screen

import android.content.Intent
import android.icu.text.DateFormat
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.mirukurusan.wificountrycode.BuildConfig
import io.github.mirukurusan.wificountrycode.R
import io.github.mirukurusan.wificountrycode.ui.components.ScrollColumn
import io.github.mirukurusan.wificountrycode.ui.components.SmallTitle
import io.github.mirukurusan.wificountrycode.ui.components.TextContent

/**
 * About screen for displaying app information
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen() {
    val context = LocalContext.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) }
            )
        }
    ) { paddingValues ->
        ScrollColumn(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                start = 24.dp,
                end = 24.dp
            ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // App Icon
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Wifi,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9F),
                    textAlign = TextAlign.Center
                )
            }

            // Description
            SmallTitle { stringResource(R.string.about_item_title_description) }
            Text(
                text = stringResource(R.string.about_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Version
            SmallTitle { stringResource(R.string.about_item_title_version) }
            TextContent { "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})" }

            // Build Time
            SmallTitle { stringResource(R.string.about_item_title_build_time) }
            val dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            val date = dateFormat.format(BuildConfig.BUILD_TIME)
            TextContent { date }

            // Author
            SmallTitle { stringResource(R.string.about_item_title_author) }
            LinkText(
                text = "Mirukurusan",
                url = "https://github.com/mirukurusan"
            )

            // Project
            SmallTitle { stringResource(R.string.about_item_title_project) }
            LinkText(
                text = "github.com/mirukurusan/WifiCountryCode",
                url = "https://github.com/mirukurusan/WifiCountryCode"
            )

            Spacer(modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
private fun LinkText(
    text: String,
    url: String
) {
    val context = LocalContext.current
    
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
            .padding(vertical = 2.dp)
    )
}
