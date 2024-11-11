package com.picpay.desafio.android.presentation.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.R
import com.picpay.desafio.android.presentation.ui.theme.DesafioAndroidPicPayTheme

@Composable
fun ContactDetailBottomSheet(user: User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = user.img,
                contentDescription = "Foto de ${user.name ?: "usuário sem foto"}",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.ic_round_account_circle)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Nome:",
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.semantics { contentDescription = "Nome do usuário" }
        )
        Text(
            text = user.name ?: "Nome não disponível",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .semantics {
                    contentDescription = "Nome do usuário: ${user.name ?: "não disponível"}"
                }
        )

        Text(
            text = "Usuário:",
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.semantics { contentDescription = "Nome de usuário" }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .align(Alignment.Start)
                .fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = if (user.username != null) "@${user.username}" else "usuário não encontrado",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.semantics {
                        contentDescription =
                            if (user.username != null) "Usuário: @${user.username}" else "usuário não encontrado"
                    }
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier
                        .height(40.dp)
                        .align(Alignment.End)
                ) {
                    Text(text = "Seguir", fontSize = 14.sp)
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ContactDetailBottomSheetPreview() {
    DesafioAndroidPicPayTheme {
        ContactDetailBottomSheet(
            User(
                id = 1,
                name = "Guilherme Reis",
                img = "https://randomuser.me/api/portraits/men/1.jpg",
                username = "guilhermereis",
            )
        )
    }
}
