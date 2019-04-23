import android.accounts.NetworkErrorException
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.util.LruCache
import com.example.android.database.App
import com.example.android.imageloader.Callback
import java.io.IOException
import java.net.URL

class ImageLoader private constructor() {

    companion object {

        @Volatile
        private var instance: ImageLoader? = null

        fun getInstance(): ImageLoader? {
            if (instance == null) {
                synchronized(ImageLoader::class.java) {
                    if (instance == null) {
                        instance = ImageLoader()
                    }
                }
            }

            return instance
        }
    }

    private val cache: LruCache<String, Bitmap>

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8

        this.cache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }
    }

    fun load(url: String, callback: Callback) {
        val bitmap = cache.get(url)

        val connectivityManager =
            App.get()?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        if (bitmap != null && url != "") {
            callback.onSuccess(url, bitmap)
        } else if (networkInfo != null && networkInfo.isConnected) {
            LoaderTask(callback).execute(url)
        } else {
            callback.onError(url, NetworkErrorException("No internet connection"))
        }
    }

    private class LoaderTask(var callback: Callback) :
        AsyncTask<String, Void, Bitmap>() {

        lateinit var url: String

        override fun doInBackground(vararg urls: String): Bitmap? {
            url = urls[0]
            val image: Bitmap?

            try {
                val inputStream = URL(url).openStream()
                image = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

            } catch (e: IOException) {
                callback.onError(url, NullPointerException("Not found image"))
                return null
            }

            return image
        }

        override fun onPostExecute(bitmap: Bitmap) {
            ImageLoader.instance?.cache?.put(url, bitmap)
            callback.onSuccess(url, bitmap)

        }
    }
}

