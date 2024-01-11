package web.points;

import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.Serializable;

//@ManagedBean
@Named("pointBean")
@ApplicationScoped
public class PointBean implements Serializable {
    private double x;
    private double y;
    private double r;

    public PointBean() {
        x = y = 0;
        r = 1;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public void selectR(double selectedR) {
        this.r = selectedR;
    }

    public boolean isInArea() {
        if (y >= 0 && y <= r && x >= 0 && x <= r / 2) return true;
        if (y < 0 && x >= 0 && x * x + y * y <= r * r / 4) return true;
        return y <= 0 && x <= 0 && y >= -0.5 * x - r / 2;
    }

}
