package vinho.andre.android.com.gerenciadorvinho.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.custom_title_purchase.view.*
import kotlinx.android.synthetic.main.proxy_screen.view.*
import kotlinx.android.synthetic.main.purchase_register_in_the_wine_register.view.*
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.util.masks.MoneyTextWatcher

class PurchaseDialogUtil(
    private val context: Context
) {
    private lateinit var layoutPurchase: View
    private lateinit var layoutTitlePurchase: View
    private lateinit var purchaseDialog: AlertDialog

    fun getLayoutPurchase(): View = layoutPurchase

    @SuppressLint("InflateParams")
    fun createDialogPurchase(
        idLayout: Int
    ) {
        val inflater = LayoutInflater.from(context)
        layoutPurchase = inflater.inflate(idLayout, null)
        layoutTitlePurchase = inflater.inflate(R.layout.custom_title_purchase, null)

        if (idLayout == R.layout.purchase_register_in_the_wine_register) {
            layoutTitlePurchase.tv_title_purchase.text =
                context.getString(R.string.title_dialog_new_purchase_record)

            layoutPurchase.et_price.addTextChangedListener(
                MoneyTextWatcher(
                    layoutPurchase.et_price
                )
            )
        } else {
            setTextTitleInDetailActivity()
        }

        purchaseDialog = AlertDialog
            .Builder(
                context
            )
            .setCancelable(
                false
            )
            .setCustomTitle(
                layoutTitlePurchase
            )
            .setView(
                layoutPurchase
            )
            .create()
    }

    fun showDialog(
        status: Boolean
    ) {
        if (status)

            purchaseDialog.show()
        else
            purchaseDialog.dismiss()
    }

    fun setTextWithoutPurchaseRecord() {
        layoutTitlePurchase.tv_title_purchase.text =
            context.getString(R.string.title_dialog_without_purchase_record)
    }

    fun setTextTitleInDetailActivity() {
        layoutTitlePurchase.tv_title_purchase.text =
            context.getString(R.string.title_dialog_purchase)
    }

    fun setTextTitleInUpdatePurchase() {
        layoutTitlePurchase.tv_title_purchase.text =
            context.getString(R.string.title_dialog_update_purchase_record)
    }

    fun showProxy(
        status: Boolean
    ) {
        layoutPurchase.fl_proxy_container.visibility =
            if (status)
                View.VISIBLE
            else
                View.GONE
    }
}