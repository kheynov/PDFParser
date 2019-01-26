import java.sql.*;

public class SQLiteDB {
    private Connection c;
    private Statement statement;

    public SQLiteDB() {
        statement = null;
        c = null;
    }

    public void OpenDatabase(){
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:HAMRadioDB.db");
            c.setAutoCommit(false);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void InsertItem(int NUMBER, String Text, String Variant_1, String Variant_2, String Variant_3, String Variant_4, String RVAR, int Image_ID){
        try {
            statement = c.createStatement();
            String sql = "INSERT INTO exam (NUMBER,TEXT,VAR1,VAR2,VAR3,VAR4,RVAR,IMAGE_ID) " +
                    "VALUES ("+ NUMBER +","+ Text +","+ Variant_1 +","+ Variant_2 +","+ Variant_3 +","+ Variant_4 +","+ RVAR +",NULL);";
            statement.executeUpdate(sql);
            statement.close();
            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void CloseStreams(){
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
