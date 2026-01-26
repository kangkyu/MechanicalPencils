package com.lininglink.mechanicalpencils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lininglink.mechanicalpencils.ui.navigation.AppNavGraph
import com.lininglink.mechanicalpencils.ui.theme.MechanicalPencilsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MechanicalPencilsTheme {
                AppNavGraph()
            }
        }
    }
}
