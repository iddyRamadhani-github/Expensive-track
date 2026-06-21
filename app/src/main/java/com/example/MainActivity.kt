package com.example

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.Expense
import com.example.ui.ExpenseViewModel
import com.example.ui.theme.MyApplicationTheme
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

// Category Styling Model
data class CategoryStyle(
    val color: Color,
    val icon: ImageVector
)

// Global map of category styles to satisfy the "unique color for each category" instruction
val CategoryStyles = mapOf(
    "Food" to CategoryStyle(Color(0xFFFF8F00), Icons.Default.Restaurant),
    "Transport" to CategoryStyle(Color(0xFF29B6F6), Icons.Default.DirectionsCar),
    "Rent" to CategoryStyle(Color(0xFFAB47BC), Icons.Default.Home),
    "Health" to CategoryStyle(Color(0xFF66BB6A), Icons.Default.Favorite),
    "Utilities" to CategoryStyle(Color(0xFF26C6DA), Icons.Default.Bolt),
    "Entertainment" to CategoryStyle(Color(0xFFEC407A), Icons.Default.PlayArrow),
    "Clothing" to CategoryStyle(Color(0xFFFFCA28), Icons.Default.ShoppingBag),
    "Education" to CategoryStyle(Color(0xFF26A69A), Icons.Default.School),
    "Other" to CategoryStyle(Color(0xFF78909C), Icons.Default.MoreHoriz)
)

object Loc {
    private val en = mapOf(
        "overview" to "OVERVIEW",
        "app_title" to "Expense Tracker",
        "dashboard" to "Dashboard",
        "reports" to "Reports",
        "spent_this_month" to "Total spent this month",
        "remaining" to "Remaining",
        "edit_budget" to "Edit Budget",
        "budget_limit" to "Budget: %s",
        "biggest_category" to "Biggest Category",
        "daily_average" to "Daily Average",
        "this_month" to "This Month",
        "recent_transactions" to "Recent Transactions",
        "empty_expenses" to "No expenses recorded",
        "empty_desc" to "Tap the '+' button at the bottom\nto start tracking your spends.",
        "today" to "Today",
        "this_week" to "This Week",
        "custom" to "Custom",
        "from" to "From: %s",
        "to" to "To: %s",
        "filtered_expenditures" to "Filtered Period Expenditures",
        "expenditure_history" to "Last 7 Days Expenditure History",
        "category_percentages" to "Category Spending Percentages",
        "no_expenditures" to "No expenditures found for the selected period",
        "payment_method" to "Payment Method",
        "cash" to "Cash",
        "card" to "Card",
        "mobile_money" to "Mobile Money",
        "date" to "Date",
        "amount" to "Amount (TZS)",
        "amount_placeholder" to "e.g. 15000",
        "description" to "Description",
        "desc_placeholder" to "e.g. Lunch with team",
        "category" to "Category",
        "save_transaction" to "Save Transaction",
        "delete_expense" to "Delete Expense",
        "customize_budget" to "Customize Budget",
        "monthly_budget_cap" to "Monthly Budget Cap (TZS)",
        "budget_placeholder" to "e.g. 1000000",
        "save_budget_limit" to "Save Budget Limit",
        "error_amount" to "Please enter a valid expense limit amount!",
        "error_budget" to "Please enter a valid monthly budget limit!",
        "add_expense" to "Add Expense",
        "edit_expense" to "Edit Expense",
        
        // Categories
        "Food" to "Food",
        "Transport" to "Transport",
        "Rent" to "Rent",
        "Health" to "Health",
        "Utilities" to "Utilities",
        "Entertainment" to "Entertainment",
        "Clothing" to "Clothing",
        "Education" to "Education",
        "Other" to "Other"
    )

