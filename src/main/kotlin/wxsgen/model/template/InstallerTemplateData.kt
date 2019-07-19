package wxsgen.model.template

data class InstallerTemplateData(
    val productName: String,
    val productVersion: String,
    val productComment: String,
    val manufacturer: String,
    val mainExecutable: String,
    val autostart: Boolean,
    val requestAdminPrivileges: Boolean,
    val dialogBackground: String,
    val bannerTop: String,
    val iconPath: String,
    val licenceRtfPath: String,
    val idUUID: String,
    val upgradeCodeUUID: String,
    val components: MutableList<String> = mutableListOf(),
    val shortcuts: MutableList<ShortcutData> = mutableListOf(),
    val rootDir: DirectoryTemplateData,
    val runPostInstall: List<ActionTemplateData> = emptyList(),
    val runPreUninstall: List<ActionTemplateData> = emptyList()
)


