package com.avsaverage.fooddeliveryapp.presentation.manager.menu_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avsaverage.fooddeliveryapp.data.models.FoodModelResponse
import com.avsaverage.fooddeliveryapp.data.repositories.FoodRepository
import com.avsaverage.fooddeliveryapp.data.repositories.ImageUploadService
import com.avsaverage.fooddeliveryapp.presentation.states.FoodState
import com.avsaverage.fooddeliveryapp.presentation.states.SaveSate
import com.avsaverage.fooddeliveryapp.presentation.states.UriState
import com.avsaverage.fooddeliveryapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuScreenManagerViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val imageUploadService: ImageUploadService
) : ViewModel() {

    private val _res: MutableState<FoodState> = mutableStateOf(FoodState())
    val res: State<FoodState> = _res

    val _uriState : MutableState<UriState> = mutableStateOf(UriState())
    val uriState = _uriState

    private val _foodAdd : MutableState<UriState> = mutableStateOf(UriState())
    val foodAdd = _foodAdd

    private val _deleteState: MutableState<SaveSate> = mutableStateOf(SaveSate())
    val deleteState = _deleteState


    var selectedItem : FoodModelResponse.FoodItems? = null
    var isFoodEditDialogShown by mutableStateOf(false)
        private set

    var isFoodAddDialogShown by mutableStateOf(false)
        private set

    private val _saveState = Channel<SaveSate>()
    val saveSate = _saveState.receiveAsFlow()

    fun onDismissDialog()
    {
        isFoodEditDialogShown = false
        isFoodAddDialogShown = false
    }

    fun uploadImg(selectedImage: ByteArray?) {
        viewModelScope.launch {
            imageUploadService.updateImage(selectedImage!!, "images").collect{ result ->
                when(result) {
                    is Resource.Success ->{
                        _uriState.value.isSuccess = result.data
                    }

                    is Resource.Loading ->{
                        _uriState.value.isLoading = true
                    }

                    is Resource.Error ->{
                        _uriState.value.isError = result.message
                    }
                }
            }
        }
    }

    fun clickEditFood(itemState: FoodModelResponse.FoodItems)
    {
        selectedItem = itemState
        isFoodEditDialogShown = true
    }

    fun clickAddFood()
    {
        isFoodAddDialogShown = true
    }

    fun addFood(itemState: FoodModelResponse.FoodItems) {
        viewModelScope.launch {
            foodRepository.insert(itemState).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _foodAdd.value.isSuccess = result.data
                    }

                    is Resource.Loading -> {
                        _foodAdd.value.isLoading = true
                    }

                    is Resource.Error -> {
                        _uriState.value.isError = result.message
                    }
                }
            }
        }
    }

    fun delete(key: String)
    {
        viewModelScope.launch {
            foodRepository.delete(key).collect{
                    result ->
                when(result) {
                    is Resource.Success ->{
                        _deleteState.value.isSuccess = true
                    }

                    is Resource.Loading ->{
                        _deleteState.value.isLoading = true
                    }

                    is Resource.Error ->{
                        _deleteState.value.isError = result.message
                    }
                }
            }
        }
    }

    suspend fun deleteFoodImg(accessToken : String)
    {
        viewModelScope.launch {
            imageUploadService.deleteImageByAccessToken(accessToken)
        }
    }

    fun updateFood(itemState: FoodModelResponse.FoodItems?)
    {
        val index = _res.value.food?.indexOfFirst { it.food?.id == itemState?.id }
        index?.let { idx ->
            val updatedFood = _res.value.food?.toMutableList()
            updatedFood?.set(idx, FoodModelResponse(food = itemState, key = _res.value.food?.get(idx)?.key.orEmpty()))
            viewModelScope.launch {
                foodRepository.update(updatedFood!![index]).collect { result ->
                    when(result)
                    {
                        is Resource.Success ->{
                            _saveState.send(SaveSate(isSuccess = true))
                        }
                        is Resource.Loading ->{
                            _saveState.send(SaveSate(isLoading = true))
                        }
                        is Resource.Error ->{
                            _saveState.send(SaveSate(isError = result.message))
                        }
                    }
                }
            }
        }
        onDismissDialog()
    }

    init {
        viewModelScope.launch {
            foodRepository.getFood().collect {
                when (it) {
                    is Resource.Success -> {
                        _res.value = FoodState(
                            food = it.data
                        )
                    }

                    is Resource.Error -> {
                        _res.value = FoodState(
                            error = it.message.toString()
                        )
                    }

                    is Resource.Loading -> {
                        _res.value = FoodState(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

}