    private val sw = mapOf(
        "overview" to "MUHTASARI",
        "app_title" to "Kifuatilia Matumizi",
        "dashboard" to "Dashibodi",
        "reports" to "Ripoti",
        "spent_this_month" to "Jumla ya matumizi mwezi huu",
        "remaining" to "Msalio",
        "edit_budget" to "Hariri Bajeti",
        "budget_limit" to "Bajeti: %s",
        "biggest_category" to "Kundi Kuu",
        "daily_average" to "Wastani wa Kila Siku",
        "this_month" to "Mwezi Huu",
        "recent_transactions" to "Miamala ya Hivi Karibuni",
        "empty_expenses" to "Hakuna matumizi yaliyorekodiwa",
        "empty_desc" to "Gonga kitufe cha '+' chini kabisa\nilianze kufuatilia matumizi yako.",
        "today" to "Leo",
        "this_week" to "Wiki Hii",
        "custom" to "Maalum",
        "from" to "Kuanzia: %s",
        "to" to "Hadi: %s",
        "filtered_expenditures" to "Matumizi ya Kipindi",
        "expenditure_history" to "Matumizi ya Siku 7 Zilizopita",
        "category_percentages" to "Asilimia za Kundi la Matumizi",
        "no_expenditures" to "Hakuna matumizi katika kipindi hiki",
        "payment_method" to "Njia ya Malipo",
        "cash" to "Pesa Taslimu",
        "card" to "Kadi",
        "mobile_money" to "Pesa ya Simu",
        "date" to "Tarehe",
        "amount" to "Kiasi (TZS)",
        "amount_placeholder" to "mfano 15000",
        "description" to "Maelezo",
        "desc_placeholder" to "mfano Chakula cha mchana",
        "category" to "Kundi",
        "save_transaction" to "Hifadhi Muamala",
        "delete_expense" to "Futa Matumizi",
        "customize_budget" to "Weka Bajeti",
        "monthly_budget_cap" to "Kiwango cha Bajeti kwa Mwezi (TZS)",
        "budget_placeholder" to "mfano 1000000",
        "save_budget_limit" to "Hifadhi Kiwango cha Bajeti",
        "error_amount" to "Tafadhali weka kiasi halali!",
        "error_budget" to "Tafadhali weka kiwango cha bajeti kilicho sahihi!",
        "add_expense" to "Ongeza Matumizi",
        "edit_expense" to "Hariri Matumizi",
        
        // Categories
        "Food" to "Chakula",
        "Transport" to "Usafiri",
        "Rent" to "Kodi ya Nyumba",
        "Health" to "Afya",
        "Utilities" to "Huduma",
        "Entertainment" to "Burudani",
        "Clothing" to "Mavazi",
        "Education" to "Elimu",
        "Other" to "Mengineyo"
    )

    fun t(key: String, lang: String): String {
        return if (lang == "sw") {
            sw[key] ?: en[key] ?: key
        } else {
            en[key] ?: key
        }
    }

