package com.example.basedatosjugadores.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.basedatosjugadores.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MenuScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    var competicionesExpanded by remember { mutableStateOf(false) }

    val ligas = listOf(
        Liga("Liga Española", R.drawable.logo_la_liga, "tabla2"),
        Liga("Premier League", R.drawable.logo_premier, "tabla1"),
        Liga("Serie A", R.drawable.logo_serie_a, "tabla3"),
        Liga("Bundesliga", R.drawable.logo_bundesliga, "tabla4"),
        Liga("Ligue 1", R.drawable.logo_ligue1, "tabla5"),
        Liga("Champions League", R.drawable.logo_champions, "tabla6"),
        Liga("Europa League", R.drawable.logo_europa, "tabla7")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.futech_logo),
                        contentDescription = "Logo Futech",
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = "Futech",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                backgroundColor = Color(0xFF215679),
                actions = {
                    if (currentUser != null) {
                        IconButton(onClick = { navController.navigate("favoritos_screen") }) {
                            Icon(Icons.Filled.Star, contentDescription = "Favoritos", tint = Color.Yellow)
                        }
                    }
                    IconButton(onClick = {
                        if (currentUser != null) navController.navigate("session_verification")
                        else navController.navigate("login_screen")
                    }) {
                        Icon(Icons.Filled.Person, contentDescription = "Usuario", tint = Color.White)
                    }
                }
            )
        },
        backgroundColor = Color(0xFF272e3f),
        bottomBar = {
            BottomAppBar(
                backgroundColor = Color(0xFF215679),
                contentPadding = PaddingValues(horizontal = 32.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* no-op */ }) {
                        Icon(Icons.Filled.Home, contentDescription = "Inicio", tint = Color.White)
                    }
                    Box {
                        IconButton(onClick = { competicionesExpanded = true }) {
                            Icon(Icons.Filled.SportsSoccer, contentDescription = "Competiciones", tint = Color.White)
                        }
                        DropdownMenu(
                            expanded = competicionesExpanded,
                            onDismissRequest = { competicionesExpanded = false }
                        ) {
                            ligas.forEach { liga ->
                                DropdownMenuItem(onClick = {
                                    competicionesExpanded = false
                                    if (auth.currentUser != null) navController.navigate(liga.route)
                                    else navController.navigate("session_verification")
                                }) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = liga.logoRes),
                                            contentDescription = liga.name,
                                            modifier = Modifier
                                                .size(24.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(text = liga.name)
                                    }
                                }
                            }
                        }
                    }
                    IconButton(onClick = {
                        if (auth.currentUser != null) navController.navigate("favoritos_screen")
                        else navController.navigate("session_verification")
                    }) {
                        Icon(Icons.Filled.Star, contentDescription = "Favoritos", tint = Color.White)
                    }
                    IconButton(onClick = {
                        if (currentUser != null) navController.navigate("session_verification")
                        else navController.navigate("login_screen")
                    }) {
                        Icon(Icons.Filled.Person, contentDescription = "Usuario", tint = Color.White)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido a Futech",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2de0cb),
                modifier = Modifier.padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )

            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                items(ligas.size) { index ->
                    val liga = ligas[index]
                    Column(
                        Modifier
                            .padding(horizontal = 12.dp)
                            .clickable {
                                if (auth.currentUser != null) navController.navigate(liga.route)
                                else navController.navigate("session_verification")
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF215679))
                                .border(3.dp, Color(0xFF2de0cb), CircleShape)
                                .shadow(4.dp, CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = liga.logoRes),
                                contentDescription = liga.name,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = liga.name,
                            fontSize = 12.sp,
                            color = Color(0xFFc0e1ec),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionButton(
                    text = "Noticias",
                    icon = { Icon(Icons.Filled.Newspaper, contentDescription = "Noticias", tint = Color.Yellow) },
                    backgroundColor = Color(0xFF2de0cb),
                    onClick = { /* Acción noticias */ }
                )
                ActionButton(
                    text = "Favoritos",
                    icon = { Icon(Icons.Filled.Star, contentDescription = "Favoritos", tint = Color.Yellow) },
                    backgroundColor = Color(0xFF224d63),
                    onClick = {
                        if (auth.currentUser != null) navController.navigate("favoritos_screen")
                        else navController.navigate("session_verification")
                    }
                )
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Explora las mejores ligas de fútbol 🌍",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFc0e1ec),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun ActionButton(text: String, icon: @Composable () -> Unit, backgroundColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        Modifier
            .padding(8.dp)
            .size(140.dp, 48.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            icon()
            Spacer(Modifier.width(8.dp))
            Text(text = text, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

data class Liga(val name: String, val logoRes: Int, val route: String)
