package com.example.flashcardquizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


// =========================================================
// DATA CLASS
// =========================================================

data class Flashcard(
    val question: String,
    val answer: String
)


// =========================================================
// COLORS
// =========================================================

private val Purple = Color(0xFF6C4AB6)
private val LightPurple = Color(0xFF8B5CF6)

private val DarkBackground = Color(0xFF0D0B14)
private val DarkSurface = Color(0xFF171321)
private val DarkCard = Color(0xFF211B30)

private val LightBackground = Color(0xFFF8F6FC)

private val DeleteRed = Color(0xFFD94A4A)


// =========================================================
// MAIN ACTIVITY
// =========================================================

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FlashcardApp()
        }
    }
}


// =========================================================
// MAIN APP
// =========================================================

@Composable
fun FlashcardApp() {

    var darkMode by remember {
        mutableStateOf(false)
    }

    var loggedIn by remember {
        mutableStateOf(false)
    }

    var showRegister by remember {
        mutableStateOf(false)
    }

    var registeredEmail by remember {
        mutableStateOf("")
    }

    var registeredPassword by remember {
        mutableStateOf("")
    }

    val colors = if (darkMode) {

        darkColorScheme(
            primary = LightPurple,
            secondary = Color(0xFFB79CFF),
            background = DarkBackground,
            surface = DarkSurface,
            surfaceVariant = DarkCard,
            onBackground = Color.White,
            onSurface = Color.White,
            onSurfaceVariant = Color(0xFFD0C9DA)
        )

    } else {

        lightColorScheme(
            primary = Purple,
            secondary = LightPurple,
            background = LightBackground,
            surface = Color.White,
            surfaceVariant = Color(0xFFF0EAF8),
            onBackground = Color(0xFF17121F),
            onSurface = Color(0xFF17121F),
            onSurfaceVariant = Color(0xFF625A6D)
        )
    }

    MaterialTheme(
        colorScheme = colors
    ) {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            if (!loggedIn) {

                if (showRegister) {

                    RegisterScreen(
                        darkMode = darkMode,
                        onDarkModeChange = {
                            darkMode = it
                        },
                        onRegister = { email, password ->

                            registeredEmail = email
                            registeredPassword = password
                            showRegister = false
                        },
                        onSignIn = {
                            showRegister = false
                        }
                    )

                } else {

                    SignInScreen(
                        darkMode = darkMode,
                        onDarkModeChange = {
                            darkMode = it
                        },
                        registeredEmail = registeredEmail,
                        registeredPassword = registeredPassword,
                        onSignIn = {
                            loggedIn = true
                        },
                        onRegister = {
                            showRegister = true
                        }
                    )
                }

            } else {

                MainDashboard(
                    darkMode = darkMode,
                    onDarkModeChange = {
                        darkMode = it
                    },
                    onLogout = {
                        loggedIn = false
                    }
                )
            }
        }
    }
}


// =========================================================
// LOGO
// =========================================================

@Composable
fun AppLogo(
    size: Int = 58
) {

    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Purple,
                        LightPurple
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "FQ",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.titleLarge
        )
    }
}


// =========================================================
// SIGN IN SCREEN
// =========================================================

