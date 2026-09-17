package com.example.data

import kotlinx.coroutines.flow.Flow

class FinanceRepository(private val dao: FinanceDao) {
    val allPockets: Flow<List<Pocket>> = dao.getAllPockets()
    val allTransactions: Flow<List<Transaction>> = dao.getAllTransactions()
    val allTargets: Flow<List<FinancialGoal>> = dao.getAllGoals()
    val allSchedules: Flow<List<Schedule>> = dao.getAllSchedules()

    suspend fun insertPocket(pocket: Pocket) = dao.insertPocket(pocket)
    suspend fun updatePocket(pocket: Pocket) = dao.updatePocket(pocket)
    suspend fun deletePocket(id: Int) = dao.deletePocket(id)

    suspend fun insertTransaction(transaction: Transaction) {
        dao.insertTransaction(transaction)
        // We shouldn't do complex transaction updates directly here, better let ViewModel handle balance sync or use a Database Transaction,
        // but for simplicity, we will assume ViewModel handles pocket balance updates.
    }
    suspend fun deleteTransaction(id: Int) = dao.deleteTransaction(id)
    fun getTransactionsBetween(start: Long, end: Long) = dao.getTransactionsBetween(start, end)

    suspend fun insertTarget(target: FinancialGoal) = dao.insertGoal(target)
    suspend fun deleteTarget(id: Int) = dao.deleteGoal(id)

    suspend fun insertSchedule(schedule: Schedule) = dao.insertSchedule(schedule)
    suspend fun deleteSchedule(id: Int) = dao.deleteSchedule(id)
}
