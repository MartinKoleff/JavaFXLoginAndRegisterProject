package sample;

import java.sql.*;
import java.util.*;

public class DBConnector {
    /**
     * Fields
     */


    /**
     * jdbcURL resets every time!!!
     */
    private static String jdbcURL; //"jdbc:mysql://192.168.0.116:3306"
    private static String user; //"martin"
    private static String password; //"password"
    private static Connection connection = null;

    private Scanner sc = new Scanner(System.in);
    private static boolean hasConnected = false;
    private static String tableName = null; //"projectDB.accounts"
    private static StringBuilder sql = new StringBuilder();

    private static Statement statement;
    private static ResultSet result;
    private static PreparedStatement preparedStatement;


    /**
     * Getting all accounts from the DB
     */
    private static Map<String, String> accounts = new HashMap<>();


    /**
     * Constructor
     */
    public DBConnector(String jdbcURL, String user, String password) {
        this.jdbcURL = jdbcURL;
        this.user = user;
        this.password = password;
    }


    /**
     * Constructor Chaining
     */
    public DBConnector(String jdbcURL, String user, String password, String tableName) {
        //constructor chaining...
        this.tableName = tableName;
    }


    /**
     * Getter for the HashMap
     */
    public static Map<String, String> getMap() {
        return accounts;
    }


    /**
     * Connect to DB server
     */
    private static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, user, password);

            if (connection != null) {
                System.out.println("Successfully connected to MySQL database test.");
                hasConnected = true;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("An error occurred while connecting MySQL database.");
            ex.printStackTrace();
        }
    }


    /**
     * Create DB if table with the tableName name doesn't exists
     */
    public void createDB() {
        if (hasConnected) {
            try {
                if (tableName == null) {
                    setTableName();
                }
                resetLengthOfSQL(sql);

                sql.append("CREATE DATABASE IF NOT EXISTS ").append(tableName.split("[.]")[0]); //.append(tableName);
//                        .append(tableName);//"projectDB.accounts"
                statement = connection.createStatement();

                statement.execute(String.valueOf(sql));

                fillMap();
            } catch (SQLException e) { //SQLSyntaxErrorException
                System.out.println("Database creation failed.");
                e.printStackTrace();
            }
        } else {
            connect();
            createDB();
        }
    }


    /**
     * Set the table name of the DB (custom name) / can be removed
     */
    private static void setTableName() {
//        System.out.println("Please type down your Database name:");
//        tableName = sc.nextLine();

        tableName = "default";
        if (tableName.equals("Default".toLowerCase())) {
            tableName = "projectDB.accounts";
        }
    }


    /**
     * SQL string re-setter
     */
    private static void resetLengthOfSQL(StringBuilder sql) {
        sql.setLength(0);
    }


    /**
     * Fill the HashMap with DB data
     */
    public void fillMap() {
        if (hasConnected) {
            resetLengthOfSQL(sql);
//            resetMap();

            sql.append("SELECT * FROM ").append(tableName);
            try {
                statement = connection.createStatement();
                result = statement.executeQuery(String.valueOf(sql));
                while (result.next()) {
//                    int id = result.getInt("id");
//                    String first_name = result.getString("first_name");
//                    String last_name = result.getString("last_name");
                    String username = result.getString("username");
                    String password = result.getString("password");

                    accounts.put(username, password);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            connect();
            fillMap();
        }
    }


    /**
     * Reset the map
     */
    private void resetMap() {
        accounts.clear();
    }


    /**
     * Register account / add to DB user and have them to login after registering
     * - gets valid input everytime! The tests are done before the data gets sent.
     */
    protected static void registerUser(String... str) {
        if (hasConnected) {
            if (tableName == null) {
                setTableName();
            }

            String id = getID();
            resetLengthOfSQL(sql);

            String first_name = str[0];
            String last_name = str[1];
            String username = str[2];
            String password = str[3];

            sql.append("INSERT INTO ")
                    .append(tableName)
                    .append(" VALUES (")
                    .append(id)
                    .append(", ?, ?, ?, ?, ?)"); //DEFAULT | id + 1 -> method to find the gap
            try {
                preparedStatement = connection.prepareStatement(sql.toString());
                preparedStatement.setString(1, first_name); //first_name
                preparedStatement.setString(2, last_name); //last_name
                preparedStatement.setString(3, username); //username
                preparedStatement.setString(4, password); //password
                preparedStatement.setString(5, "no"); //admin //checkbox

                preparedStatement.execute();
                accounts.put(str[2], str[3]);
                System.out.println("User Registered Successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            connect();
            registerUser(str);
        }
    }

    /**
     * Get ID based on the gap (When account is removed - ID is skipped)
     */

    private static String getID() {
        resetLengthOfSQL(sql);

        List<Integer> IDs = new ArrayList<Integer>();
        try {
            sql.append("SELECT id FROM ").append(tableName);
            statement = connection.createStatement();
            result = statement.executeQuery(String.valueOf(sql));

            while (result.next()) {
                int id = result.getInt("id");
                IDs.add(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /**If there are gaps in the IDs*/
        if(IDs.size() > 1) {
            for (int i = 0; i < IDs.size() - 1; i++) {
                if (IDs.get(i) + 1 != IDs.get(i + 1)) {
                    return String.valueOf(IDs.get(i) + 1);
                }
            }
        }
        /**if there aren't gaps in the IDs ->
         * -  max number + 1
         * -  or else 1 if empty*/
        Integer max = IDs.stream()
                .mapToInt(v -> v)
                .max().orElse(0) + 1;

        return String.valueOf(max);
        //        return IDs.stream().max(Integer::valueOf).toString();
        //        return "DEFAULT";
    }


    /**
     * Insert a row in DB
     */
//    protected static void insertData(String... data) {
//
//    }


    /**
     * Delete ALL DATA in DB
     */
    public void resetDB() { //clear the DB

    }


    /**
     * Modify to get specific data (first / last name | username / password | everything)
     */
    public void getData() {
        if (hasConnected) {
            try {
                statement = connection.createStatement();
                resetLengthOfSQL(sql);

                if (tableName == null) {
                    setTableName();
                }

                sql.append("SELECT * FROM ").append(tableName);

                result = statement.executeQuery(sql.toString());
                while ((result.next())) {
                    System.out.println(result.getString("first_name") + " " + result.getString("last_name"));
                }
            } catch (SQLException e) {
                System.out.println("An error occurred while getting data from the MySQL database.");
                e.printStackTrace();
            }
        } else {
            connect();
            getData();
        }
    }
}

