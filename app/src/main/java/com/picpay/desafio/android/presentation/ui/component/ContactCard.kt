package com.picpay.desafio.android.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.R

@Composable
fun ContactCard(user: User, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(true, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            SubcomposeAsyncImage(
                model = user.img?.let {
                    ImageRequest.Builder(LocalContext.current)
                        .data(it)
                        .crossfade(true)
                        .build()
                } ?: R.drawable.error_paper,
                contentDescription = if (user.img != null) "User Profile Image" else "Error Image",
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(50.dp)),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("loading_indicator"),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
                error = {
                    Image(
                        painter = painterResource(R.drawable.error_paper),
                        contentDescription = "Erro ao carregar imagem",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = if (user.username != null) "@" + user.username else "Usuário não encontrado",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = user.name ?: "Nome não encontrado",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Preview
@Composable
fun ContactCardPreview() {
    ContactCard(
        user = User(
            id = 1,
            name = "John Doe",
            img = "https://randomuser.me/api/portraits/men/1.jpg",
            username = "johndoe",
        )
    )
}
