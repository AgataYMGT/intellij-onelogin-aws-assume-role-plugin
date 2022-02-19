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
    textFieldWithBrowseButton(value = settings.oneloginSdkPropertyPath) { chosenFile ->
      settings.oneloginSdkPropertyPath = chosenFile.path
      settings.oneloginSdkPropertyPath
    }
  }
}
