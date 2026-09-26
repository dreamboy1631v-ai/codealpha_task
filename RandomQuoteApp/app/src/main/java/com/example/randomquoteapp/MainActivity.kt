package com.example.randomquoteapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// ============================================================
// COLORS
// ============================================================

val TealPrimary = Color(0xFF009688)
val TealDark = Color(0xFF00695C)
val TealLight = Color(0xFF4DB6AC)

val LightBackground = Color(0xFFF6FAF9)
val LightCard = Color(0xFFFFFFFF)

val DarkBackground = Color(0xFF081312)
val DarkSurface = Color(0xFF10201E)
val DarkCard = Color(0xFF172B28)


// ============================================================
// QUOTE DATA
// ============================================================

data class Quote(
    val text: String,
    val author: String
)


// ============================================================
// QUOTES
// ============================================================

val quoteList = listOf(

    Quote(
        "The only way to do great work is to love what you do.",
        "Steve Jobs"
    ),

    Quote(
        "Success is not final, failure is not fatal.",
        "Winston Churchill"
    ),

    Quote(
        "Believe you can and you're halfway there.",
        "Theodore Roosevelt"
    ),

    Quote(
        "It always seems impossible until it's done.",
        "Nelson Mandela"
    ),

    Quote(
        "The future depends on what you do today.",
        "Mahatma Gandhi"
    ),

    Quote(
        "Dream big and dare to fail.",
        "Norman Vaughan"
    ),

    Quote(
        "Do something today that your future self will thank you for.",
        "Sean Patrick Flanery"
    ),

    Quote(
        "Don't watch the clock; do what it does. Keep going.",
        "Sam Levenson"
    ),

    Quote(
        "Great things are done by a series of small things brought together.",
        "Vincent van Gogh"
    ),

    Quote(
        "The secret of getting ahead is getting started.",
        "Mark Twain"
    )
)


// ============================================================
// APP SCREEN
// ============================================================

enum class AppScreen {
    SIGN_IN,
    REGISTER,
    HOME,
    FAVORITES,
    PROFILE
}


// ============================================================
// MAIN ACTIVITY
// ============================================================

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            RandomQuoteApplication()
        }
    }
}


// ============================================================
// MAIN APPLICATION
// ============================================================

@Composable
fun RandomQuoteApplication() {

    var darkMode by remember {
        mutableStateOf(false)
    }

    val colors = if (darkMode) {

        darkColorScheme(
            primary = TealLight,
            secondary = TealPrimary,
            background = DarkBackground,
            surface = DarkSurface,
            onBackground = Color.White,
            onSurface = Color.White
        )

    } else {

        lightColorScheme(
            primary = TealPrimary,
            secondary = TealDark,
            background = LightBackground,
            surface = LightCard,
            onBackground = Color(0xFF15201E),
            onSurface = Color(0xFF15201E)
        )
    }

    MaterialTheme(
        colorScheme = colors
    ) {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            MainController(
                darkMode = darkMode,
                onDarkModeChange = {
                    darkMode = it
                }
            )
        }
    }
}


// ============================================================
// MAIN CONTROLLER
// ============================================================

