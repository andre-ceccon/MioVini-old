package vinho.andre.android.com.gerenciadorvinho.data.firebase

import android.content.Context
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.domain.Wine
import vinho.andre.android.com.gerenciadorvinho.interfaces.data.MainActivityDataInterface
import vinho.andre.android.com.gerenciadorvinho.interfaces.presenter.MainActivityPresenterInterface

class MainActivityData(
    private var presenter: MainActivityPresenterInterface
) : FirebaseBase(),
    MainActivityDataInterface {

    override fun getFirebaseUser(): FirebaseUser = getAuth().currentUser!!

    override fun onSignOut(
        context: Context
    ) {
        if (getAuth().currentUser!!
                .providerData[1]
                .providerId == "google.com"
        ) {
            GoogleSignIn.getClient(
                context,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            ).signOut()
        }

        getAuth().signOut()
        presenter.callLoginActivity()
    }

    override fun prepareFirestoreRecyclerOptions(
        menuItemId: Int
    ): FirestoreRecyclerOptions<Wine> = prepareQuery(menuItemId)

    private fun prepareQuery(
        menuItemId: Int
    ): FirestoreRecyclerOptions<Wine> {
        var query: Query? = null
        when (menuItemId) {
            R.id.nav_in_the_cellar -> {
                query = getWinesCollection()
                    .orderBy("wineHouse", Query.Direction.DESCENDING)
                    .orderBy("rating", Query.Direction.DESCENDING)
                    .orderBy("bookmark", Query.Direction.DESCENDING)
                    .orderBy("name")
                    .whereGreaterThanOrEqualTo("wineHouse", 1)
            }
            R.id.nav_rating -> {
                query = getWinesCollection()
                    .orderBy("rating", Query.Direction.DESCENDING)
                    .orderBy("bookmark", Query.Direction.DESCENDING)
                    .orderBy("wineHouse", Query.Direction.DESCENDING)
                    .orderBy("name")
            }
            R.id.nav_bookmark -> {
                query = getWinesCollection()
                    .orderBy("rating", Query.Direction.DESCENDING)
                    .orderBy("wineHouse", Query.Direction.DESCENDING)
                    .orderBy("name")
                    .whereEqualTo("bookmark", true)
            }
            R.id.nav_name -> {
                query = getWinesCollection().orderBy("name")

            }
            R.id.nav_country_of_origin -> {
                query = getWinesCollection()
                    .orderBy("country")
                    .orderBy("rating", Query.Direction.DESCENDING)
                    .orderBy("bookmark", Query.Direction.DESCENDING)
                    .orderBy("wineHouse", Query.Direction.DESCENDING)
                    .orderBy("name")
            }
        }

        return FirestoreRecyclerOptions
            .Builder<Wine>()
            .setQuery(
                query!!,
                Wine::class.java
            ).build()
    }
}