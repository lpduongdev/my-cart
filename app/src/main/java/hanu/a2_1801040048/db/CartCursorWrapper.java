package hanu.a2_1801040048.db;

import static hanu.a2_1801040048.utils.constants.KeyConstants.*;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;

import hanu.a2_1801040048.models.Product;


public class CartCursorWrapper extends CursorWrapper {
    private final Cursor cursor;

    public CartCursorWrapper(Cursor cursor) {
        super(cursor);
        this.cursor = cursor;
    }

    public Product getProduct() {
        if (cursor.moveToNext())
            return new Product(cursor.getInt((int) cursor.getColumnIndex(TABLE_COL_ID)),
                    cursor.getString((int) cursor.getColumnIndex(TABLE_COL_THUMBNAIL)),
                    cursor.getString((int) cursor.getColumnIndex(TABLE_COL_NAME)),
                    cursor.getString((int) cursor.getColumnIndex(TABLE_COL_UNIT_PRICE)),
                    cursor.getInt((int) cursor.getColumnIndex(TABLE_COL_QUANTITY)));
        return null;
    }

    public ArrayList<Product> getProducts() {
        ArrayList<Product> list = new ArrayList<>();

        while (cursor.moveToNext())
            list.add(new Product(cursor.getInt((int) cursor.getColumnIndex(TABLE_COL_ID)),
                    cursor.getString((int) cursor.getColumnIndex(TABLE_COL_THUMBNAIL)),
                    cursor.getString((int) cursor.getColumnIndex(TABLE_COL_NAME)),
                    cursor.getString((int) cursor.getColumnIndex(TABLE_COL_UNIT_PRICE)),
                    cursor.getInt((int) cursor.getColumnIndex(TABLE_COL_QUANTITY))));
        return list;
    }
}