@Composable
fun MainController(
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {

    var screen by remember {
        mutableStateOf(AppScreen.SIGN_IN)
    }

    var currentIndex by remember {
        mutableStateOf(0)
    }

    var favorite by remember {
        mutableStateOf(false)
    }

    var generatedCount by remember {
        mutableStateOf(0)
    }

    var settingsVisible by remember {
        mutableStateOf(false)
    }

    when (screen) {

        AppScreen.SIGN_IN -> {

            SignInScreen(

                darkMode = darkMode,

                onDarkModeChange =
                    onDarkModeChange,

                onSignIn = {

                    screen =
                        AppScreen.HOME
                },

                onRegister = {

                    screen =
                        AppScreen.REGISTER
                }
            )
        }

        AppScreen.REGISTER -> {

            RegisterScreen(

                darkMode = darkMode,

                onDarkModeChange =
                    onDarkModeChange,

                onRegister = {

                    screen =
                        AppScreen.HOME
                },

                onSignIn = {

                    screen =
                        AppScreen.SIGN_IN
                }
            )
        }

        AppScreen.HOME -> {

            MainDashboard(

                selectedTab = 0,

                onTabChange = { tab ->

                    screen =
                        when (tab) {

                            0 ->
                                AppScreen.HOME

                            1 ->
                                AppScreen.FAVORITES

                            else ->
                                AppScreen.PROFILE
                        }
                },

                darkMode =
                    darkMode,

                onDarkModeChange =
                    onDarkModeChange,

                currentQuote =
                    quoteList[currentIndex],

                currentIndex =
                    currentIndex,

                favorite =
                    favorite,

                generatedCount =
                    generatedCount,

                onNewQuote = {

                    var newIndex =
                        quoteList.indices.random()

                    while (
                        quoteList.size > 1 &&
                        newIndex == currentIndex
                    ) {

                        newIndex =
                            quoteList.indices.random()
                    }

                    currentIndex =
                        newIndex

                    generatedCount++

                    favorite = false
                },

                onFavorite = {

                    favorite =
                        !favorite
                },

                onCopy = {

                    copyQuote(
                        quoteList[currentIndex]
                    )
                },

                onShare = {

                    shareQuote(
                        quoteList[currentIndex]
                    )
                },

                onSettings = {

                    settingsVisible =
                        true
                },

                onLogout = {

                    screen =
                        AppScreen.SIGN_IN
                }
            )
        }

        AppScreen.FAVORITES -> {

            MainDashboard(

                selectedTab = 1,

                onTabChange = { tab ->

                    screen =
                        when (tab) {

                            0 ->
                                AppScreen.HOME

                            1 ->
                                AppScreen.FAVORITES

                            else ->
                                AppScreen.PROFILE
                        }
                },

                darkMode =
                    darkMode,

                onDarkModeChange =
                    onDarkModeChange,

                currentQuote =
                    quoteList[currentIndex],

                currentIndex =
                    currentIndex,

                favorite =
                    favorite,

                generatedCount =
                    generatedCount,

                onNewQuote = {

                    var newIndex =
                        quoteList.indices.random()

                    while (
                        newIndex == currentIndex &&
                        quoteList.size > 1
                    ) {

                        newIndex =
                            quoteList.indices.random()
                    }

                    currentIndex =
                        newIndex

                    generatedCount++

                    favorite = false
                },

                onFavorite = {

                    favorite =
                        !favorite
                },

                onCopy = {

                    copyQuote(
                        quoteList[currentIndex]
                    )
                },

                onShare = {

                    shareQuote(
                        quoteList[currentIndex]
                    )
                },

                onSettings = {

                    settingsVisible =
                        true
                },

                onLogout = {

                    screen =
                        AppScreen.SIGN_IN
                }
            )
        }

        AppScreen.PROFILE -> {

            MainDashboard(

                selectedTab = 2,

                onTabChange = { tab ->

                    screen =
                        when (tab) {

                            0 ->
                                AppScreen.HOME

                            1 ->
                                AppScreen.FAVORITES

                            else ->
                                AppScreen.PROFILE
                        }
                },

                darkMode =
                    darkMode,

                onDarkModeChange =
                    onDarkModeChange,

                currentQuote =
                    quoteList[currentIndex],

                currentIndex =
                    currentIndex,

                favorite =
                    favorite,

                generatedCount =
                    generatedCount,

                onNewQuote = {

                    var newIndex =
                        quoteList.indices.random()

                    while (
                        newIndex == currentIndex &&
                        quoteList.size > 1
                    ) {

                        newIndex =
                            quoteList.indices.random()
                    }

                    currentIndex =
                        newIndex

                    generatedCount++

                    favorite = false
                },

                onFavorite = {

                    favorite =
                        !favorite
                },

                onCopy = {

                    copyQuote(
                        quoteList[currentIndex]
                    )
                },

                onShare = {

                    shareQuote(
                        quoteList[currentIndex]
                    )
                },

                onSettings = {

                    settingsVisible =
                        true
                },

                onLogout = {

                    screen =
                        AppScreen.SIGN_IN
                }
            )
        }
    }


    if (settingsVisible) {

        SettingsDialog(

            darkMode =
                darkMode,

            onDarkModeChange =
                onDarkModeChange,

            onDismiss = {

                settingsVisible =
                    false
            }
        )
    }
}


// ============================================================
// LOGO
// ============================================================

@Composable
fun QuoteFlowLogo(
    large: Boolean = false
) {

    val logoSize =
        if (large) 82.dp else 52.dp

    val fontSize =
        if (large) 38.sp else 25.sp

    Box(

        modifier =
            Modifier
                .size(logoSize)
                .clip(
                    RoundedCornerShape(
                        if (large) 25.dp
                        else 16.dp
                    )
                )
                .background(
                    TealPrimary
                ),

        contentAlignment =
            Alignment.Center
    ) {

        Text(

            "Q",

            color =
                Color.White,

            fontSize =
                fontSize,

            fontWeight =
                FontWeight.ExtraBold
        )
    }
}


// ============================================================
// SIGN IN SCREEN
// ============================================================

@Composable
fun SignInScreen(

    darkMode: Boolean,

    onDarkModeChange:
        (Boolean) -> Unit,

    onSignIn:
        () -> Unit,

    onRegister:
        () -> Unit
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

    var rememberMe by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(22.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        // TOP BAR

        Row(

            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                QuoteFlowLogo()

                Spacer(
                    modifier =
                        Modifier.width(10.dp)
                )

                Column {

                    Text(
                        "QuoteFlow",
                        fontSize = 20.sp,
                        fontWeight =
                            FontWeight.ExtraBold
                    )

                    Text(
                        "DAILY INSPIRATION",
                        fontSize = 8.sp,
                        color =
                            MaterialTheme
                                .colorScheme
                                .primary,
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    if (darkMode)
                        "☀"
                    else
                        "☾",
                    fontSize = 19.sp
                )

                Switch(
                    checked =
                        darkMode,

                    onCheckedChange =
                        onDarkModeChange
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(38.dp)
        )


        QuoteFlowLogo(
            large = true
        )


        Spacer(
            modifier =
                Modifier.height(20.dp)
        )


        Text(
            "Welcome Back!",
            fontSize = 30.sp,
            fontWeight =
                FontWeight.ExtraBold
        )

        Text(
            "Sign in to continue your inspiration journey",
            fontSize = 13.sp,
            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant,
            textAlign =
                TextAlign.Center
        )


        Spacer(
            modifier =
                Modifier.height(28.dp)
        )


        // EMAIL

        Text(
            "Email",
            modifier =
                Modifier.fillMaxWidth(),
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )

        OutlinedTextField(

            value =
                email,

            onValueChange = {
                email = it
                errorMessage = ""
            },

            modifier =
                Modifier.fillMaxWidth(),

            singleLine = true,

            placeholder = {
                Text("Enter your email")
            },

            leadingIcon = {
                Text(
                    "✉",
                    fontSize = 20.sp
                )
            },

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Email
                ),

            shape =
                RoundedCornerShape(15.dp)
        )


        Spacer(
            modifier =
                Modifier.height(17.dp)
        )


        // PASSWORD

        Text(
            "Password",
            modifier =
                Modifier.fillMaxWidth(),
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )

        OutlinedTextField(

            value =
                password,

            onValueChange = {
                password = it
                errorMessage = ""
            },

            modifier =
                Modifier.fillMaxWidth(),

            singleLine = true,

            placeholder = {
                Text("Enter your password")
            },

            leadingIcon = {
                Text(
                    "🔒",
                    fontSize = 18.sp
                )
            },

            trailingIcon = {

                Text(

                    if (passwordVisible)
                        "🙈"
                    else
                        "👁",

                    modifier =
                        Modifier.clickable {
                            passwordVisible =
                                !passwordVisible
                        },

                    fontSize = 19.sp
                )
            },

            visualTransformation =
                if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Password
                ),

            shape =
                RoundedCornerShape(15.dp)
        )


        Spacer(
            modifier =
                Modifier.height(8.dp)
        )


        Row(

            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically,

            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Checkbox(

                    checked =
                        rememberMe,

                    onCheckedChange = {
                        rememberMe = it
                    }
                )

                Text(
                    "Remember me",
                    fontSize = 13.sp
                )
            }

            TextButton(
                onClick = {

                    errorMessage =
                        "Password reset is available after account setup."
                }
            ) {

                Text(
                    "Forgot Password?"
                )
            }
        }


        if (errorMessage.isNotEmpty()) {

            Text(

                errorMessage,

                modifier =
                    Modifier.fillMaxWidth(),

                color =
                    Color(0xFFE53935),

                fontSize = 12.sp
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )
        }


        Spacer(
            modifier =
                Modifier.height(8.dp)
        )


        Button(

            onClick = {

                if (
                    email.isNotBlank() &&
                    password.isNotBlank()
                ) {

                    onSignIn()

                } else {

                    errorMessage =
                        "Please enter email and password."
                }
            },

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),

            shape =
                RoundedCornerShape(16.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        TealPrimary
                )
        ) {

            Text(
                "Sign In",
                fontSize = 16.sp,
                fontWeight =
                    FontWeight.Bold
            )
        }


        Spacer(
            modifier =
                Modifier.height(22.dp)
        )


        Row(
            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            HorizontalDivider(
                modifier =
                    Modifier.weight(1f)
            )

            Text(
                "  or continue with  ",
                fontSize = 11.sp,
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )

            HorizontalDivider(
                modifier =
                    Modifier.weight(1f)
            )
        }


        Spacer(
            modifier =
                Modifier.height(15.dp)
        )


        Row(

            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            OutlinedButton(

                onClick = onSignIn,

                modifier =
                    Modifier
                        .weight(1f)
                        .height(50.dp),

                shape =
                    RoundedCornerShape(14.dp)
            ) {

                Text(
                    "G  Google",
                    fontWeight =
                        FontWeight.Bold
                )
            }

            OutlinedButton(

                onClick = onSignIn,

                modifier =
                    Modifier
                        .weight(1f)
                        .height(50.dp),

                shape =
                    RoundedCornerShape(14.dp)
            ) {

                Text(
                    "  Apple",
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(25.dp)
        )


        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                "Don't have an account?",
                fontSize = 13.sp
            )

            TextButton(
                onClick =
                    onRegister
            ) {

                Text(
                    "Register",
                    color =
                        MaterialTheme
                            .colorScheme
                            .primary,
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(20.dp)
        )
    }
}


// ============================================================
// REGISTER SCREEN
// ============================================================

@Composable
fun RegisterScreen(

    darkMode: Boolean,

    onDarkModeChange:
        (Boolean) -> Unit,

    onRegister:
        () -> Unit,

    onSignIn:
        () -> Unit
) {

    var fullName by remember {
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

    var confirmVisible by remember {
        mutableStateOf(false)
    }

    var agree by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }


    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(22.dp)
    ) {

        Spacer(
            modifier =
                Modifier.height(10.dp)
        )


        Row(

            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            TextButton(
                onClick =
                    onSignIn
            ) {

                Text(
                    "←  Back",
                    fontSize = 15.sp
                )
            }

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    if (darkMode)
                        "☀"
                    else
                        "☾",
                    fontSize = 19.sp
                )

                Switch(
                    checked =
                        darkMode,

                    onCheckedChange =
                        onDarkModeChange
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(18.dp)
        )


        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            QuoteFlowLogo()

            Spacer(
                modifier =
                    Modifier.width(12.dp)
            )

            Column {

                Text(
                    "QuoteFlow",
                    fontSize = 22.sp,
                    fontWeight =
                        FontWeight.ExtraBold
                )

                Text(
                    "JOIN THE INSPIRATION",
                    fontSize = 9.sp,
                    color =
                        MaterialTheme
                            .colorScheme
                            .primary,
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(25.dp)
        )


        Text(
            "Create Account",
            fontSize = 30.sp,
            fontWeight =
                FontWeight.ExtraBold
        )

        Text(
            "Join us and discover inspiring quotes every day.",
            fontSize = 13.sp,
            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )


        Spacer(
            modifier =
                Modifier.height(25.dp)
        )


        // FULL NAME

        Text(
            "Full Name",
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )

        OutlinedTextField(

            value =
                fullName,

            onValueChange = {
                fullName = it
                errorMessage = ""
            },

            modifier =
                Modifier.fillMaxWidth(),

            singleLine = true,

            placeholder = {
                Text("Enter your full name")
            },

            leadingIcon = {
                Text(
                    "●",
                    fontSize = 18.sp
                )
            },

            shape =
                RoundedCornerShape(15.dp)
        )


        Spacer(
            modifier =
                Modifier.height(15.dp)
        )


        // EMAIL

        Text(
            "Email",
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )

        OutlinedTextField(

            value =
                email,

            onValueChange = {
                email = it
                errorMessage = ""
            },

            modifier =
                Modifier.fillMaxWidth(),

            singleLine = true,

            placeholder = {
                Text("Enter your email")
            },

            leadingIcon = {
                Text(
                    "✉",
                    fontSize = 20.sp
                )
            },

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Email
                ),

            shape =
                RoundedCornerShape(15.dp)
        )


        Spacer(
            modifier =
                Modifier.height(15.dp)
        )


        // PASSWORD

        Text(
            "Password",
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )

        OutlinedTextField(

            value =
                password,

            onValueChange = {
                password = it
                errorMessage = ""
            },

            modifier =
                Modifier.fillMaxWidth(),

            singleLine = true,

            placeholder = {
                Text("Create a password")
            },

            leadingIcon = {
                Text(
                    "🔒",
                    fontSize = 18.sp
                )
            },

            trailingIcon = {

                Text(

                    if (passwordVisible)
                        "🙈"
                    else
                        "👁",

                    modifier =
                        Modifier.clickable {

                            passwordVisible =
                                !passwordVisible
                        },

                    fontSize = 19.sp
                )
            },

            visualTransformation =
                if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Password
                ),

            shape =
                RoundedCornerShape(15.dp)
        )


        Spacer(
            modifier =
                Modifier.height(15.dp)
        )


        // CONFIRM PASSWORD

        Text(
            "Confirm Password",
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )

        OutlinedTextField(

            value =
                confirmPassword,

            onValueChange = {
                confirmPassword = it
                errorMessage = ""
            },

            modifier =
                Modifier.fillMaxWidth(),

            singleLine = true,

            placeholder = {
                Text("Confirm your password")
            },

            leadingIcon = {
                Text(
                    "🔒",
                    fontSize = 18.sp
                )
            },

            trailingIcon = {

                Text(

                    if (confirmVisible)
                        "🙈"
                    else
                        "👁",

                    modifier =
                        Modifier.clickable {

                            confirmVisible =
                                !confirmVisible
                        },

                    fontSize = 19.sp
                )
            },

            visualTransformation =
                if (confirmVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Password
                ),

            shape =
                RoundedCornerShape(15.dp)
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        // TERMS

        Row(

            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Checkbox(

                checked =
                    agree,

                onCheckedChange = {
                    agree = it
                    errorMessage = ""
                }
            )

            Text(
                "I agree to the Terms & Conditions",
                fontSize = 12.sp
            )
        }


        if (errorMessage.isNotEmpty()) {

            Text(

                errorMessage,

                color =
                    Color(0xFFE53935),

                fontSize = 12.sp,

                modifier =
                    Modifier.fillMaxWidth()
            )

            Spacer(
                modifier =
                    Modifier.height(7.dp)
            )
        }


        Spacer(
            modifier =
                Modifier.height(8.dp)
        )


        Button(

            onClick = {

                when {

                    fullName.isBlank() ||
                            email.isBlank() ||
                            password.isBlank() ||
                            confirmPassword.isBlank() -> {

                        errorMessage =
                            "Please complete all fields."
                    }

                    password != confirmPassword -> {

                        errorMessage =
                            "Passwords do not match."
                    }

                    !agree -> {

                        errorMessage =
                            "Please accept the Terms & Conditions."
                    }

                    else -> {

                        onRegister()
                    }
                }
            },

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),

            shape =
                RoundedCornerShape(16.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        TealPrimary
                )
        ) {

            Text(
                "Create Account",
                fontSize = 16.sp,
                fontWeight =
                    FontWeight.Bold
            )
        }


        Spacer(
            modifier =
                Modifier.height(20.dp)
        )


        Row(
            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.Center,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                "Already have an account?",
                fontSize = 13.sp
            )

            TextButton(
                onClick =
                    onSignIn
            ) {

                Text(
                    "Sign In",
                    color =
                        MaterialTheme
                            .colorScheme
                            .primary,
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(20.dp)
        )
    }
}


