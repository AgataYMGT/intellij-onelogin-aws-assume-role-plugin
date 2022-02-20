package net.millenary.intellij.plugin.onelogin_aws

data class SamlAssertionException(

  override val message: String? = "",

  override val cause: Throwable? = null
) : RuntimeException(message, cause)
