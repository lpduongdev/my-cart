package hanu.a2_1801040048.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Scanner;
import java.util.regex.Pattern;

import hanu.a2_1801040048.MainActivity;
import hanu.a2_1801040048.models.Product;

public class Utils {

    public static Bitmap downloadImage(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String loadJSON(String link) {
        URL url;
        HttpURLConnection urlConnection;
        try {
            url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            Scanner sc = new Scanner(is);
            StringBuilder result = new StringBuilder();
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String priceConvert(String price) {
        String newPrice = new StringBuilder(price).reverse().toString();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < newPrice.length(); i++) {
            builder.append(newPrice.charAt(i));
            if ((i + 1) % 3 == 0 && i != newPrice.length()-1) builder.append(",");
        }

        return builder.reverse().append("đ").toString();
    }

    public static Product productFromString(String stringLine) {
        String[] productTemp = stringLine.split(Pattern.quote("$|"));

        return new Product(
                Integer.parseInt(productTemp[0]),
                productTemp[1],
                productTemp[2],
                productTemp[3]);

    }
}
