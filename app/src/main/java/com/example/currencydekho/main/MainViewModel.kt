package com.example.currencydekho.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencydekho.data.models.Rates
import com.example.currencydekho.util.DispatcherProvider
import com.example.currencydekho.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.round

class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository,
    //here we are injecting coroutines dispatcher, when we test coroutines we some time needs to pass special coroutines test dispatcher
    private val dispatchers: DispatcherProvider

) : ViewModel() {

    sealed class CurrencyEvent {
        class Success(val resultText: String): CurrencyEvent()
        class Failure(val errorText: String): CurrencyEvent()
        object Loading: CurrencyEvent()
        object Empty : CurrencyEvent()

    }

    private val  _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun  convert(
        amountStr: String,
        fromCurrency: String,
        toCurrency: String
    ) {
        val fromAmount = amountStr.toFloatOrNull()
        if(fromAmount == null) {
            _conversion.value = CurrencyEvent.Failure("Not a Valid amount")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CurrencyEvent.Loading
            when(val ratesResponse = repository.getRates(fromCurrency)) {
                is Resource.Error -> _conversion.value = CurrencyEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.rates
                    val rate = getRateForCurrency(toCurrency, rates)
                    if (rate == null) {
                        _conversion.value = CurrencyEvent.Failure("Unexpected error")
                    } else {
                    val convertedCurrency = round(fromAmount * rate * 100) / 100
                        _conversion.value = CurrencyEvent.Success(
                            "$fromAmount $fromCurrency  = $convertedCurrency $toCurrency"
                        )
                    }
                }
            }
        }
        
    }


    private fun  getRateForCurrency(currency: String, rates: Rates) = when (currency){

            ("AED") -> rates.aED

            ("AFN") -> rates.aFN

            ("ALL") -> rates.aLL

            ("EUR") -> rates.eUR

            ("AMD") -> rates.aMD

            ("ANG") -> rates.aNG

            ("AOA") -> rates.aOA

            ("ARS") -> rates.aRS

            ("AUD")  -> rates.aUD

            ("AWG") -> rates.aWG

            ("AZN") -> rates.aZN

            ("BAM") -> rates.bAM

            ("BBD") -> rates.bBD

            ("BDT") -> rates.bDT

            ("BGN") -> rates. bGN

            ("BHD") -> rates.bHD

            ("BIF") -> rates.bIF

            ("BMD") -> rates.bMD

            ("BND") -> rates.bND

            ("BOB") -> rates.bOB

            ("BRL") -> rates.bRL

           ("BSD") -> rates.bSD

            ("BTC") -> rates.bTC

            ("BTN") -> rates.bTN

            ("BWP") -> rates.bWP

            ("BYN") -> rates.bYN

            ("BYR") -> rates.bYR

            ("BZD") -> rates.bZD

            ("CAD") -> rates.cAD

            ("CDF") -> rates.cDF

            ("CHF") -> rates.cHF

            ("CLF") -> rates.cLF

            ("CLP") -> rates.cLP

            ("CNY")-> rates.cNY

            ("COP")-> rates.cOP

            ("CRC") -> rates.cRC

            ("CUC") -> rates.cUC

            ("CUP") -> rates.cUP

            ("CVE") -> rates.cVE

            ("CZK") -> rates.cZK

            ("DJF") -> rates.dJF

            ("DKK") -> rates.dKK

            ("DOP") -> rates.dOP

            ("DZD") -> rates.dZD

            ("EGP") -> rates.eGP

            ("ERN") -> rates.eRN

            ("ETB") -> rates.eTB

            ("INT") -> rates.iNT

            ("FJD") -> rates.fJD

            ("FKP") -> rates.fKP

            ("GBP") -> rates. gBP

            ("GEL") -> rates.gEL

            ("GGP") -> rates.gGP

            ("GHS") -> rates.gHS

            ("GIP") -> rates.gIP

            ("GMD") -> rates.gMD

            ("GNF") -> rates.gNF

            ("GTQ") -> rates.gTQ

            ("GYD") -> rates.gYD

            ("HKD") -> rates.hKD

            ("HNL") -> rates.hNL

            ("HRK") -> rates.hRK

            ("HTG") -> rates.hTG

            ("HUF") -> rates.hUF

            ("IDR") -> rates.iDR

            ("ILS") -> rates.iLS

            ("IMP") -> rates.iMP

            ("INR") -> rates.iNR

            ("IQD") -> rates.iQD

            ("IRR") -> rates.iRR

            ("ISK") -> rates.iSK

            ("JEP") -> rates.jEP

            ("JMD") -> rates.jMD

            ("JOD") -> rates.jOD

            ("JPY") -> rates.jPY

            ("KES") -> rates.kES

            ("KGS") -> rates.kGS

            ("KHR")  -> rates.kHR

            ("KMF") -> rates.kMF

            ("KPW") -> rates.kPW

            ("KRW") -> rates.kRW

            ("KWD") -> rates.kWD

            ("KYD") -> rates.kYD

            ("KZT") -> rates.kZT

            ("LAK") -> rates.lAK

            ("LBP") -> rates.lBP

            ("LKR") -> rates.lKR

            ("LRD") -> rates.lRD

            ("LSL") -> rates.lSL

            ("LTL") -> rates.lTL

            ("LVL") -> rates.lVL

            ("LYD") -> rates.lYD

            ("MAD") -> rates.mAD

            ("MDL") -> rates.mDL

            ("MGA") -> rates.mGA

            ("MKD") -> rates.mKD

            ("MMK") -> rates.mMK

            ("MNT") -> rates.mNT

            ("MOP") -> rates.mOP

            ("MRO") -> rates.mRO

            ("MUR") -> rates.mUR

            ("MVR") -> rates.mVR

            ("MWK") -> rates.mWK

            ("MXN") -> rates.mXN

            ("MYR") -> rates.mYR

            ("MZN") -> rates.mZN

            ("NAD") -> rates.nAD

            ("NGN") -> rates.nGN

            ("NIO") -> rates.nIO

            ("NOK") -> rates.nOK

            ("NPR") -> rates.nPR

            ("NZD") -> rates.nZD

            ("OMR") -> rates.oMR

            ("PAB") -> rates.pAB

            ("PEN") -> rates.pEN

            ("PGK") -> rates.pGK

            ("PHP") -> rates.pHP

            ("MGA") -> rates.mGA

            ("PLN") -> rates.pLN

            ("PYG") -> rates.pYG

            ("QAR") -> rates.qAR

            ("RON") -> rates.rON

            ("RSD") -> rates.rSD

            ("RUB") -> rates.rUB

            ("RWF") -> rates.rWF

            ("SAR") -> rates.sAR

            ("SBD") -> rates.sBD

            ("SCR") -> rates.sCR

            ("SDG") -> rates.sDG

            ("SEK") -> rates.sEK

            ("SGD") -> rates.sDG

            ("SHP") -> rates.sHP

            ("SLL") -> rates.sLL

            ("SOS") -> rates.sOS

            ("SRD") -> rates.sRD

            ("STD") -> rates.sTD

            ("SVC") -> rates.sVC

            ("SYP") -> rates.sYP

            ("SZL") -> rates.sZL

            ("THB") -> rates.tHB

            ("TJS") -> rates.tJS

            ("TMT") -> rates.tMT

            ("TND") -> rates.tND

            ("TOP") -> rates.tOP

            ("TRY") -> rates.tRY

            ("TTD") -> rates.tTD

            ("TWD") -> rates.tWD

            ("TZS") -> rates.tZS

            ("UAH") -> rates.uAH

            ("UGX") -> rates.uGX

            ("USD") -> rates.uSD

            ("UYU") -> rates.uYU

            ("UZS") -> rates.uZS

            ("VEF") -> rates.vEF

            ("VND") -> rates.vND

            ("VUV") -> rates.vUV

            ("WST") -> rates.wST

            ("XAF") -> rates.xAF

            ("XAG") -> rates.xAG

            ("XAU") -> rates.xAU

            ("XCD") -> rates.xCD

            ("XDR") -> rates.xDR

            ("XOF") -> rates.xOF

            ("XPF") -> rates.xPF

            ("YER") -> rates.yER

            ("ZAR") -> rates.zAR

            ("ZMK") -> rates.zMK

            ("ZMW") -> rates.zMW

            ("ZWL") -> rates.zWL

        else -> null


    }

}
