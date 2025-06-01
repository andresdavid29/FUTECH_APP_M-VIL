package com.example.basedatosjugadores.server7

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

@Composable
fun TablaScreen7(viewModel: TablaViewModel7 = TablaViewModel7(LocalContext.current)) {
    val tablaState = viewModel.tabla.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Título
                        Text(
                            text = "Clasificación Europa League",
                            modifier = Modifier.weight(1f)
                        )
                        // Icono del logo
                        Icon(
                            painter = painterResource(id = R.drawable.logo_europa),
                            contentDescription = "Logo Europa League",
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
                is ResultState7.Loading -> {
                    CircularProgressIndicator()
                }
                is ResultState7.Success -> {
                    val equipos = tabla.data
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        // Encabezado de la tabla
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
                                    .weight(0.3f)
                                    .padding(start = 8.dp),
                                color = MaterialTheme.colors.onPrimary
                            )
                            Text(
                                text = "Equipo",
                                modifier = Modifier
                                    .weight(0.4f)
                                    .padding(start = 8.dp),
                                color = MaterialTheme.colors.onPrimary
                            )
                            Text(
                                text = "Pts",
                                modifier = Modifier.weight(0.1f),
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "GF",
                                modifier = Modifier.weight(0.1f),
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "GC",
                                modifier = Modifier.weight(0.1f),
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "GD",
                                modifier = Modifier.weight(0.1f),
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
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
                                        // Contenedor para la posición
                                        Row(
                                            modifier = Modifier
                                                .weight(0.1f)
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

                                        // Extraer siglas y nombre del equipo
                                        val (countryCode, teamName) = equipo.Equipo.split(" ", limit = 2)

                                        // Mostrar bandera
                                        val flagResId = getFlagResource(countryCode)
                                        Image(
                                            painter = painterResource(id = flagResId),
                                            contentDescription = "Bandera $countryCode",
                                            modifier = Modifier
                                                .size(24.dp)
                                                .padding(end = 8.dp)
                                        )

                                        // Mostrar logo del equipo
                                        val logoResId = getLogoResource(teamName)
                                        Image(
                                            painter = painterResource(id = logoResId),
                                            contentDescription = "Logo $teamName",
                                            modifier = Modifier
                                                .size(24.dp)
                                                .padding(end = 8.dp)
                                        )

                                        // Nombre del equipo
                                        Text(
                                            text = teamName,
                                            modifier = Modifier
                                                .weight(0.4f)
                                                .padding(horizontal = 8.dp),
                                            style = MaterialTheme.typography.body2.copy(fontSize = 14.sp)
                                        )

                                        // Estadísticas
                                        Text(
                                            text = equipo.Pts.toString(),
                                            modifier = Modifier.weight(0.1f),
                                            style = MaterialTheme.typography.body2,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = equipo.GF.toString(),
                                            modifier = Modifier.weight(0.1f),
                                            style = MaterialTheme.typography.body2,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = equipo.GC.toString(),
                                            modifier = Modifier.weight(0.1f),
                                            style = MaterialTheme.typography.body2,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = equipo.DG.toString(),
                                            modifier = Modifier.weight(0.1f),
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
                is ResultState7.Error -> {
                    Text("Error: ${tabla.error}")
                }
            }
        }
    }
}

// Función para obtener el recurso de la bandera
@Composable
fun getFlagResource(countryCode: String): Int {
    val context = LocalContext.current
    return context.resources.getIdentifier(
        countryCode.lowercase(), "drawable", context.packageName
    ).takeIf { it != 0 } ?: R.drawable.placeholder
}

// Función para obtener el recurso del logo
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
        .replace("'","")
        .replace("ø","0")
        .replace("/","1")
        .replace("ň","2")
        .replace("ī","5")
        .replace("ö","6")
        .replace("ş","4")
        .replace("ç","3")
        .replace("ğ","7")
    return context.resources.getIdentifier(cleanedTeamName, "drawable", context.packageName)
        .takeIf { it != 0 } ?: R.drawable.placeholder
}

// Función para determinar el color de la posición
private fun getPositionColor(pos: Int, totalEquipos: Int): Color {
    return when {
        pos in 1..8 -> Color(0xFF1E90FF) // Azul (Octavos)
        pos in 9..24 -> Color(0xFF4CAF50) // Azul (Dieciseisavos)
        pos in 25..36 -> Color(0xFFFF0000) // Azul (Out)
        else -> Color.Transparent
    }
}
