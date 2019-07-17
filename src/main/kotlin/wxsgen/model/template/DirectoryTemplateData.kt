package wxsgen.model.template


data class DirectoryTemplateData(
    val dirName: String,
    val dirId: String,
    val files: MutableList<FileTemplateData> = mutableListOf(),
    val directories: MutableList<DirectoryTemplateData> = mutableListOf(),
    val parent: DirectoryTemplateData?
)
