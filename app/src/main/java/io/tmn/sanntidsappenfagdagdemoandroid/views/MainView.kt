package io.tmn.sanntidsappenfagdagdemoandroid.views

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import io.tmn.sanntidsappenfagdagdemoandroid.MainActivity
import io.tmn.sanntidsappenfagdagdemoandroid.MyLibs
import io.tmn.sanntidsappenfagdagdemoandroid.models.Feature
import io.tmn.sanntidsappenfagdagdemoandroid.models.Geometry
import io.tmn.sanntidsappenfagdagdemoandroid.models.Properties
import io.tmn.sanntidsappenfagdagdemoandroid.models.SearchViewModel
import io.tmn.sanntidsappenfagdagdemoandroid.models.SearchViewState


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView() {
    var presentFromSheet by remember { mutableStateOf(false) }
    var fromSheetState = rememberModalBottomSheetState()

    var presentToSheet by remember { mutableStateOf(false) }
    var toSheetState = rememberModalBottomSheetState()

    var fromQuery by rememberSaveable { mutableStateOf("") }
    var toQuery by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current
    val locationProviderClient =
        remember { LocationServices.getFusedLocationProviderClient(context) }

    var lastKnownLocation by remember {
        mutableStateOf<Location?>(null)
    }
    var deviceLatLng by remember {
        mutableStateOf(LatLng(0.0, 0.0))
    }

    val locationResult = locationProviderClient.lastLocation
    locationResult.addOnCompleteListener(context as MainActivity) { task ->
        if (task.isSuccessful) {
            lastKnownLocation = task.result
            deviceLatLng = LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
        }
    }

    Column {

        ListItem(
            headlineContent = { Text(fromQuery) },
            leadingContent = { Text("Fra") },
            modifier = Modifier.clickable {
                presentFromSheet = true
            }
        )

        ListItem(
            headlineContent = { Text(toQuery) },
            leadingContent = { Text("Til") },
            modifier = Modifier.clickable {
                presentToSheet = true
            }
        )

        if (presentFromSheet) {
            ModalBottomSheet(
                onDismissRequest = { presentFromSheet = false },
                sheetState = fromSheetState
            ) {
                Scaffold {
                    Column {
                        val searchViewModel: SearchViewModel = viewModel()

                        SearchView(
                            onChange = {
                                fromQuery = it
                                if (it.length > 2) {
                                    searchViewModel.getStops(it, deviceLatLng)
                                }
                            }
                        )

                        BottomSearchSheet(searchViewModel.searchViewState, onTap = {
                            fromQuery = it
                            presentFromSheet = false
                        })
                    }
                }

            }
        }

        if (presentToSheet) {
            ModalBottomSheet(
                onDismissRequest = { presentToSheet = false },
                sheetState = toSheetState
            ) {
                Scaffold {
                    Column(

                    ) {
                        val searchViewModel: SearchViewModel = viewModel()

                        SearchView(
                            onChange = {
                                toQuery = it
                                if (it.length > 2) {
                                    searchViewModel.getStops(it, deviceLatLng)
                                }
                            }
                        )
                        BottomSearchSheet(searchViewModel.searchViewState, onTap = {
                            toQuery = it
                            presentToSheet = false
                        })
                    }
                }

            }
        }
        Divider()

        Text("Distance from Skuret to Oslo S is ${MyLibs.instance.getDistance(59.890465, 10.523493, 59.904499, 10.786372)} meters.",
            modifier = Modifier.padding(all = 16.dp))
    }
}

@Composable
fun BottomSearchSheet(searchViewState: SearchViewState, onTap: (name: String) -> Unit) {
    when (searchViewState) {
        is SearchViewState.Error -> Text("")
        is SearchViewState.Loading -> Text("")
        is SearchViewState.Success -> SearchResultList(searchViewState.stops, onTap)
    }
}
@Composable
fun SearchResultList(features: List<Feature>, onTap: (name: String) -> Unit) {
    Column {
        LazyColumn {
            items(features) {feature ->
                Column {
                    ListItem(
                        headlineContent = { Text("${feature.properties.name}", fontWeight = FontWeight.Medium, color = Color(24, 24, 86)) },
                        supportingContent = { Text("${feature.properties.locality} ${feature.properties.county}", color = Color.Gray) },
                        modifier = Modifier.clickable {
                            onTap(feature.properties.name)
                        }
                    )

                    Divider()
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchResultListPreview() {
        SearchResultList(features = listOf(Feature(
            geometry = Geometry(coordinates = arrayOf<Double>(1.0, 1.0)),
            properties = Properties("12", name = "Oslo S", locality = "Oslo", county = "Oslo"))), {})

}
