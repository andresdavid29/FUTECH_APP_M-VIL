package com.example.basedatosjugadores.server5

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.basedatosjugadores.R
import java.net.URLEncoder

@Composable
fun TablaScreen5(navController: NavController, viewModel: TablaViewModel5 = TablaViewModel5(LocalContext.current)) {
    val tablaState = viewModel.tabla.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Clasificación Ligue 1",
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.logo_ligue1),
                            contentDescription = "Logo Ligue1",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val tabla = tablaState.value) {
                is ResultState5.Loading -> {
                    CircularProgressIndicator()
                }
                is ResultState5.Success -> {
                    val equipos = tabla.data
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Encabezado
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(MaterialTheme.colors.primary),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Pos",
                                modifier = Modifier
                                    .weight(0.2f)
                                    .padding(start = 8.dp),
                                color = MaterialTheme.colors.onPrimary
                            )
                            Text(
                                text = "Equipo",
                                modifier = Modifier
                                    .weight(0.6f)
                                    .padding(start = 8.dp),
                                color = MaterialTheme.colors.onPrimary
                            )
                            Text(
                                text = "Pts",
                                modifier = Modifier.weight(0.2f),
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "GF",
                                modifier = Modifier.weight(0.2f),
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "GC",
                                modifier = Modifier.weight(0.2f),
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "GD",
                                modifier = Modifier.weight(0.2f),
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                        // Lista de equipos
                        LazyColumn {
                            itemsIndexed(equipos) { index, equipo ->
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .background(
                                                if (index % 2 == 0) MaterialTheme.colors.background
                                                else MaterialTheme.colors.surface.copy(alpha = 0.8f)
                                            )
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .weight(0.2f)
                                                .padding(start = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .width(4.dp)
                                                    .height(24.dp)
                                                    .background(getPositionColor(equipo.RL, equipos.size))
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = equipo.RL.toString(),
                                                style = MaterialTheme.typography.body2,
                                                color = MaterialTheme.colors.onSurface
                                            )
                                        }
                                        val logoResId = getLogoResource(equipo.Equipo)
                                        Image(
                                            painter = painterResource(id = logoResId),
                                            contentDescription = "${equipo.Equipo} Logo",
                                            modifier = Modifier
                                                .size(24.dp)
                                                .padding(end = 8.dp)
                                        )
                                        // Se añade clickable sobre el nombre del equipo
                                        Text(
                                            text = equipo.Equipo,
                                            modifier = Modifier
                                                .weight(0.6f)
                                                .padding(horizontal = 8.dp)
                                                .clickable {
                                                    navController.navigate(
                                                        "estadisticas5/${URLEncoder.encode(equipo.Equipo, "UTF-8")}"
                                                    )
                                                },
                                            style = MaterialTheme.typography.body2.copy(fontSize = 14.sp)
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = equipo.Pts.toString(),
                                            modifier = Modifier.weight(0.2f),
                                            style = MaterialTheme.typography.body2,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = equipo.GF.toString(),
                                            modifier = Modifier.weight(0.2f),
                                            style = MaterialTheme.typography.body2,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = equipo.GC.toString(),
                                            modifier = Modifier.weight(0.2f),
                                            style = MaterialTheme.typography.body2,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = equipo.DG.toString(),
                                            modifier = Modifier.weight(0.2f),
                                            style = MaterialTheme.typography.body2,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    Divider(color = Color.Gray, thickness = 1.dp)
                                }
                            }
                        }
                    }
                }
                is ResultState5.Error -> {
                    Text("Error: ${tabla.error}")
                }
            }
        }
    }
}

@Composable
fun getLogoResource(teamName: String): Int {
    val context = LocalContext.current
    val cleanedTeamName = teamName
        .lowercase()
        .replace(" ", "_")
        .replace("á", "a")
        .replace("é", "e")
        .replace("í", "i")
        .replace("ó", "o")
        .replace("ú", "u")
        .replace("'", "i")
        .replace("-", "7")
    return try {
        val resId = context.resources.getIdentifier(cleanedTeamName, "drawable", context.packageName)
        if (resId != 0) resId else R.drawable.placeholder
    } catch (e: Exception) {
        R.drawable.placeholder
    }
}

private fun getPositionColor(pos: Int, totalEquipos: Int): Color {
    return when {
        pos == 1 -> Color(0xFFFFD700)
        pos in 2..4 -> Color(0xFF1E90FF)
        pos == 5 -> Color(0xFFFF6C41)
        pos == 6 -> Color(0xFF4CAF50) // Ejemplo para fase previa Conference League
        pos > totalEquipos - 3 -> Color(0xFFFF0000)
        else -> Color.Transparent
    }
}
