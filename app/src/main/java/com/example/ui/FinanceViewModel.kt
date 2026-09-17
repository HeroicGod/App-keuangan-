package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FinanceViewModel(private val repository: FinanceRepository) : ViewModel() {

    val pockets: StateFlow<List<Pocket>> = repository.allPockets.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val transactions: StateFlow<List<Transaction>> = repository.allTransactions.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val targets: StateFlow<List<FinancialGoal>> = repository.allTargets.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val schedules: StateFlow<List<Schedule>> = repository.allSchedules.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Pocket Operations
    fun addPocket(name: String, balance: Double, colorHex: String) {
        viewModelScope.launch {
            repository.insertPocket(Pocket(name = name, balance = balance, colorHex = colorHex))
        }
    }
    
    fun updatePocket(pocket: Pocket) {
        viewModelScope.launch {
            repository.updatePocket(pocket)
        }
    }

    // Transaction Operations
    fun addTransaction(amount: Double, type: String, category: String, date: Long, note: String, pocketId: Int) {
        viewModelScope.launch {
            repository.insertTransaction(
                Transaction(amount = amount, type = type, category = category, date = date, note = note, pocketId = pocketId)
            )
            // Update pocket balance
            val pocket = pockets.value.find { it.id == pocketId }
            if (pocket != null) {
                val newBalance = if (type == "INCOME") pocket.balance + amount else pocket.balance - amount
                repository.updatePocket(pocket.copy(balance = newBalance))
            }
        }
    }

    // Target Operations
    fun addTarget(name: String, targetAmount: Double, deadline: Long, colorHex: String) {
        viewModelScope.launch {
            repository.insertTarget(
                FinancialGoal(name = name, targetAmount = targetAmount, deadline = deadline, colorHex = colorHex)
            )
        }
    }

    fun addSavingsToTarget(targetId: Int, amount: Double) {
        viewModelScope.launch {
            val target = targets.value.find { it.id == targetId }
            if (target != null) {
                repository.insertTarget(target.copy(savedAmount = target.savedAmount + amount))
            }
        }
    }

    // Schedule Operations
    fun addSchedule(name: String, amount: Double, type: String, nextDate: Long, frequency: String, pocketId: Int) {
        viewModelScope.launch {
            repository.insertSchedule(
                Schedule(name = name, amount = amount, type = type, nextDate = nextDate, frequency = frequency, pocketId = pocketId)
            )
        }
    }
}

class FinanceViewModelFactory(private val repository: FinanceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinanceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FinanceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
