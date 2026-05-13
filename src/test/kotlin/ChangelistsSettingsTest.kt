import com.intellij.driver.sdk.ui.components.common.ideFrame
import com.intellij.driver.sdk.ui.components.elements.button
import com.intellij.driver.sdk.ui.components.elements.checkBox
import com.intellij.driver.sdk.ui.components.settings.settingsDialog
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import com.intellij.ide.starter.models.IdeInfo
import com.intellij.ide.starter.junit5.hyphenateWithClass
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.project.GitHubProject
import com.intellij.ide.starter.runner.CurrentTestMethod
import com.intellij.ide.starter.runner.Starter
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.minutes

class ChangelistsSettingsTest {

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
                        repoRelativeUrl = "Mitya139/test_task_word2vec.git"
                    )
                )
            )
            .prepareProjectCleanImport()

        testContext.runIdeWithDriver().useDriverAndCloseIde {
            ideFrame {
                waitForIndicators(3.minutes)

                openSettingsDialog()

                settingsDialog {
                    openTreeSettingsSection(
                        "Version Control",
                        "Changelists",
                        fullMatch = false
                    )

                    val createChangelistsAutomaticallyCheckbox = content().checkBox(
                        "//div[@accessiblename='Create changelists automatically' " +
                            "or @visible_text='Create changelists automatically']"
                    )

                    createChangelistsAutomaticallyCheckbox.check()

                    assertTrue(
                        createChangelistsAutomaticallyCheckbox.isSelected(),
                        "Checkbox 'Create changelists automatically' should be selected"
                    )

                    button("OK").click()
                }
            }
        }
    }
}
