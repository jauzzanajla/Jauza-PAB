package com.example.aplikasijeje

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.aplikasijeje.ui.theme.AplikasijejeTheme
import java.util.Calendar

// ==========================================
// 1. DATA MODEL
// ==========================================
data class Task(
    val id: Int,
    val title: String,
    val category: String,
    val priority: String,
    val deadline: String,
    var isCompleted: Boolean = false
)

// ==========================================
// 2. VIEWMODEL (BUSINESS LOGIC LAYER)
// ==========================================
class TaskViewModel : ViewModel() {
    private val _tasks = mutableStateListOf(
        Task(1, "Tugas Besar Pemrograman Android", "Kuliah", "Tinggi", "25 Okt 2026, 23:59", false),
        Task(2, "Proposal Capstone Project", "Kuliah", "Tinggi", "28 Okt 2026, 15:00", false),
        Task(3, "Rapat Koordinasi HIMA", "Organisasi", "Sedang", "30 Okt 2026, 19:00", true),
        Task(4, "Bayar BPP Semester", "Pribadi", "Tinggi", "02 Nov 2026, 12:00", false)
    )

    val tasks: List<Task> get() = _tasks

    fun addTask(title: String, category: String, priority: String, deadline: String) {
        val newTask = Task(
            id = (_tasks.maxOfOrNull { it.id } ?: 0) + 1,
            title = title,
            category = category,
            priority = priority,
            deadline = deadline
        )
        _tasks.add(0, newTask)
    }

    fun toggleTaskCompletion(taskId: Int) {
        val index = _tasks.indexOfFirst { it.id == taskId }
        if (index != -1) {
            val current = _tasks[index]
            _tasks[index] = current.copy(isCompleted = !current.isCompleted)
        }
    }

    fun deleteTask(taskId: Int) {
        _tasks.removeAll { it.id == taskId }
    }
}

// ==========================================
// 3. MAIN ACTIVITY & UI PRESENTATION
// ==========================================
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AplikasijejeTheme {
                MainContainer()
            }
        }
    }
}

// Warna Tema Telkom Merah Putih & Estetik
val TelkomRed = Color(0xFFCC0000)
val TelkomRedLight = Color(0xFFFEE2E2)
val BackgroundGray = Color(0xFFF8FAFC)
val CardBackground = Color(0xFFFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainer() {
    val viewModel = remember { TaskViewModel() }
    var selectedTab by remember { mutableStateOf(0) }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_home), contentDescription = "Dashboard") },
                    label = { Text("Dashboard") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = TelkomRed)
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_favorite), contentDescription = "Tugas") },
                    label = { Text("Tugas") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = TelkomRed)
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_account_box), contentDescription = "Profil") },
                    label = { Text("Profil") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = TelkomRed)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = TelkomRed,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Text("+", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> DashboardScreen(viewModel)
                1 -> TaskListScreen(viewModel)
                2 -> ProfileScreen()
            }
        }

        if (showAddDialog) {
            AddTaskDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { title, category, priority, deadline ->
                    viewModel.addTask(title, category, priority, deadline)
                    showAddDialog = false
                }
            )
        }
    }
}

// --- SCREEN 1: DASHBOARD ---
@Composable
fun DashboardScreen(viewModel: TaskViewModel) {
    val totalTasks = viewModel.tasks.size
    val completedTasks = viewModel.tasks.count { it.isCompleted }
    val progress = if (totalTasks > 0) completedTasks.toFloat() / totalTasks else 0f

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(BackgroundGray).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Halo, TelUtizen! 👋", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                    Text("Sistem Informasi - Telkom University", fontSize = 14.sp, color = Color(0xFF64748B))
                }
                Surface(shape = CircleShape, color = TelkomRedLight, modifier = Modifier.size(44.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("T", color = TelkomRed, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TelkomRed),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Progress Akademik", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("$completedTasks dari $totalTasks Tugas Selesai", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f),
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape)
                    )
                }
            }
        }

        item {
            Text("Tugas Mendesak", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        }

        items(viewModel.tasks.filter { !it.isCompleted }.take(3)) { task ->
            TaskCard(task = task, onChecked = { viewModel.toggleTaskCompletion(task.id) })
        }
    }
}

