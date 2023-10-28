package com.chetan.mvicompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberImagePainter
import com.chetan.mvicompose.api.AnimalApi
import com.chetan.mvicompose.api.AnimalService
import com.chetan.mvicompose.model.Animal
import com.chetan.mvicompose.ui.theme.MVIComposeTheme
import com.chetan.mvicompose.view.MainIntent
import com.chetan.mvicompose.view.MainState
import com.chetan.mvicompose.view.MainViewModel
import com.chetan.mvicompose.view.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {
    private lateinit var mainViewModel : MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProviders
            .of(this, ViewModelFactory(AnimalService.api))
            .get(MainViewModel::class.java)

        val onButtonClick : () -> Unit ={
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.FetchAnimals)
            }
        }
        setContentView (ComposeView(this).apply{
            setContent {
                MVIComposeTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(color = MaterialTheme.colorScheme.background) {
                        MainScreen(vm = mainViewModel, onButtonClick)
                    }
                }
            }
        })
    }
}
@Composable
fun MainScreen(vm : MainViewModel, buttonClick : () -> Unit){

    val state = vm.state.value
    when(state){
        is MainState.Animals -> {
            AnimalList(animalList = state.animals)
        }
        is MainState.Error -> {
            IdelScreen(buttonClick)
            Toast.makeText(LocalContext.current,state.error,Toast.LENGTH_LONG).show()
        }
        MainState.Idel -> IdelScreen(buttonClick)
        MainState.Loading -> LoadingScreen()
    }
}
@Composable
fun IdelScreen( buttonClick: () -> Unit){

    Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
     Button(onClick = buttonClick) {
         Text("Fetch Animals")
     }
    }
}
@Composable
fun LoadingScreen(){
    Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        CircularProgressIndicator()
    }
}
@Composable
fun AnimalList( animalList : List<Animal>){
    LazyColumn(){
        items(animalList){
            AnimalItem(animal = it)
            Divider(color = Color.LightGray, modifier = Modifier.padding(top = 4.dp, bottom = 4.dp))
        }
    }
}
@Composable
fun AnimalItem(animal: Animal){
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)){
        val url = AnimalService.BASE_URL+animal.image
        val painter = rememberImagePainter(data = url)
        Image(painter = painter, contentDescription = null, modifier = Modifier.size(100.dp)
        , contentScale = ContentScale.FillHeight)
        Column(modifier = Modifier.fillMaxSize()
            .padding(start = 4.dp)) {
            Text(text = animal.name, fontWeight = FontWeight.Bold)
            Text(text = animal.location)
        }
    }
}

