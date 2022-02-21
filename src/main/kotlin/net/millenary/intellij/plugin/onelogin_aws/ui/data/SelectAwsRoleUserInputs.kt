package net.millenary.intellij.plugin.onelogin_aws.ui.data

/**
 * AWS ロールのユーザー選択を格納する
 */
data class SelectAwsRoleUserInputs(

  var roleArn: String = "",

  var principalArn: String = ""
)
