package wxsgen.service

import org.codehaus.plexus.util.StringUtils.isBlank
import wxsgen.common.MustacheUtil
import wxsgen.log.LogFacade
import wxsgen.model.WxsGeneratorParameter
import wxsgen.model.template.ActionTemplateData
import wxsgen.model.template.DirectoryTemplateData
import wxsgen.model.template.InstallerTemplateData
import java.lang.String.format
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * Generator for a WXS File based on mustache templates.
 *
 * @author Patrick Waldschmitt
 */
class WxsFileGenerator(private val log: LogFacade, private val uuidGenerator: UuidGenerator = JdkUuidGenerator()) {

    fun loadResourceBundle(languageTag: String): ResourceBundle {
        Locale.setDefault(Locale.ENGLISH)
        val locale = Locale.forLanguageTag(languageTag)
        return ResourceBundle.getBundle("bundles/messages", locale)
    }

    fun readMessageDowngradeError(resourceBundle: ResourceBundle, productName: String): String {
        return format(resourceBundle.getString("messageDowngradeError"), productName)
    }

    /**
     * Generates the WXS File.
     */
    fun generate(param: WxsGeneratorParameter) {
        val rootPathLocal = Paths.get(param.rootPath)

        val mainExecutablePath = if (!isBlank(param.mainExecutable)) {
            Paths.get(param.rootPath, param.mainExecutable)
        } else {
            Paths.get("")
        }

        val sharedLibraryPath = if (!isBlank(param.sharedLibraryDir)) {
            Paths.get(param.rootPath, param.sharedLibraryDir)
        } else {
            Paths.get("")
        }
        log.info(format("Shared library path is [%s]", sharedLibraryPath))

        val targetFilePath = Paths.get(param.targetFile)
        log.info(format("Generating WXS File [%s]", targetFilePath))

        Files.createDirectories(targetFilePath.parent)
        Files.deleteIfExists(targetFilePath)

        val postInstallActionList = createActions(param.runPostInstall)
        val preUninstallActionList = createActions(param.runPreUninstall)

        val dialogBackgroundChecked = Paths.get(param.dialogBackground).toString()
        val bannerTopChecked = Paths.get(param.bannerTop).toString()

        val rootDirName:String
        if (!isBlank(param.installDir)) {
            rootDirName = param.installDir!!
        } else {
            rootDirName = param.productName
        }

        val rootDir = DirectoryTemplateData(
            dirName = rootDirName,
            dirId = "INSTALLDIR",
            parent = null
        )

        val resourceBundle = loadResourceBundle(param.installerLocale)
        val messageDowngradeError = readMessageDowngradeError(resourceBundle, param.productName)

        // according to this stackoverflow entry
        // https://stackoverflow.com/questions/500703/how-to-get-wix-to-update-a-previously-installed-version-of-a-program
        // for an upgrade the product id should be an ever changing ID and upgradeId should be stable
        val templateData = InstallerTemplateData(
            idUUID = uuidGenerator.generateUuid(),
            productName = param.productName,
            productVersion = param.productVersion,
            productComment = param.productComment,
            manufacturer = param.manufacturer,
            mainExecutablePath = mainExecutablePath,
            autostart = param.autostart,
            dialogBackground = dialogBackgroundChecked,
            bannerTop = bannerTopChecked,
            requestAdminPrivileges = param.requestAdminPrivileges,
            runPostInstall = postInstallActionList,
            runPreUninstall = preUninstallActionList,
            iconPath = param.iconPath,
            licenceRtfPath = param.licenceRtfPath,
            sharedLibraryPath = sharedLibraryPath,
            upgradeCodeUUID = param.productUid,
            rootDir = rootDir,
            archX64 = param.archX64,
            messageDowngradeError = messageDowngradeError
        )

        val fileVisitor = WxsFileTreeVisitor(templateData, uuidGenerator)
        Files.walkFileTree(rootPathLocal, fileVisitor)

        val mustacheTemplate = MustacheUtil.prepareTemplateFromResource("installer.mustache")

        //create target file AFTER walking the file tree, in order to keep it out of the installed data
        Files.createFile(targetFilePath)
        val writer = Files.newBufferedWriter(targetFilePath)
        writer.use {
            mustacheTemplate?.execute(it, templateData)
        }

    }

    /**
     * Creates Template Data for the configured Post Install Actions.
     */
    private fun createActions(runPostInstall: List<String>) = runPostInstall.mapIndexed { index, exePathString ->
        val exePath = Paths.get(exePathString)
        ActionTemplateData(
            actionId = "action_$index",
            executable = exePath.toString()
        )
    }

}
