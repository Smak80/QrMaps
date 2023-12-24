package ru.smak.qrmaps.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.smak.qrmaps.ui.theme.QrMapsTheme

@Composable
fun MainPage(
    modifier: Modifier = Modifier
){
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            
        }
    }
}

@Preview(locale = "ru")
@Composable
fun MainPagePreview(){
    QrMapsTheme {
        MainPage(modifier = Modifier.fillMaxSize())
    }
}