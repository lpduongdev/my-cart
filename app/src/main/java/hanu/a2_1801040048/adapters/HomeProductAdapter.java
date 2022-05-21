package hanu.a2_1801040048.adapters;

import static hanu.a2_1801040048.utils.Utils.priceConvert;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hanu.a2_1801040048.DetailsActivity;
import hanu.a2_1801040048.db.CartManager;
import hanu.a2_1801040048.utils.Utils;
import hanu.a2_1801040048.utils.constants.HandlerConstants;
import hanu.a2_1801040048.utils.constants.ExecutorConstants;
import hanu.a2_1801040048.databinding.ProductItemBinding;
import hanu.a2_1801040048.models.Product;

public class    HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.HomeProductHolder> {

    private static final DiffUtil.ItemCallback<Product> DIFF_CALLBACK = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldProduct, @NonNull Product newProduct) {
            return oldProduct.getId() == newProduct.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldProduct, @NonNull Product newProduct) {
            return oldProduct.compareTo(newProduct) == 0;
        }
    };

    private final AsyncListDiffer<Product> differ = new AsyncListDiffer<>(this, DIFF_CALLBACK);

    public void submitList(ArrayList<Product> products){
        differ.submitList(products);
    }

    protected static class HomeProductHolder extends RecyclerView.ViewHolder {
        private final Context context;
        private final ProductItemBinding binding;

        public HomeProductHolder(@NonNull ProductItemBinding binding, Context context) {
            super(binding.getRoot().getRootView());
            this.binding = binding;
            this.context = context;
        }

        public void bind(Product product) {

            initImage(product);
            initText(product);
            initBtnListener(product);

        }

        private void initImage(Product product) {
            ExecutorConstants.getInstance().execute(() -> {
                Bitmap bitmap = Utils.downloadImage(product.getThumbnail());

                HandlerConstants.getInstance().post(() -> {
                    if (bitmap != null) binding.ivProductImage.setImageBitmap(bitmap);
                });
            });
        }

        private void initText(Product product) {
            binding.tvProductName.setText(product.getName());
            binding.tvProductPrice.setText(priceConvert(product.getUnitPrice()));

        }

        private void initBtnListener(Product product) {
            binding.btnAddToCart.setOnClickListener(v -> {
                int currentQuantity = product.getQuantity();
                Product dbProduct = CartManager.getInstance(context).getProductById(product.getId());
                if (dbProduct != null) currentQuantity = dbProduct.getQuantity();
                CartManager.getInstance(context).addProduct(new Product(
                        product.getId(),
                        product.getThumbnail(),
                        product.getName(),
                        product.getUnitPrice(),
                        currentQuantity + 1
                ));
                Toast.makeText(context, "Added item to cart", Toast.LENGTH_SHORT).show();
            });
            binding.tvProductName.setOnClickListener(v -> Utils.switchActivity(context, DetailsActivity.class, product));
            binding.ivProductImage.setOnClickListener(v -> Utils.switchActivity(context, DetailsActivity.class, product));
        }
    }

    @NonNull
    @Override
    public HomeProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeProductHolder(ProductItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent.getContext());

    }

    @Override
    public void onBindViewHolder(@NonNull HomeProductHolder holder, int position) {
        holder.bind(differ.getCurrentList().get(position));
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }
}
