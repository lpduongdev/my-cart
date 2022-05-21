package hanu.a2_1801040048.db;

import static hanu.a2_1801040048.utils.constants.KeyConstants.TABLE_COL_ID;
import static hanu.a2_1801040048.utils.constants.KeyConstants.TABLE_COL_NAME;
import static hanu.a2_1801040048.utils.constants.KeyConstants.TABLE_COL_QUANTITY;
import static hanu.a2_1801040048.utils.constants.KeyConstants.TABLE_COL_THUMBNAIL;
import static hanu.a2_1801040048.utils.constants.KeyConstants.TABLE_COL_UNIT_PRICE;
import static hanu.a2_1801040048.utils.constants.KeyConstants.TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1801040048.models.Product;


public class CartManager {
    private static CartManager instance;
    private static SQLiteDatabase db;

    private CartManager(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getReadableDatabase();
    }

    public static CartManager getInstance(Context context) {
        if (instance == null)
            instance = new CartManager(context);
        return instance;
    }

    public void addProduct(Product product) {
        if (getProductById(product.getId()) != null) {
            updateProduct(product);
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_COL_ID, product.getId());
        contentValues.put(TABLE_COL_THUMBNAIL, product.getThumbnail());
        contentValues.put(TABLE_COL_NAME, product.getName());
        contentValues.put(TABLE_COL_UNIT_PRICE, product.getUnitPrice());
        contentValues.put(TABLE_COL_QUANTITY, product.getQuantity());
        db.insert(TABLE_NAME, null, contentValues);
    }

    public void updateProduct(Product product) {
        SQLiteStatement statement = db.compileStatement("UPDATE " + TABLE_NAME + " SET quantity=? WHERE id=?");
        statement.bindLong(1, product.getQuantity());
        statement.bindLong(2, product.getId());
        statement.executeUpdateDelete();
    }

    public void removeProduct(Product product) {
        SQLiteStatement statement = db.compileStatement("DELETE FROM " + TABLE_NAME + " WHERE id=?");
        statement.bindLong(1, product.getId());
        statement.executeUpdateDelete();
    }

    public ArrayList<Product> getProducts() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        CartCursorWrapper cursorWrapper = new CartCursorWrapper(cursor);
        ArrayList<Product> list = cursorWrapper.getProducts();
        cursor.close();
        return list;
    }

    public Product getProductById(int id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id=?", new String[]{String.valueOf(id)});
        CartCursorWrapper cursorWrapper = new CartCursorWrapper(cursor);
        Product product = cursorWrapper.getProduct();
        cursor.close();
        return product;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        db.close();
    }
}
