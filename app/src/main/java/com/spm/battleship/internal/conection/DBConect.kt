package com.spm.battleship.internal.conection

import java.sql.*
import java.util.Properties

/**
 * Program to list databases in MySQL using Kotlin
class DBConect {
}
 */
object MySQLDatabaseExampleKotlin {

    internal var conn: Connection? = null
    internal var username = "billy" // provide the username
    internal var password = "0208" // provide the corresponding password

    @JvmStatic fun main(args: Array<String>) {
        // make a connection to MySQL Server
        getConnection()
        // execute the query via connection object
        executeMySQLQuery()
    }

    fun executeMySQLQuery() {
        var stmt: Statement? = null
        var resultset: ResultSet? = null

        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SHOW DATABASES;")

            if (stmt.execute("SHOW DATABASES;")) {
                resultset = stmt.resultSet
            }

            while (resultset!!.next()) {
                println(resultset.getString("Database"))
            }
        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        } finally {
            // release resources
            if (resultset != null) {
                try {
                    resultset.close()
                } catch (sqlEx: SQLException) {
                }

                resultset = null
            }

            if (stmt != null) {
                try {
                    stmt.close()
                } catch (sqlEx: SQLException) {
                }

                stmt = null
            }

            if (conn != null) {
                try {
                    conn!!.close()
                } catch (sqlEx: SQLException) {
                }

                conn = null
            }
        }
    }

    /**
     * This method makes a connection to MySQL Server
     * In this example, MySQL Server is running in the local host (so 127.0.0.1)
     * at the standard port 3306
     */
    fun getConnection() {
        val connectionProps = Properties()
        connectionProps.put("user", username)
        connectionProps.put("password", password)
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance()
            conn = DriverManager.getConnection("jdbc:mysql://52.90.22.0:3306/", connectionProps)
        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        } catch (ex: Exception) {
            // handle any errors
            ex.printStackTrace()
        }
    }
}