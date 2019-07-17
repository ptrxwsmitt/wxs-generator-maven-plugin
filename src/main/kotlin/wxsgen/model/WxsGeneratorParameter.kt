package wxsgen.model

/**
 * Parameters for the WXS Generator.
 *
 * @author Patrick Waldschmitt
 */
data class WxsGeneratorParameter(
    val productUid: String,
    val productName: String,
    val productVersion: String,
    val productComment: String,
    val manufacturer: String,
    val mainExecutable: String,
    val autostart: Boolean,
    val requestAdminPrivileges: Boolean,
    val dialogBackground: String = "",
    val bannerTop: String = "",
    val iconPath: String,
    val licenceRtfPath: String,
    val installerLocale: String,
    val rootPath: String,
    val targetFile: String,
    val runPostInstall: List<String> = emptyList(),
    val runPreUninstall: List<String> = emptyList()
)
