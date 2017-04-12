package com.example.uddishverma22.mait_go.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.uddishverma22.mait_go.Activities.UserProfile;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by uddishverma22 on 10/04/17.
 */

public class ImageLoader extends AsyncTask<Void, Void, Bitmap> {

    private String barcodeString;
    private int type;
    private Context context;

    public static final String TAG = "ImageLoader";

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;


    public ImageLoader(String barcodeString, int type, Context context) {
        this.barcodeString = barcodeString;
        this.type = type;
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {

        Bitmap bitmap = null;
        bitmap = encodeAsBitmap(barcodeString, BarcodeFormat.UPC_E, 400, 200);
        while (bitmap == null) {
            Log.d(TAG, "doInBackground: BITMAP IS NULL");
        }
        return bitmap;

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

    }

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) {
        String contentsToEncode = contents;
        if(contentsToEncode == null)    {
            return null;
        }

        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = null;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }
}
