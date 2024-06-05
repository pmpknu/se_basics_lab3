package web.beans

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PointBeanTest {

    @Test
    fun testSelectR() {
        val pointBean = PointBean()
        pointBean.selectR(2.5)
        assertEquals(2.5, pointBean.r)
    }

    @Test
    fun testIsInArea() {
        val pointBean = PointBean()

        pointBean.x = 0.5
        pointBean.y = 0.5
        pointBean.r = 2.0
        assertTrue(pointBean.isInArea)

        pointBean.x = 1.5
        pointBean.y = 0.5
        assertFalse(pointBean.isInArea)

        pointBean.x = 0.5
        pointBean.y = -0.5
        assertTrue(pointBean.isInArea)

        pointBean.x = 1.5
        pointBean.y = -1.5
        assertFalse(pointBean.isInArea)

    }
}
