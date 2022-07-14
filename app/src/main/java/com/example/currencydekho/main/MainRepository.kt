package com.example.currencydekho.main

import com.example.currencydekho.data.models.CurrencyResponse
import com.example.currencydekho.util.Resource

interface MainRepository {

    suspend fun getRates(base: String) :  Resource<CurrencyResponse>
}