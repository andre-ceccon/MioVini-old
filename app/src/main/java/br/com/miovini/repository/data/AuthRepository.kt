package br.com.miovini.repository.data

import android.content.Context

class AuthRepository(context: Context) {
//    var googleSignInClient: GoogleSignInClient
//    var firebaseAuth: FirebaseAuth
//
//    init {
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(context.getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        googleSignInClient = GoogleSignIn.getClient(context, gso)
//        firebaseAuth = Firebase.auth
//    }
//
//    fun requestUser(): FirebaseUser? {
//        return firebaseAuth.currentUser
//    }
//
//    fun login(
//        activity: AppCompatActivity, userResult: (FirebaseUser?, String?) -> Unit
//    ) {
//        val intent = googleSignInClient.signInIntent
//        activity.ActivityResultContract(intent) { success, data ->
//            if (success) {
//                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//                try {
//                    val account = task.getResult(ApiException::class.java)
//                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
//                    firebaseAuth.signInWithCredential(credential)
//                        .addOnCompleteListener {
//                            if (it.isSuccessful) {
//                                val user = firebaseAuth.currentUser
//                                userResult.invoke(user, null)
//                            } else {
//                                userResult.invoke(null, "Login failed, user null")
//                            }
//                        }
//                } catch (e: ApiException) {
//                    userResult.invoke(null, e.localizedMessage)
//                }
//            } else {
//                userResult.invoke(null, "failed")
//            }
//        }
//    }
}