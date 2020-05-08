package wxsgen.plugin

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import wxsgen.model.WxsGeneratorParameter
import wxsgen.plugin.log.PluginLogImpl
import wxsgen.service.BatchFileGenerator
import wxsgen.service.WxsFileGenerator
import java.nio.file.Paths

/**
 * Maven Plugin Adapter for the WXS Generator.
 *
 * @author Patrick Waldschmitt
 */
@Mojo(name = "generate-wxs") //goal name
class WxsGeneratorMojo : AbstractMojo() {

    private val defaultUnknown = "UNKNOWN"

    /**
     * Root Path with all Files to be extracted by the installer on the target System (Absolute Path recommended).
     */
    @Parameter
    private var rootPath = ""

    @Parameter
    private var productUid = ""

    @Parameter(property = "project.name")
    private var productName = defaultUnknown

    @Parameter(property = "project.version")
    private var productVersion = defaultUnknown

    @Parameter(property = "project.description")
    private var productDescription = defaultUnknown

    @Parameter(property = "project.organization.name")
    private var manufacturer = defaultUnknown

    /**
     * Locale for the resulting installers language (e.g. en-us, de-de).
     */
    @Parameter
    private var installerLocale = "en-us"

    /**
     * Switch for providing installer batch.
     */
    @Parameter
    private var provideBuildBatch = "true"

    /**
     * Executable relative to Root Path
     */
    @Parameter
    private var mainExecutable = ""


    /**
     * Switch for enabling autostart for main executable.
     */
    @Parameter
    private var autostart = "true"

    /**
     * Icon Path (*.ico) relative to Root Path
     */
    @Parameter
    private var iconPath = ""

    /**
     * Licence Path (*.rtf) relative to Root Path
     */
    @Parameter
    private var licenceRtfPath = ""

    /**
     * Name of the wxs file.
     */
    @Parameter
    private var targetFile = "installer.wxs"

    /**
     * List of Paths to executables to be run after install (separated by ';').
     */
    @Parameter
    private var runPostInstall = ""

    /**
     * Path to the Background Image for the welcome and finish screens (493 x 312)
     */
    @Parameter
    private var dialogBackground = ""

    /**
     * Path to the Banner Image for banner on the top (493 x 58)
     */
    @Parameter
    private var bannerTop = ""

    /**
     * List of Paths to executables to be run before uninstall.
     */
    @Parameter
    private var runPreUninstall = ""


    /**
     * optional parameter to define the install directory manually
     */
    @Parameter
    private var installDir: String? = null


    /**
     * optional parameter to define the directory with shared libraries. The path is relative to the rootPath.<br>
     * The libraries in this directory will be only uninstalled when no other application is installed which also use the libraries.
     */
    @Parameter
    private var sharedLibraryDir: String? = null

    /**
     * Switch for enabling/disabling x64 architecture (default 'true').
     */
    @Parameter
    private var x64 = "true"

    @Parameter
    private var requestAdminPrivileges = "true"


    private val wixCreator =
        WxsFileGenerator(PluginLogImpl(log))
    private val batGenerator = BatchFileGenerator()

    override fun execute() {
        if (productUid.isBlank()) {
            throw MojoFailureException("productUid must be set")
        }
        if (rootPath.isBlank()) {
            throw MojoFailureException("rootPath must be set")
        }
        if (targetFile.isBlank()) {
            throw MojoFailureException("targetFile must not be empty")
        }
        if (!targetFile.endsWith(suffix = ".wxs", ignoreCase = true)) {
            throw MojoFailureException("targetFile [$targetFile] must end with \".wxs\"")
        }
        if (!isVersionValid(productVersion)) {
            throw MojoFailureException("version must match pattern '${VERSION_PATTERN}' ")
        }


        val runPostInstallList = if (runPostInstall.isBlank()) emptyList() else runPostInstall.split(";")
        val runPreUninstallList = if (runPreUninstall.isBlank()) emptyList() else runPreUninstall.split(";")

        //remove snapshot version
        val projectVersionCut = normalizeVersion(productVersion)

        val params = WxsGeneratorParameter(
            productUid = productUid,
            rootPath = rootPath,
            targetFile = targetFile,
            mainExecutable = mainExecutable,
            autostart = autostart.toBoolean(),
            requestAdminPrivileges = requestAdminPrivileges.toBoolean(),
            dialogBackground = dialogBackground,
            bannerTop = bannerTop,
            productName = productName,
            productVersion = projectVersionCut,
            manufacturer = manufacturer,
            iconPath = iconPath,
            licenceRtfPath = licenceRtfPath,
            installerLocale = installerLocale,
            productComment = productDescription,
            runPostInstall = runPostInstallList,
            runPreUninstall = runPreUninstallList,
            archX64 = x64.toBoolean(),
            installDir = installDir,
            sharedLibraryDir = sharedLibraryDir
        )

        log.debug("wxp generator parameters:\n$params")

        wixCreator.generate(params)

        if (provideBuildBatch.toBoolean()) {
            batGenerator.generateBuildBatch(Paths.get(targetFile), installerLocale, x64.toBoolean())
        }

    }

}
