package wxsgen.model.template


data class FileTemplateData(
    val fileName: String,
    val fileId: String,
    val fileSource: String,
    val mainExecutable: Boolean = false,
    val componentId: String,
    val componentUUID: String,
    val info: InstallerTemplateData,
    val parent: DirectoryTemplateData
)
