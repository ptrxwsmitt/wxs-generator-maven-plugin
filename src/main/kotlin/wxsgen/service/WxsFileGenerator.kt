package wxsgen.service

import wxsgen.common.MustacheUtil
import wxsgen.log.LogFacade
import wxsgen.model.WxsGeneratorParameter
import wxsgen.model.template.ActionTemplateData
import wxsgen.model.template.DirectoryTemplateData
import wxsgen.model.template.InstallerTemplateData
import java.io.FileWriter
import java.lang.String.format
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Generator for a WXS File based on mustache templates.
 *
 * @author Patrick Waldschmitt
 */
class WxsFileGenerator(private val log: LogFacade, private val uuidGenerator: UuidGenerator = JdkUuidGenerator()) {

    /**
     * Generates the WXS File.
     */
    fun generate(param: WxsGeneratorParameter) {
        val rootPathLocal = Paths.get(param.rootPath)
        val executablePath = Paths.get(param.rootPath, param.mainExecutable)
        val targetFilePath = Paths.get(param.targetFile)
        log.info(format("Generating WXS File [%s]", targetFilePath))

        Files.createDirectories(targetFilePath.parent)
        Files.deleteIfExists(targetFilePath)

        val upgradeUUID = uuidGenerator.generateUuid()
        val postInstallActionList = createActions(param.runPostInstall)
        val preUninstallActionList = createActions(param.runPreUninstall)
        val mainExecutable = executablePath.toString()

        val dialogBackgroundChecked = Paths.get(param.dialogBackground).toString()
        val bannerTopChecked = Paths.get(param.bannerTop).toString()

        val rootDir = DirectoryTemplateData(
            dirName = param.productName,
            dirId = "INSTALLDIR",
            parent = null
        )

        val templateData = InstallerTemplateData(
            idUUID = param.productUid,
            productName = param.productName,
            productVersion = param.productVersion,
            productComment = param.productComment,
            manufacturer = param.manufacturer,
            mainExecutable = mainExecutable,
            autostart = param.autostart,
            dialogBackground = dialogBackgroundChecked,
            bannerTop = bannerTopChecked,
            requestAdminPrivileges = param.requestAdminPrivileges,
            runPostInstall = postInstallActionList,
            runPreUninstall = preUninstallActionList,
            iconPath = param.iconPath,
            licenceRtfPath = param.licenceRtfPath,
            upgradeCodeUUID = upgradeUUID,
            rootDir = rootDir,
            archX64 = param.archX64
        )

        val fileVisitor = WxsFileTreeVisitor(templateData, uuidGenerator)
        Files.walkFileTree(rootPathLocal, fileVisitor)

        val mustacheTemplate = MustacheUtil.prepareTemplateFromResource("installer.mustache")

        //create target file AFTER walking the file tree, in order to keep it out of the installed data
        Files.createFile(targetFilePath)
        val writer = Files.newBufferedWriter(targetFilePath)
        writer.use {
            mustacheTemplate?.execute(writer, templateData)
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
