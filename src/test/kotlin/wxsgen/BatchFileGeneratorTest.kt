package wxsgen

import org.apache.commons.io.FileUtils
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import wxsgen.service.BatchFileGenerator
import wxsgen.util.readToString
import java.nio.file.Path
import java.nio.file.Paths

class BatchFileGeneratorTest {
    private val tempRoot = Paths.get("tmp/")
    private val wxsFile: Path = tempRoot.resolve("BatchFileGeneratorTest.wxs")
    private val generatedBatFile: Path = tempRoot.resolve("buildInstaller.bat")

    @Before
    fun setup() {
        FileUtils.deleteDirectory(tempRoot.toFile())
    }

    @After
    fun teardown() {
        FileUtils.deleteDirectory(tempRoot.toFile())
    }

    @Test
    fun generate(){
        val generator = BatchFileGenerator()
        generator.generateBuildBatch(wxsFile,"de-de", archX64 = false)
        val generatedBat = generatedBatFile.readToString()
        val expectedBat = fillTemplate("batch.mustache",null)
        assertThat(generatedBat).isEqualTo(expectedBat)
    }

    @Test
    fun generateX64(){
        val generator = BatchFileGenerator()
        generator.generateBuildBatch(wxsFile,"de-de", archX64 = true)
        val generatedBat = generatedBatFile.readToString()
        val expectedBat = fillTemplate("batch64.mustache",null)
        assertThat(generatedBat).isEqualTo(expectedBat)
    }


}
