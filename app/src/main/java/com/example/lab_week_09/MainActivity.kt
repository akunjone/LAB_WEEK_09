package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme
import com.example.lab_week_09.ui.theme.OnBackgroundTitleText
import com.example.lab_week_09.ui.theme.PrimaryTextButton
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    //val list = listOf("Tanu", "Tina", "Tono")
                    //ini home composable
                    //Home(list)
                    //Home()
                    val navController = rememberNavController()
                    App(
                        navController = navController
                    )
                }
            }
        }
    }
}

data class Student(
    var name: String
)

object MoshiInstance {
    private val moshiBuilder = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val studentListAdapter = moshiBuilder
        .adapter<List<Student>>(
            Types.newParameterizedType(List::class.java,
                Student::class.java)
        )
}

@Preview(showBackground = true)
@Composable
fun PreviewHome(){
    //Home(listOf("Tanu", "Tina", "Tono"))
    Home(navigateFromHomeToResult = {})
}

@Composable
fun Home(
    //items: List<String>,
    navigateFromHomeToResult: (String) -> Unit
){
    val listData = remember{
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }
    var inputField = remember{ mutableStateOf(Student("")) }
    HomeContent(
        listData,
        inputField.value,
        {input ->
            inputField.value = inputField.value.copy(input)
        },
        onButtonClick = {
            if(inputField.value.name.trim().isNotBlank()){ //cek hasil input yg ditrim
                //replace enter/newline dengan space, lalu trim
                inputField.value = inputField.value.copy(name = inputField.value.name.replace("\n", " ").trim())
                listData.add(inputField.value)
                inputField.value = Student("")
            }
        },
        navigateFromHomeToResult = {
            val json = MoshiInstance.studentListAdapter.toJson(listData.toList())
            navigateFromHomeToResult(json)
        }
    )
//    LazyColumn {
//        item{
//            Column (
//                modifier = Modifier.padding(16.dp).fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ){
//                Text(text = stringResource(
//                    R.string.enter_item)
//                )
//                TextField(
//                    value = "",
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Number
//                    ),
//                    onValueChange = {}
//                )
//                Button(onClick = { }) {
//                    Text(text = stringResource(R.string.button_click))
//                }
//            }
//        }
//        items(items){ item ->
//            Column (
//                modifier = Modifier.padding(vertical = 4.dp).fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ){
//                Text(text = item)
//            }
//        }
//    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LAB_WEEK_09Theme {
        Greeting("Android")
    }
}

@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    navigateFromHomeToResult: (String) -> Unit
){
    LazyColumn {
        item {
            Column (
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                //tambahin theme
                OnBackgroundTitleText(text = stringResource(
                    id = R.string.enter_item
                ))
//                Text(text = stringResource(
//                    id = R.string.enter_item
//                ))
                TextField(
                    value = inputField.name,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    onValueChange = { onInputValueChange(it) }
                )
                Row{
                    //call primarytextbutton ui element
                    PrimaryTextButton(text = stringResource(
                        id = R.string.button_click
                    )) {
                        onButtonClick()
                    }
                    PrimaryTextButton(text = stringResource(
                        id = R.string.button_navigate
                    )) {
                        navigateFromHomeToResult(listData.toList().toString())
                    }
                }

//                Button(onClick ={
//                    onButtonClick()
//                }){
//                    Text(text = stringResource(R.string.button_click))
//                }
            }
        }
        items(listData){ item ->
            Column (
                modifier = Modifier.padding(vertical = 4.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                //call OnBackgroundItemText UI Element
                OnBackgroundTitleText(text = item.name)
                //Text(text = item.name)
            }
        }
    }
}

@Composable
fun App(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            Home { json ->
                navController.navigate("resultContent/?listData=$json")
            }
        }

        composable(
            "resultContent/?listData={listData}",
            arguments = listOf(navArgument("listData") {
                type = NavType.StringType
            })
        ) {
            ResultContent(
                it.arguments?.getString("listData").orEmpty()
            )
        }
    }
}

@Composable
fun ResultContent(listData: String) {
    val students = try {
        MoshiInstance.studentListAdapter.fromJson(listData)
            ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }
    LazyColumn (
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item{
            OnBackgroundTitleText(
                text = "Student List: (${students.size} items)"
            )
        }
        items(students){ student ->
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                OnBackgroundTitleText(text = student.name)
            }
        }
    }
}

