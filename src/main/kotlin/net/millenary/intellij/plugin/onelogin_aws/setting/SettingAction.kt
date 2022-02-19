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
  abstract class CheckboxAction(actionName: String, settingProperty: KMutableProperty0<Boolean>) :
    SettingAction<Boolean>(
      actionName = actionName,
      defaultValue = true,
      settingProperty = settingProperty
    )

  /**
   * テキスト入力のアクション(文字列)
   */
  abstract class InputTextAction(actionName: String, settingProperty: KMutableProperty0<String>) :
    SettingAction<String>(
      actionName = actionName,
      defaultValue = "",
      settingProperty = settingProperty
    )

  /**
   * プラグインの有効化
   */
  data class Enabled(private val settings: OneloginAwsAssumeRoleSettings) : CheckboxAction(
    actionName = "Enable OneLogin AWS Assume Role Plugin",
    settingProperty = settings::isEnabled
  )

  /**
   * onelogin.sdk.properties のパス
   */
  data class OneloginSdkPropertiesPath(private val settings: OneloginAwsAssumeRoleSettings) : InputTextAction(
    actionName = "Path of onelogin.sdk.properties",
    settingProperty = settings::oneloginSdkPropertiesPath
  ) {

    override val defaultValue: String = Paths.get(System.getProperty("user.home"), "onelogin.sdk.properties").toString()
  }

  /**
   * OneLogin の インスタンスサブドメイン
   */
  data class OneloginInstanceSubDomain(private val settings: OneloginAwsAssumeRoleSettings) : InputTextAction(
    actionName = "Instance Sub-domain",
    settingProperty = settings::oneloginSdkInstanceSubDomain
  )
}
