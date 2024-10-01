package com.app.composestorage.page

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.app.composestorage.R
import com.app.composestorage.model.UserDetails

import com.app.composestorage.ui.theme.ComposeStorageTheme
import com.app.composestorage.utlis.DataStoreManger
import com.app.composestorage.utlis.DataStoreManger.Companion.EMAIL
import com.app.composestorage.utlis.preferenceDataStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RegisterScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeStorageTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val dataStoreContext = LocalContext.current
                    val dataStoreManger = DataStoreManger(dataStoreContext)

                    AppContent(
                        this@RegisterScreen,
                        preferenceDataStore,
                        dataStoreManger
                    )

                }
            }
        }
    }


}

@Composable
fun AppContent(
    registerScreen: RegisterScreen,
    preferenceDataStore: DataStore<Preferences>,
    dataStoreManger: DataStoreManger
) {

    var isRegistered by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val onRegisterSuccess = { isRegistered = true }
    val onLogout = {
        isRegistered = false
        scope.launch {
            dataStoreManger.clearDataStore()
        }
    }

    //lets check if user is regsitered in when the app start
    LaunchedEffect(key1 = Unit) {
        checkRegisterState(preferenceDataStore) { it ->
            isRegistered = it
        }
    }

    if (isRegistered) {
        HomePage(onLogout, dataStoreManger)
    } else {
        RegisterPageUI(onRegisterSuccess, dataStoreManger)
    }

}

suspend fun checkRegisterState(
    preferenceDataStore: DataStore<Preferences>,
    onResult: (Boolean) -> Unit
) {
    val preferences = preferenceDataStore.data.first()
    val email = preferences[EMAIL]
    val isRegistered = email != null
    onResult(isRegistered)
}

@Composable
fun RegisterPageUI(onRegisterSuccess: () -> Unit, dataStoreManger: DataStoreManger) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    val mContext = LocalContext.current
    val scope = rememberCoroutineScope()

    // Create focus requesters for each TextField
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusRequester4= remember { FocusRequester() }

    // Get the current focus manager
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(16.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Main card Content for Register
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Add verticalScroll modifier here
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp)
            ) {

                // Heading Jetpack Compose
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.app_name),
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp

                )

                // Register Logo
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(128.dp)
                        .padding(top = 8.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data = R.drawable.fsc).crossfade(enable = true).scale(Scale.FILL)
                        .build(),
                    contentDescription = stringResource(id = R.string.app_name)
                )


                Text(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                    text = "Register",
                    fontSize = 25.sp
                )

                // Name Field
                OutlinedTextField(
                    value = name, // Add a variable for name
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next // Set imeAction to Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusRequester2.requestFocus() // Move to next TextField
                        }
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .focusRequester(focusRequester1)
                )

                OutlinedTextField(
                    value = mobileNumber,
                    onValueChange = { mobileNumber = it },
                    label = { Text("Mobile Number") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next // Set imeAction to Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusRequester3.requestFocus() // Move to next TextField
                        }
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .focusRequester(focusRequester2)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next // Set imeAction to Done
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusRequester4.requestFocus() // Move to next TextField
                        }
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth().focusRequester(focusRequester3)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done // Set imeAction to Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus() // Hide the keyboard
                        }
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .focusRequester(focusRequester4)
                )
                Button(
                    onClick = {
                        if (name.isEmpty()) {
                            Toast.makeText(mContext, "Name is Empty", Toast.LENGTH_SHORT).show()
                        } else if (mobileNumber.isEmpty()) {
                            Toast.makeText(mContext, "Mobile No. is Empty", Toast.LENGTH_SHORT)
                                .show()
                        } else if (email.isEmpty()) {
                            Toast.makeText(mContext, "Email is Empty", Toast.LENGTH_SHORT).show()
                        } else if (password.isEmpty()) {
                            Toast.makeText(mContext, "Password is Empty", Toast.LENGTH_SHORT).show()
                        } else {
                            //Submit you data
                            scope.launch {
                                dataStoreManger.saveToDataStore(
                                    UserDetails(
                                        emailAddress = email,
                                        name = name,
                                        mobileNumber = mobileNumber
                                    )
                                )
                                onRegisterSuccess()
                            }
                        }

                    }, modifier = Modifier.padding(16.dp)
                ) {
                    Text("Submit")
                }

            }
        }


    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(onLogout: () -> Job, dataStoreManger: DataStoreManger) {
    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary),
            title = { Text("HomePage", color = Color.White) },
        )

    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val userDetails by dataStoreManger.getFromDataStore().collectAsState(initial = null)
            Text(
                text = "Hi, ${"\nWelcome to the Home Page "}",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
            ) {

                Text(
                    text = "Name: ${userDetails?.name ?: ""}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = "Mobile: ${userDetails?.mobileNumber ?: ""}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = "Email Id: ${userDetails?.emailAddress ?: ""}",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Logout Button
                Button(
                    onClick = { onLogout() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Logout",
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }


        }
    }
}


