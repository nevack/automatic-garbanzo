import kotlin.test.Test
import kotlin.test.assertEquals

class DirectionTest {
    @Test
    fun xor() {
        assertEquals(Direction.DOWN, Direction.RIGHT xor 0b01)
        assertEquals(Direction.RIGHT, Direction.DOWN xor 0b01)
        assertEquals(Direction.UP, Direction.LEFT xor 0b01)
        assertEquals(Direction.LEFT, Direction.UP xor 0b01)
    }

    @Test
    fun plus() {
        assertEquals(Direction.DOWN, Direction.RIGHT + 1)
        assertEquals(Direction.LEFT, Direction.DOWN + 1)
        assertEquals(Direction.UP, Direction.LEFT + 1)
        assertEquals(Direction.RIGHT, Direction.UP + 1)

        assertEquals(Direction.RIGHT, Direction.RIGHT + 4)
        assertEquals(Direction.DOWN, Direction.DOWN + 4)
        assertEquals(Direction.LEFT, Direction.LEFT + 4)
        assertEquals(Direction.UP, Direction.UP + 4)
    }

    @Test
    fun minus() {
        assertEquals(Direction.UP, Direction.RIGHT - 1)
        assertEquals(Direction.RIGHT, Direction.DOWN - 1)
        assertEquals(Direction.DOWN, Direction.LEFT - 1)
        assertEquals(Direction.LEFT, Direction.UP - 1)

        assertEquals(Direction.RIGHT, Direction.RIGHT - 4)
        assertEquals(Direction.DOWN, Direction.DOWN - 4)
        assertEquals(Direction.LEFT, Direction.LEFT - 4)
        assertEquals(Direction.UP, Direction.UP - 4)
    }

    @Test
    fun not() {
        assertEquals(Direction.LEFT, !Direction.RIGHT)
        assertEquals(Direction.UP, !Direction.DOWN)
        assertEquals(Direction.RIGHT, !Direction.LEFT)
        assertEquals(Direction.DOWN, !Direction.UP)
    }
}