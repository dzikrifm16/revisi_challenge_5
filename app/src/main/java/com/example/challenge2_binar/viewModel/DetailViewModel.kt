package com.example.challenge2_binar.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge2_binar.database.Repository
import com.example.challenge2_binar.database.SimpleChart
import com.example.challenge2_binar.produk.ListData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : ViewModel(){
    private val _counter: MutableLiveData<Int> = MutableLiveData(0)
    val counter: LiveData<Int> get() = _counter

    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice: LiveData<Int> = _totalPrice

    private val _itemMenu = MutableLiveData<ListData>()

    private val repository: Repository
    init {
        repository = Repository(application)
    }


    fun incrementCount(){
        _counter.value = _counter.value?.plus(1)
    }

    fun decrementCount(){
        _counter.value?.let {
            if (it > 0){
                _counter.value = _counter.value?.minus(1)
            }
        }
    }

    fun itemMenu(item: ListData) {
        _itemMenu.value = item
        _totalPrice.value = item.harga!!
    }

    fun updateTotalPrice() {
        val currentAmount = _counter.value ?: 1
        val selectedItem = _itemMenu.value
        if (selectedItem != null) {
            val totalPrice = selectedItem.harga!! * currentAmount
            _totalPrice.value = totalPrice
        }
    }

    private fun insertCartItem(cartItem: SimpleChart) {
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(cartItem)
        }
    }


    fun addToCart() {
        val itemMenu = _itemMenu.value
        itemMenu?.let {
            val chartItem =
                totalPrice.value?.let { it1 ->
                    counter.value?.let { it2 ->
                        SimpleChart(
                            itemName = it.nama!!,
                            itemQuantity = it2,
                            itemImage = it.image_url,
                            itemPrice = it.harga!!,
                            totalPrice = it1,
                        )
                    }
                }
            chartItem?.let { it1 -> insertCartItem(it1) }
        }
    }

}