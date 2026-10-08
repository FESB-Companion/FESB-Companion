package com.tstudioz.fax.fme.feature.home.compose

import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.tstudioz.fax.fme.R
import com.tstudioz.fax.fme.theme.AppTheme
import com.tstudioz.fax.fme.theme.notesContainer

@Composable
fun GithubMessage(hideGithubMessage: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(notesContainer)
    ) {
        val context = LocalContext.current
        val link = stringResource(R.string.github_repo_url)
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    try {
                        context.startActivity(Intent(Intent.ACTION_VIEW, link.toUri()))
                    } catch (e: ActivityNotFoundException) {
                        Log.e("GithubMessage", "No activity found to handle URL", e)
                    }
                }
                .padding(start = 16.dp, top = 12.dp, end = 8.dp, bottom = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.github),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.fesb_companion_je_sada_na_githubu),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.istra_i_projekt_ili_doprinesi_njegovom_razvoju_na_githubu),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        IconButton(
            onClick = hideGithubMessage,
            modifier = Modifier.padding(end = 4.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.close_x),
                contentDescription = stringResource(R.string.close),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview
@Composable
fun GithubPreview() {
    AppTheme {
        Scaffold {
            Column(Modifier.padding(it)) { GithubMessage() }
        }
    }
}
