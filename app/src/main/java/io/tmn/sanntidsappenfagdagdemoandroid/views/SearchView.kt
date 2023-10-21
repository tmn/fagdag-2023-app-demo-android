package io.tmn.sanntidsappenfagdagdemoandroid.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun SearchView(onChange: (query: String) -> Unit) {
    var searchQuery by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onChange(searchQuery)
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}