package com.demoproject.mynativemodule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;

import androidx.annotation.FloatRange;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BlurImageModule extends ReactContextBaseJavaModule {
    private static ReactApplicationContext reactContext;

    public BlurImageModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @Override
    public String getName() {
        return "BlurImageModule";
    }

    @ReactMethod
    public void blurImage(String url, int x, int y, int width, int height, Callback callback) {

        Bitmap blurImageResponse;
        try {
            Bitmap bitmapImage = getBitmapFromURL(url);
            blurImageResponse = blurExceptRectangle(reactContext, bitmapImage,25.0f,x,y,width,height);
            String blurImageResponseString = bitmapToBase64(blurImageResponse);
            callback.invoke(blurImageResponseString);
        } catch (Exception exception) {
            callback.invoke(exception);
        }
    }

    public static Bitmap blurExceptRectangle(Context context, Bitmap bitmapOriginal, @FloatRange(from = 0.0f, to = 25.0f) float radius, int x, int y, int width, int height) {
        if (bitmapOriginal == null || bitmapOriginal.isRecycled())
            return null;
        RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(rs, bitmapOriginal);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        Bitmap blurredBitmap = Bitmap.createBitmap(bitmapOriginal.getWidth(), bitmapOriginal.getHeight(), bitmapOriginal.getConfig());
        output.copyTo(blurredBitmap);
        Canvas canvas = new Canvas(blurredBitmap);
        Bitmap clearSection = Bitmap.createBitmap(bitmapOriginal, x, y, width, height);
        canvas.drawBitmap(clearSection, x, y, null);
        return blurredBitmap;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