    fun t(key: String, lang: String, vararg args: Any): String {
        val pattern = if (lang == "sw") {
            sw[key] ?: en[key] ?: key
        } else {
            en[key] ?: key
        }
        return try {
            String.format(pattern, *args)
        } catch (e: Exception) {
            pattern
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true, dynamicColor = false) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFF0B1121)
                ) { innerPadding ->
                    // Centered mobile-first constraint (480dp max width)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(Color(0xFF0B1121)),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Box(
                            modifier = Modifier
                                .widthIn(max = 480.dp)
                                .fillMaxHeight()
                                .background(Color(0xFF0B1121))
                        ) {
                            ExpenseTrackerApp()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseTrackerApp(viewModel: ExpenseViewModel = viewModel()) {
    val expenses by viewModel.allExpenses.collectAsStateWithLifecycle()
    val budget by viewModel.budget.collectAsStateWithLifecycle()
    val language by viewModel.language.collectAsStateWithLifecycle()

    var activeTab by remember { mutableIntStateOf(0) } // 0: Dashboard, 1: Reports

    // Bottom sheet dialog state management
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedExpenseForEdit by remember { mutableStateOf<Expense?>(null) }
    var showBudgetDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header bar with language selection toggling
            HeaderSection(
                language = language,
                onLanguageToggle = { viewModel.updateLanguage(if (language == "en") "sw" else "en") }
            )

            // Capsule tabs selector
            TabSection(
                activeTab = activeTab,
                onTabSelected = { activeTab = it },
                language = language
            )

            // Dynamic view based on tab selection
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (activeTab == 0) {
                    DashboardView(
                        expenses = expenses,
                        budget = budget,
                        onEditBudget = { showBudgetDialog = true },
                        onExpenseClick = { selectedExpenseForEdit = it },
                        language = language
                    )
                } else {
                    ReportsView(
                        expenses = expenses,
                        language = language
                    )
                }
            }
        }

        // Beautiful floating action button for adding custom transactions
        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = Color(0xFF4F46E5),
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .border(4.dp, Color(0xFF0B1121), RoundedCornerShape(16.dp))
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Expense", modifier = Modifier.size(28.dp))
        }

        // Add Expense Dialog Setup
        if (showAddDialog) {
            ExpenseFormDialog(
                title = Loc.t("add_expense", language),
                language = language,
                onDismiss = { showAddDialog = false },
                onSave = { amount, category, description, date, paymentMethod ->
                    viewModel.addExpense(amount, category, description, date, paymentMethod)
                    showAddDialog = false
                }
            )
        }

        // Edit/Delete Expense Dialog Setup
        if (selectedExpenseForEdit != null) {
            val expense = selectedExpenseForEdit!!
            ExpenseFormDialog(
                title = Loc.t("edit_expense", language),
                expense = expense,
                language = language,
                onDismiss = { selectedExpenseForEdit = null },
                onSave = { amount, category, description, date, paymentMethod ->
                    viewModel.updateExpense(expense.id, amount, category, description, date, paymentMethod)
                    selectedExpenseForEdit = null
                },
                onDelete = {
                    viewModel.deleteExpense(expense)
                    selectedExpenseForEdit = null
                }
            )
        }

        // Custom Budget Editor Dialog
        if (showBudgetDialog) {
            BudgetEditDialog(
                currentBudget = budget,
                language = language,
                onDismiss = { showBudgetDialog = false },
                onSave = {
                    viewModel.updateBudget(it)
                    showBudgetDialog = false
                }
            )
        }
    }
}

