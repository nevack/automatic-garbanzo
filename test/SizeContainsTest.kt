import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SizeContainsTest {
    private val size = 10 to 10
    @Test
    fun contains() {
        assertTrue((0 to 0) in size)
        assertTrue((9 to 9) in size)
        assertTrue((5 to 5) in size)

        assertFalse((-1 to 0) in size)
        assertFalse((0 to -1) in size)
        assertFalse((0 to 10) in size)
        assertFalse((10 to 0) in size)
        assertFalse((-1 to -1) in size)
        assertFalse((10 to 10) in size)
    }
}
