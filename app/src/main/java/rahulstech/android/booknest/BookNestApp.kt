package rahulstech.android.booknest

import android.app.Application
import com.google.firebase.FirebaseApp

class BookNestApp: Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }
}