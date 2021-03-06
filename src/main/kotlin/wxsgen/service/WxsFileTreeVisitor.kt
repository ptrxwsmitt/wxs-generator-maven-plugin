package wxsgen.service

import wxsgen.model.template.DirectoryTemplateData
import wxsgen.model.template.FileTemplateData
import wxsgen.model.template.InstallerTemplateData
import wxsgen.model.template.ShortcutData
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.Objects.isNull


/**
 * Visitor that generates the Template Data based on the Files in the File Tree.
 *
 * @author Patrick Waldschmitt
 */
class WxsFileTreeVisitor(private val templateData: InstallerTemplateData, private val uuidGenerator: UuidGenerator = JdkUuidGenerator()) : SimpleFileVisitor<Path>() {

    private val wxsExcludeDirName = ".wxs-exclude"

    // visitor working variables:
    private var currentDirectory = templateData.rootDir
    private var directoryIndex = 0
    private var componentAndFileIndex = 0
    private var directoryLevel = 0

    @Throws(IOException::class)
    override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
        val directoryName = dir.fileName.toString()
        //exclude root dir
        if (directoryLevel > 0 && directoryName != wxsExcludeDirName) {
            directoryIndex++

            //create a directory and use it as current directory
            val parentDir = currentDirectory
            currentDirectory =
                DirectoryTemplateData(
                    dirName = directoryName,
                    dirId = "directory_$directoryIndex",
                    directories = mutableListOf(),
                    files = mutableListOf(),
                    parent = parentDir
                )
            parentDir.directories.add(currentDirectory)
        }
        directoryLevel++
        return super.preVisitDirectory(dir, attrs)
    }

    @Throws(IOException::class)
    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
        //create file entry and add it to the current directory
        componentAndFileIndex++

        val isMainExecutable = templateData.mainExecutablePath.toString() == file.toString()

        val sharedFile = if (file.startsWith(templateData.sharedLibraryPath)) {
            "yes"
        } else {
            "no"
        }

        val currentFile = FileTemplateData(
            fileId = "file_$componentAndFileIndex",
            fileName = file.fileName.toString(),
            fileSource = file.toString(),
            componentId = "component_$componentAndFileIndex",
            componentUUID = uuidGenerator.generateUuid(),
            componentShared = sharedFile,
            mainExecutable = isMainExecutable,
            info = templateData,
            parent = currentDirectory
        )
        currentDirectory.files.add(currentFile)
        templateData.components.add(currentFile.componentId)

        if (isMainExecutable) {

            val isBatch = currentFile.fileName.endsWith(".bat")
            val shortcut = ShortcutData(
                shortcutId = "shortcut_$componentAndFileIndex",
                componentId = "component_shortcut_$componentAndFileIndex",
                componentUUID = uuidGenerator.generateUuid(),
                refFileId = currentFile.fileId,
                refFileName = currentFile.fileName,
                refDirectoryId = currentDirectory.dirId,
                isBatch = isBatch
            )

            templateData.desktopShortcuts.add(shortcut)
            templateData.components.add(shortcut.componentId)

            val startMenuShortcut = ShortcutData(
                shortcutId = "start_menu_shortcut_$componentAndFileIndex",
                componentId = "component_start_menu_shortcut_$componentAndFileIndex",
                componentUUID = uuidGenerator.generateUuid(),
                refFileId = currentFile.fileId,
                refFileName = currentFile.fileName,
                refDirectoryId = currentDirectory.dirId,
                isBatch = isBatch
            )
            templateData.startMenuShortcuts.add(startMenuShortcut)
            templateData.components.add(startMenuShortcut.componentId)
        }
        return super.visitFile(file, attrs)
    }

    @Throws(IOException::class)
    override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
        //exclude root dir
        if (directoryLevel > 0 && currentDirectory.parent != null) {
            //use parent dir as current dir
            currentDirectory = currentDirectory.parent!!
        }
        directoryLevel--
        return super.postVisitDirectory(dir, exc)
    }
}
