package id.co.mka.narasource.presentation.findNarasumber

import id.co.mka.narasource.R
import id.co.mka.narasource.core.domain.model.Payment

object PaymentData {

    val listVA = listOf(
        Payment(1, "Bank BRI ", R.drawable.img_bank_bri),
        Payment(2, "Bank BCA", R.drawable.img_bank_bca),
        Payment(3, "Bank CIMB Niaga", R.drawable.img_bank_cimb)
    )

    val listEWallet = listOf(
        Payment(4, "GOPAY", R.drawable.img_ewallet_gopay),
        Payment(5, "OVO", R.drawable.img_ewallet_ovo),
        Payment(6, "ShopeePay", R.drawable.img_ewallet_spay)
    )

    val listPayment = listVA + listEWallet
}
