package vinho.andre.android.com.gerenciadorvinho.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.content_form.*
import kotlinx.android.synthetic.main.content_wine_register.*
import kotlinx.android.synthetic.main.purchase_register_in_the_wine_register.view.*
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.domain.Comment
import vinho.andre.android.com.gerenciadorvinho.domain.ImageUtil
import vinho.andre.android.com.gerenciadorvinho.domain.Purchase
import vinho.andre.android.com.gerenciadorvinho.domain.Wine
import vinho.andre.android.com.gerenciadorvinho.domain.WineComplement
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.WineRegisterPresenterInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.WineRegisterViewInterface
import vinho.andre.android.com.gerenciadorvinho.presenter.WineRegisterPresenter
import vinho.andre.android.com.gerenciadorvinho.util.DataUtil
import vinho.andre.android.com.gerenciadorvinho.util.PurchaseDialogUtil
import vinho.andre.android.com.gerenciadorvinho.util.function.checkConnection
import vinho.andre.android.com.gerenciadorvinho.util.function.isValidLength
import vinho.andre.android.com.gerenciadorvinho.util.function.validate
import vinho.andre.android.com.gerenciadorvinho.view.abstracts.FormActivity
import java.io.File

class WineRegisterActivity :
    FormActivity(),
    WineRegisterViewInterface {

    private var image: ImageUtil? = null
    private var isUpdateOf: String? = null
    private var hasPurchase: Boolean = false

    private lateinit var purchaseDialogUtil: PurchaseDialogUtil
    private lateinit var presenter: WineRegisterPresenterInterface

    fun callCamera(
        view: View
    ) {
        ImagePicker.with(this)
            .setCameraOnly(true)
            .setKeepScreenOn(true)
            .start()
    }

    fun callGallery(
        view: View
    ) {
        val colorPrimary = ColorUtils.int2ArgbString(
            ColorUtils.getColor(R.color.colorPrimary)
        )

        val colorPrimaryDark = ColorUtils.int2ArgbString(
            ColorUtils.getColor(R.color.colorPrimaryDark)
        )

        val colorWhite = ColorUtils.int2ArgbString(
            Color.WHITE
        )

        ImagePicker
            .with(this)
            .setToolbarColor(colorPrimary)
            .setStatusBarColor(colorPrimaryDark)
            .setToolbarTextColor(colorWhite)
            .setToolbarIconColor(colorWhite)
            .setProgressBarColor(colorPrimaryDark)
            .setBackgroundColor(colorWhite)
            .setMultipleMode(false)
            .setFolderMode(true)
            .setShowCamera(true)
            .setLimitMessage(getString(R.string.imagepicker_selection_limit))
            .setFolderTitle(getString(R.string.imagepicker_gallery_activity)) //Nome da tela de galeria da ImagePicker API (funciona quando FolderMode = true)
            .setKeepScreenOn(true)
            .setAlwaysShowDoneButton(true)
            .start()
    }

    fun callDialogPurchase(
        view: View
    ) {
        purchaseDialogUtil.showDialog(
            true
        )
    }

    fun closePurchaseButton(
        view: View? = null
    ) {
        purchaseDialogUtil.showDialog(false)
        if (isUpdateOf.equals(Purchase.newPurchase)) {
            openDetailActivity()
        }
    }

    fun savePurchaseButton(
        view: View? = null
    ) {
        if (purchaseFormValidation()) {
            hasPurchase = true
            KeyboardUtils.hideSoftInput(this)

            when {
                isEqualPurchase() -> {
                    Toast.makeText(
                        this,
                        getString(R.string.no_change_of_information),
                        Toast.LENGTH_LONG
                    ).show()
                }
                !isEqualPurchase() && isUpdateOf != null &&
                        checkConnection(this) &&
                        (isUpdateOf == Purchase.newPurchase || isUpdateOf == Purchase.updatePurchase) -> {

                    purchaseDialogUtil.showDialog(false)
                    mainAction()
                }
                else -> {
                    closePurchaseButton()
                    snackBarFeedback(
                        fl_form_container,
                        getString(R.string.purchase_information_attached)
                    )
                }
            }
        }
    }

    override fun getWine(): Wine {
        val wine: Wine? = intent.getParcelableExtra(Wine.ParcelableWine)
        val layoutPurchase: View = purchaseDialogUtil.getLayoutPurchase()
        return Wine(
            wine?.wineId ?: "",
            et_name.text.toString().trim(),
            et_country.text.toString().trim(),
            wine?.image ?: mapOf(
                Wine.NameImage to "",
                Wine.UrlImage to ""
            ),
            if (!hasPurchase) {
                wine?.vintage ?: 0
            } else {
                layoutPurchase.et_vintage.text.toString().trim().toInt()
            },
            if (et_winehouse.text.isNullOrBlank()) {
                0
            } else {
                et_winehouse.text.toString().trim().toInt()
            },
            wine?.bookmark ?: false,
            rb_rating.rating
        )
    }

    override fun getComment(): Comment {
        val comment: Comment? = intent.getParcelableExtra(Comment.ParcelableComment)
        return Comment(
            comment?.commentId ?: "",
            comment?.date ?: DataUtil().getCurrentDateTime(),
            et_comment.text.toString().trim()
        )
    }

    override fun getPurchase(): Purchase {
        val layoutPurchase: View = purchaseDialogUtil.getLayoutPurchase()
        val purchase = intent.getParcelableExtra<Purchase>(Purchase.ParcelablePurchase)

        return Purchase(
            layoutPurchase.et_vintage.text
                .toString()
                .trim()
                .toInt(),
            layoutPurchase.et_amount.text
                .toString()
                .trim()
                .toInt(),
            layoutPurchase.et_price.text
                .toString()
                .trim()
                .toDouble(),
            DataUtil()
                .isDate(
                    layoutPurchase.et_date.text.toString().trim()
                ),
            purchase?.purchaseId ?: "",
            layoutPurchase.et_store.text
                .toString()
                .trim(),
            layoutPurchase.et_comment.text
                .toString()
                .trim()
        )
    }

    override fun getImage(): ImageUtil? = image

    override fun getWineComplement(): WineComplement {
        val wineComplement: WineComplement? =
            intent.getParcelableExtra(WineComplement.ParcelableWineComplement)
        return WineComplement(
            wineComplement?.wineComplementId ?: "",
            if (et_grape.text.isNullOrBlank()) {
                ""
            } else {
                et_grape.text.toString().trim()
            },
            if (et_harmonization.text.isNullOrBlank()) {
                ""
            } else {
                et_harmonization.text.toString().trim()
            },
            if (et_temperature.text.isNullOrBlank()) {
                0
            } else {
                et_temperature.text.toString().trim().toInt()
            },
            if (et_producer.text.isNullOrBlank()) {
                ""
            } else {
                et_producer.text.toString().trim()
            },
            wineComplement?.dateWineHouse ?: DataUtil().getCurrentDateTime()
        )
    }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        presenter = WineRegisterPresenter(this)
        purchaseDialogUtil = PurchaseDialogUtil(this)

        purchaseDialogUtil.createDialogPurchase(
            R.layout.purchase_register_in_the_wine_register
        )

        checkIfIsUpdate()

        et_name.validate(
            {
                it.isValidLength()
            },
            getString(R.string.field_required)
        )

        et_country.validate(
            {
                it.isValidLength()
            },
            getString(R.string.field_required)
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == Config.RC_PICK_IMAGES &&
            resultCode == Activity.RESULT_OK &&
            data != null
        ) {
            val images =
                data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)

            if (images!!.isNotEmpty()) {
                val dataUtil = DataUtil()
                val imgOriginal = File(images.first().path)
                val directoryPathFile =
                    File(
                        Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES
                        ).absolutePath,
                        getString(R.string.imagepicker_cam_photos_activity)
                    ).absolutePath

                val imageResized = Compressor(this)
                    .setMaxWidth(858) /*old value 640*/
                    .setMaxHeight(480)
                    .setQuality(75)
                    .setCompressFormat(
                        Bitmap.CompressFormat.JPEG
                    )
                    .setDestinationDirectoryPath(
                        directoryPathFile
                    )
                    .compressToFile(
                        imgOriginal
                    )

                val folder = File("/storage/emulated/0/Pictures/Camera")
                folder.deleteRecursively()

                image = ImageUtil(
                    imageResized.path,
                    dataUtil.generateNameForImage()
                )

                if (isUpdateOf == Wine.UpdateWine) {
                    image!!.nameOldImage =
                        getWine().image[Wine.NameImage].toString()
                }

                iv_wine.setImageURI(
                    Uri.parse(imageResized.path)
                )
            }
        }

        /*
         * A invocação a super.onActivityResult() tem que
         * vir após a verificação / obtenção da imagem.
         * */
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        val hasName = et_name.text.toString().trim().isNotEmpty()
        val hasCountry = et_country.text.toString().trim().isNotEmpty()

        when {
            isUpdateOf != null && (!isEqualWine() || image != null || hasPurchase) -> {
                dialogForConfirmationOperation()
            }
            isUpdateOf == null && (hasName || hasCountry || image != null || hasPurchase) -> {
                dialogForConfirmationOperation()
            }
            isUpdateOf != null -> {
                openDetailActivity()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun blockFields(
        status: Boolean
    ) {
        et_name.isEnabled = !status
        et_grape.isEnabled = !status
        rb_rating.setIsIndicator(status)
        ibt_camera.isEnabled = !status
        et_country.isEnabled = !status
        et_comment.isEnabled = !status
        et_producer.isEnabled = !status
        et_winehouse.isEnabled = !status
        et_temperature.isEnabled = !status
        et_harmonization.isEnabled = !status

        ibt_gallery.isEnabled = !status
        bt_saveWine.isEnabled = !status
        ibt_purchase_history.isEnabled = !status
    }

    override fun isMainButtonSending(
        status: Boolean
    ) {
        bt_saveWine.text =
            if (status)
                getString(R.string.save_going)
            else
                getString(R.string.save)
    }

    override fun requestActionOnFirebase() {
        if (wineFormValidation()) {
            val hasComment: Boolean =
                et_comment.text.toString().isNotEmpty()

            val hasImage: Boolean = image != null

            if (hasImage ||
                hasPurchase ||
                !isEqualWine() ||
                (hasComment && !isEqualComment())
            ) {
                presenter.onSaveWine(
                    hasImage,
                    isUpdateOf,
                    hasComment,
                    hasPurchase
                )
            } else {
                unBlockFields()
                snackBarFeedback(
                    fl_form_container,
                    getString(R.string.no_change_of_information)
                )
            }
        } else {
            unBlockFields()
        }
    }

    override fun callWineDetailsActivity(
        wine: Wine
    ) {
        runOnUiThread {
            Toast.makeText(
                this,
                getString(R.string.success_in_operation),
                Toast.LENGTH_LONG
            ).show()
        }
        unBlockFields()
        openDetailActivity(wine)
    }

    private fun setTextWine() {
        val wine = getWineIntent()!!
        val wineComplement =
            getWineComplementIntent()!!

        et_name.setText(wine.name)
        rb_rating.rating = wine.rating
        et_country.setText(wine.country)
        textInputLayout21.hint = getString(R.string.hint_new_comment_record)
        et_winehouse.setText(
            if (wine.wineHouse == 0) {
                ""
            } else {
                wine.wineHouse.toString()
            }
        )

        et_grape.setText(wineComplement.grape)
        et_producer.setText(wineComplement.producer)
        et_harmonization.setText(wineComplement.harmonization)

        et_temperature.setText(
            if (wineComplement.temperature == 0) {
                ""
            } else {
                wineComplement.temperature.toString()
            }
        )

        if (wine.image[Wine.UrlImage]?.isNotBlank()!!) {
            Picasso
                .get()
                .load(
                    wine.image[Wine.UrlImage]
                )
                .into(iv_wine)
        }
    }

    private fun setTextComment() {
        et_comment.setText(getCommentIntent()?.comment)
        textInputLayout21.hint = getString(R.string.hint_update_comment_record)
    }

    private fun setTextPurchase() {
        Handler().postDelayed(
            {
                val purchase = getPurchaseIntent()
                val layoutPurchase = purchaseDialogUtil.getLayoutPurchase()

                layoutPurchase.et_store.setText(purchase?.store)
                layoutPurchase.et_comment.setText(purchase?.comment)
                layoutPurchase.et_price.setText(purchase?.price.toString())
                layoutPurchase.et_amount.setText(purchase?.amount.toString())
                layoutPurchase.et_vintage.setText(purchase?.vintage.toString())
                layoutPurchase.et_date.setText(DataUtil().dateToString(purchase!!.date))
                purchaseDialogUtil.showDialog(true)
            },
            500
        )
    }

    private fun checkIfIsUpdate() {
        when {
            getCommentIntent() != null -> {
                setTextWine()
                setTextComment()
                blockFields(true)
                et_comment.isEnabled = true
                bt_saveWine.isEnabled = true
                isUpdateOf = Comment.updateComment
            }
            getPurchaseIntent() != null -> {
                setTextWine()
                blockFields(true)
                isUpdateOf = Purchase.updatePurchase
                purchaseDialogUtil.setTextTitleInUpdatePurchase()

                setTextPurchase()
            }
            getWineIntent() != null -> {
                setTextWine()

                if (intent.getStringExtra(Purchase.newPurchase) != null) {
                    isUpdateOf = Purchase.newPurchase
                    blockFields(true)

                    Handler().postDelayed({
                        purchaseDialogUtil.showDialog(true)
                    }, 500)
                } else {
                    isUpdateOf = Wine.UpdateWine
                    ibt_purchase_history.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun deleteImageOnExit() {
        if (image != null) {
            if (!File(image!!.currentPathImage).delete()) {
                Log.e(
                    "MAIN",
                    "Erro ao deletar a imagem ao sair"
                )
            }
        }
    }

    private fun openDetailActivity(
        wine: Wine? = getWine()
    ) {
        val intent =
            Intent(
                this,
                WineDetailsActivity::class.java
            ).putExtra(
                Wine.ParcelableWine,
                wine
            )

        if (isUpdateOf.equals(Purchase.newPurchase)) {
            intent.putExtra(
                Purchase.newPurchase,
                Purchase.newPurchase
            )
        }

        startActivity(
            intent
        )
        finish()
    }

    private fun wineFormValidation(): Boolean {
        if (et_name.error != null || et_country.error != null) {
            return false
        } else {
            if (TextUtils.isEmpty(
                    et_name.text.toString()
                )
            ) {
                et_name.error = getString(R.string.field_required)
                return false
            } else {
                et_name.error = null
            }

            if (TextUtils.isEmpty(
                    et_country.text.toString()
                )
            ) {
                et_country.error = getString(R.string.field_required)
                return false
            } else {
                et_country.error = null
            }

            if (isUpdateOf == Comment.updateComment &&
                TextUtils.isEmpty(et_comment.text.toString())
            ) {
                et_comment.error = getString(R.string.field_required)
                return false
            } else {
                et_comment.error = null
            }

            if (!checkConnection(this)) {
                snackBarFeedback(
                    fl_form_container,
                    getString(R.string.no_internet_connection)
                )
                return false
            }

            return true
        }
    }

    private fun purchaseFormValidation(): Boolean {
        val layoutPurchase: View = purchaseDialogUtil.getLayoutPurchase()

        if (TextUtils.isEmpty(
                layoutPurchase.et_date.text.toString()
            )
        ) {
            layoutPurchase.et_date.error = getString(R.string.field_required)
            return false
        } else {
            layoutPurchase.et_date.error = null
        }

        if (TextUtils.isEmpty(
                layoutPurchase.et_price.text.toString()
            )
        ) {
            layoutPurchase.et_price.error = getString(R.string.field_required)
            return false
        } else {
            layoutPurchase.et_price.error = null
        }

        when {
            TextUtils.isEmpty(
                layoutPurchase.et_vintage.text.toString()
            ) -> {
                layoutPurchase.et_vintage.error = getString(R.string.field_required)
                return false
            }
            layoutPurchase.et_vintage.text!!.length < 4 -> {
                layoutPurchase.et_vintage.error = getString(R.string.validation_purchase_vintage)
                return false
            }
            else -> layoutPurchase.et_vintage.error = null
        }

        if (TextUtils.isEmpty(
                layoutPurchase.et_amount.text.toString()
            )
        ) {
            layoutPurchase.et_amount.error = getString(R.string.field_required)
            return false
        } else {
            layoutPurchase.et_amount.error = null
        }

        if (TextUtils.isEmpty(
                layoutPurchase.et_store.text.toString()
            )
        ) {
            layoutPurchase.et_store.error = getString(R.string.field_required)
            return false
        } else {
            layoutPurchase.et_store.error = null
        }

        val dateUtil = DataUtil()
        dateUtil.isDate(
            layoutPurchase.et_date.text.toString()
        )

        return if (dateUtil.getIsValid()) {
            layoutPurchase.et_date.error = null
            true
        } else {
            layoutPurchase.et_date.error = getString(R.string.validation_date)
            false
        }
    }

    private fun dialogForConfirmationOperation() {
        AlertDialog
            .Builder(this)
            .setTitle(
                getString(R.string.title_dialog_delete)
            )
            .setMessage(
                getString(R.string.message_exit_without_save)
            )
            .setPositiveButton(getString(R.string.yes_button)) { _, _ ->
                deleteImageOnExit()
                if (isUpdateOf != null) {
                    openDetailActivity()
                } else {
                    finish()
                }
            }
            .setNegativeButton(getString(R.string.no_button)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun getLayoutResourceID(): Int =
        R.layout.content_wine_register

    override fun getContext(): Context = this

    private fun getWineIntent(): Wine? =
        intent.getParcelableExtra(Wine.ParcelableWine)

    private fun getCommentIntent(): Comment? =
        intent.getParcelableExtra(Comment.ParcelableComment)

    private fun getPurchaseIntent(): Purchase? =
        intent.getParcelableExtra(Purchase.ParcelablePurchase)

    private fun isEqualPurchase(): Boolean =
        getPurchaseIntent() == getPurchase()

    private fun isEqualComment(): Boolean =
        getCommentIntent() == getComment()

    private fun isEqualWine(): Boolean =
        getWineIntent() == getWine() &&
                getWineComplementIntent() == getWineComplement()

    private fun getWineComplementIntent(): WineComplement? =
        intent.getParcelableExtra(WineComplement.ParcelableWineComplement)
}