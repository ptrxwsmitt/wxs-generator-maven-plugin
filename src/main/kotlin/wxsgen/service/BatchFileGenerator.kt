package wxsgen.service

import wxsgen.common.MustacheUtil
import wxsgen.plugin.model.BuildInstallerBatchTemplateData
import java.nio.file.Files
import java.nio.file.Path

class BatchFileGenerator {

    fun generateBuildBatch(wxsFile: Path, locale: String, archX64: Boolean) {

        val targetDirectory = wxsFile.parent

        val fileName = removeFileEnding(wxsFile.fileName.toString())
        val batchTemplateData = BuildInstallerBatchTemplateData(fileName, locale, archX64)

        Files.createDirectories(targetDirectory)
        val mustacheTemplate = MustacheUtil.prepareTemplateFromResource("buildInstaller.bat.mustache")

        val batchPath = targetDirectory.resolve("buildInstaller.bat")
        val batchWriter = Files.newBufferedWriter(batchPath)
        batchWriter.use {
            mustacheTemplate?.execute(it, batchTemplateData)?.flush()
        }

    }


    private fun removeFileEnding(fileName: String): String {
        val lastDot = fileName.lastIndexOf('.')
        return if (lastDot > 0) {
            fileName.substring(0, lastDot)
        } else {
            fileName
        }
    }
}
