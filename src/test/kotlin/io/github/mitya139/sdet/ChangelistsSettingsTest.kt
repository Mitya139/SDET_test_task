package io.github.mitya139.sdet

import com.intellij.driver.sdk.ui.components.common.IdeaFrameUI
import com.intellij.driver.sdk.ui.components.common.ideFrame
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
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.awt.event.KeyEvent
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

private const val PYCHARM_BUILD = "262.4852.46"
private const val TEST_PROJECT_BRANCH = "main"
private const val TEST_PROJECT_COMMIT = "55cfb0e6021d86e957025bc40d8de3b0cb686e99"
private const val TEST_PROJECT_REPO = "Mitya139/test_task_word2vec.git"
private const val VERSION_CONTROL_SECTION = "Version Control"
private const val CHANGELISTS_SECTION = "Changelists"
private const val CREATE_CHANGELISTS_AUTOMATICALLY = "Create changelists automatically"

class ChangelistsSettingsTest {

    private val pyCharm = IdeInfo(
        productCode = "PY",
        platformPrefix = "Python",
        executableFileName = "pycharm",
        fullName = "PyCharm"
    )

    @Test
    @DisplayName("Enable 'Create changelists automatically' and confirm with OK")
    fun shouldEnableCreateChangelistsAutomatically() {
        val testContext = Starter
            .newContext(
                CurrentTestMethod.hyphenateWithClass(),
                TestCase(
                    pyCharm,
                    GitHubProject.fromGithub(
                        branchName = TEST_PROJECT_BRANCH,
                        commitHash = TEST_PROJECT_COMMIT,
                        repoRelativeUrl = TEST_PROJECT_REPO
                    )
                ).withBuildNumber(PYCHARM_BUILD)
            )
            .prepareProjectCleanImport()

        testContext.runIdeWithDriver().useDriverAndCloseIde {
            ideFrame {
                waitForIndicatorsAndEnsureFocused(3.minutes)

                openSettingsUsingShortcut()

                settingsDialog {
                    selectSettingsSection(
                        VERSION_CONTROL_SECTION,
                        CHANGELISTS_SECTION,
                        fullMatch = true
                    )

                    val createChangelistsAutomaticallyCheckbox =
                        content().checkBoxWithName(CREATE_CHANGELISTS_AUTOMATICALLY)

                    if (!createChangelistsAutomaticallyCheckbox.isSelected()) {
                        createChangelistsAutomaticallyCheckbox.click()
                    }
                    createChangelistsAutomaticallyCheckbox.waitSelected(true)

                    assertTrue(
                        createChangelistsAutomaticallyCheckbox.isSelected(),
                        "Checkbox '$CREATE_CHANGELISTS_AUTOMATICALLY' should be selected after click"
                    )

                    button("OK").click()
                    waitNotFound()
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
