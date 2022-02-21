package net.millenary.intellij.plugin.onelogin_aws.ui.data

import com.intellij.util.io.exists
import com.intellij.util.io.inputStream
import com.onelogin.sdk.util.Settings
import net.millenary.intellij.plugin.onelogin_aws.RequiredValueNotFoundException
import java.nio.file.Path
import java.util.Properties

/**
 * OneLogin SDK のプロパティ (onelogin.sdk.properties) をデータクラスとして表現するクラス
 */
data class OneloginSdkProperties(

  val clientId: String,

  val clientSecret: String,

  val region: String,

  val ipAddress: String
) {

  companion object {

    fun from(path: Path): OneloginSdkProperties {
      if (!path.exists()) {
        throw RequiredValueNotFoundException("onelogin.sdk.properties not found at: $path")
      }

      val properties = Properties()
      path.inputStream().use { propertiesFileIs -> properties.load(propertiesFileIs) }

      return OneloginSdkProperties(
        clientId = properties.getProperty(Settings.CLIENT_ID_KEY)
          ?: throw RequiredValueNotFoundException("OneLogin Client ID must not be null"),
        clientSecret = properties.getProperty(Settings.CLIENT_SECRET_KEY)
          ?: throw RequiredValueNotFoundException("OneLogin Client Secret must not be null"),
        region = properties.getProperty(Settings.REGION, "us"),
        ipAddress = properties.getProperty(Settings.IP, "")
      )
    }
  }
}
