package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.Transaction
import com.example.ui.FinanceViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(viewModel: FinanceViewModel) {
    val transactions by viewModel.transactions.collectAsState()
    val pockets by viewModel.pockets.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Transaction")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Text(
                "Transactions", 
                style = MaterialTheme.typography.headlineMedium, 
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            
            if (transactions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No transactions yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(transactions) { tx ->
                        TransactionItem(tx)
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        // Dummy add dialog for now, in a real app this would be a full form
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Dummy Transaction") },
            text = { Text("Creates a $50 expense in the first available pocket.") },
            confirmButton = {
                TextButton(onClick = { 
                    if (pockets.isNotEmpty()) {
                        viewModel.addTransaction(
                            amount = 50.0,
                            type = "EXPENSE",
                            category = "Food",
                            date = System.currentTimeMillis(),
                            note = "Lunch",
                            pocketId = pockets.first().id
                        )
                    }
                    showAddDialog = false 
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun TransactionItem(tx: Transaction) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(tx.category, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(tx.note, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                Text(sdf.format(Date(tx.date)), style = MaterialTheme.typography.bodySmall)
            }
            val isIncome = tx.type == "INCOME"
            val sign = if (isIncome) "+" else "-"
            val color = if (isIncome) Color(0xFF4CAF50) else Color(0xFFE53935)
            Text(
                "$sign$${String.format("%.2f", tx.amount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}
