package net.millenary.intellij.plugin.onelogin_aws.ui.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.onelogin.saml2.authn.SamlResponse
import com.onelogin.saml2.http.HttpRequest
import com.onelogin.saml2.settings.Saml2Settings
import com.onelogin.sdk.conn.Client
import com.onelogin.sdk.model.MFA
import com.onelogin.sdk.model.SAMLEndpointResponse
import net.millenary.intellij.plugin.onelogin_aws.SamlAssertionException
import net.millenary.intellij.plugin.onelogin_aws.setting.OneloginAwsAssumeRoleSettings
import net.millenary.intellij.plugin.onelogin_aws.ui.AwsAssumeRoleWithOneloginDialog
import net.millenary.intellij.plugin.onelogin_aws.ui.OneloginSamlAssertionMfaDialog
import net.millenary.intellij.plugin.onelogin_aws.ui.SelectAwsRoleDialog
import net.millenary.intellij.plugin.onelogin_aws.ui.data.AwsAssumeRoleWithOneloginProperties
import net.millenary.intellij.plugin.onelogin_aws.ui.data.OneloginMfaUserInputs
import net.millenary.intellij.plugin.onelogin_aws.ui.data.OneloginSdkProperties
import net.millenary.intellij.plugin.onelogin_aws.ui.data.SelectAwsRoleUserInputs
import java.nio.file.Paths

/**
 * [AwsAssumeRoleWithOneloginDialog] を表示するトリガーアクション
 */
class AwsAssumeRoleWithOneloginAction : AnAction() {

  companion object {

    const val SAML_AWS_ROLE_ATTRIBUTE_NAME = "https://aws.amazon.com/SAML/Attributes/Role"
  }

  override fun actionPerformed(e: AnActionEvent) {
    val project = requireNotNull(e.project)
    val projectSettings: OneloginAwsAssumeRoleSettings = OneloginAwsAssumeRoleSettings.getInstance(project)

    /* OneLogin のアカウント情報を入力する */
    val dialog = AwsAssumeRoleWithOneloginDialog(project)
    if (dialog.showAndGet()) {
      loginWithOnelogin(dialog.properties, projectSettings, project)
    }
  }

  private fun loginWithOnelogin(
    accountProperties: AwsAssumeRoleWithOneloginProperties,
    projectSettings: OneloginAwsAssumeRoleSettings,
    project: Project
  ) {
    val oneloginSdkPropertiesPath = Paths.get(projectSettings.oneloginSdkPropertiesPath)
    val oneloginSdkProperties = OneloginSdkProperties.from(oneloginSdkPropertiesPath)
    val oneloginClient = Client(
      oneloginSdkProperties.clientId,
      oneloginSdkProperties.clientSecret,
      oneloginSdkProperties.region,
      accountProperties.instanceSubDomain,
      true
    )

    /* OneLogin から SAML レスポンスを受け取る */
    oneloginClient.getAccessToken()
    var samlAssertionResponse = oneloginClient.getSAMLAssertion(
      accountProperties.username,
      accountProperties.password,
      accountProperties.appId,
      accountProperties.instanceSubDomain,
      oneloginSdkProperties.ipAddress
    )
    if (samlAssertionResponse.type != "success") {
      throw SamlAssertionException("OneLogin SAML Assertion failed with message: ${samlAssertionResponse.message}")
    }

    /* MFA が有効になっている場合に追加ダイアログを出す */
    val mfa = samlAssertionResponse.mfa
    val mfaUserInputs = OneloginMfaUserInputs()
    if (mfa != null) {
      samlAssertionResponse =
        samlAssertionWithMfa(mfa, project, mfaUserInputs, oneloginClient, accountProperties)
    }

    selectAwsRole(samlAssertionResponse, project)
  }

  private fun selectAwsRole(
    samlAssertionResponse: SAMLEndpointResponse,
    project: Project
  ) {
    val awsRoleAttributes = parseAwsRoleAttributes(samlAssertionResponse)
    val selectRoleDialog = SelectAwsRoleDialog(
      project = project,
      userInputs = SelectAwsRoleUserInputs(),
      samlAwsRoleAttributes = awsRoleAttributes
    )

    selectRoleDialog.show()
  }

  private fun parseAwsRoleAttributes(samlAssertionResponse: SAMLEndpointResponse): List<String> {
    val simulatedRequest = HttpRequest("http://example.com", "")
      .addParameter("SAMLResponse", samlAssertionResponse.samlResponse)
    val samlResponseObj = SamlResponse(Saml2Settings(), simulatedRequest)
    val samlResponseAttributes = samlResponseObj.attributes
    if (!samlResponseAttributes.containsKey(SAML_AWS_ROLE_ATTRIBUTE_NAME)) {
      throw SamlAssertionException("SAMLResponse from Identity Provider does not contain AWS Roles")
    }

    return samlResponseAttributes[SAML_AWS_ROLE_ATTRIBUTE_NAME].orEmpty()
  }

  private fun samlAssertionWithMfa(
    mfa: MFA,
    project: Project,
    mfaUserInputs: OneloginMfaUserInputs,
    oneloginClient: Client,
    accountProperties: AwsAssumeRoleWithOneloginProperties
  ): SAMLEndpointResponse {
    val mfaDevices = mfa.devices
    val mfaDialog = OneloginSamlAssertionMfaDialog(
      project = project,
      mfaDevices = mfaDevices,
      mfaUserInputs = mfaUserInputs
    )
    mfaDialog.show()

    return oneloginClient.getSAMLAssertionVerifying(
      accountProperties.appId,
      mfaUserInputs.deviceId,
      mfa.stateToken,
      mfaUserInputs.otpToken
    )
  }
}
