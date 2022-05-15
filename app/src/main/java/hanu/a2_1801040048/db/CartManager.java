package hanu.a2_1801040048.db;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import java.util.ArrayList;

import hanu.a2_1801040048.models.Product;

public class CartManager {
    private static CartManager instance;
    private Database database;
    private Context context;

    private CartManager(Context context) {
        this.context = context;
        database = new Database(context, "cart.db", null, 1);
        database.queryData("CREATE TABLE IF NOT EXISTS products" +
                "(id INTEGER PRIMARY KEY," +
                "thumbnail TEXT NOT NULL," +
                "name TEXT NOT NULL," +
                "unitPrice TEXT NOT NULL," +
                "quantity INTEGER NOT NULL)");
    }

    public static CartManager getInstance(Context context) {
        if (instance == null) {
            instance = new CartManager(context);
        }
        return instance;
    }

    public void addProduct(Product product) {
        if (getProductById(product.getId()) != null) {
            setProductQuantity(product.getId(), getProductById(product.getId()).getQuantity() + 1);
            return;
        }
        database.queryData("INSERT INTO products VALUES(" +
                product.getId() + ",'" +
                product.getThumbnail() + "','" +
                product.getName() + "','" +
                product.getUnitPrice() + "'," +
                product.getQuantity() + ")");
    }

    public void removeProducts (int id) {
        database.queryData("DELETE FROM products WHERE id=" + id);
    }

    public void setProductQuantity(int id, int quantity) {
        database.queryData("UPDATE products SET quantity=" + quantity + " WHERE id=" + id);
    }

    public Product getProductById(int idGet) {
        Cursor cursor = database.getData("SELECT * FROM products WHERE id=" + idGet);

        if (cursor.moveToNext())
            return (new Product(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4)));
        return null;
    }

    public ArrayList<Product> getProducts() {
        ArrayList<Product> products = new ArrayList<>();
        Cursor cursor = database.getData("SELECT * FROM products");

        while (cursor.moveToNext()) {
            Product newProduct = new Product(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4));
            products.add(newProduct);
        }
        return products;
    }
}
