package hanu.a2_1801040048;

import static hanu.a2_1801040048.utils.Utils.loadJSON;
import static hanu.a2_1801040048.utils.constants.HandlerConstants.handler;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hanu.a2_1801040048.adapters.HomeProductAdapter;
import hanu.a2_1801040048.utils.constants.ExecutorConstants;
import hanu.a2_1801040048.databinding.ActivityMainBinding;
import hanu.a2_1801040048.models.Product;
import hanu.a2_1801040048.utils.constants.KeyConstants;

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

        setThreadPolicy();

        products = new ArrayList<>();

        fetchDataJSON();

        binding.btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        initAdapter();
        addSearchListener();
    }

    private void setThreadPolicy() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void fetchDataJSON() {
        ExecutorConstants.executor.execute(() -> {
            String json = loadJSON(URL);

            handler.post(() -> {
                products.clear();
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

    private void filterData() {
        ArrayList<Product> newList = new ArrayList<>();
        for (Product product : products)
            if (product.getName().toLowerCase().contains(binding.edtSearchProduct.getText().toString().toLowerCase()))
                newList.add(product);
        homeProductAdapter.submitList(newList);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(KeyConstants.SAVED_LIST_INSTANCE, products);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        binding.searchBar.setVisibility((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? View.GONE : View.VISIBLE);
        binding.landscapeSearchBar.setVisibility((getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ? View.GONE : View.VISIBLE);
        products = savedInstanceState.getParcelableArrayList(KeyConstants.SAVED_LIST_INSTANCE);
        initAdapter();
    }

    private void initAdapter() {
        homeProductAdapter = new HomeProductAdapter();
        homeProductAdapter.submitList(products);
        binding.recyclerViewListHome.setAdapter(homeProductAdapter);
    }

    private void addSearchListener() {
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
        binding.edtSearchProductLandscape.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.edtSearchProductLandscape.getText() != null) filterData();
            }
        });
    }

}