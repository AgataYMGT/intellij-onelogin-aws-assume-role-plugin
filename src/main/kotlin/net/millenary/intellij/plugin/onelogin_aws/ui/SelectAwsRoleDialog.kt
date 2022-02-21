package net.millenary.intellij.plugin.onelogin_aws.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.Row
import com.intellij.ui.layout.panel
import net.millenary.intellij.plugin.onelogin_aws.ui.data.SamlAvailableAwsRole
import net.millenary.intellij.plugin.onelogin_aws.ui.data.SelectAwsRoleUserInputs
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent

class SelectAwsRoleDialog(

  project: Project,

  val userInputs: SelectAwsRoleUserInputs,

  samlAwsRoleAttributes: List<String>
) : DialogWrapper(project, true) {

  private val rolesByFrontString = samlAwsRoleAttributes.map { roleAttribute ->
    val colonSeparated = roleAttribute.split(",")[0].split(":")
    val accountId = colonSeparated[4]
    val roleName = colonSeparated[5].replace("role/", "")

    SamlAvailableAwsRole(accountId, roleName, roleAttribute)
  }.associateBy { "${it.accountId}: ${it.roleName}" }

  private var selectedRole = rolesByFrontString.keys.first()
    set(value) {
      val colonSeparated = rolesByFrontString[value]!!.originalAttribute.split(",")
      userInputs.roleArn = colonSeparated[0]
      userInputs.principalArn = colonSeparated[1]
      field = value
    }

  init {
    title = "Select Available AWS Role"
    setOKButtonText("Next")
    setSize(480, 200)

    init()
  }

  override fun createCenterPanel(): JComponent = panel {
    blockRow {
      row {
        selectRoleComboBox()
      }
    }
  }

  private fun Row.selectRoleComboBox(): Row {
    val comboBoxModel = DefaultComboBoxModel(rolesByFrontString.keys.toTypedArray())

    return row {
      label("Select Available AWS Role")
      comboBox(comboBoxModel, this@SelectAwsRoleDialog::selectedRole)
    }
  }
}