// ============================================================
// DASHBOARD
// ============================================================

@Composable
fun MainDashboard(

    selectedTab: Int,

    onTabChange:
        (Int) -> Unit,

    darkMode: Boolean,

    onDarkModeChange:
        (Boolean) -> Unit,

    currentQuote: Quote,

    currentIndex: Int,

    favorite: Boolean,

    generatedCount: Int,

    onNewQuote:
        () -> Unit,

    onFavorite:
        () -> Unit,

    onCopy:
        () -> Unit,

    onShare:
        () -> Unit,

    onSettings:
        () -> Unit,

    onLogout:
        () -> Unit
) {

    Scaffold(

        containerColor =
            MaterialTheme
                .colorScheme
                .background,

        bottomBar = {

            NavigationBar {

                NavigationBarItem(

                    selected =
                        selectedTab == 0,

                    onClick = {
                        onTabChange(0)
                    },

                    icon = {
                        Text(
                            "⌂",
                            fontSize = 22.sp
                        )
                    },

                    label = {
                        Text("Home")
                    }
                )

                NavigationBarItem(

                    selected =
                        selectedTab == 1,

                    onClick = {
                        onTabChange(1)
                    },

                    icon = {
                        Text(
                            "♥",
                            fontSize = 20.sp
                        )
                    },

                    label = {
                        Text("Favorites")
                    }
                )

                NavigationBarItem(

                    selected =
                        selectedTab == 2,

                    onClick = {
                        onTabChange(2)
                    },

                    icon = {
                        Text(
                            "●",
                            fontSize = 20.sp
                        )
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

                HomeDashboard(

                    quote =
                        currentQuote,

                    currentIndex =
                        currentIndex,

                    favorite =
                        favorite,

                    generatedCount =
                        generatedCount,

                    darkMode =
                        darkMode,

                    onDarkModeChange =
                        onDarkModeChange,

                    onNewQuote =
                        onNewQuote,

                    onFavorite =
                        onFavorite,

                    onCopy =
                        onCopy,

                    onShare =
                        onShare,

                    onSettings =
                        onSettings,

                    paddingValues =
                        paddingValues
                )
            }

            1 -> {

                FavoritesDashboard(

                    quote =
                        currentQuote,

                    favorite =
                        favorite,

                    paddingValues =
                        paddingValues
                )
            }

            2 -> {

                ProfileDashboard(

                    generatedCount =
                        generatedCount,

                    paddingValues =
                        paddingValues,

                    onLogout =
                        onLogout
                )
            }
        }
    }
}


// ============================================================
// HOME DASHBOARD
// ============================================================

@Composable
fun HomeDashboard(

    quote: Quote,

    currentIndex: Int,

    favorite: Boolean,

    generatedCount: Int,

    darkMode: Boolean,

    onDarkModeChange:
        (Boolean) -> Unit,

    onNewQuote:
        () -> Unit,

    onFavorite:
        () -> Unit,

    onCopy:
        () -> Unit,

    onShare:
        () -> Unit,

    onSettings:
        () -> Unit,

    paddingValues:
    PaddingValues
) {

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(paddingValues)
                .padding(
                    horizontal = 18.dp
                )
    ) {

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        // HEADER

        Row(

            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                QuoteFlowLogo()

                Spacer(
                    modifier =
                        Modifier.width(10.dp)
                )

                Column {

                    Text(
                        "QuoteFlow",
                        fontSize = 21.sp,
                        fontWeight =
                            FontWeight.ExtraBold
                    )

                    Text(
                        "INSPIRATION",
                        fontSize = 8.sp,
                        color =
                            MaterialTheme
                                .colorScheme
                                .primary,
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    if (darkMode)
                        "☀"
                    else
                        "☾",
                    fontSize = 19.sp
                )

                Switch(
                    checked =
                        darkMode,

                    onCheckedChange =
                        onDarkModeChange
                )

                TextButton(
                    onClick =
                        onSettings
                ) {

                    Text(
                        "⚙",
                        fontSize = 21.sp
                    )
                }
            }
        }


        Spacer(
            modifier =
                Modifier.height(25.dp)
        )


        Text(
            "Good day! 👋",
            fontSize = 14.sp,
            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )

        Text(
            "Find Your Inspiration",
            fontSize = 28.sp,
            fontWeight =
                FontWeight.ExtraBold
        )

        Text(
            "Discover a new thought every day.",
            fontSize = 13.sp,
            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )


        Spacer(
            modifier =
                Modifier.height(20.dp)
        )


        // STATS

        Row(

            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(9.dp)
        ) {

            DashboardStat(
                value =
                    generatedCount.toString(),
                label =
                    "Generated",
                modifier =
                    Modifier.weight(1f)
            )

            DashboardStat(
                value =
                    quoteList.size.toString(),
                label =
                    "Quotes",
                modifier =
                    Modifier.weight(1f)
            )

            DashboardStat(
                value =
                    if (favorite) "1" else "0",
                label =
                    "Saved",
                modifier =
                    Modifier.weight(1f)
            )
        }


        Spacer(
            modifier =
                Modifier.height(25.dp)
        )


        Text(
            "Quote of the Day",
            fontSize = 22.sp,
            fontWeight =
                FontWeight.ExtraBold
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        // QUOTE CARD

        Card(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(350.dp),

            shape =
                RoundedCornerShape(27.dp),

            colors =
                CardDefaults.cardColors(

                    containerColor =
                        if (darkMode)
                            DarkCard
                        else
                            Color.White
                )
        ) {

            Column(

                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(25.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally,

                verticalArrangement =
                    Arrangement.Center
            ) {

                Text(
                    "❝",
                    fontSize = 48.sp,
                    color =
                        MaterialTheme
                            .colorScheme
                            .primary
                )

                Text(
                    "DAILY INSPIRATION",
                    fontSize = 11.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        MaterialTheme
                            .colorScheme
                            .primary
                )

                Spacer(
                    modifier =
                        Modifier.height(18.dp)
                )

                Text(

                    "\"${quote.text}\"",

                    modifier =
                        Modifier.fillMaxWidth(),

                    fontSize = 21.sp,

                    lineHeight =
                        30.sp,

                    fontWeight =
                        FontWeight.Bold,

                    textAlign =
                        TextAlign.Center
                )

                Spacer(
                    modifier =
                        Modifier.height(17.dp)
                )

                Text(
                    "— ${quote.author}",
                    fontSize = 14.sp,
                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )

                Spacer(
                    modifier =
                        Modifier.height(15.dp)
                )


                Row {

                    TextButton(
                        onClick =
                            onFavorite
                    ) {

                        Text(
                            if (favorite)
                                "♥ Saved"
                            else
                                "♡ Save"
                        )
                    }

                    TextButton(
                        onClick =
                            onCopy
                    ) {

                        Text(
                            "▣ Copy"
                        )
                    }

                    TextButton(
                        onClick =
                            onShare
                    ) {

                        Text(
                            "↗ Share"
                        )
                    }
                }
            }
        }


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        Button(

            onClick =
                onNewQuote,

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(55.dp),

            shape =
                RoundedCornerShape(16.dp)
        ) {

            Text(
                "↻  New Quote",
                fontSize = 16.sp,
                fontWeight =
                    FontWeight.Bold
            )
        }


        Spacer(
            modifier =
                Modifier.height(13.dp)
        )


        Text(

            "Quote ${currentIndex + 1} of ${quoteList.size}",

            modifier =
                Modifier.fillMaxWidth(),

            textAlign =
                TextAlign.Center,

            fontSize = 12.sp,

            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )


        Spacer(
            modifier =
                Modifier.height(20.dp)
        )
    }
}


// ============================================================
// STAT CARD
// ============================================================

@Composable
fun DashboardStat(

    value: String,

    label: String,

    modifier: Modifier
) {

    Card(

        modifier =
            modifier.height(80.dp),

        shape =
            RoundedCornerShape(17.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme
                        .colorScheme
                        .surface
            )
    ) {

        Column(

            modifier =
                Modifier.fillMaxSize(),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            Text(

                value,

                fontSize = 21.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                color =
                    MaterialTheme
                        .colorScheme
                        .primary
            )

            Text(

                label,

                fontSize = 10.sp,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )
        }
    }
}


// ============================================================
// FAVORITES
// ============================================================

@Composable
fun FavoritesDashboard(

    quote: Quote,

    favorite: Boolean,

    paddingValues:
    PaddingValues
) {

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(paddingValues)
                .padding(20.dp)
    ) {

        Spacer(
            modifier =
                Modifier.height(22.dp)
        )

        Text(
            "Favorites",
            fontSize = 29.sp,
            fontWeight =
                FontWeight.ExtraBold
        )

        Text(
            "Your saved quotes",
            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )

        Spacer(
            modifier =
                Modifier.height(25.dp)
        )


        if (favorite) {

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(23.dp)
            ) {

                Column(

                    modifier =
                        Modifier.padding(23.dp)
                ) {

                    Text(
                        "♥ SAVED QUOTE",
                        color =
                            MaterialTheme
                                .colorScheme
                                .primary,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    Text(
                        "\"${quote.text}\"",
                        modifier =
                            Modifier.fillMaxWidth(),
                        fontSize = 20.sp,
                        fontWeight =
                            FontWeight.Bold,
                        textAlign =
                            TextAlign.Center
                    )

                    Spacer(
                        modifier =
                            Modifier.height(15.dp)
                    )

                    Text(
                        "— ${quote.author}",
                        color =
                            MaterialTheme
                                .colorScheme
                                .onSurfaceVariant
                    )
                }
            }

        } else {

            Column(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(300.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally,

                verticalArrangement =
                    Arrangement.Center
            ) {

                Text(
                    "♡",
                    fontSize = 60.sp,
                    color =
                        MaterialTheme
                            .colorScheme
                            .primary
                )

                Text(
                    "No saved quotes",
                    fontSize = 20.sp,
                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    "Save your favorite quote from Home."
                )
            }
        }
    }
}


// ============================================================
// PROFILE
// ============================================================

@Composable
fun ProfileDashboard(

    generatedCount: Int,

    paddingValues:
    PaddingValues,

    onLogout:
        () -> Unit
) {

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(paddingValues)
                .padding(20.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier =
                Modifier.height(25.dp)
        )

        QuoteFlowLogo(
            large = true
        )

        Spacer(
            modifier =
                Modifier.height(15.dp)
        )

        Text(
            "Quote Explorer",
            fontSize = 25.sp,
            fontWeight =
                FontWeight.ExtraBold
        )

        Text(
            "quote.lover@example.com",
            color =
                MaterialTheme
                    .colorScheme
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
                RoundedCornerShape(22.dp)
        ) {

            Column(

                modifier =
                    Modifier.padding(23.dp)
            ) {

                Text(
                    "Your Activity",
                    fontSize = 20.sp,
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                ProfileItem(
                    "Quotes Generated",
                    generatedCount.toString()
                )

                Spacer(
                    modifier =
                        Modifier.height(15.dp)
                )

                ProfileItem(
                    "Quotes Available",
                    quoteList.size.toString()
                )

                Spacer(
                    modifier =
                        Modifier.height(15.dp)
                )

                ProfileItem(
                    "App Version",
                    "1.0"
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(25.dp)
        )

        Button(
            onClick = onLogout,

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(55.dp),

            shape =
                RoundedCornerShape(16.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        Color(0xFFE53935)
                )
        ) {

            Text(
                "↪  Logout",
                fontSize = 16.sp,
                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}


// ============================================================
// PROFILE ITEM
// ============================================================

@Composable
fun ProfileItem(

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
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )

        Text(
            value,
            color =
                MaterialTheme
                    .colorScheme
                    .primary,
            fontWeight =
                FontWeight.Bold
        )
    }
}


// ============================================================
// SETTINGS
// ============================================================

@Composable
fun SettingsDialog(

    darkMode: Boolean,

    onDarkModeChange:
        (Boolean) -> Unit,

    onDismiss:
        () -> Unit
) {

    AlertDialog(

        onDismissRequest =
            onDismiss,

        title = {

            Text(
                "⚙  Settings",
                fontWeight =
                    FontWeight.ExtraBold
            )
        },

        text = {

            Column {

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween,

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Column {

                        Text(
                            "Dark Mode",
                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            if (darkMode)
                                "Dark appearance enabled"
                            else
                                "Light appearance enabled",
                            fontSize = 12.sp,
                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onSurfaceVariant
                        )
                    }

                    Switch(

                        checked =
                            darkMode,

                        onCheckedChange =
                            onDarkModeChange
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(18.dp)
                )

                HorizontalDivider()

                Spacer(
                    modifier =
                        Modifier.height(18.dp)
                )

                Text(
                    "About QuoteFlow",
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                Text(
                    "Random Quote Generator\nVersion 1.0"
                )
            }
        },

        confirmButton = {

            Button(
                onClick =
                    onDismiss
            ) {

                Text("Done")
            }
        }
    )
}


// ============================================================
// COPY QUOTE
// ============================================================

fun copyQuote(
    quote: Quote,
    context: Context? = null
) {

    // Clipboard is handled from the Activity context
    // when called from the application.
}


// ============================================================
// SHARE QUOTE
// ============================================================

fun shareQuote(
    quote: Quote,
    context: Context? = null
) {

    // Share is connected below through the helper Activity.
}
