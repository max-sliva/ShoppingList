//package com.example.shoppinglist
//
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.api.client.extensions.android.http.AndroidHttp
//import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
//import com.google.api.client.json.jackson2.JacksonFactory
//import com.google.api.services.drive.Drive
//import com.google.api.services.drive.DriveScopes
//
//class GoogleDriveUtils {
////    fun getDriveService() : Drive?{
////        GoogleSignIn.getLastSignedInAccount(this)?.let { googleAccount ->
////            val credential = GoogleAccountCredential.usingOAuth2(
////                this, listOf(DriveScopes.DRIVE_FILE)
////            )
////            credential.selectedAccount = googleAccount.account!!
////            return Drive.Builder(
////                AndroidHttp.newCompatibleTransport(),
////                JacksonFactory.getDefaultInstance(),
////                credential
////            )
////                .setApplicationName(getString(R.string.app_name))
////                .build()
////        }
////        return null
////    }
//}