package net.millenary.intellij.plugin.onelogin_aws.setting

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.layout.panel
import net.millenary.intellij.plugin.onelogin_aws.setting.form.createGeneralPanel
import net.millenary.intellij.plugin.onelogin_aws.setting.form.createOneloginAccountDetails
import net.millenary.intellij.plugin.onelogin_aws.setting.form.createOneloginSdkPanel

class OneloginAwsAssumeRoleConfiguration(project: Project) : BoundConfigurable("OneLogin AWS Assume Role") {

  private val settings: OneloginAwsAssumeRoleSettings

  init {
    settings = OneloginAwsAssumeRoleSettings.getInstance(project)
  }

  override fun createPanel(): DialogPanel = panel {
    blockRow {
      createGeneralPanel(settings)
      createOneloginSdkPanel(settings)
      createOneloginAccountDetails(settings)
    }
  }
}
