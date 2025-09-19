package com.example.class3

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.runtime.mutableStateOf
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.class3.ui.theme.Class3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Class3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EnvioDineroUI(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    EnvioDineroUI(modifier = Modifier.padding(16.dp))
}


@Composable
fun EnvioDineroUI(modifier: Modifier = Modifier) {
    var nombre by remember { mutableStateOf("") }
    var montoEnviar by remember { mutableStateOf("") }
    var montoActual by remember { mutableStateOf(1000) } // ahora es estado mutable

    var mensaje by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Formulario con campos
        FormularioEnvio(
            nombre = nombre,
            onNombreChange = { nombre = it },
            montoEnviar = montoEnviar,
            onMontoEnviarChange = { montoEnviar = it },
            montoActual = montoActual
        )

        Spacer(Modifier.height(16.dp))

        // Botón
        BotonRegistrar(
            onRegistrar = {
                val monto = montoEnviar.toIntOrNull()
                if (nombre.isNotBlank() && monto != null && monto <= montoActual) {
                    montoActual -= monto // 🔥 restar del saldo actual
                    mensaje = "Se envió: $nombre, monto $monto"
                    montoEnviar = "" // limpiar campo
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        // Mensaje temporal
        MensajeEnvio(mensaje = mensaje, onTimeout = { mensaje = null })
    }
}
@Composable
fun FormularioEnvio(
    nombre: String,
    onNombreChange: (String) -> Unit,
    montoEnviar: String,
    onMontoEnviarChange: (String) -> Unit,
    montoActual: Int
) {
    OutlinedTextField(
        value = nombre,
        onValueChange = onNombreChange,
        label = { Text("Nombre del destinatario") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(12.dp))

    OutlinedTextField(
        value = montoEnviar,
        onValueChange = onMontoEnviarChange,
        label = { Text("Monto a enviar") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(12.dp))

    OutlinedTextField(
        value = montoActual.toString(),
        onValueChange = {},
        label = { Text("Monto actual") },
        readOnly = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun BotonRegistrar(onRegistrar: () -> Unit) {
    Button(
        onClick = onRegistrar,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Registrar")
    }
}

@Composable
fun MensajeEnvio(mensaje: String?, onTimeout: () -> Unit) {
    mensaje?.let {
        Text(
            text = it,
            color = Color.Green,
            fontWeight = FontWeight.Bold
        )

        LaunchedEffect(it) {
            kotlinx.coroutines.delay(3000)
            onTimeout()
        }
    }
}
