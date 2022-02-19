package net.millenary.intellij.plugin.onelogin_aws.setting

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "OneloginAwsAssumeRoleSettings", storages = [Storage("onelogin-aws-assume-role-settings.xml")])
class OneloginAwsAssumeRoleSettings(

  /**
   * プラグインが有効か
   */
  var isEnabled: Boolean,

  /**
   * OneLogin SDK のプロパティファイルパス
   */
  var oneloginSdkPropertyPath: String
) : PersistentStateComponent<OneloginAwsAssumeRoleSettings> {

  override fun getState(): OneloginAwsAssumeRoleSettings = this

  override fun loadState(state: OneloginAwsAssumeRoleSettings) {
    XmlSerializerUtil.copyBean(state, this)
  }
}
