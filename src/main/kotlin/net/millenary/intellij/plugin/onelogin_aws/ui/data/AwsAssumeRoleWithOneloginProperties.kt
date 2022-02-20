package net.millenary.intellij.plugin.onelogin_aws.ui.data

import net.millenary.intellij.plugin.onelogin_aws.setting.OneloginAwsAssumeRoleSettings

data class AwsAssumeRoleWithOneloginProperties(

  var username: String = "",

  var password: String = "",

  var appId: String = "",

  var instanceSubDomain: String = ""
) {

  companion object {

    fun from(projectSettings: OneloginAwsAssumeRoleSettings) = AwsAssumeRoleWithOneloginProperties(
      instanceSubDomain = projectSettings.oneloginSdkInstanceSubDomain
    )
  }
}
