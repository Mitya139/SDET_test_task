import com.intellij.driver.sdk.ui.components.common.ideFrame
import com.intellij.driver.sdk.ui.components.common.IdeaFrameUI
import com.intellij.driver.sdk.ui.components.elements.button
import com.intellij.driver.sdk.ui.components.elements.checkBoxWithName
import com.intellij.driver.sdk.ui.components.elements.waitSelected
import com.intellij.driver.sdk.ui.components.settings.SettingsDialogUiComponent
import com.intellij.driver.sdk.ui.components.settings.settingsDialog
import com.intellij.driver.sdk.ui.should
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import com.intellij.ide.starter.junit5.hyphenateWithClass
import com.intellij.ide.starter.models.IdeInfo
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.project.GitHubProject
import com.intellij.ide.starter.runner.CurrentTestMethod
import com.intellij.ide.starter.runner.Starter
import com.intellij.openapi.util.SystemInfo
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.awt.event.KeyEvent
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class ChangelistsSettingsTest {

    private val createChangelistsAutomaticallyText = "Create changelists automatically"

    private val pyCharm = IdeInfo(
        productCode = "PY",
        platformPrefix = "Python",
        executableFileName = "pycharm",
        fullName = "PyCharm"
    )

    @Test
    fun shouldEnableCreateChangelistsAutomatically() {
        val testContext = Starter
            .newContext(
                CurrentTestMethod.hyphenateWithClass(),
                TestCase(
                    pyCharm,
                    GitHubProject.fromGithub(
                        branchName = "main",
                        commitHash = "55cfb0e6021d86e957025bc40d8de3b0cb686e99",
                        repoRelativeUrl = "Mitya139/test_task_word2vec.git"
                    )
                ).withBuildNumber("262.4852.46")
            )
            .prepareProjectCleanImport()

        testContext.runIdeWithDriver().useDriverAndCloseIde {
            ideFrame {
                waitForIndicatorsAndEnsureFocused(3.minutes)

                openSettingsUsingShortcut()

                settingsDialog {
                    selectSettingsSection(
                        "Version Control",
                        "Changelists",
                        fullMatch = true
                    )

                    val createChangelistsAutomaticallyCheckbox =
                        content().checkBoxWithName(createChangelistsAutomaticallyText)

                    createChangelistsAutomaticallyCheckbox.check()
                    createChangelistsAutomaticallyCheckbox.waitSelected(true)

                    assertTrue(
                        createChangelistsAutomaticallyCheckbox.isSelected(),
                        "Checkbox '$createChangelistsAutomaticallyText' should be selected"
                    )

                    button("OK").click()
                }
            }
        }
    }
}

private fun IdeaFrameUI.openSettingsUsingShortcut() {
    keyboard {
        if (SystemInfo.isMac) {
            hotKey(KeyEvent.VK_META, KeyEvent.VK_COMMA)
        } else {
            hotKey(KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_S)
        }
    }
}

private fun SettingsDialogUiComponent.selectSettingsSection(
    vararg path: String,
    fullMatch: Boolean = true
) {
    settingsTree.should(message = "Settings tree is empty", timeout = 5.seconds) {
        collectExpandedPaths().isNotEmpty()
    }

    settingsTree.clickPath(*path, fullMatch = fullMatch)
}
