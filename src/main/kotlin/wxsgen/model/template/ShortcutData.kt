package wxsgen.model.template

data class ShortcutData(
    val componentId: String,
    val componentUUID: String,
    val shortcutId: String,
    val refFileId: String,
    val refDirectoryId: String,
    val refFileName: String,
    val isBatch: Boolean = false
)