@Composable
fun SignInScreen(
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    registeredEmail: String,
    registeredPassword: String,
    onSignIn: () -> Unit,
    onRegister: () -> Unit
) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            Text(
                if (darkMode) "☀" else "☾",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Switch(
                checked = darkMode,
                onCheckedChange = onDarkModeChange
            )
        }

        Spacer(
            modifier = Modifier.height(35.dp)
        )

        AppLogo(76)

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Text(
            "Welcome Back!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Sign in to continue learning",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                error = ""
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Email")
            },
            placeholder = {
                Text("Enter your email")
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                error = ""
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Password")
            },
            placeholder = {
                Text("Enter your password")
            },
            singleLine = true,
            visualTransformation =
                if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
            trailingIcon = {

                TextButton(
                    onClick = {
                        passwordVisible =
                            !passwordVisible
                    }
                ) {

                    Text(
                        if (passwordVisible)
                            "Hide"
                        else
                            "Show"
                    )
                }
            },
            shape = RoundedCornerShape(14.dp)
        )

        if (error.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                error,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        Button(
            onClick = {

                if (
                    email.isBlank() ||
                    password.isBlank()
                ) {

                    error =
                        "Please enter email and password."

                } else if (
                    registeredEmail.isNotEmpty() &&
                    (
                            email != registeredEmail ||
                                    password != registeredPassword
                            )
                ) {

                    error =
                        "Invalid email or password."

                } else {

                    onSignIn()
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(15.dp)
        ) {

            Text(
                "Sign In",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        HorizontalDivider()

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                "Don't have an account?"
            )

            TextButton(
                onClick = onRegister
            ) {

                Text(
                    "Register",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


// =========================================================
// REGISTER SCREEN
// =========================================================

@Composable
fun RegisterScreen(
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onRegister: (String, String) -> Unit,
    onSignIn: () -> Unit
) {

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            Text(
                if (darkMode) "☀" else "☾"
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Switch(
                checked = darkMode,
                onCheckedChange = onDarkModeChange
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        AppLogo(64)

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Start your learning journey",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                error = ""
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Full Name")
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                error = ""
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Email")
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                error = ""
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Password")
            },
            singleLine = true,
            visualTransformation =
                if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
            trailingIcon = {

                TextButton(
                    onClick = {
                        passwordVisible =
                            !passwordVisible
                    }
                ) {

                    Text(
                        if (passwordVisible)
                            "Hide"
                        else
                            "Show"
                    )
                }
            },
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                error = ""
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Confirm Password")
            },
            singleLine = true,
            visualTransformation =
                if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
            trailingIcon = {

                TextButton(
                    onClick = {
                        passwordVisible =
                            !passwordVisible
                    }
                ) {

                    Text(
                        if (passwordVisible)
                            "Hide"
                        else
                            "Show"
                    )
                }
            },
            shape = RoundedCornerShape(14.dp)
        )

        if (error.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                error,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Button(
            onClick = {

                when {

                    name.isBlank() ||
                            email.isBlank() ||
                            password.isBlank() ||
                            confirmPassword.isBlank() -> {

                        error =
                            "Please fill all fields."
                    }

                    password != confirmPassword -> {

                        error =
                            "Passwords do not match."
                    }

                    password.length < 6 -> {

                        error =
                            "Password must contain at least 6 characters."
                    }

                    else -> {

                        onRegister(
                            email,
                            password
                        )
                    }
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(15.dp)
        ) {

            Text(
                "Create Account",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                "Already have an account?"
            )

            TextButton(
                onClick = onSignIn
            ) {

                Text(
                    "Sign In",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


// =========================================================
// MAIN DASHBOARD
// =========================================================

@Composable
fun MainDashboard(
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onLogout: () -> Unit
) {

    val flashcards = remember {

        mutableStateListOf(

            Flashcard(
                "What is Kotlin?",
                "Kotlin is a modern programming language used for Android development."
            ),

            Flashcard(
                "What is Android?",
                "Android is a mobile operating system developed by Google."
            ),

            Flashcard(
                "What is an APK?",
                "APK stands for Android Package Kit."
            )
        )
    }

    var selectedTab by remember {
        mutableStateOf(0)
    }

    var currentIndex by remember {
        mutableStateOf(0)
    }

    var showAnswer by remember {
        mutableStateOf(false)
    }

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    var showEditDialog by remember {
        mutableStateOf(false)
    }

    var showSettings by remember {
        mutableStateOf(false)
    }

    var menuExpanded by remember {
        mutableStateOf(false)
    }

    Scaffold(

        containerColor =
            MaterialTheme.colorScheme.background,

        bottomBar = {

            NavigationBar(
                containerColor =
                    MaterialTheme.colorScheme.surface
            ) {

                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                    },
                    icon = {
                        Text("⌂")
                    },
                    label = {
                        Text("Home")
                    }
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                    },
                    icon = {
                        Text("▣")
                    },
                    label = {
                        Text("Cards")
                    }
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                    },
                    icon = {
                        Text("▥")
                    },
                    label = {
                        Text("Progress")
                    }
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = {
                        selectedTab = 3
                    },
                    icon = {
                        Text("●")
                    },
                    label = {
                        Text("Profile")
                    }
                )
            }
        }

    ) { paddingValues ->

        when (selectedTab) {

            0 -> {

                HomeScreen(
                    flashcards = flashcards,
                    currentIndex = currentIndex,
                    showAnswer = showAnswer,
                    darkMode = darkMode,
                    menuExpanded = menuExpanded,

                    onMenuChange = {
                        menuExpanded = it
                    },

                    onDarkModeChange =
                        onDarkModeChange,

                    onNext = {

                        if (
                            currentIndex <
                            flashcards.lastIndex
                        ) {

                            currentIndex++
                            showAnswer = false
                        }
                    },

                    onPrevious = {

                        if (currentIndex > 0) {

                            currentIndex--
                            showAnswer = false
                        }
                    },

                    onShowAnswer = {
                        showAnswer = !showAnswer
                    },

                    onAdd = {
                        showAddDialog = true
                    },

                    onEdit = {
                        showEditDialog = true
                    },

                    onDelete = {

                        if (flashcards.isNotEmpty()) {

                            flashcards.removeAt(
                                currentIndex
                            )

                            if (
                                currentIndex >=
                                flashcards.size
                            ) {

                                currentIndex =
                                    (
                                            flashcards.size - 1
                                            ).coerceAtLeast(0)
                            }

                            showAnswer = false
                        }
                    },

                    onProfile = {

                        selectedTab = 3
                        menuExpanded = false
                    },

                    onSettings = {

                        showSettings = true
                        menuExpanded = false
                    },

                    onLogout = onLogout,

                    paddingValues = paddingValues
                )
            }

            1 -> {

                CardsScreen(
                    flashcards = flashcards,
                    paddingValues = paddingValues
                )
            }

            2 -> {

                ProgressScreen(
                    flashcards = flashcards,
                    paddingValues = paddingValues
                )
            }

            3 -> {

                ProfileScreen(
                    flashcards = flashcards,
                    onLogout = onLogout,
                    paddingValues = paddingValues
                )
            }
        }
    }


    // ADD DIALOG

    if (showAddDialog) {

        FlashcardDialog(
            title = "Add Flashcard",
            initialQuestion = "",
            initialAnswer = "",

            onDismiss = {
                showAddDialog = false
            },

            onSave = { question, answer ->

                if (
                    question.isNotBlank() &&
                    answer.isNotBlank()
                ) {

                    flashcards.add(
                        Flashcard(
                            question.trim(),
                            answer.trim()
                        )
                    )

                    currentIndex =
                        flashcards.lastIndex
                }

                showAddDialog = false
            }
        )
    }


    // EDIT DIALOG

    if (
        showEditDialog &&
        flashcards.isNotEmpty()
    ) {

        FlashcardDialog(
            title = "Edit Flashcard",

            initialQuestion =
                flashcards[currentIndex].question,

            initialAnswer =
                flashcards[currentIndex].answer,

            onDismiss = {
                showEditDialog = false
            },

            onSave = { question, answer ->

                if (
                    question.isNotBlank() &&
                    answer.isNotBlank()
                ) {

                    flashcards[currentIndex] =
                        Flashcard(
                            question.trim(),
                            answer.trim()
                        )
                }

                showEditDialog = false
            }
        )
    }


    // SETTINGS

    if (showSettings) {

        SettingsDialog(
            darkMode = darkMode,
            onDarkModeChange =
                onDarkModeChange,

            onDismiss = {
                showSettings = false
            }
        )
    }
}


// =========================================================
// HOME SCREEN
// =========================================================

@Composable
fun HomeScreen(
    flashcards: List<Flashcard>,
    currentIndex: Int,
    showAnswer: Boolean,
    darkMode: Boolean,
    menuExpanded: Boolean,
    onMenuChange: (Boolean) -> Unit,
    onDarkModeChange: (Boolean) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onShowAnswer: () -> Unit,
    onAdd: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onProfile: () -> Unit,
    onSettings: () -> Unit,
    onLogout: () -> Unit,
    paddingValues: PaddingValues
) {

    if (flashcards.isEmpty()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.Center
        ) {

            Text(
                "No flashcards yet",
                style =
                    MaterialTheme.typography.headlineSmall
            )

            Spacer(
                modifier = Modifier.height(15.dp)
            )

            Button(
                onClick = onAdd
            ) {

                Text("+ Add Flashcard")
            }
        }

        return
    }

    val card = flashcards[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {

        // HEADER

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment =
                Alignment.CenterVertically,
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                AppLogo(44)

                Spacer(
                    modifier = Modifier.width(9.dp)
                )

                Column {

                    Text(
                        "Flashcard",
                        style =
                            MaterialTheme.typography.titleMedium,
                        fontWeight =
                            FontWeight.ExtraBold
                    )

                    Text(
                        "QUIZ",
                        color =
                            MaterialTheme.colorScheme.primary,
                        fontWeight =
                            FontWeight.ExtraBold
                    )
                }
            }

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    if (darkMode) "☀" else "☾"
                )

                Switch(
                    checked = darkMode,
                    onCheckedChange =
                        onDarkModeChange
                )

                Box {

                    TextButton(
                        onClick = {
                            onMenuChange(true)
                        }
                    ) {

                        Text(
                            "•••",
                            fontWeight =
                                FontWeight.Bold
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = {
                            onMenuChange(false)
                        }
                    ) {

                        DropdownMenuItem(
                            text = {
                                Text("👤 Profile")
                            },
                            onClick = onProfile
                        )

                        DropdownMenuItem(
                            text = {
                                Text("⚙ Settings")
                            },
                            onClick = onSettings
                        )

                        DropdownMenuItem(
                            text = {
                                Text("↪ Logout")
                            },
                            onClick = {

                                onMenuChange(false)
                                onLogout()
                            }
                        )
                    }
                }
            }
        }


        // WELCOME

        Spacer(
            modifier = Modifier.height(9.dp)
        )

        Text(
            "Good day! 👋",
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme
                    .onSurfaceVariant
        )

        Text(
            "Keep Learning",
            style =
                MaterialTheme.typography.headlineSmall,
            fontWeight =
                FontWeight.ExtraBold
        )

        Text(
            "Build your knowledge one card at a time.",
            style =
                MaterialTheme.typography.bodySmall,
            color =
                MaterialTheme.colorScheme
                    .onSurfaceVariant
        )


        // STATS

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            CompactStatCard(
                number =
                    flashcards.size.toString(),
                label = "Cards",
                modifier =
                    Modifier.weight(1f)
            )

            CompactStatCard(
                number = "75%",
                label = "Progress",
                modifier =
                    Modifier.weight(1f)
            )

            CompactStatCard(
                number = "12",
                label = "Learned",
                modifier =
                    Modifier.weight(1f)
            )
        }


        // TITLE

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            "Today's Flashcard",
            style =
                MaterialTheme.typography.titleLarge,
            fontWeight =
                FontWeight.ExtraBold
        )


        // FLASHCARD

        Spacer(
            modifier = Modifier.height(7.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp),

            shape =
                RoundedCornerShape(24.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        if (darkMode)
                            DarkCard
                        else
                            Color(0xFFEFE9F8)
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally,

                verticalArrangement =
                    Arrangement.Center
            ) {

                Text(
                    "QUESTION",
                    color =
                        MaterialTheme.colorScheme.primary,
                    fontWeight =
                        FontWeight.ExtraBold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    card.question,
                    style =
                        MaterialTheme.typography.titleLarge,
                    fontWeight =
                        FontWeight.Bold,
                    textAlign =
                        TextAlign.Center
                )

                if (showAnswer) {

                    Spacer(
                        modifier = Modifier.height(7.dp)
                    )

                    Text(
                        card.answer,
                        style =
                            MaterialTheme.typography.bodySmall,
                        textAlign =
                            TextAlign.Center,
                        color =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Button(
                    onClick = onShowAnswer,
                    modifier =
                        Modifier.height(42.dp),
                    shape =
                        RoundedCornerShape(25.dp)
                ) {

                    Text(
                        if (showAnswer)
                            "Hide Answer"
                        else
                            "Show Answer"
                    )
                }
            }
        }


        // PREVIOUS / NEXT

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            Button(
                onClick = onPrevious,
                modifier = Modifier
                    .weight(1f)
                    .height(45.dp),
                shape =
                    RoundedCornerShape(13.dp)
            ) {

                Text("← Previous")
            }

            Button(
                onClick = onNext,
                modifier = Modifier
                    .weight(1f)
                    .height(45.dp),
                shape =
                    RoundedCornerShape(13.dp)
            ) {

                Text("Next →")
            }
        }


        // ADD / EDIT / DELETE

        Spacer(
            modifier = Modifier.height(7.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(7.dp)
        ) {

            Button(
                onClick = onAdd,
                modifier = Modifier
                    .weight(1f)
                    .height(43.dp),
                shape =
                    RoundedCornerShape(12.dp)
            ) {

                Text(
                    "+ Add",
                    fontWeight =
                        FontWeight.Bold
                )
            }

            Button(
                onClick = onEdit,
                modifier = Modifier
                    .weight(1f)
                    .height(43.dp),
                shape =
                    RoundedCornerShape(12.dp)
            ) {

                Text(
                    "✎ Edit",
                    fontWeight =
                        FontWeight.Bold
                )
            }

            Button(
                onClick = onDelete,
                modifier = Modifier
                    .weight(1f)
                    .height(43.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            DeleteRed
                    ),
                shape =
                    RoundedCornerShape(12.dp)
            ) {

                Text(
                    "Delete",
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }


        // PROGRESS

        Spacer(
            modifier = Modifier.height(7.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(
                "Learning Progress",
                style =
                    MaterialTheme.typography.bodySmall,
                fontWeight =
                    FontWeight.Bold
            )

            Text(
                "${currentIndex + 1}/${flashcards.size}",
                style =
                    MaterialTheme.typography.bodySmall,
                color =
                    MaterialTheme.colorScheme.primary,
                fontWeight =
                    FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        LinearProgressIndicator(
            progress = {
                (currentIndex + 1).toFloat() /
                        flashcards.size.toFloat()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(
                    RoundedCornerShape(20.dp)
                )
        )

        Spacer(
            modifier = Modifier.height(15.dp)
        )
    }
}


// =========================================================
// COMPACT STAT CARD
// =========================================================

@Composable
fun CompactStatCard(
    number: String,
    label: String,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.height(72.dp),
        shape = RoundedCornerShape(15.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surface
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),

            verticalArrangement =
                Arrangement.Center
        ) {

            Text(
                number,
                style =
                    MaterialTheme.typography.titleLarge,
                fontWeight =
                    FontWeight.ExtraBold,
                color =
                    MaterialTheme.colorScheme.primary
            )

            Text(
                label,
                style =
                    MaterialTheme.typography.labelSmall,
                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant
            )
        }
    }
}


// =========================================================
// CARDS SCREEN
// =========================================================

@Composable
fun CardsScreen(
    flashcards: List<Flashcard>,
    paddingValues: PaddingValues
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(20.dp)
    ) {

        Text(
            "My Flashcards",
            style =
                MaterialTheme.typography.headlineMedium,
            fontWeight =
                FontWeight.ExtraBold
        )

        Text(
            "${flashcards.size} cards available",
            color =
                MaterialTheme.colorScheme
                    .onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        LazyColumn(
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            itemsIndexed(
                flashcards
            ) { index, card ->

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(18.dp)
                ) {

                    Column(
                        modifier =
                            Modifier.padding(18.dp)
                    ) {

                        Text(
                            "CARD ${index + 1}",
                            color =
                                MaterialTheme.colorScheme
                                    .primary,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Text(
                            card.question,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(5.dp)
                        )

                        Text(
                            card.answer,
                            color =
                                MaterialTheme.colorScheme
                                    .onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}


// =========================================================
// PROGRESS SCREEN
// =========================================================

@Composable
fun ProgressScreen(
    flashcards: List<Flashcard>,
    paddingValues: PaddingValues
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(20.dp)
    ) {

        Text(
            "Your Progress",
            style =
                MaterialTheme.typography.headlineMedium,
            fontWeight =
                FontWeight.ExtraBold
        )

        Text(
            "Track your learning journey",
            color =
                MaterialTheme.colorScheme
                    .onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        Card(
            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(22.dp)
        ) {

            Column(
                modifier =
                    Modifier.padding(25.dp)
            ) {

                Text(
                    "Overall Progress",
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(15.dp)
                )

                LinearProgressIndicator(
                    progress = {
                        0.75f
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(
                            RoundedCornerShape(20.dp)
                        )
                )

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Text(
                    "75% Completed",
                    color =
                        MaterialTheme.colorScheme
                            .primary,
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(18.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            CompactStatCard(
                number =
                    flashcards.size.toString(),
                label = "Total Cards",
                modifier =
                    Modifier.weight(1f)
            )

            CompactStatCard(
                number = "12",
                label = "Learned",
                modifier =
                    Modifier.weight(1f)
            )
        }
    }
}


// =========================================================
// PROFILE SCREEN
// =========================================================

@Composable
fun ProfileScreen(
    flashcards: List<Flashcard>,
    onLogout: () -> Unit,
    paddingValues: PaddingValues
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(20.dp)
            .verticalScroll(
                rememberScrollState()
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        AppLogo(90)

        Spacer(
            modifier =
                Modifier.height(15.dp)
        )

        Text(
            "Student",
            style =
                MaterialTheme.typography.headlineSmall,
            fontWeight =
                FontWeight.Bold
        )

        Text(
            "student@example.com",
            color =
                MaterialTheme.colorScheme
                    .onSurfaceVariant
        )

        Spacer(
            modifier =
                Modifier.height(25.dp)
        )

        Card(
            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(20.dp)
        ) {

            Column(
                modifier =
                    Modifier.padding(20.dp)
            ) {

                ProfileRow(
                    "Flashcards",
                    flashcards.size.toString()
                )

                Spacer(
                    modifier =
                        Modifier.height(15.dp)
                )

                ProfileRow(
                    "Cards Learned",
                    "12"
                )

                Spacer(
                    modifier =
                        Modifier.height(15.dp)
                )

                ProfileRow(
                    "Progress",
                    "75%"
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(25.dp)
        )

        Button(
            onClick = onLogout,

            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        DeleteRed
                ),

            shape =
                RoundedCornerShape(14.dp)
        ) {

            Text("Logout")
        }

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )
    }
}


// =========================================================
// PROFILE ROW
// =========================================================

@Composable
fun ProfileRow(
    title: String,
    value: String
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            title,
            color =
                MaterialTheme.colorScheme
                    .onSurfaceVariant
        )

        Text(
            value,
            fontWeight =
                FontWeight.Bold
        )
    }
}


// =========================================================
// SETTINGS DIALOG
// =========================================================

@Composable
fun SettingsDialog(
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {

            Text(
                "Settings",
                fontWeight =
                    FontWeight.Bold
            )
        },

        text = {

            Column {

                Text(
                    "Appearance",
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(15.dp)
                )

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween,

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        if (darkMode)
                            "Dark Mode"
                        else
                            "Light Mode"
                    )

                    Switch(
                        checked = darkMode,
                        onCheckedChange =
                            onDarkModeChange
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                HorizontalDivider()

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Text(
                    "About",
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    "Flashcard Quiz App\nVersion 1.0"
                )
            }
        },

        confirmButton = {

            Button(
                onClick = onDismiss
            ) {

                Text("Done")
            }
        }
    )
}


// =========================================================
// ADD / EDIT FLASHCARD DIALOG
// =========================================================

@Composable
fun FlashcardDialog(
    title: String,
    initialQuestion: String,
    initialAnswer: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {

    var question by remember {
        mutableStateOf(initialQuestion)
    }

    var answer by remember {
        mutableStateOf(initialAnswer)
    }

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {

            Text(
                title,
                fontWeight =
                    FontWeight.Bold
            )
        },

        text = {

            Column {

                OutlinedTextField(
                    value = question,

                    onValueChange = {
                        question = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Question")
                    },

                    shape =
                        RoundedCornerShape(12.dp)
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                OutlinedTextField(
                    value = answer,

                    onValueChange = {
                        answer = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Answer")
                    },

                    shape =
                        RoundedCornerShape(12.dp)
                )
            }
        },

        confirmButton = {

            Button(
                onClick = {
                    onSave(
                        question,
                        answer
                    )
                }
            ) {

                Text("Save")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Cancel")
            }
        }
    )
}