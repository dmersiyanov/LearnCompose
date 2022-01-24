package com.dmersiyanov.learncompose

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.Coil
import coil.compose.rememberImagePainter
import com.dmersiyanov.learncompose.ui.theme.LearnComposeTheme
import com.dmersiyanov.learncompose.ui.theme.green2
import org.intellij.lang.annotations.JdkConstants

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearnComposeTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun Toolbar(title: String) {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back icon",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
        title = { Text(text = title) },
    )
}

@Composable
fun MainScreen(usersList: List<UserProfile> = userProfileList) {
    Scaffold(topBar = { Toolbar("Users list") }) {
        Surface(color = Color.LightGray, modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.padding(vertical = 8.dp), content = {
                items(usersList) { user: UserProfile ->
                    ProfileCard(userProfile = user)
                }
            })
        }
    }
}

@Composable
fun ProfileCard(userProfile: UserProfile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
fun DetailsScreen(userProfile: UserProfile = userProfileList.first()) {
    Scaffold(topBar = { Toolbar(userProfile.name) }) {
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
        DetailsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LearnComposeTheme {
        MainScreen()
    }
}

