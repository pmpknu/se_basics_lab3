package web.beans

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Named
import java.io.Serializable

//@ManagedBean
@Named("pointBean")
@ApplicationScoped
class PointBean : Serializable {
    var x: Double = 0.0
    var y: Double = 0.0
    var r: Double = 1.0

    fun selectR(selectedR: Double) {
        r = selectedR
    }

    val isInArea: Boolean
        get() = isInsideRectangle || isInsideCircle || isInsideTriangle

    private val isInsideRectangle: Boolean
        get() = y in 0.0..r && x in 0.0..r / 2

    private val isInsideCircle: Boolean
        get() = y < 0 && x >= 0 && (x * x + y * y <= r * r / 4)

    private val isInsideTriangle: Boolean
        get() = y <= 0 && x <= 0 && y >= -0.5 * x - r / 2
}
