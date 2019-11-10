package com.grafsemmel.valium.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.grafsemmel.valium.ValidationState

class SpinnerField<T>(var items: List<T> = listOf(), isOptional: Boolean = false) :
    Field(isOptional) {
    val selectedPosition: MutableLiveData<Int> = MutableLiveData()
    val selectedItem: LiveData<T> = Transformations.map(selectedPosition) {
        it?.let { position -> items[position] }
    }

    init {
        validationState.addSource(selectedItem) {
            validationState.value = when {
                isOptional -> ValidationState.Valid
                else -> ValidationState.from(it != null)
            }
        }
    }

    fun setSelectedPosition(position: Int) {
        when (position) {
            in 0 until items.size -> {
                selectedPosition.value = position
            }
        }
    }

    fun setupItems(items: List<T>) {
        this.items = items
    }
}

