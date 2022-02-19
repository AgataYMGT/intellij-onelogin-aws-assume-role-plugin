package net.millenary.intellij.plugin.onelogin_aws.setting

import java.nio.file.Paths
import kotlin.reflect.KMutableProperty0

/**
 * 設定アクション
 */
sealed class SettingAction<T>(

  /**
   * アクションを説明するテキスト
   */
  val actionName: String,

  /**
   * デフォルト値
   */
  open val defaultValue: T,

  /**
   * [OneloginAwsAssumeRoleSettings] においてアクションによる値変更に対応するプロパティ
   */
  val settingProperty: KMutableProperty0<T>
) {

  /**
   * チェックボックスのアクション(論理値)
   */
  abstract class CheckboxAction(actionName: String, settings: OneloginAwsAssumeRoleSettings) : SettingAction<Boolean>(
    actionName = actionName,
    defaultValue = true,
    settingProperty = settings::isEnabled
  )

  /**
   * テキスト入力のアクション(文字列)
   */
  abstract class InputTextAction(actionName: String, settings: OneloginAwsAssumeRoleSettings) : SettingAction<String>(
    actionName = actionName,
    defaultValue = "",
    settingProperty = settings::oneloginSdkPropertyPath
  )

  /**
   * プラグインの有効化
   */
  data class Enabled(private val settings: OneloginAwsAssumeRoleSettings) : CheckboxAction(
    actionName = "Enable OneLogin AWS Assume Role Plugin",
    settings = settings
  )

  /**
   * onelogin.sdk.properties のパス
   */
  data class OneloginSdkPropertiesPath(private val settings: OneloginAwsAssumeRoleSettings) : InputTextAction(
    actionName = "Path of onelogin.sdk.properties",
    settings = settings
  ) {

    override val defaultValue: String = Paths.get(System.getProperty("user.home"), "onelogin.sdk.properties").toString()
  }
}
