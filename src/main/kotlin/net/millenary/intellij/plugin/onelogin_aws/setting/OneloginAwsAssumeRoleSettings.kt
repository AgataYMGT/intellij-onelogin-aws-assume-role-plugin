package net.millenary.intellij.plugin.onelogin_aws.setting

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * プラグインのプロパティを保持するクラス
 */
@Service
@State(name = "OneloginAwsAssumeRoleSettings", storages = [Storage("onelogin-aws-assume-role-settings.xml")])
class OneloginAwsAssumeRoleSettings : PersistentStateComponent<OneloginAwsAssumeRoleSettings> {

  companion object {

    fun getInstance(project: Project) = project.service<OneloginAwsAssumeRoleSettings>()
  }

  /**
   * プラグインが有効か
   */
  var isEnabled: Boolean by object :
    LoggingProperty<OneloginAwsAssumeRoleSettings, Boolean>(SettingAction.Enabled(this).defaultValue) {}

  /**
   * OneLogin SDK のプロパティファイルパス
   */
  var oneloginSdkPropertiesPath: String by object :
    LoggingProperty<OneloginAwsAssumeRoleSettings, String>(SettingAction.OneloginSdkPropertiesPath(this).defaultValue) {}

  /**
   * OneLogin のインスタンスサブドメイン
   */
  var oneloginSdkInstanceSubDomain: String by object :
    LoggingProperty<OneloginAwsAssumeRoleSettings, String>(SettingAction.OneloginInstanceSubDomain(this).defaultValue) {}

  override fun getState(): OneloginAwsAssumeRoleSettings = this

  override fun loadState(state: OneloginAwsAssumeRoleSettings) {
    XmlSerializerUtil.copyBean(state, this)
  }

  /** Factory class to generate synthetic properties, that log every access and mutation to each property. */
  internal open class LoggingProperty<R, T>(initValue: T) : ReadWriteProperty<R, T> {
    var backingField: T = initValue

    override fun getValue(thisRef: R, property: KProperty<*>): T {
      println("State::${property.name}.getValue(), value: '$backingField'")
      return backingField
    }

    override fun setValue(thisRef: R, property: KProperty<*>, value: T) {
      backingField = value
      println("State::${property.name}.setValue(), value: '$backingField'")
    }
  }
}
