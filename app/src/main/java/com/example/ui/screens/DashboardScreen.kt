package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.FinanceViewModel

@Composable
fun DashboardScreen(viewModel: FinanceViewModel) {
    val pockets by viewModel.pockets.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    
    val totalBalance = pockets.sumOf { it.balance }
    
    // Calculate simple cash flow
    val incomes = transactions.filter { it.type == "INCOME" }.sumOf { it.amount }
    val expenses = transactions.filter { it.type == "EXPENSE" }.sumOf { it.amount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Overview", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Total Balance", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("$${String.format("%.2f", totalBalance)}", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Income", style = MaterialTheme.typography.labelLarge)
                    Text("+$${String.format("%.2f", incomes)}", color = Color(0xFF4CAF50), style = MaterialTheme.typography.titleLarge)
                }
            }
            Card(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Expense", style = MaterialTheme.typography.labelLarge)
                    Text("-$${String.format("%.2f", expenses)}", color = Color(0xFFE53935), style = MaterialTheme.typography.titleLarge)
                }
            }
        }
        
        Text("Activity Simulation (Last 7 Days)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        
        // Simple Canvas Chart with Animation
        val animatedProgress = remember { Animatable(0f) }
        LaunchedEffect(Unit) {
            animatedProgress.animateTo(1f, animationSpec = tween(durationMillis = 1500))
        }
        
        Card(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)) {
            val primaryColor = MaterialTheme.colorScheme.primary
            Canvas(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                
                val w = size.width
                val h = size.height
                
                // Draw some dummy path for smooth simulation look
                val path = Path().apply {
                    moveTo(0f, h * 0.8f)
                    cubicTo(w * 0.2f, h * 0.9f, w * 0.4f, h * 0.4f, w * 0.6f, h * 0.6f)
                    cubicTo(w * 0.8f, h * 0.8f, w * 0.9f, h * 0.2f, w, h * 0.3f)
                }
                
                // Clip the path based on animation progress
                val measure = androidx.compose.ui.graphics.PathMeasure()
                measure.setPath(path, false)
                val length = measure.length
                val animPath = Path()
                measure.getSegment(0f, length * animatedProgress.value, animPath, true)
                
                drawPath(
                    path = animPath,
                    color = primaryColor,
                    style = Stroke(width = 4.dp.toPx())
                )
            }
        }
    }
}
