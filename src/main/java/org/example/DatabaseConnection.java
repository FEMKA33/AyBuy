package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "ya3urodec";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void addUser(String name, String email, String password) {
        String sql = "INSERT INTO users (Name, Email, Password) VALUES (?, ?, ?)";
        String hashedPassword = PasswordHasher.hashPassword(password);
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            stmt.executeUpdate();
            System.out.println("User added successfully!");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
        }
    }

    public boolean isLogin(String email, String password) {
        String sql = "SELECT password, id FROM users WHERE email = ?";
        String hashedPassword = PasswordHasher.hashPassword(password);
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            //stmt.setString(2, hashedPassword);
            //stmt.executeUpdate();
            //System.out.println("User added successfully!");
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String storedHash = resultSet.getString("password");

                System.out.println(hashedPassword);
                return storedHash.equals(hashedPassword);
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
            return false;
        }
    }

    public boolean isUser(String email) {
        String sql = "SELECT email FROM users WHERE email = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String storedHash = resultSet.getString("email");

                return storedHash.equals(email);
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
            return false;
        }
    }

    public String getId(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String storedHash = resultSet.getString("id");

                System.out.println(storedHash);
                return storedHash;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> getUserInfo(int id) {
        String sql = "SELECT name, email FROM users WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                var storedHash = new ArrayList<String>();
                storedHash.add(resultSet.getString("name"));
                storedHash.add(resultSet.getString("email"));

                System.out.println(storedHash.get(0) + storedHash.get(1));
                return storedHash;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
            return null;
        }
    }

    public static void addComponent(String name, String price, String image, String description, String date, int user_id) {
        String sql = "INSERT INTO ComponentInfo (Name, Price, Image, Description, date_end, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, price);
            stmt.setString(3, image);
            stmt.setString(4, description);
            stmt.setString(5, date);
            stmt.setInt(6, user_id);
            stmt.executeUpdate();
            System.out.println("Component added successfully!");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
        }
    }

    public void insertPriceComponent(int price, int user_id, int id) {
        String sql = "UPDATE ComponentInfo SET history_price = array_append(history_price, ?), history_users = array_append(history_users, ?) WHERE id = ?;";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, price);
            stmt.setInt(2, user_id);
            stmt.setInt(3, id);
            stmt.executeUpdate();
            System.out.println("User added successfully!");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
        }
    }

    public static ArrayList<ArrayList<Integer>> getHistory(int id) {
        String sql = "SELECT history_users, history_price FROM ComponentInfo WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            System.out.println("User added successfully!");

            ArrayList<ArrayList<Integer>> storedHash = new ArrayList<>();
            while (resultSet.next()) {

                Array usersArray = resultSet.getArray("history_users");
                Array priceArray = resultSet.getArray("history_price");

                Integer[] users = (Integer[]) usersArray.getArray();
                Integer[] prices = (Integer[]) priceArray.getArray();

                storedHash.add(new ArrayList<>(List.of(users)));
                storedHash.add(new ArrayList<>(List.of(prices)));
            }

            return storedHash;
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> getComponents(int id) {
        String sql = "SELECT id, name, price, image, description, attendance, user_id FROM componentinfo";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = stmt.executeQuery();


            var storedHash = new ArrayList<String>();
            while (resultSet.next()) {
                if (id != resultSet.getInt("user_id")) {
                    storedHash.add(resultSet.getString("id"));
                    storedHash.add(resultSet.getString("name"));
                    storedHash.add(resultSet.getString("price"));
                    storedHash.add(resultSet.getString("image"));
                    storedHash.add(resultSet.getString("description"));
                    storedHash.add(resultSet.getString("attendance"));
                }
            }

            return storedHash;
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> getComponentsById(int id) {
        String sql = "SELECT id, name, price, image, description FROM componentinfo WHERE user_id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();


            var storedHash = new ArrayList<String>();
            while (resultSet.next()) {
                storedHash.add(resultSet.getString("id"));
                storedHash.add(resultSet.getString("name"));
                storedHash.add(resultSet.getString("price"));
                storedHash.add(resultSet.getString("image"));
                storedHash.add(resultSet.getString("description"));
            }

            return storedHash;
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> getComponentsByAuction(int id) {
        String sql = "SELECT id, name, price, image, description FROM componentinfo WHERE auction_user = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();


            var storedHash = new ArrayList<String>();
            while (resultSet.next()) {
                storedHash.add(resultSet.getString("id"));
                storedHash.add(resultSet.getString("name"));
                storedHash.add(resultSet.getString("price"));
                storedHash.add(resultSet.getString("image"));
                storedHash.add(resultSet.getString("description"));
            }

            return storedHash;
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> getComponentById(int id) {
        String sql = "SELECT name, price, image, description, date_end FROM componentinfo WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            var storedHash = new ArrayList<String>();
            if (resultSet.next()) {
                storedHash.add(resultSet.getString("name"));
                storedHash.add(resultSet.getString("price"));
                storedHash.add(resultSet.getString("image"));
                storedHash.add(resultSet.getString("description"));
                storedHash.add(resultSet.getString("date_end"));
            }

            return storedHash;
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
            return null;
        }
    }

    public void ChangePrice(int id, String price, int user_id) {
        String sql = "UPDATE componentinfo SET price = ?, auction_user = ? WHERE id = ?;";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, price);
            stmt.setInt(2, user_id);
            stmt.setInt(3, id);
            System.out.println(stmt);
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
        }
    }

    public void UpdateBalance(int id, int balance) {
        String sql = "UPDATE users SET balance = ? WHERE id = ?;";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, balance);
            stmt.setInt(2, id);
            System.out.println(stmt);
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
        }
    }

    public static int getUserBalance(int id) {
        String sql = "SELECT balance FROM users WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                int storedHash = resultSet.getInt("balance");

                System.out.println(storedHash);
                return storedHash;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
            return 0;
        }
    }

    public void deleteComponent(int id) {
        String sql = "DELETE FROM componentInfo WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("User added successfully!");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
        }
    }

    public int GetAttendance(int id) {
        String sql = "SELECT attendance FROM componentinfo WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {

                int storedHash = resultSet.getInt("attendance");

                System.out.println(storedHash);
                return storedHash;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
            return 0;
        }
    }

    public void ChangeAttendance(int id, int attendance) {
        String sql = "UPDATE componentinfo SET attendance = ? WHERE id = ?;";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, attendance);
            stmt.setInt(2, id);
            System.out.println(stmt);
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getTheMostPopularComponent(int id) {
        String sql = "SELECT id, name, price, image, description, attendance, user_id FROM componentinfo ORDER BY attendance DESC LIMIT 6";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = stmt.executeQuery();


            var storedHash = new ArrayList<String>();
            while (resultSet.next()) {
                if (id != resultSet.getInt("user_id")) {
                    storedHash.add(resultSet.getString("id"));
                    storedHash.add(resultSet.getString("name"));
                    storedHash.add(resultSet.getString("price"));
                    storedHash.add(resultSet.getString("image"));
                    storedHash.add(resultSet.getString("description"));
                    storedHash.add(resultSet.getString("attendance"));
                }
            }

            return storedHash;
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> getComponentsByIdUser(int id) {
        String sql = "SELECT id, name, price, image, description FROM componentinfo WHERE auction_user = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();


            var storedHash = new ArrayList<String>();
            while (resultSet.next()) {
                storedHash.add(resultSet.getString("id"));
                storedHash.add(resultSet.getString("name"));
                storedHash.add(resultSet.getString("price"));
                storedHash.add(resultSet.getString("image"));
                storedHash.add(resultSet.getString("description"));
            }

            return storedHash;
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
            return null;
        }
    }
}