@Composable
fun HeaderSection(language: String, onLanguageToggle: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = Loc.t("overview", language),
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = Loc.t("app_title", language),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Currency Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF6366F1).copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .border(1.dp, Color(0xFF6366F1).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = "TZS",
                        color = Color(0xFFC7D2FE),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                // In-App Language Toggle Button
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
                        .clickable { onLanguageToggle() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Switch Language",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = if (language == "en") "EN" else "SW",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TabSection(activeTab: Int, onTabSelected: (Int) -> Unit, language: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2235)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth()
            .border(1.dp, Color(0x80334155), RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (activeTab == 0) Color(0xFF6366F1) else Color.Transparent)
                    .clickable { onTabSelected(0) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = Loc.t("dashboard", language),
                    color = if (activeTab == 0) Color.White else Color(0xFF94A3B8),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (activeTab == 1) Color(0xFF6366F1) else Color.Transparent)
                    .clickable { onTabSelected(1) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = Loc.t("reports", language),
                    color = if (activeTab == 1) Color.White else Color(0xFF94A3B8),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

// ---------------- DASHBOARD VIEW ----------------
@Composable
fun DashboardView(
    expenses: List<Expense>,
    budget: Double,
    onEditBudget: () -> Unit,
    onExpenseClick: (Expense) -> Unit,
    language: String
) {
    // Filter this month's expenses for dashboard calculations
    val thisMonthExpenses = remember(expenses) {
        val startOfThisMonth = getStartOfThisMonth()
        expenses.filter { it.date >= startOfThisMonth }
    }

    val totalSpentThisMonth = remember(thisMonthExpenses) {
        thisMonthExpenses.sumOf { it.amount }
    }

    val remainingBudget = remember(budget, totalSpentThisMonth) {
        budget - totalSpentThisMonth
    }

    val biggestCategory = remember(thisMonthExpenses) {
        if (thisMonthExpenses.isEmpty()) "N/A"
        else {
            thisMonthExpenses.groupBy { it.category }
                .mapValues { entry -> entry.value.sumOf { it.amount } }
                .maxByOrNull { it.value }?.key ?: "N/A"
        }
    }

    val biggestCategoryAmount = remember(thisMonthExpenses, biggestCategory) {
        if (biggestCategory == "N/A" || thisMonthExpenses.isEmpty()) 0.0
        else {
            thisMonthExpenses.filter { it.category == biggestCategory }.sumOf { it.amount }
        }
    }

    val dailyAverageSpent = remember(thisMonthExpenses) {
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        totalSpentThisMonth / currentDay.coerceAtLeast(1)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Budget & Spend card
        item {
            BudgetSpendCard(
                spentValue = totalSpentThisMonth,
                remainingValue = remainingBudget,
                budgetLimit = budget,
                onEditBudget = onEditBudget,
                language = language
            )
        }

        // Substatistics Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = Loc.t("biggest_category", language),
                    value = if (biggestCategory == "N/A") Loc.t("N/A", language) else Loc.t(biggestCategory, language),
                    subtext = if (biggestCategory == "N/A") "" else formatTzs(biggestCategoryAmount),
                    categoryColor = if (biggestCategory == "N/A") null else CategoryStyles[biggestCategory]?.color,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = Loc.t("daily_average", language),
                    value = formatTzs(dailyAverageSpent),
                    subtext = Loc.t("this_month", language),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Transaction History Header
        item {
            Text(
                text = Loc.t("recent_transactions", language),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Transaction List
        if (expenses.isEmpty()) {
            item {
                EmptyStateCard(language = language)
            }
        } else {
            items(expenses) { expense ->
                TransactionRowItem(
                    expense = expense,
                    onClick = { onExpenseClick(expense) },
                    language = language
                )
            }
        }
    }
}

@Composable
fun BudgetSpendCard(
    spentValue: Double,
    remainingValue: Double,
    budgetLimit: Double,
    onEditBudget: () -> Unit,
    language: String
) {
    val consumptionPercent = if (budgetLimit <= 0.0) 1f else (spentValue / budgetLimit).toFloat()
    val isOverBudget = remainingValue < 0.0

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161D31)),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0x80334155), RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = Loc.t("spent_this_month", language),
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = formatTzsValueOnly(spentValue),
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "TZS",
                            color = Color(0xFF94A3B8),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = Loc.t("remaining", language),
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isOverBudget) "- ${formatTzsValueOnly(-remainingValue)}" else "+ ${formatTzsValueOnly(remainingValue)}",
                        color = if (isOverBudget) Color(0xFFF87171) else Color(0xFF34D399),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onEditBudget() }
                    ) {
                        Text(
                            text = Loc.t("edit_budget", language),
                            color = Color(0xFF6366F1),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Budget",
                            tint = Color(0xFF6366F1),
                            modifier = Modifier.size(11.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Modern indicator progress bar
            val progressBarColor = if (isOverBudget) Color(0xFFF87171) else Color(0xFF6366F1)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xFF1E293B))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = consumptionPercent.coerceIn(0f, 1f))
                        .clip(RoundedCornerShape(5.dp))
                        .background(progressBarColor)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "0 TZS",
                    color = Color(0xFF64748B),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = Loc.t("budget_limit", language, formatTzs(budgetLimit)),
                    color = Color(0xFF64748B),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtext: String,
    categoryColor: Color? = null,
    modifier: Modifier = Modifier
) {
    val displayColor = categoryColor ?: Color(0xFF60A5FA)
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161D31)),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .border(1.dp, Color(0x80334155), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title.uppercase(),
                color = Color(0xFF94A3B8),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (categoryColor != null) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(categoryColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = value,
                    color = displayColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (subtext.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtext,
                    color = Color(0xFF64748B),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun TransactionRowItem(
    expense: Expense,
    onClick: () -> Unit,
    language: String
) {
    val style = CategoryStyles[expense.category] ?: CategoryStyle(Color.White, Icons.Default.Category)
    val displayPaymentMethod = when (expense.paymentMethod) {
        "Cash" -> Loc.t("cash", language)
        "Card" -> Loc.t("card", language)
        "Mobile Money" -> Loc.t("mobile_money", language)
        else -> expense.paymentMethod
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161D31)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0x80334155), RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Circle category colored representation
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(style.color.copy(alpha = 0.15f))
                        .border(1.dp, style.color.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = style.icon,
                        contentDescription = expense.category,
                        tint = style.color,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = expense.description.ifEmpty { Loc.t(expense.category, language) },
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = formatDateString(expense.date, if (language == "sw") Locale("sw") else Locale.ENGLISH),
                            color = Color(0xFF94A3B8),
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.dp, Color(0xFF334155), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = displayPaymentMethod,
                                color = Color(0xFF94A3B8),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "- ${formatTzsValueOnly(expense.amount)}",
                color = Color(0xFFF87171),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun EmptyStateCard(language: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Wallet,
            contentDescription = "Wallet",
            tint = Color(0xFF151C33),
            modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = Loc.t("empty_expenses", language),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = Loc.t("empty_desc", language),
            color = Color(0xFF7585A3),
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
    }
}

// ---------------- REPORTS VIEW ----------------
@Composable
fun ReportsView(
    expenses: List<Expense>,
    language: String
) {
    var selectedFilterIndex by remember { mutableIntStateOf(2) } // Default is This Month (2)
    val filterOptions = listOf(
        Loc.t("today", language),
        Loc.t("this_week", language),
        Loc.t("this_month", language),
        Loc.t("custom", language)
    )

    var customStartDate by remember { mutableStateOf(getStartOfThisMonth()) }
    var customEndDate by remember { mutableStateOf(System.currentTimeMillis()) }

    val context = LocalContext.current

    // Reactive filter logic based on the user's selection
    val filteredExpenses = remember(expenses, selectedFilterIndex, customStartDate, customEndDate) {
        val now = System.currentTimeMillis()
        when (selectedFilterIndex) {
            0 -> expenses.filter { it.date >= getStartOfToday() }
            1 -> expenses.filter { it.date >= getStartOfThisWeek() }
            2 -> expenses.filter { it.date >= getStartOfThisMonth() }
            else -> expenses.filter { it.date in customStartDate..customEndDate }
        }
    }

    val totalSpent = remember(filteredExpenses) {
        filteredExpenses.sumOf { it.amount }
    }

    // Category breakdown calculations sorted by amount descending
    val categoryBreakdown = remember(filteredExpenses) {
        if (filteredExpenses.isEmpty()) emptyList<Pair<String, Double>>()
        else {
            filteredExpenses.groupBy { it.category }
                .mapValues { entry -> entry.value.sumOf { it.amount } }
                .toList()
                .sortedByDescending { it.second }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Report Segment Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filterOptions.forEachIndexed { index, option ->
                    val isSelected = selectedFilterIndex == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color(0xFF6366F1) else Color(0xFF161D31))
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Color.Transparent else Color(0x80334155),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedFilterIndex = index }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = option,
                            color = if (isSelected) Color.White else Color(0xFF94A3B8),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Custom Date Pickers visible when Custom Filter index is active
        if (selectedFilterIndex == 3) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161D31)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0x80334155), RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                val cal = Calendar.getInstance().apply { timeInMillis = customStartDate }
                                DatePickerDialog(
                                    context,
                                    { _, year, month, day ->
                                        val newCal = Calendar.getInstance().apply {
                                            set(year, month, day, 0, 0, 0)
                                        }
                                        customStartDate = newCal.timeInMillis
                                    },
                                    cal.get(Calendar.YEAR),
                                    cal.get(Calendar.MONTH),
                                    cal.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color(0xFF334155), RoundedCornerShape(10.dp))
                        ) {
                            Text(Loc.t("from", language, formatDateString(customStartDate, if (language == "sw") Locale("sw") else Locale.ENGLISH)), fontSize = 11.sp, color = Color.White)
                        }

                        Button(
                            onClick = {
                                val cal = Calendar.getInstance().apply { timeInMillis = customEndDate }
                                DatePickerDialog(
                                    context,
                                    { _, year, month, day ->
                                        val newCal = Calendar.getInstance().apply {
                                            set(year, month, day, 23, 59, 59)
                                        }
                                        customEndDate = newCal.timeInMillis
                                    },
                                    cal.get(Calendar.YEAR),
                                    cal.get(Calendar.MONTH),
                                    cal.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color(0xFF334155), RoundedCornerShape(10.dp))
                        ) {
                            Text(Loc.t("to", language, formatDateString(customEndDate, if (language == "sw") Locale("sw") else Locale.ENGLISH)), fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
            }
        }

        // Sum metric Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161D31)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0x80334155), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = Loc.t("filtered_expenditures", language),
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formatTzs(totalSpent),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6366F1).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Wallet,
                            contentDescription = "Wallet Icon",
                            tint = Color(0xFF6366F1),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Inline Custom Canvas drawing for Last 7 days spending
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161D31)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0x80334155), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = Loc.t("expenditure_history", language),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    HistoryChart(expenses = expenses, language = language)
                }
            }
        }

        // Category breakdowns header
        item {
            Text(
                text = Loc.t("category_percentages", language),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Display category breakdowns with custom percentage bars
        if (categoryBreakdown.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF151C33).copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = Loc.t("no_expenditures", language),
                        color = Color(0xFF7585A3),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth()
                    )
                }
            }
        } else {
            items(categoryBreakdown) { (catName, catAmount) ->
                val percentage = if (totalSpent <= 0.0) 0f else (catAmount / totalSpent).toFloat()
                CategoryPercentageRow(
                    categoryName = catName,
                    amount = catAmount,
                    percentage = percentage,
                    language = language
                )
            }
        }
    }
}

