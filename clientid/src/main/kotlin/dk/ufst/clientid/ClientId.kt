package dk.ufst.clientid

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import java.util.UUID

class ClientId internal constructor(context: Context)
{
    private var clientId: String = ""
    private var clientPlatform: String = "Android ${android.os.Build.VERSION.RELEASE} - ${android.os.Build.MANUFACTURER} ${android.os.Build.DEVICE} (${android.os.Build.MODEL})"
    private var clientVersion: String = ""

    init {
        val sharedPrefs = context.getSharedPreferences("clientid", Context.MODE_PRIVATE)
        sharedPrefs.getString("clientId", null)?.let {
            clientId = it
            log("Using existing clientId: $it")
        } ?: run {
            clientId = UUID.randomUUID().toString()
            log("Generating new clientId: $clientId")
            sharedPrefs.edit().putString("clientId", clientId).apply()
        }

        val pm: PackageManager = context.packageManager
        val pkgName: String = context.packageName
        val pkgInfo: PackageInfo?
        try {
            pkgInfo = pm.getPackageInfo(pkgName, 0)
            clientVersion = "${pkgInfo.versionName} (${pkgInfo.versionCode}) "
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        log("clientId: $clientId")
        log("clientPlatform: $clientPlatform")
        log("clientVersion: $clientVersion")
    }

    private fun getRequestHeaders(): Map<String, String> = mapOf(
        "X-UFST-Client-ID" to clientId,
        "X-UFST-Client-Platform" to clientPlatform,
        "X-UFST-Client-Version" to clientVersion,
        "X-UFST-Client-ID" to UUID.randomUUID().toString(),
    )


    companion object {
        fun init(context: Context) {
            instance = ClientId(context)
        }

        fun getRequestHeaders() = checkInit {
            getRequestHeaders()
        }

        private fun <T>checkInit(block: ClientId.()->T): T =
            block.invoke(instance ?: throw RuntimeException("You must call ClientId.init() before attempting to use the library"))

        private suspend fun <T>checkInitSuspend(block: suspend ClientId.()->T): T =
            block.invoke(instance ?: throw RuntimeException("You must call ClientId.init() before attempting to use the library"))

        private var instance: ClientId? = null
    }
}