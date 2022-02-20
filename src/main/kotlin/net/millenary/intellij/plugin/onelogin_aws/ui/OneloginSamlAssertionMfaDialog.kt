package net.millenary.intellij.plugin.onelogin_aws.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.Row
import com.intellij.ui.layout.panel
import com.jetbrains.rd.util.first
import com.onelogin.sdk.model.Device
import net.millenary.intellij.plugin.onelogin_aws.ui.data.OneloginMfaUserInputs
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent

class OneloginSamlAssertionMfaDialog(

  project: Project,

  private val mfaDevices: List<Device>,

  val mfaUserInputs: OneloginMfaUserInputs,
) : DialogWrapper(project, true) {

  private val mfaDeviceIdsByType = mfaDevices.associate { device ->
    "${device.type}: ${device.id}" to device.id
  }

  private var selectedDeviceType: String = mfaDeviceIdsByType.first().key
    set(value) {
      mfaUserInputs.deviceId = mfaDeviceIdsByType[value]!!.toString()
      field = value
    }

  init {
    title = "OneLogin SAML Assertion: MFA Required"
    setOKButtonText("Next")
    setSize(480, 200)

    init()
  }

  override fun createCenterPanel(): JComponent = panel {
    blockRow {
      row {
        label("MFA is required, authenticate using one of these devices")
      }
      deviceSelectionComboBox(mfaDevices)
      oneTimeToken(mfaUserInputs)
    }
  }

  private fun Row.deviceSelectionComboBox(mfaDevices: List<Device>): Row {
    val mfaDeviceByType = mfaDevices.associate { device -> device.type to device.id }
    val comboBoxModel = DefaultComboBoxModel(mfaDeviceByType.keys.toTypedArray())

    return row {
      label("Device")
      comboBox(comboBoxModel, this@OneloginSamlAssertionMfaDialog::selectedDeviceType)
    }
  }

  private fun Row.oneTimeToken(mfaUserInputs: OneloginMfaUserInputs) = row {
    label("Enter OTP Token:")
    textField(mfaUserInputs::otpToken)
  }
}
