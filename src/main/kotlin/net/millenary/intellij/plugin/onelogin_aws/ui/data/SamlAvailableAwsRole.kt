package net.millenary.intellij.plugin.onelogin_aws.ui.data

/**
 * SAML レスポンスより利用可能な AWS ロールの表現
 */
data class SamlAvailableAwsRole(

  val accountId: String,

  val roleName: String,

  val originalAttribute: String
)
