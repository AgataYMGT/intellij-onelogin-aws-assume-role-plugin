package net.millenary.intellij.plugin.onelogin_aws.ui.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import net.millenary.intellij.plugin.onelogin_aws.ui.AwsAssumeRoleWithOneloginDialog

/**
 * [AwsAssumeRoleWithOneloginDialog] を表示するトリガーアクション
 */
class AwsAssumeRoleWithOneloginAction : AnAction() {

  override fun actionPerformed(e: AnActionEvent) {
    val dialog = AwsAssumeRoleWithOneloginDialog(requireNotNull(e.project))
    dialog.show()
  }
}
