package de.kleinanzeigen.challenge.ui.details

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.kleinanzeigen.challenge.R
import de.kleinanzeigen.challenge.data.repositpry.realestate.RealEstateRepository
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.AdsDataDetails
import de.kleinanzeigen.challenge.ui.ViewState
import de.kleinanzeigen.challenge.util.constants.PREDEFINED_ADS_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject


@HiltViewModel
class ItemDetailsViewModel @Inject constructor(private val realEstateRepo: RealEstateRepository) :
    ViewModel() {

    private val _viewState: MutableStateFlow<ViewState<AdsDataDetails>?> = MutableStateFlow(null)
    val viewState: StateFlow<ViewState<AdsDataDetails>?> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            realEstateRepo.adsDetailsFlow.collect { adsData ->
                adsData?.onSuccess { data ->
                    if(data != null){
                        _viewState.value = ViewState.Success(data)
                    } else {
                        _viewState.value = ViewState.Failure
                    }

                }?.onFailure {
                    _viewState.value = ViewState.Failure
                }

            }
        }
    }

    fun onUIEvent(event: UiEvent) {
        when (event) {

            UiEvent.OnInit -> {
                _viewState.value = ViewState.Loading
                triggerAdsDetailsDataFetch(PREDEFINED_ADS_ID)
            }

            UiEvent.OnClose -> {
                //todo: to be implemented if needed
            }

            else -> {}
        }
    }

    fun shareImageDirectly(context: Context, bitmap: Bitmap, title: String) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val cachePath = File(context.cacheDir, "images").apply { mkdirs() }
        val file = File(cachePath, "shared_image.jpg")
        file.outputStream().use {
            it.write(byteArray)
        }
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file )
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpg"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, uri)
            putExtra(Intent.EXTRA_TEXT, title)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        context.startActivity(Intent.createChooser(shareIntent, title))
    }

    fun openGoogleMaps(context: Context, latitude: String, longitude: String) {
        val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage(context.getString(R.string.navigation_package_name))

        // Check if an app exists to handle the intent
        if (context.packageManager?.let { intent.resolveActivity(it) } != null) {
            context.startActivity(intent)
        } else {
            // Fallback: open in browser if Google Maps app isn't installed
            val fallbackUri = Uri.parse("https://www.google.com/maps?q=$latitude,$longitude")
            context.startActivity(Intent(Intent.ACTION_VIEW, fallbackUri))
        }
    }

    fun retryLoadData(){
        triggerAdsDetailsDataFetch(PREDEFINED_ADS_ID)
        _viewState.value = ViewState.Loading
        Timber.d("retry triggered")
    }

    private fun triggerAdsDetailsDataFetch(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            realEstateRepo.getSingleAdsDetails(id)
        }
    }

    sealed interface UiEvent {
        object OnInit : UiEvent

        object OnClose : UiEvent

       //todo: extend by adding other ui events ..
    }
}