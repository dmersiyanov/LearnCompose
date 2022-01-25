package com.dmersiyanov.learncompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter
import com.dmersiyanov.learncompose.ui.theme.LearnComposeTheme
import com.dmersiyanov.learncompose.ui.theme.green2

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearnComposeTheme {
                App()
            }
        }
    }
}

@Composable
fun App(usersList: List<UserProfile> = userProfileList) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "users_list") {
        composable("users_list") {
            MainScreen(usersList, navController)
        }
        composable("user_details/{userId}", arguments = listOf(navArgument("userId") {
            type = NavType.IntType
        })) {
            DetailsScreen(
                navController = navController,
                userId = it.arguments?.getInt("userId") ?: 0
            )
        }
    }
}

@Composable
fun Toolbar(title: String, navController: NavController?) {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back icon",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        navController?.navigateUp()
                    }
            )
        },
        title = { Text(text = title) },
    )
}

@Composable
fun MainScreen(usersList: List<UserProfile>, navController: NavController?) {
    Scaffold(topBar = { Toolbar("Users list", navController) }) {
        Surface(color = Color.LightGray, modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.padding(vertical = 8.dp), content = {
                items(usersList) { user: UserProfile ->
                    ProfileCard(userProfile = user) {
                        navController?.navigate("user_details/${user.id}")
                    }
                }
            })
        }
    }
}

@Composable
fun ProfileCard(userProfile: UserProfile, onClick: (userId: Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = {
                onClick.invoke(userProfile.id)
            }),
        elevation = 8.dp
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage(userProfile)
            ProfileContent(userProfile)
        }
    }
}

@Composable
fun ProfileImage(userProfile: UserProfile, imageSize: Dp = 125.dp) {
    Card(
        shape = CircleShape,
        modifier = Modifier
            .size(imageSize)
            .padding(16.dp),
        border = BorderStroke(
            width = 3.dp,
            color = if (userProfile.isOnline) MaterialTheme.colors.green2 else Color.Red
        )
    ) {
        Image(
            painter = rememberImagePainter(userProfile.pictureUrl),
            contentDescription = "profile",
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun ProfileContent(userProfile: UserProfile, alignment: Alignment.Horizontal = Alignment.Start) {
    Column(horizontalAlignment = alignment) {
        Text(
            text = userProfile.name,
            modifier = Modifier.padding(end = 16.dp),
            style = MaterialTheme.typography.h5
        )
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = if (userProfile.isOnline) "Active now" else "Not active",
                modifier = Modifier.padding(end = 16.dp),
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
fun DetailsScreen(
    userId: Int,
    navController: NavController?
) {
    val userProfile: UserProfile = userProfileList.first { it.id == userId }
    Scaffold(topBar = { Toolbar(userProfile.name, navController) }) {
        Surface(color = Color.LightGray, modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileImage(userProfile, 250.dp)
                ProfileContent(userProfile, Alignment.CenterHorizontally)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsPreview() {
    LearnComposeTheme {
        DetailsScreen(navController = null, userId = 0)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LearnComposeTheme {
        MainScreen(userProfileList, null)
    }
}

