package hanu.a2_1801040048.adapters;

import static hanu.a2_1801040048.utils.Utils.downloadImage;
import static hanu.a2_1801040048.utils.Utils.priceConvert;
import static hanu.a2_1801040048.utils.constants.HandlerConstants.handler;

import android.content.Context;
import android.content.Intent;
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
import hanu.a2_1801040048.utils.constants.KeyConstants;
import hanu.a2_1801040048.utils.constants.ExecutorConstants;
import hanu.a2_1801040048.databinding.ProductItemBinding;
import hanu.a2_1801040048.models.Product;

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.HomeProductHolder> {

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
            binding.tvProductName.setText(product.getName());
            binding.tvProductPrice.setText(priceConvert(product.getUnitPrice()));

            ExecutorConstants.executor.execute(() -> handler.post(() -> {
                Bitmap bitmap = downloadImage(product.getThumbnail());
                if (bitmap != null) binding.ivProductImage.setImageBitmap(bitmap);
            }));

            binding.btnAddToCart.setOnClickListener(v -> {
                CartManager.getInstance(context).addProduct(new Product(
                        product.getId(),
                        product.getThumbnail(),
                        product.getName(),
                        product.getUnitPrice(),
                        product.getQuantity() + 1
                ));
                Toast.makeText(context, "Added item to cart", Toast.LENGTH_SHORT).show();
            });
            binding.tvProductName.setOnClickListener(v -> initIntentToDetails(product));
            binding.ivProductImage.setOnClickListener(v -> initIntentToDetails(product));

        }

        void initIntentToDetails(Product product) {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra(KeyConstants.PRODUCT_DETAILS,
                    product.getId() + "$|" +
                            product.getThumbnail() + "$|" +
                            product.getName() + "$|" +
                            product.getUnitPrice() + "$|");

            context.startActivity(intent);
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
