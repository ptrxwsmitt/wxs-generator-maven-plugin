package wxsgen

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import wxsgen.plugin.isVersionValid
import wxsgen.plugin.normalizeVersion

class VersionHelperTest {

    @Test
    fun isVersionValidTest() {
        assertThat(isVersionValid("1")).isTrue()
        assertThat(isVersionValid("1-SNAPSHOT")).isTrue()
        assertThat(isVersionValid("10")).isTrue()
        assertThat(isVersionValid("1a")).isFalse()
        assertThat(isVersionValid("a")).isFalse()
        assertThat(isVersionValid(".")).isFalse()
        assertThat(isVersionValid("1.")).isFalse()
        assertThat(isVersionValid("1.2")).isTrue()
        assertThat(isVersionValid("1.b")).isFalse()
        assertThat(isVersionValid("1.2b")).isFalse()
        assertThat(isVersionValid("1.b2")).isFalse()
        assertThat(isVersionValid("1.2-SNAPSHOT")).isTrue()
        assertThat(isVersionValid("1.2-SNAPSHOT-buildXYZ")).isTrue()
        assertThat(isVersionValid("11.22.33")).isTrue()
        assertThat(isVersionValid("11.22.33.44")).isTrue()
        assertThat(isVersionValid("11.22.33.44.55")).isTrue()
        assertThat(isVersionValid("11.22.33.44.55-SNAPSHOT")).isTrue()
        assertThat(isVersionValid("11.22.33.44.55.66")).isFalse()
        assertThat(isVersionValid("11.22.33.44.55.66-SNAPSHOT")).isFalse()
        assertThat(isVersionValid("2-SNAPSHOT")).isTrue()
        assertThat(isVersionValid("2-SNAPSHOT-buildxyz")).isTrue()
        assertThat(isVersionValid("2--SOMETHING")).isTrue()
        assertThat(isVersionValid("1.")).isFalse()
        assertThat(isVersionValid("10.")).isFalse()
        assertThat(isVersionValid("1.1.")).isFalse()
        assertThat(isVersionValid("a.b.c")).isFalse()
        assertThat(isVersionValid("1a.b.c")).isFalse()
        assertThat(isVersionValid("1a.2b.c")).isFalse()
        assertThat(isVersionValid("2.b2.c")).isFalse()
        assertThat(isVersionValid("2.2b")).isFalse()
    }

    @Test
    fun normalizeVersionTest() {
        assertThat(normalizeVersion("1")).isEqualTo("1")
        assertThat(normalizeVersion("1.2")).isEqualTo("1.2")
        assertThat(normalizeVersion("1.2.3")).isEqualTo("1.2.3")
        assertThat(normalizeVersion("1.2.3.4")).isEqualTo("1.2.3")
        assertThat(normalizeVersion("1.2.3.4.5")).isEqualTo("1.2.3")
        assertThat(normalizeVersion("1-SNAPSHOT")).isEqualTo("1")
        assertThat(normalizeVersion("1.2-SNAPSHOT")).isEqualTo("1.2")
        assertThat(normalizeVersion("1.2.3-SNAPSHOT")).isEqualTo("1.2.3")
        assertThat(normalizeVersion("1.2.3.4-SNAPSHOT")).isEqualTo("1.2.3")
        assertThat(normalizeVersion("1.2.3.4.5-SNAPSHOT")).isEqualTo("1.2.3")

        assertThat(normalizeVersion("1.2.3-SNAPSHOT-buildXYZ")).isEqualTo("1.2.3")
        assertThat(normalizeVersion("11.22.33.44.55-SNAPSHOT")).isEqualTo("11.22.33")
    }

}
