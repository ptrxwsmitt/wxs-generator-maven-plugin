package wxsgen

import wxsgen.model.WxsGeneratorParameter
import org.apache.commons.io.FileUtils
import java.io.FileReader
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


class WxsFileCreatorTest {

    private var root: Path? = null
    private val testTargetFile: Path = Paths.get("target/unittest/temp/test.wxs")

    @BeforeTest
    fun setup() {
        root = createSampleFiles()
    }

    @Test
    fun createWXSTest() {
        val testData = WxsGeneratorParameter(
            productUid = "myprodiuct.id",
            rootPath = root.toString(),
            targetFile = testTargetFile.toString(),
            productVersion = "0.5.1",
            mainExecutable = "run.bat",
            productComment = "test comment",
            iconPath = "",
            licenceRtfPath = "",
            installerLocale = "de-de",
            manufacturer = "test orga name",
            productName = "test product 1.5",
            requestAdminPrivileges = true,
            autostart = true
        )
        WxsFileGenerator(TestLogImpl()).generate(testData)
        val fr = FileReader(testTargetFile.toFile())
        println(fr.toString())
    }

    @AfterTest
    fun teardown() {
        FileUtils.deleteDirectory(root!!.toFile())
        FileUtils.deleteDirectory(testTargetFile.parent.toFile())
    }
}

