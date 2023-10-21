package io.tmn.sanntidsappenfagdagdemoandroid.models

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import io.tmn.sanntidsappenfagdagdemoandroid.MyLibs
import io.tmn.sanntidsappenfagdagdemoandroid.services.GeocoderApi
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

sealed interface SearchViewState {
    data class Success(val stops: List<Feature>) : SearchViewState
    object Error : SearchViewState
    object Loading : SearchViewState
}

class SearchViewModel: ViewModel() {
    var searchViewState: SearchViewState by mutableStateOf(SearchViewState.Loading)

    var executor: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    var future: ScheduledFuture<*>? = null

    fun getStops(query: String, currentLocation: LatLng) {
        future?.cancel(true)

        future = executor.schedule({
            viewModelScope.launch {
                searchViewState = SearchViewState.Loading
                searchViewState = try {
                    val data = GeocoderApi.geocoderService.getAutocompleteBusStop(query)
//                    SearchViewState.Success(data.features)

                    // TODO: Comment in to get sorted
                    val sortedFeatures = data.features.sortedWith <Feature> (object : Comparator <Feature> {
                        override fun compare(o1: Feature?, o2: Feature?): Int {
                            if (MyLibs.instance.distanceToCurrentLocation(o1!!, currentLocation) > MyLibs.instance.distanceToCurrentLocation(o2!!, currentLocation))
                                return 1
                            else if (MyLibs.instance.distanceToCurrentLocation(o1!!, currentLocation) < MyLibs.instance.distanceToCurrentLocation(o2!!, currentLocation))
                                return -1
                            else
                                return 0
                        }
                    })
                    SearchViewState.Success(sortedFeatures)


                } catch (e: Exception) {
                    e.printStackTrace()
                    SearchViewState.Error
                }
            }
        }, 300, TimeUnit.MILLISECONDS)
    }
}