package hanu.a2_1801040048;

import static hanu.a2_1801040048.utils.Utils.loadJSON;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hanu.a2_1801040048.adapters.HomeProductAdapter;
import hanu.a2_1801040048.utils.constants.ExecutorConstants;
import hanu.a2_1801040048.databinding.ActivityMainBinding;
import hanu.a2_1801040048.models.Product;
import hanu.a2_1801040048.utils.constants.HandlerConstants;

public class MainActivity extends AppCompatActivity {
    private static final String URL = "https://mpr-cart-api.herokuapp.com/products";
    private ActivityMainBinding binding;
    private ArrayList<Product> products;
    private HomeProductAdapter homeProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();

        binding.btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        initSearchListener();

        fetchDataJSON();

        initAdapter();
    }

    private void fetchDataJSON() {
        ExecutorConstants.getInstance().execute(() -> {
            String json = loadJSON(URL);

            HandlerConstants.getInstance().post(() -> {
                if (json != null)
                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            int id = data.getInt("id");
                            String thumbnail = data.getString("thumbnail");
                            String name = data.getString("name");
                            String unitPrice = data.getString("unitPrice");
                            products.add(new Product(id, thumbnail, name, unitPrice));
                            initAdapter();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            });
        });

    }

    private void initAdapter() {
        homeProductAdapter = new HomeProductAdapter();
        homeProductAdapter.submitList(products);
        binding.recyclerViewListHome.setAdapter(homeProductAdapter);
    }

    private void initSearchListener() {
        binding.edtSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.edtSearchProduct.getText() != null) filterData();
            }
        });
    }

    private void filterData() {
        ArrayList<Product> newList = new ArrayList<>();
        for (Product product : products)
            if (product.getName().toLowerCase().contains(binding.edtSearchProduct.getText().toString().toLowerCase()))
                newList.add(product);
        homeProductAdapter.submitList(newList);
    }

}
