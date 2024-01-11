package web.database;

import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import web.models.PointModel;
import web.points.PointBean;

import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//@ManagedBean
@Named("dataBean")
@ApplicationScoped
public class DataBean {
    private Connection conn;
    private List<PointModel> points;


    public DataBean() {
        try {
            Class.forName("org.postgresql.Driver");
            Properties properties = new Properties();

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db.cfg");
            properties.load(inputStream);
            inputStream.close();

            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String url = properties.getProperty("URL");

            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }


    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public void addToTable(PointBean pointBean) {
        try(PreparedStatement statement = conn.prepareStatement("INSERT INTO webpoints(x, y, r, inArea) VALUES (?, ?, ?, ?);")) {
            statement.setDouble(1, pointBean.getX());
            statement.setDouble(2, pointBean.getY());
            statement.setDouble(3, pointBean.getR());
            statement.setBoolean(4, pointBean.isInArea());
            statement.execute();

        } catch (Exception e) {
            return;
        }
    }

    public void addToTableFromJS() {
        final Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        try(PreparedStatement statement = conn.prepareStatement("INSERT INTO webpoints(x, y, r, inArea) VALUES (?, ?, ?, ?);")) {
            PointBean p = new PointBean();

            p.setX(Double.parseDouble(params.get("x")));
            p.setY(Double.parseDouble(params.get("y")));
            p.setR(Double.parseDouble(params.get("r")));

            statement.setDouble(1, p.getX());
            statement.setDouble(2, p.getY());
            statement.setDouble(3, p.getR());
            statement.setBoolean(4, p.isInArea());
            statement.execute();

        } catch (Exception e) {
            return;
        }
    }

    public List<PointModel> getPoints() {
        points = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM webpoints;");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                PointModel point = new PointModel();

                point.setX(resultSet.getDouble("x"));
                point.setY(resultSet.getDouble("y"));
                point.setR(resultSet.getDouble("r"));
                point.setInArea(resultSet.getBoolean("inArea"));
                point.setId(resultSet.getInt("attempt_id"));
                points.add(point);
            }

        } catch (SQLException e) {
            return null;
        }

        return points;
    }

    public void deletePoints() {
        try (PreparedStatement statement = conn.prepareStatement("TRUNCATE TABLE webpoints;")) {
            statement.execute();
        } catch (SQLException e) {
            return;
        }
    }

    public String getPointsJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(getPoints());
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
