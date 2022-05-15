package hanu.a2_1801040048.callback;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1801040048.models.Product;

public interface ProductListCallback {
    void catchDataSetChanged(ArrayList<Product> products);
}