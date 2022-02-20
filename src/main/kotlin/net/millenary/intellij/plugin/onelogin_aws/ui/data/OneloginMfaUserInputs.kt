package net.millenary.intellij.plugin.onelogin_aws.ui.data

/**
 * OneLogin の SAML アサーションにおける MFA のユーザー選択値を格納する
 */
data class OneloginMfaUserInputs(

  var deviceId: String = "",

  var otpToken: String = ""
)
