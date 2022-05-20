package hanu.a2_1801040048.adapters;

import static hanu.a2_1801040048.utils.Utils.downloadImage;
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

import hanu.a2_1801040048.callback.ProductListCallback;
import hanu.a2_1801040048.databinding.ProductItemCartBinding;
import hanu.a2_1801040048.db.CartManager;
import hanu.a2_1801040048.models.Product;
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
            ExecutorConstants.getInstance().execute(() -> {
                Bitmap bitmap = downloadImage(product.getThumbnail());
                HandlerConstants.getInstance().post(() -> {
                    if (bitmap != null) {
                        binding.ivProductImage.setImageBitmap(bitmap);
                    }
                });
            });


            binding.tvProductName.setText(product.getName());
            binding.tvProductPrice.setText(priceConvert(product.getUnitPrice()));
            binding.tvQuantity.setText(String.valueOf(product.getQuantity()));
            binding.tvProductPriceTotal.setText(priceConvert((product.getQuantity() * Integer.parseInt(product.getUnitPrice()) + "")));

            binding.btnAddQuantity.setOnClickListener(v -> {
                CartManager.getInstance(context).setProductQuantity(product.getId(), product.getQuantity() + 1);
                product.setQuantity(product.getQuantity() + 1);
                callback.catchDataSetChanged(new ArrayList<>(differ.getCurrentList()));
                reinitializeTextView(product);
            });

            binding.btnRemoveQuantity.setOnClickListener(v -> {
                if (product.getQuantity() == 1) {
                    ArrayList<Product> newList = new ArrayList<>(differ.getCurrentList());
                    for (Product p : newList) {
                        if (p.getId() == product.getId()) {
                            newList.remove(p);
                            break;
                        }
                    }
                    CartManager.getInstance(context).removeProducts(product.getId());
                    callback.catchDataSetChanged(newList);
                    submitList(newList);
                    return;
                }

                CartManager.getInstance(context).setProductQuantity(product.getId(), product.getQuantity() - 1);
                product.setQuantity(product.getQuantity() - 1);
                callback.catchDataSetChanged(new ArrayList<>(differ.getCurrentList()));
                reinitializeTextView(product);
            });

        }

        private void reinitializeTextView(Product product) {
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