// --- SCREEN 2: DAFTAR TUGAS ---
@Composable
fun TaskListScreen(viewModel: TaskViewModel) {
    var filterCategory by remember { mutableStateOf("Semua") }
    val categories = listOf("Semua", "Kuliah", "Organisasi", "Pribadi")

    Column(modifier = Modifier.fillMaxSize().background(BackgroundGray).padding(20.dp)) {
        Text("Daftar Tugas", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categories.forEach { cat ->
                FilterChip(
                    selected = filterCategory == cat,
                    onClick = { filterCategory = cat },
                    label = { Text(cat) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TelkomRed,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val displayedTasks = if (filterCategory == "Semua") viewModel.tasks else viewModel.tasks.filter { it.category == filterCategory }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(displayedTasks) { task ->
                TaskCard(
                    task = task,
                    onChecked = { viewModel.toggleTaskCompletion(task.id) },
                    onDelete = { viewModel.deleteTask(task.id) }
                )
            }
        }
    }
}

// --- SCREEN 3: PROFIL ---
@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundGray).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Surface(shape = CircleShape, color = TelkomRedLight, modifier = Modifier.size(90.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Text("JN", color = TelkomRed, fontWeight = FontWeight.Bold, fontSize = 28.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Jauza Najla Putri", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        Text("NIM: 102042300011", fontSize = 14.sp, color = Color(0xFF64748B))
        Text("Sistem Informasi - Telkom University", fontSize = 14.sp, color = TelkomRed, fontWeight = FontWeight.Medium)

        Spacer(modifier = Modifier.height(30.dp))

        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Informasi Arsitektur", fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(8.dp))
                Text("• Presentation Layer: Jetpack Compose UI", fontSize = 14.sp, color = Color(0xFF64748B))
                Text("• Domain Layer: TaskViewModel & State Management", fontSize = 14.sp, color = Color(0xFF64748B))
                Text("• Data Layer: MutableStateList & Repository State", fontSize = 14.sp, color = Color(0xFF64748B))
            }
        }
    }
}

@Composable
fun TaskCard(task: Task, onChecked: () -> Unit, onDelete: (() -> Unit)? = null) {
    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Checkbox(checked = task.isCompleted, onCheckedChange = { onChecked() }, colors = CheckboxDefaults.colors(checkedColor = TelkomRed))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = task.title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = if (task.isCompleted) Color.Gray else Color(0xFF1E293B))
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(shape = RoundedCornerShape(4.dp), color = TelkomRedLight) {
                            Text(text = task.category, fontSize = 11.sp, color = TelkomRed, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                        Text(text = "🕒 ${task.deadline}", fontSize = 12.sp, color = Color(0xFF64748B))
                    }
                }
            }
            if (onDelete != null) {
                IconButton(onClick = onDelete) {
                    Text("🗑️", fontSize = 16.sp)
                }
            }
        }
    }
}

// --- DIALOG TAMBAH TUGAS DENGAN DATE & TIME PICKER ---
@Composable
fun AddTaskDialog(onDismiss: () -> Unit, onAdd: (String, String, String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Kuliah") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var deadlineStr by remember { mutableStateOf("Pilih Tanggal & Waktu") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Tugas Baru") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul Tugas") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Kategori (Kuliah/Organisasi/Pribadi)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedButton(
                    onClick = {
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                TimePickerDialog(
                                    context,
                                    { _, hourOfDay, minute ->
                                        val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
                                        val months = arrayOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Ags", "Sep", "Okt", "Nov", "Des")
                                        deadlineStr = "$dayOfMonth ${months[month]} $year, $formattedTime"
                                    },
                                    calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE),
                                    true
                                ).show()
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("📅 $deadlineStr", color = Color(0xFF1E293B))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && deadlineStr != "Pilih Tanggal & Waktu") {
                        onAdd(title, category, "Tinggi", deadlineStr)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = TelkomRed)
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = TelkomRed)
            }
        }
    )
}