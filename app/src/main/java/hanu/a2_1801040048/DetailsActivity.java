package hanu.a2_1801040048;

import static hanu.a2_1801040048.utils.Utils.downloadImage;
import static hanu.a2_1801040048.utils.Utils.priceConvert;
import static hanu.a2_1801040048.utils.constants.HandlerConstants.handler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.regex.Pattern;

import hanu.a2_1801040048.databinding.ActivityCartBinding;
import hanu.a2_1801040048.databinding.ActivityDetailsBinding;
import hanu.a2_1801040048.db.CartManager;
import hanu.a2_1801040048.models.Product;
import hanu.a2_1801040048.utils.Utils;
import hanu.a2_1801040048.utils.constants.ExecutorConstants;
import hanu.a2_1801040048.utils.constants.KeyConstants;

public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(LayoutInflater.from(DetailsActivity.this));
        setContentView(binding.getRoot());

        Product product = Utils.productFromString(getIntent().getStringExtra(KeyConstants.PRODUCT_DETAILS));

        initView(product);
    }

    private void initView(Product product) {
        ExecutorConstants.executor.execute(() -> handler.post(() -> {
            Bitmap bitmap = downloadImage(product.getThumbnail());
            if (bitmap != null) binding.ivProductImage.setImageBitmap(bitmap);
        }));

        binding.tvProductName.setText(product.getName());
        binding.tvProductPrice.setText(priceConvert(product.getUnitPrice() + ""));
        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.btnAddToCart.setOnClickListener(v-> {
            CartManager.getInstance(this).addProduct(new Product(
                    product.getId(),
                    product.getThumbnail(),
                    product.getName(),
                    product.getUnitPrice(),
                    product.getQuantity() + 1
            ));
            Toast.makeText(this, "Added item to cart", Toast.LENGTH_SHORT).show();
        });

    }
}