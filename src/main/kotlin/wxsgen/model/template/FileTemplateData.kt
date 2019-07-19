package wxsgen.model.template


data class FileTemplateData(
    val fileName: String,
    val fileId: String,
    val fileSource: String,
    val componentId: String,
    val componentUUID: String,
    val mainExecutable: Boolean = false,
    val info: InstallerTemplateData,
    val parent: DirectoryTemplateData
)
