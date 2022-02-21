package net.millenary.intellij.plugin.onelogin_aws

/**
 * 必要なパラメーターが存在しないときに投げられる例外
 */
data class RequiredValueNotFoundException(

  override val message: String? = "",

  override val cause: Throwable? = null
) : RuntimeException(message, cause)
