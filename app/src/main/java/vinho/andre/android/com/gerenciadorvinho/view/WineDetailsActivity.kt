package vinho.andre.android.com.gerenciadorvinho.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.content_wine_details.*
import kotlinx.android.synthetic.main.dialog_list_preco.view.*
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.data.sqlite.DBHelper
import vinho.andre.android.com.gerenciadorvinho.domain.Comment
import vinho.andre.android.com.gerenciadorvinho.domain.Purchase
import vinho.andre.android.com.gerenciadorvinho.domain.Wine
import vinho.andre.android.com.gerenciadorvinho.domain.WineComplement
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.WineDetailsPresenterInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.WineDetailsActivityInterface
import vinho.andre.android.com.gerenciadorvinho.presenter.WineDetailsPresenter
import vinho.andre.android.com.gerenciadorvinho.util.DataUtil
import vinho.andre.android.com.gerenciadorvinho.util.PurchaseDialogUtil
import vinho.andre.android.com.gerenciadorvinho.view.adaprers.winedetailsactivity.CommentAdapter
import vinho.andre.android.com.gerenciadorvinho.view.adaprers.winedetailsactivity.PurchaseAdapter
import vinho.andre.android.com.gerenciadorvinho.view.adaprers.winedetailsactivity.VintageAdapter
import java.util.*

