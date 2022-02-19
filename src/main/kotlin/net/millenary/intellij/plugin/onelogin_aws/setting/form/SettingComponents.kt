package net.millenary.intellij.plugin.onelogin_aws.setting.form

import com.intellij.ui.layout.Row
import net.millenary.intellij.plugin.onelogin_aws.setting.OneloginAwsAssumeRoleSettings
import net.millenary.intellij.plugin.onelogin_aws.setting.SettingAction

/**
 * 基本設定 UI パネル
 */
fun Row.createGeneralPanel(settings: OneloginAwsAssumeRoleSettings) = titledRow("General") {
  val enabledAction = SettingAction.Enabled(settings)

  row {
    checkBox(text = enabledAction.actionName, enabledAction.settingProperty)
  }
}

/**
 * OneLogin SDK 項目設定 UI パネル
 */
fun Row.createOneloginSdkPanel(settings: OneloginAwsAssumeRoleSettings) = titledRow("OneLogin SDK") {
  val sdkPathAction = SettingAction.OneloginSdkPropertiesPath(settings)

  row {
    label(sdkPathAction.actionName)
    textFieldWithBrowseButton(value = settings.oneloginSdkPropertiesPath) { chosenFile ->
      settings.oneloginSdkPropertiesPath = chosenFile.path
      settings.oneloginSdkPropertiesPath
    }
  }
}

/**
 * OneLogin アカウント設定 UI パネル
 */
fun Row.createOneloginAccountDetails(settings: OneloginAwsAssumeRoleSettings) = titledRow("OneLogin Account Details") {
  val subDomainAction = SettingAction.OneloginInstanceSubDomain(settings)

  row {
    label(subDomainAction.actionName)
    textField(subDomainAction.settingProperty)
  }
}
