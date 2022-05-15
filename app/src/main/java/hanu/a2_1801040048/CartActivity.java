package hanu.a2_1801040048;

import static hanu.a2_1801040048.utils.Utils.priceConvert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;

import java.util.ArrayList;

import hanu.a2_1801040048.adapters.CartProductsAdapter;
import hanu.a2_1801040048.callback.ProductListCallback;
import hanu.a2_1801040048.databinding.ActivityCartBinding;
import hanu.a2_1801040048.db.CartManager;
import hanu.a2_1801040048.models.Product;

public class CartActivity extends AppCompatActivity implements ProductListCallback {
    private ActivityCartBinding binding;
    private ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(LayoutInflater.from(CartActivity.this));
        setContentView(binding.getRoot());

        products = new ArrayList<>();

        binding.btnBack.setOnClickListener(v-> onBackPressed());

        products.addAll(CartManager.getInstance(this).getProducts());
        initAdapter();
    }

    private void initAdapter() {
        CartProductsAdapter cartProductAdapter = new CartProductsAdapter();
        cartProductAdapter.submitList(products);
        cartProductAdapter.setCallback(this);

        binding.recyclerViewListCart.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewListCart.setAdapter(cartProductAdapter);
        catchDataSetChanged(products);
    }

    @Override
    public void catchDataSetChanged(ArrayList<Product> products) {
        this.products = products;
        int total = 0;
        for (int i = 0; i < products.size(); i++)
            total += Integer.parseInt(products.get(i).getUnitPrice()) * products.get(i).getQuantity();
        binding.tvCartPriceTotal.setText(priceConvert(total + ""));
    }
}