class WineDetailsActivity :
    AppCompatActivity(),
    WineDetailsActivityInterface {

    private var adapterComments: CommentAdapter? = null
    private var adapterPurchases: PurchaseAdapter? = null

    private lateinit var wine: Wine
    private lateinit var wineComplement: WineComplement
    private lateinit var purchaseDialogUtil: PurchaseDialogUtil
    private lateinit var presenter: WineDetailsPresenterInterface

    private fun getWineRegisterIntent(): Intent {
        return Intent(
            this,
            WineRegisterActivity::class.java
        ).putExtra(
            Wine.ParcelableWine,
            wine
        ).putExtra(
            WineComplement.ParcelableWineComplement,
            wineComplement
        )
    }

    fun deleteWine(
        view: View
    ) {
        dialogForConfirmationOperation(
            wine.wineId,
            wine.name,
            Wine.UpdateWine
        )
    }

    fun saveBookmark(
        view: View
    ) {
        presenter.saveBookmark(
            wine.wineId,
            this,
            cb_bookmark.isChecked
        )
    }

    fun callUpdateWine(
        view: View
    ) {
        startActivity(
            getWineRegisterIntent()
        )
        finish()
    }

    fun callImageActiity(
        view: View
    ) {
        if (wine.image[Wine.UrlImage].toString().isNotEmpty()) {
            startActivity(
                Intent(
                    this,
                    ImageActivity::class.java
                ).putExtra(
                    Wine.FieldImage,
                    wine.image[Wine.UrlImage].toString()
                )
            )
        }
    }

    fun decreaseWineHouse(
        view: View
    ) {
        val wineHouse: Int = wine.wineHouse
        if (wineHouse > 0) {
            presenter.modifyWineHouse(
                wine.wineId,
                wineHouse - 1,
                this,
                wineComplement.wineComplementId
            )
        }
    }

    fun increaseWineHouse(
        view: View
    ) {
        val wineHouse: Int = wine.wineHouse
        if (wineHouse < 999) {
            presenter.modifyWineHouse(
                wine.wineId,
                wineHouse + 1,
                this,
                wineComplement.wineComplementId
            )
        }
    }

    fun closePurchaseDialog(
        view: View?
    ) {
        purchaseDialogUtil.showDialog(false)
        adapterPurchases?.stopListening()
    }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wine_details)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter = WineDetailsPresenter(this)
        purchaseDialogUtil = PurchaseDialogUtil(this)
        purchaseDialogUtil.createDialogPurchase(
            R.layout.dialog_list_preco
        )

        wine = intent.getParcelableExtra(Wine.ParcelableWine)!!
    }

    override fun onOptionsItemSelected(
        item: MenuItem
    ): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        if (intent.getStringExtra(Purchase.newPurchase) != null) {
            openDialogPurchase(null)
        }
    }

    override fun onResume() {
        super.onResume()
        setTextWine()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapterComments?.stopListening()
        adapterPurchases?.stopListening()
    }

    override fun showProxy(
        status: Boolean
    ) {
        runOnUiThread {
            fl_proxy_container.visibility =
                if (status)
                    View.VISIBLE
                else
                    View.GONE

            purchaseDialogUtil.showProxy(status)
        }
    }

    override fun blockFields(
        status: Boolean
    ) {
        runOnUiThread {
            ibt_update.isEnabled = !status
            ibt_delete.isEnabled = !status
            cb_bookmark.isEnabled = !status
            ibt_purchase.isEnabled = !status
            ibt_increaseWineHouse.isEnabled = !status
            ibt_decreaseWineHouse.isEnabled = !status
        }
    }

    override fun snackBarFeedback(
        message: String
    ) {
        runOnUiThread {
            Snackbar.make(
                datails_container,
                message,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun toastFeedback(
        message: String
    ) {
        runOnUiThread {
            Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun deleteComment(
        comment: Comment
    ) {
        dialogForConfirmationOperation(
            comment.commentId,
            comment.comment,
            Comment.updateComment
        )
    }

    override fun deletePurchase(
        purchase: Purchase
    ) {
        dialogForConfirmationOperation(
            purchase.purchaseId,
            DataUtil().dateToString(purchase.date),
            Purchase.updatePurchase
        )
    }

    override fun callUpdateComment(
        comment: Comment
    ) {
        startActivity(
            getWineRegisterIntent()
                .putExtra(
                    Comment.ParcelableComment,
                    comment
                )
        )
        finish()
    }

    override fun getStringFormatted(
        text: String,
        field: String
    ): String {
        when (field) {
            "store" -> {
                return getString(R.string.field_store_purchase, text)
            }
            "amount" -> {
                return getString(R.string.field_amount_purchase, text)
            }
            "price" -> {
                return getString(R.string.field_price_purchase, text)
            }
            "vintage" -> {
                return getString(R.string.field_vintage_purchase, text)
            }
            else -> {
                return ""
            }
        }
    }

    override fun openDialogPurchase(
        view: View?
    ) {
        if (adapterPurchases == null) {
            adapterPreparationPurchase()
        }

        adapterPurchases!!.startListening()
        adapterPreparationVintage()

        runOnUiThread {
            purchaseDialogUtil.showDialog(true)
        }
    }

    override fun callUpdatePurchase(
        purchase: Purchase
    ) {
        purchaseDialogUtil.showDialog(false)

        startActivity(
            getWineRegisterIntent()
                .putExtra(
                    Purchase.ParcelablePurchase,
                    purchase
                )
        )
        finish()
    }

    fun callNewPurchase(
        view: View? = null
    ) {
        purchaseDialogUtil.showDialog(false)

        startActivity(
            getWineRegisterIntent()
                .putExtra(
                    Purchase.newPurchase,
                    Purchase.newPurchase
                )
        )
        finish()
    }

    override fun updateSaveBookmark(
        success: Boolean
    ) {
        if (success) {
            wine.bookmark = cb_bookmark.isChecked
            snackBarFeedback(
                if (cb_bookmark.isChecked) {
                    getString(R.string.added_to_favorite)
                } else {
                    getString(R.string.removed_from_favorite)
                }
            )
        } else {
            snackBarFeedback(
                getString(R.string.failed_favorite)
            )
        }
    }

    override fun updateTextWineHouse(
        date: Date,
        wineHouse: Int,
        success: Boolean
    ) {
        if (success) {
            runOnUiThread {
                wine.wineHouse = wineHouse
                wineComplement.dateWineHouse = date

                tv_wineHouse.text = wineHouse.toString()
                tv_dateWineHouse.text = DataUtil().dateToString(date)
                snackBarFeedback(
                    getString(R.string.updated_winery)
                )
            }
        } else {
            snackBarFeedback(
                getString(R.string.winery_upgrade_failed)
            )
        }
    }

    override fun setTextWineComplement(
        wineComplement: WineComplement
    ) {
        this.wineComplement = wineComplement

        /*Desbloquear o botão edit wine*/
        ibt_update.isClickable = true

        runOnUiThread {
            tv_grape.text = wineComplement.grape
            tv_producer.text = wineComplement.producer
            tv_harmonization.text = wineComplement.harmonization
            tv_temperature.text =
                if (wineComplement.temperature == 0) "" else wineComplement.temperature.toString()
            tv_dateWineHouse.text =
                DataUtil().dateToString(
                    wineComplement.dateWineHouse
                )
        }
    }

    override fun modifyTitleDialogPurchase(
        itemCount: Int
    ) {
        if (itemCount == 0) {
            purchaseDialogUtil.setTextWithoutPurchaseRecord()
            purchaseDialogUtil.showRecycler(
                false
            )
        } else {
            purchaseDialogUtil.setTextTitleInDetailActivity()
            purchaseDialogUtil.showRecycler(
                true
            )
        }
    }

    override fun getContext(): Context = this

    override fun finishDetailActivity() = finish()

    private fun setTextWine() {
        tv_name.text = wine.name
        rating.rating = wine.rating
        tv_country.text = wine.country
        cb_bookmark.isChecked = wine.bookmark
        tv_wineHouse.text = wine.wineHouse.toString()

        if (wine.image[Wine.UrlImage]?.isNotBlank()!!) {
            Picasso
                .get()
                .load(
                    wine.image[Wine.UrlImage]
                ).resize(
                    480, 854
                )
                .into(iv_wine)
        }

        /*Baixa o restante das informações do vinho*/
        presenter.retriveWineComplement(
            wine.wineId
        )

        /* Baixa os Comentarios */
        adapterPreparationComment()
        adapterPreparationVintage()
    }

    private fun adapterPreparationVintage() {
        val adapter = VintageAdapter(
            this,
            DBHelper(this).getVintage(
                wine.wineId
            )
        )

        rv_safra.adapter = adapter
    }

    private fun adapterPreparationComment() {
        adapterComments =
            CommentAdapter(
                presenter
                    .getFirestoreRecyclerOptionsComments(
                        wine.wineId
                    ),
                this
            )

        rv_comment.adapter = adapterComments
        adapterComments?.startListening()
    }

    private fun adapterPreparationPurchase() {
        adapterPurchases =
            PurchaseAdapter(
                presenter
                    .getFirestoreRecyclerOptionsPurchases(
                        wine.wineId
                    ),
                this
            )

        purchaseDialogUtil
            .getLayoutPurchase()
            .rv_preco
            .adapter = adapterPurchases
    }

    private fun dialogForConfirmationOperation(
        id: String,
        information: String,
        whichOperation: String
    ) {
        AlertDialog
            .Builder(this)
            .setTitle(
                getString(R.string.title_dialog_delete)
            )
            .setMessage(
                getString(R.string.dialog_message_delete, information)
            )
            .setPositiveButton(getString(R.string.yes_button)) { _, _ ->
                when (whichOperation) {
                    Wine.UpdateWine -> {
                        presenter.deleteWine(
                            id,
                            this,
                            wine.image
                        )
                    }
                    Purchase.updatePurchase -> {
                        closePurchaseDialog(null)
                        presenter.deletePurchase(
                            wine.wineId,
                            this,
                            id
                        )
                    }
                    Comment.updateComment -> {
                        presenter.deleteComment(
                            wine.wineId,
                            this,
                            id
                        )
                    }
                }
            }
            .setNegativeButton(getString(R.string.no_button)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}