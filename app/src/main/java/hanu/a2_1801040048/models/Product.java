package hanu.a2_1801040048.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private int id;
    private String thumbnail;
    private String name;
    private String unitPrice;
    private int quantity;

    public Product(int id, String thumbnail, String name, String unitPrice) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
    }

    public Product(int id, String thumbnail, String name, String unitPrice, int quantity) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    protected Product(Parcel in) {
        id = in.readInt();
        thumbnail = in.readString();
        name = in.readString();
        unitPrice = in.readString();
        quantity = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int compareTo(Product product) {
        if (product.getId() == getId() &&
        product.getName() == getName() &&
        product.getThumbnail() == getThumbnail() &&
        product.getUnitPrice() == getUnitPrice())
            return 0;
        return 1;
    }

    @Override
    public int describeContents() {
        return this.id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getThumbnail());
        dest.writeString(getName());
        dest.writeString(getUnitPrice());
        dest.writeInt(getQuantity());
    }
}
