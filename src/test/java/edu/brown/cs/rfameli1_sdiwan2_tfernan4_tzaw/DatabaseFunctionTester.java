package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Database.Database;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Database.DbUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseFunctionTester {
  private static Connection conn;

  private DatabaseFunctionTester() { }

  public static Connection setEmptyDatabaseAndGetConn(String filename)
      throws IOException, SQLException, ClassNotFoundException {
    deleteTestDatabaseIfExists(filename);
    createDatabaseIfNotExists(filename);
    Database db = new Database(filename);
    conn = db.getConnection();
//    dropAllTables(conn);
    loadAllTables(conn);
    return conn;
  }

  public static void deleteTestDatabaseIfExists(String filename) throws SQLException, IOException {
    // this will mess up if you use the connection elsewhere and do not close it
    if (conn != null) {
      if (!conn.isClosed()) {
        DbUtils.closeQuietly(conn);
      }
    }
    File file = new File(filename);
    if (file.exists()) {
      if (!file.delete()) {
        throw new IOException("ERROR: Could not delete file " + filename + " in deleteTestDatabaseIfExists");
      }
    }
  }

  public static void createDatabaseIfNotExists(String filename)
      throws IOException {
    File newDb = new File(filename);
    if (!newDb.exists()) {
      // Try to create the file
      if (!newDb.createNewFile()) {
        throw new IOException("File " + filename + " could not be created in " +
            "DatabaseFunctionTester.createDatabaseFileIfNotExists");
      }
    }
  }


  /**
   * Clears a given table in the current database. Only works for questions and tags tables.
   * @param tableName the name of the table to clear
   * @throws SQLException if no connection has been established or if an error occurs with the SQL
   * update
   */
  public static void clearTable(Connection conn, String tableName) throws SQLException {
    PreparedStatement ps = conn.prepareStatement("DELETE FROM ?");
    ps.setString(1, tableName);
    ps.executeUpdate();
  }

  private static void loadAllTables(Connection conn) throws SQLException {
    PreparedStatement ps = conn.prepareStatement(
        "CREATE TABLE \"entries\" ( " +
        "  \"id\"  INTEGER NOT NULL UNIQUE, " +
        "  \"date\"  DATE, " +
        "  \"entry_text\"  TEXT, " +
        "  \"author\"  TEXT, " +
        "  FOREIGN KEY(\"author\") REFERENCES \"users\"(\"username\"), " +
        "  PRIMARY KEY(\"id\" AUTOINCREMENT) " +
        ")");
    ps.executeUpdate();

    ps = conn.prepareStatement(
        "CREATE TABLE \"entries_to_questions\" ( " +
            "  \"entry_id\"  INTEGER, " +
            "  \"question_id\"  INTEGER, " +
            "  FOREIGN KEY (\"entry_id\") REFERENCES entries " +
            "  FOREIGN KEY (\"question_id\") REFERENCES questions " +
            ")"
    );
    ps.executeUpdate();

    ps = conn.prepareStatement(
        "CREATE TABLE \"questions\" ( " +
            "  \"id\"  INTEGER NOT NULL UNIQUE, " +
            "  \"text\"  TEXT UNIQUE, " +
            "  PRIMARY KEY(\"id\" AUTOINCREMENT) " +
            ")"
    );
    ps.executeUpdate();

    ps = conn.prepareStatement(
        "CREATE TABLE \"tags\" ( " +
            "  \"id\"  INTEGER NOT NULL UNIQUE, " +
            "  \"text\"  INTEGER NOT NULL, " +
            "  PRIMARY KEY(\"id\" AUTOINCREMENT) " +
            ")"
    );
    ps.executeUpdate();

    ps = conn.prepareStatement(
        "CREATE TABLE \"tags_to_entries\" ( " +
            "  \"tag_id\"  INTEGER NOT NULL, " +
            "  \"entry_id\"  INTEGER NOT NULL, " +
            "  FOREIGN KEY(\"tag_id\") REFERENCES \"tags\" ON DELETE CASCADE " +
            "  FOREIGN KEY(\"entry_id\") REFERENCES \"entries\" ON DELETE CASCADE " +
            ")"
    );
    ps.executeUpdate();

    ps = conn.prepareStatement(
        "CREATE TABLE \"tags_to_questions\" ( " +
            "  \"tag_id\"  INTEGER NOT NULL, " +
            "  \"question_id\"  INTEGER NOT NULL, " +
            "  FOREIGN KEY(\"tag_id\") REFERENCES \"tags\" ON DELETE CASCADE " +
            "  FOREIGN KEY(\"question_id\") REFERENCES \"questions\" ON DELETE CASCADE " +
            ")"
    );
    ps.executeUpdate();

    ps = conn.prepareStatement(
        "CREATE TABLE \"users\" ( " +
            "  \"username\"  TEXT NOT NULL UNIQUE, " +
            "  \"password\"  BLOB NOT NULL, " +
            "  PRIMARY KEY(\"username\") " +
            ")"
    );
    ps.executeUpdate();
    DbUtils.closeQuietly(ps);
  }

  public static void deleteFileIfExists(String filename) throws IOException {
    File f = new File(filename);
    if (f.exists()) {
      if (!f.delete()) {
        throw new IOException("File " + filename + " could not be deleted in DatabaseGenerator.deleteDatabaseFile");
      }
    }
  }

  //  /**
//   * Drops all JournalTexter-related tables from the database.
//   * @param conn
//   * @throws SQLException
//   */
//  public static void dropAllTables(Connection conn) throws SQLException {
//    List<String> tableNames = Arrays.asList(
//        "entries_to_questions", "tags_to_entries", "tags_to_questions",
//        "users", "questions", "tags", "entries");
//    for (String table : tableNames) {
//      dropTable(conn, table);
//    }
//  }
//
//  /**
//   * Drops the given table in the
//   * @param tableName the name of the table to clear
//   * @throws SQLException if no connection has been established or if an error occurs with the SQL
//   * update
//   */
//  public static void dropTable(Connection conn, String tableName) throws SQLException {
//    // BUG HERE
//    PreparedStatement ps = conn.prepareStatement("DROP TABLE IF EXISTS ?");
//    ps.setString(1, tableName);
//    ps.executeUpdate();
//    DbUtils.closeQuietly(ps);
//  }

}
