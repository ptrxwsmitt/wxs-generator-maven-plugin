package wxsgen

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.apache.commons.io.FileUtils
import org.assertj.core.api.Java6Assertions.assertThat
import wxsgen.common.MustacheUtil
import wxsgen.model.WxsGeneratorParameter
import wxsgen.service.UuidGenerator
import wxsgen.service.WxsFileGenerator
import java.io.FileReader
import java.io.StringWriter
import java.lang.StringBuilder
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.math.min
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class WxsFileCreatorTest {

    val testLogger = TestLogImpl()
    val uuidGeneratorService: UuidGenerator = mock()

    private var root: Path? = null
    private val testTargetFile: Path = Paths.get("tmp/test.wxs")
    private val expectedFile: Path = Paths.get("src/test/resources/expected.wxs")

    class ExpectedTemplate(val testDirectory: String)

    @BeforeTest
    fun setup() {
        root = createSampleFiles()
    }


    @Test
    fun createWXSTest() {

        // given
        whenever(uuidGeneratorService.generateUuid()).thenReturn("new-UUID")
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

        // when
        val wxsGenerator = WxsFileGenerator(testLogger, uuidGeneratorService)
        wxsGenerator.generate(testData)

        // then
        val generatedWxs = readGeneratedWxs()
        val expectedWxs = buildExpectedWxs()

        assertThat(generatedWxs).isEqualTo(expectedWxs)
    }


    private fun buildExpectedWxs(): String {
        val mustacheTemplate = MustacheUtil.prepareTemplateFromResource("expected.wxs.mustache")
        val writer = StringWriter()
        writer.use {
            mustacheTemplate.execute(writer, ExpectedTemplate(root!!.toAbsolutePath().toString()))
        }
        return writer.toString()
    }

    private fun readGeneratedWxs(): String {
        val generatedWxsReader = FileReader(testTargetFile.toFile())
        var content: String = ""
        generatedWxsReader.use {
            content = generatedWxsReader.readText()
        }
        return content
    }

    @AfterTest
    fun teardown() {
        FileUtils.deleteDirectory(root!!.toFile())
        FileUtils.deleteDirectory(testTargetFile.parent.toFile())
    }
}

