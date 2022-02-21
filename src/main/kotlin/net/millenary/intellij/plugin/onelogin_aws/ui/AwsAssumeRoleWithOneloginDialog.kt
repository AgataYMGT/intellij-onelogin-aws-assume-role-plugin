package net.millenary.intellij.plugin.onelogin_aws.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.layout.Row
import com.intellij.ui.layout.panel
import com.intellij.ui.layout.toBinding
import com.intellij.ui.layout.withTextBinding
import net.millenary.intellij.plugin.onelogin_aws.setting.OneloginAwsAssumeRoleSettings
import net.millenary.intellij.plugin.onelogin_aws.ui.data.AwsAssumeRoleWithOneloginProperties
import javax.swing.JComponent

/**
 * AWS Assume Role 開始 (OneLogin アカウント情報入力)ダイアログ
 */
class AwsAssumeRoleWithOneloginDialog(project: Project) : DialogWrapper(project, true) {

  private val projectSettings: OneloginAwsAssumeRoleSettings

  val properties: AwsAssumeRoleWithOneloginProperties

  init {
    projectSettings = OneloginAwsAssumeRoleSettings.getInstance(project)
    properties = AwsAssumeRoleWithOneloginProperties.from(projectSettings)

    title = "AWS Assume Role With OneLogin"
    setOKButtonText("Next")
    setSize(480, size.height)

    init()
  }

  override fun createCenterPanel(): JComponent = panel {
    blockRow {
      oneloginAccountDetailsRow()
    }
  }

  private fun Row.oneloginAccountDetailsRow() =
    titledRow("OneLogin Account Details") {
      row {
        label("Username")
        textField(properties::username)
      }
      row {

        label("Password")
        component(JBPasswordField())
          .withTextBinding(properties::password.toBinding())
      }
      row {
        label("App ID")
        textField(properties::appId)
      }
      row {
        label("Instance Sub-domain")
        textField(properties::instanceSubDomain)
      }
    }
}
