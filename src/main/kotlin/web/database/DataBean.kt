package web.database

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.enterprise.context.ApplicationScoped
import jakarta.faces.context.FacesContext
import jakarta.inject.Named
import web.model.PointModel
import web.beans.PointBean
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

//@ManagedBean
@Named("dataBean")
@ApplicationScoped
class DataBean {
    var conn: Connection? = null
    //private var points: MutableList<PointModel> = mutableListOf()

    init {
        conn = try {
            Class.forName("org.postgresql.Driver")
            val properties = Properties()
            val inputStream = javaClass.getClassLoader().getResourceAsStream("db.cfg")
            properties.load(inputStream)
            inputStream.close()
            val user = properties.getProperty("user")
            val password = properties.getProperty("password")
            val url = properties.getProperty("URL")
            DriverManager.getConnection(url, user, password)
        } catch (e: Exception) {
           error(e)
        }
    }


    fun addToTable(pointBean: PointBean) {
        val sql = "INSERT INTO webpoints(x, y, r, inArea) VALUES (?, ?, ?, ?);"
        executeUpdate(sql) { statement ->
            statement.setDouble(1, pointBean.x)
            statement.setDouble(2, pointBean.y)
            statement.setDouble(3, pointBean.r)
            statement.setBoolean(4, pointBean.isInArea)
        }
    }

    fun addToTableFromJS() {
        val params = FacesContext.getCurrentInstance().externalContext.requestParameterMap
        val pointBean = PointBean().apply {
            x = params["x"]?.toDouble() ?: 0.0
            y = params["y"]?.toDouble() ?: 0.0
            r = params["r"]?.toDouble() ?: 0.0
        }

        addToTable(pointBean)
    }

    fun getPoints(): List<PointModel> {
        val sql = "SELECT * FROM webpoints;"
        return executeQuery(sql) { resultSet ->
            generateSequence { if (resultSet.next()) resultSet else null }.map {
                PointModel().apply {
                    x = it.getDouble("x")
                    y = it.getDouble("y")
                    r = it.getDouble("r")
                    isInArea = it.getBoolean("inArea")
                    id = it.getInt("attempt_id")
                }
            }.toList()
        }
    }

    fun deletePoints() {
        val sql = "TRUNCATE TABLE webpoints;"
        executeUpdate(sql)
    }


    private fun <T> executeQuery(sql: String, handler: (java.sql.ResultSet) -> T): T {
        conn?.let { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.executeQuery().use { resultSet ->
                    return handler(resultSet)
                }
            }
        } ?: throw SQLException("Database connection is not initialized")
    }

    private fun executeUpdate(sql: String, statementHandler: (java.sql.PreparedStatement) -> Unit = {}) {
        conn?.let { connection ->
            connection.prepareStatement(sql).use { statement ->
                statementHandler(statement)
                statement.executeUpdate()
            }
        } ?: throw SQLException("Database connection is not initialized")
    }


    val pointsJson: String
        get() = ObjectMapper().writeValueAsString(getPoints())

}