// ---------------- HISTORY BAR CHART DRAWN VIA CANVAS ----------------
@Composable
fun HistoryChart(expenses: List<Expense>, language: String) {
    val textMeasurer = rememberTextMeasurer()

    val last7DaysData = remember(expenses, language) {
        val list = mutableListOf<Pair<String, Double>>()
        val locale = if (language == "sw") Locale("sw") else Locale.ENGLISH
        val sdf = SimpleDateFormat("EEE", locale)

        for (i in 6 downTo 0) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -i)

            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            val startTime = cal.timeInMillis

            cal.set(Calendar.HOUR_OF_DAY, 23)
            cal.set(Calendar.MINUTE, 59)
            cal.set(Calendar.SECOND, 59)
            cal.set(Calendar.MILLISECOND, 999)
            val endTime = cal.timeInMillis

            val totalForDay = expenses.filter { it.date in startTime..endTime }.sumOf { it.amount }
            val dayLabel = sdf.format(cal.time)
            list.add(Pair(dayLabel, totalForDay))
        }
        list
    }

    val maxAmount = last7DaysData.maxOfOrNull { it.second } ?: 1.0
    val maxCalculated = if (maxAmount == 0.0) 1.0 else maxAmount

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
    ) {
        val itemsCount = last7DaysData.size
        val gap = 12.dp.toPx()
        val totalGaps = gap * (itemsCount - 1)
        val barWidth = (size.width - totalGaps) / itemsCount

        val labelHeight = 22.dp.toPx()
        val chartHeight = size.height - labelHeight

        // Draw light dotted grid boundaries
        val gridCount = 2
        for (i in 0..gridCount) {
            val gridY = (chartHeight / gridCount) * i
            drawLine(
                color = Color(0xFF334155).copy(alpha = 0.3f),
                start = Offset(0f, gridY),
                end = Offset(size.width, gridY),
                strokeWidth = 0.5.dp.toPx()
            )
        }

        // Draw bars and keys
        last7DaysData.forEachIndexed { index, (dayLabel, sumValue) ->
            val x = index * (barWidth + gap)
            val barFilledHeight = ((sumValue / maxCalculated) * (chartHeight - 12.dp.toPx())).toFloat()
            val y = chartHeight - barFilledHeight

            // Background pill trace inside chart
            drawRoundRect(
                color = Color(0xFF0F172A).copy(alpha = 0.6f),
                topLeft = Offset(x, 0f),
                size = Size(barWidth, chartHeight),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )

            // Primary value bar with Indigo High Density gradient
            if (sumValue > 0) {
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF818CF8), Color(0xFF4F46E5))
                    ),
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barFilledHeight.coerceAtLeast(6.dp.toPx())),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )
            }

            // Draw Week day label
            val textLayoutResult = textMeasurer.measure(
                text = dayLabel,
                style = TextStyle(color = Color(0xFF94A3B8), fontSize = 10.sp)
            )
            val textW = textLayoutResult.size.width
            val textX = x + (barWidth - textW) / 2
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(textX, chartHeight + 4.dp.toPx())
            )
        }
    }
}

