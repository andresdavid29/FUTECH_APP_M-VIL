package com.example.basedatosjugadores.server4

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.basedatosjugadores.R
import com.example.basedatosjugadores.server3.TablaViewModel3
import com.example.basedatosjugadores.server4.ResultState4
import com.example.basedatosjugadores.server4.TablaViewModel4

@Composable
fun TablaScreen4(viewModel: TablaViewModel4 = TablaViewModel4(LocalContext.current)) {
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
                            text = "Clasificación Bundesliga",
                            modifier = Modifier.weight(1f) // Ocupa todo el espacio disponible excepto el reservado para la imagen
                        )

                        Image(
                            painter = painterResource(id = R.drawable.logo_bundesliga),
                            contentDescription = "Logo Bundesliga",
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
                is ResultState4.Loading -> {
                    CircularProgressIndicator()
                }
                is ResultState4.Success -> {
                    val equipos = tabla.data
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Encabezado de la tabla
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(MaterialTheme.colors.primary),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Pos", Modifier.weight(0.2f), color = MaterialTheme.colors.onPrimary)
                            Text("Equipo", Modifier.weight(0.6f), color = MaterialTheme.colors.onPrimary)
                            Text("Pts", Modifier.weight(0.2f), color = MaterialTheme.colors.onPrimary, textAlign = TextAlign.Center)
                            Text("GF", Modifier.weight(0.2f), color = MaterialTheme.colors.onPrimary, textAlign = TextAlign.Center)
                            Text("GC", Modifier.weight(0.2f), color = MaterialTheme.colors.onPrimary, textAlign = TextAlign.Center)
                            Text("GD", Modifier.weight(0.2f), color = MaterialTheme.colors.onPrimary, textAlign = TextAlign.Center)
                        }

                        // Filas dinámicas para cada equipo
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
                                        // Contenedor para la posición con línea de color
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

                                        // Mostrar logo del equipo
                                        val logoResId = getLogoResource(equipo.Equipo)
                                        Image(
                                            painter = painterResource(id = logoResId),
                                            contentDescription = "${equipo.Equipo} Logo",
                                            modifier = Modifier
                                                .size(24.dp)
                                                .padding(end = 8.dp)
                                        )

                                        // Mostrar nombre del equipo
                                        Text(
                                            text = equipo.Equipo,
                                            modifier = Modifier
                                                .weight(0.6f)
                                                .padding(horizontal = 8.dp),
                                            style = MaterialTheme.typography.body2.copy(fontSize = 14.sp)
                                        )

                                        Spacer(modifier = Modifier.width(16.dp)) // Más espacio entre "Equipo" y "Pts"
                                        Text(
                                            text = equipo.Pts.toString(),
                                            modifier = Modifier.weight(0.2f),
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = equipo.GF.toString(),
                                            modifier = Modifier.weight(0.2f),
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = equipo.GC.toString(),
                                            modifier = Modifier.weight(0.2f),
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = equipo.DG.toString(),
                                            modifier = Modifier.weight(0.2f),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    Divider(color = Color.Gray, thickness = 1.dp)
                                }
                            }
                        }
                    }
                }
                is ResultState4.Error -> {
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
        .replace(" ", "_") // Reemplazar espacios por guiones bajos
        .replace("á", "a")
        .replace("é", "e")
        .replace("í", "i")
        .replace("ó", "o")
        .replace("ú", "u")
        .replace("'","i")
        .replace(".","7")

    return try {
        // Obtiene el ID del recurso de la imagen
        val resId = context.resources.getIdentifier(cleanedTeamName, "drawable", context.packageName)
        if (resId != 0) resId else R.drawable.placeholder // Si no existe, usamos un placeholder
    } catch (e: Exception) {
        R.drawable.placeholder // Si ocurre un error, devolvemos un recurso por defecto
    }
}

private fun getPositionColor(pos: Int, totalEquipos: Int): Color {
    return when {
        pos == 1 -> Color(0xFFFFD700) // Oro (campeón)
        pos in 2..4 -> Color(0xFF1E90FF) // Azul (Champions League)
        pos == 5 -> Color(0xFFFF6C41) // Verde (Europa League)
        pos == 6 -> Color(0xFF4CAF50) // Fase previa Conference League
        pos == 16 -> Color(0xFFFFEB3B) // Play Off descenso
        pos > totalEquipos - 2 -> Color(0xFFFF0000) // Rojo (Descenso)
        else -> Color.Transparent // Default (sin color)
    }
}
