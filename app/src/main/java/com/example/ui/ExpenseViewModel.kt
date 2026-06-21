package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Expense
import com.example.data.ExpenseDatabase
import com.example.data.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ExpenseRepository
    val allExpenses: StateFlow<List<Expense>>

    private val prefs = application.getSharedPreferences("expense_tracker_prefs", Context.MODE_PRIVATE)
    private val _budget = MutableStateFlow(prefs.getFloat("monthly_budget", 1000000f).toDouble())
    val budget: StateFlow<Double> = _budget.asStateFlow()

    private val _language = MutableStateFlow(prefs.getString("app_language", "en") ?: "en")
    val language: StateFlow<String> = _language.asStateFlow()

    fun updateLanguage(newLanguage: String) {
        _language.value = newLanguage
        prefs.edit().putString("app_language", newLanguage).apply()
    }

    init {
        val database = ExpenseDatabase.getDatabase(application)
        repository = ExpenseRepository(database.expenseDao())
        allExpenses = repository.allExpenses.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun updateBudget(newBudget: Double) {
        _budget.value = newBudget
        prefs.edit().putFloat("monthly_budget", newBudget.toFloat()).apply()
    }

    fun addExpense(
        amount: Double,
        category: String,
        description: String,
        date: Long,
        paymentMethod: String
    ) {
        viewModelScope.launch {
            val expense = Expense(
                amount = amount,
                category = category,
                description = description,
                date = date,
                paymentMethod = paymentMethod
            )
            repository.insertExpense(expense)
        }
    }

    fun updateExpense(
        id: Long,
        amount: Double,
        category: String,
        description: String,
        date: Long,
        paymentMethod: String
    ) {
        viewModelScope.launch {
            val expense = Expense(
                id = id,
                amount = amount,
                category = category,
                description = description,
                date = date,
                paymentMethod = paymentMethod
            )
            repository.updateExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }
}