@Composable
fun CategoryPercentageRow(
    categoryName: String,
    amount: Double,
    percentage: Float,
    language: String
) {
    val style = CategoryStyles[categoryName] ?: CategoryStyle(Color.White, Icons.Default.Category)

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161D31)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0x80334155), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(style.color.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = style.icon,
                            contentDescription = Loc.t(categoryName, language),
                            tint = style.color,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Loc.t(categoryName, language),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formatTzs(amount),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "(${(percentage * 100).toInt()}%)",
                        color = style.color,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Percentage comparative bar filled with category's unique color
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFF1E293B))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = percentage.coerceIn(0f, 1f))
                        .background(style.color)
                )
            }
        }
    }
}

// ---------------- MAIN FORM DIALOG (EDIT / ADD) ----------------
@Composable
fun ExpenseFormDialog(
    title: String,
    expense: Expense? = null,
    onDismiss: () -> Unit,
    onSave: (amount: Double, category: String, description: String, date: Long, paymentMethod: String) -> Unit,
    onDelete: (() -> Unit)? = null,
    language: String
) {
    var amountStr by remember { mutableStateOf(expense?.amount?.toLong()?.toString() ?: "") }
    var description by remember { mutableStateOf(expense?.description ?: "") }
    var category by remember { mutableStateOf(expense?.category ?: "Food") }
    var paymentMethod by remember { mutableStateOf(expense?.paymentMethod ?: "Cash") }
    var dateInMillis by remember { mutableLongStateOf(expense?.date ?: System.currentTimeMillis()) }

    var localError by remember { mutableStateOf("") }
    val context = LocalContext.current
    val categoriesList = CategoryStyles.keys.toList()
    val paymentMethods = listOf("Cash", "Card", "Mobile Money")

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF161D31)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .border(1.dp, Color(0x80334155), RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close Dialogue", tint = Color(0xFF94A3B8))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Amount Text Field Input with standard prompt
                Text(
                    text = Loc.t("amount", language),
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { stringVal ->
                        if (stringVal.all { it.isDigit() }) {
                            amountStr = stringVal
                        }
                    },
                    placeholder = { Text(Loc.t("amount_placeholder", language), color = Color(0xFF94A3B8)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6366F1),
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF0F172A),
                        unfocusedContainerColor = Color(0xFF0F172A)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category Selector label
                Text(
                    text = Loc.t("category", language),
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Scrollable category row or neat flowing grid capsules
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val chunks = categoriesList.chunked(3)
                    chunks.forEach { chunk ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            chunk.forEach { cat ->
                                val isSelected = category == cat
                                val style = CategoryStyles[cat] ?: CategoryStyle(Color.White, Icons.Default.Category)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) style.color else Color(0xFF0F172A))
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) Color.Transparent else Color(0xFF334155),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { category = cat }
                                        .padding(horizontal = 4.dp, vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = style.icon,
                                            contentDescription = Loc.t(cat, language),
                                            tint = if (isSelected) Color.White else style.color,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = Loc.t(cat, language),
                                            color = if (isSelected) Color.White else Color(0xFF94A3B8),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Description Field
                Text(
                    text = Loc.t("description", language),
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text(Loc.t("desc_placeholder", language), color = Color(0xFF94A3B8)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6366F1),
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF0F172A),
                        unfocusedContainerColor = Color(0xFF0F172A)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Payment Method Selector Option
                Text(
                    text = Loc.t("payment_method", language),
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    paymentMethods.forEach { method ->
                        val isSelected = paymentMethod == method
                        val displayName = when (method) {
                            "Cash" -> Loc.t("cash", language)
                            "Card" -> Loc.t("card", language)
                            "Mobile Money" -> Loc.t("mobile_money", language)
                            else -> method
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0xFF6366F1) else Color(0xFF0F172A))
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) Color.Transparent else Color(0xFF334155),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { paymentMethod = method }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = displayName,
                                color = if (isSelected) Color.White else Color(0xFF94A3B8),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Date Picker trigger button
                Text(
                    text = Loc.t("date", language),
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = {
                        val calendarInstance = Calendar.getInstance().apply { timeInMillis = dateInMillis }
                        DatePickerDialog(
                            context,
                            { _, year, month, day ->
                                val newCal = Calendar.getInstance().apply {
                                    set(year, month, day, 12, 0, 0)
                                }
                                dateInMillis = newCal.timeInMillis
                            },
                            calendarInstance.get(Calendar.YEAR),
                            calendarInstance.get(Calendar.MONTH),
                            calendarInstance.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp)),
                    contentPadding = PaddingValues(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatDateString(dateInMillis, if (language == "sw") Locale("sw") else Locale.ENGLISH),
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Calendar Picker Icon",
                            tint = Color(0xFF6366F1),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                if (localError.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = localError,
                        color = Color(0xFFF87171),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action validation and submission
                Button(
                    onClick = {
                        val parsedAmount = amountStr.toDoubleOrNull()
                        if (parsedAmount == null || parsedAmount <= 0) {
                            localError = Loc.t("error_amount", language)
                        } else {
                            onSave(parsedAmount, category, description, dateInMillis, paymentMethod)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(Loc.t("save_transaction", language), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                // Delete button styled only for existing items
                if (onDelete != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { onDelete() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF87171).copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFF87171).copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Icon", tint = Color(0xFFF87171), modifier = Modifier.size(16.dp))
                            Text(Loc.t("delete_expense", language), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF87171))
                        }
                    }
                }
            }
        }
    }
}

// ---------------- CUSTOM BUDGET DIALOG ----------------
@Composable
fun BudgetEditDialog(
    currentBudget: Double,
    onDismiss: () -> Unit,
    onSave: (Double) -> Unit,
    language: String
) {
    var budgetStr by remember { mutableStateOf(currentBudget.toLong().toString()) }
    var localError by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF161D31)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .border(1.dp, Color(0x80334155), RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = Loc.t("customize_budget", language),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Dismiss Dialogue", tint = Color(0xFF94A3B8))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = Loc.t("monthly_budget_cap", language),
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = budgetStr,
                    onValueChange = { stringValue ->
                        if (stringValue.all { it.isDigit() }) {
                            budgetStr = stringValue
                        }
                    },
                    placeholder = { Text(Loc.t("budget_placeholder", language), color = Color(0xFF94A3B8)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6366F1),
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF0F172A),
                        unfocusedContainerColor = Color(0xFF0F172A)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (localError.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = localError,
                        color = Color(0xFFF87171),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        val parsedBudget = budgetStr.toDoubleOrNull()
                        if (parsedBudget == null || parsedBudget <= 0) {
                            localError = Loc.t("error_budget", language)
                        } else {
                            onSave(parsedBudget)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(Loc.t("save_budget_limit", language), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

// ---------------- TIME CALCULATION HELPERS ----------------
fun getStartOfToday(): Long {
    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

fun getStartOfThisWeek(): Long {
    val cal = Calendar.getInstance()
    cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

fun getStartOfThisMonth(): Long {
    val cal = Calendar.getInstance()
    cal.set(Calendar.DAY_OF_MONTH, 1)
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

fun formatDateString(millis: Long, locale: java.util.Locale = java.util.Locale.getDefault()): String {
    val sdf = SimpleDateFormat("dd MMM yyyy", locale)
    return sdf.format(Date(millis))
}

fun formatTzs(amount: Double): String {
    val formatter = NumberFormat.getIntegerInstance()
    return "${formatter.format(amount.toLong())} TZS"
}

fun formatTzsValueOnly(amount: Double): String {
    val formatter = NumberFormat.getIntegerInstance()
    return formatter.format(amount.toLong())
}
