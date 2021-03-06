package wxsgen.model.template

import java.nio.file.Path

data class InstallerTemplateData(
    val productName: String,
    val productVersion: String,
    val productComment: String,
    val manufacturer: String,
    val mainExecutablePath: Path,
    val autostart: Boolean,
    val requestAdminPrivileges: Boolean,
    val dialogBackground: String,
    val bannerTop: String,
    val iconPath: String,
    val licenceRtfPath: String,
    val sharedLibraryPath: Path,
    val idUUID: String,
    val upgradeCodeUUID: String,
    val components: MutableList<String> = mutableListOf(),
    val desktopShortcuts: MutableList<ShortcutData> = mutableListOf(),
    val startMenuShortcuts: MutableList<ShortcutData> = mutableListOf(),
    val rootDir: DirectoryTemplateData,
    val runPostInstall: List<ActionTemplateData> = emptyList(),
    val runPreUninstall: List<ActionTemplateData> = emptyList(),
    val messageDowngradeError: String,
    val archX64: Boolean = true
)
