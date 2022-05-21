package hanu.a2_1801040048.adapters;

import static hanu.a2_1801040048.utils.Utils.priceConvert;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hanu.a2_1801040048.DetailsActivity;
import hanu.a2_1801040048.callback.ProductListCallback;
import hanu.a2_1801040048.databinding.ProductItemCartBinding;
import hanu.a2_1801040048.db.CartManager;
import hanu.a2_1801040048.models.Product;
import hanu.a2_1801040048.utils.Utils;
import hanu.a2_1801040048.utils.constants.ExecutorConstants;
import hanu.a2_1801040048.utils.constants.HandlerConstants;

public class CartProductsAdapter extends RecyclerView.Adapter<CartProductsAdapter.CartProductsHolder> {
    private ProductListCallback callback;

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

    public void setCallback(ProductListCallback callback) {
        this.callback = callback;
    }

    protected class CartProductsHolder extends RecyclerView.ViewHolder {
        private final Context context;
        private final ProductItemCartBinding binding;

        public CartProductsHolder(ProductItemCartBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }

        private void bind(Product product) {
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

        private void initBtnListener(Product product) {
            binding.ivProductImage.setOnClickListener(v -> Utils.switchActivity(context, DetailsActivity.class, product));
            binding.tvProductName.setOnClickListener(v -> Utils.switchActivity(context, DetailsActivity.class, product));

            binding.btnAddQuantity.setOnClickListener(v -> {
                product.setQuantity(product.getQuantity() + 1);
                CartManager.getInstance(context).updateProduct(product);
                callback.catchDataSetChanged(new ArrayList<>(differ.getCurrentList()));
                reinitializeQuantityView(product);
            });

            binding.btnRemoveQuantity.setOnClickListener(v -> {
                if (product.getQuantity() == 1) {
                    ArrayList<Product> newList = new ArrayList<>(differ.getCurrentList());
                    newList.remove(product);
                    CartManager.getInstance(context).removeProduct(product);
                    callback.catchDataSetChanged(newList);
                    submitList(newList);
                    return;
                }
                product.setQuantity(product.getQuantity() - 1);
                CartManager.getInstance(context).updateProduct(product);
                callback.catchDataSetChanged(new ArrayList<>(differ.getCurrentList()));
                reinitializeQuantityView(product);
            });
        }

        private void initText(Product product) {
            binding.tvProductName.setText(product.getName());
            binding.tvProductPrice.setText(priceConvert(product.getUnitPrice()));
            binding.tvQuantity.setText(String.valueOf(product.getQuantity()));
            binding.tvProductPriceTotal.setText(priceConvert((product.getQuantity() * Integer.parseInt(product.getUnitPrice()) + "")));

        }

        private void reinitializeQuantityView(Product product) {
            binding.tvQuantity.setText(String.valueOf(product.getQuantity()));
            binding.tvProductPriceTotal.setText(priceConvert((product.getQuantity() * Integer.parseInt(product.getUnitPrice()) + "")));
        }
    }

    @NonNull
    @Override
    public CartProductsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartProductsHolder(ProductItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductsHolder holder, int position) {
        holder.bind(differ.getCurrentList().get(position));
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }
}
