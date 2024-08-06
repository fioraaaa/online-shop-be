import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/online-shop";
        String user = "root";
        String password = "";

        String[] createTables = {
                "CREATE TABLE IF NOT EXISTS customers ("
                        + "customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                        + "customer_name VARCHAR(100),"
                        + "customer_address VARCHAR(255),"
                        + "customer_code VARCHAR(50),"
                        + "customer_phone VARCHAR(20),"
                        + "is_active BOOLEAN,"
                        + "last_order_date DATE,"
                        + "pic VARCHAR(255));",

                "CREATE TABLE IF NOT EXISTS items ("
                        + "items_id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                        + "items_name VARCHAR(100),"
                        + "items_code VARCHAR(50),"
                        + "stock INT,"
                        + "price INT,"
                        + "is_available BOOLEAN,"
                        + "last_re_stock DATE);",

                "CREATE TABLE IF NOT EXISTS orders ("
                        + "order_id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                        + "order_code VARCHAR(50),"
                        + "order_date DATE,"
                        + "total_price INT,"
                        + "customer_id BIGINT,"
                        + "items_id BIGINT,"
                        + "quantity INT,"
                        + "FOREIGN KEY (customer_id) REFERENCES customers(customer_id),"
                        + "FOREIGN KEY (items_id) REFERENCES items(items_id));"
        };

        String[] insertData = {
                "INSERT INTO customers (customer_name, customer_address, customer_code, customer_phone, is_active, last_order_date, pic) VALUES "
                        + "('John Doe', '123 Main St', 'CUST001', '555-1234', TRUE, '2023-07-15', 'johndoe.jpg'),"
                        + "('Jane Smith', '456 Elm St', 'CUST002', '555-5678', FALSE, '2023-06-25', 'janesmith.png'),"
                        + "('Bob Johnson', '789 Oak St', 'CUST003', '555-8765', TRUE, '2023-08-01', 'bobjohnson.jpg'),"
                        + "('Alice Brown', '321 Maple St', 'CUST004', '555-4321', TRUE, '2023-05-20', 'alicebrown.png'),"
                        + "('Charlie Davis', '654 Pine St', 'CUST005', '555-9876', FALSE, '2023-04-10', 'charliedavis.jpg'),"
                        + "('Eve Foster', '987 Birch St', 'CUST006', '555-6789', TRUE, '2023-03-05', 'evefoster.png'),"
                        + "('Frank Green', '111 Cedar St', 'CUST007', '555-5432', FALSE, '2023-02-15', 'frankgreen.jpg'),"
                        + "('Grace Harris', '222 Spruce St', 'CUST008', '555-6543', TRUE, '2023-01-25', 'graceharris.png'),"
                        + "('Hank Irving', '333 Willow St', 'CUST009', '555-7654', TRUE, '2022-12-30', 'hankirving.jpg'),"
                        + "('Ivy Johnson', '444 Aspen St', 'CUST010', '555-8765', FALSE, '2022-11-10', 'ivyjohnson.png');",

                "INSERT INTO items (items_name, items_code, stock, price, is_available, last_re_stock) VALUES "
                        + "('Handphone A', 'ITEM001', 100, 20000, TRUE, '2023-07-01'),"
                        + "('Handphone B', 'ITEM002', 50, 19990, TRUE, '2023-07-10'),"
                        + "('Handphone C', 'ITEM003', 200, 499000, FALSE, '2023-06-20'),"
                        + "('Cardigan', 'ITEM004', 75, 299900, TRUE, '2023-05-15'),"
                        + "('Skirt', 'ITEM005', 150, 14990000, FALSE, '2023-04-05'),"
                        + "('Shirt', 'ITEM006', 30, 3999500, TRUE, '2023-03-10'),"
                        + "('Eye Makeup', 'ITEM007', 90, 24990, TRUE, '2023-02-20'),"
                        + "('Liptint', 'ITEM008', 120, 4999, FALSE, '2023-01-15'),"
                        + "('Sunscreen', 'ITEM009', 60, 34990, TRUE, '2022-12-10'),"
                        + "('Laptop', 'ITEM010', 80, 59990, TRUE, '2022-11-05');",

                "INSERT INTO orders (order_code, order_date, total_price, customer_id, items_id, quantity) VALUES "
                        + "('ORD001', '2023-07-15', 499500, 1, 1, 5),"
                        + "('ORD002', '2023-06-25', 399800, 2, 2, 2),"
                        + "('ORD003', '2023-08-01', 149700, 3, 3, 3),"
                        + "('ORD004', '2023-05-20', 899700, 4, 4, 3),"
                        + "('ORD005', '2023-04-10', 299800, 5, 5, 2),"
                        + "('ORD006', '2023-03-05', 799800, 6, 6, 2),"
                        + "('ORD007', '2023-02-15', 999600, 7, 7, 4),"
                        + "('ORD008', '2023-01-25', 499900, 8, 8, 1),"
                        + "('ORD009', '2022-12-30', 104700, 9, 9, 3),"
                        + "('ORD010', '2022-11-10', 1199800, 10, 10, 2);"
        };

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            for (String createTable : createTables) {
                stmt.executeUpdate(createTable);
            }

            for (String insert : insertData) {
                stmt.executeUpdate(insert);
            }

            System.out.println("Database initialized with dummy data.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
