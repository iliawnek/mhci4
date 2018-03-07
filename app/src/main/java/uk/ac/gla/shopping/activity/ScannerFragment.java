package uk.ac.gla.shopping.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;

import devliving.online.mvbarcodereader.BarcodeCaptureFragment;
import devliving.online.mvbarcodereader.MVBarcodeScanner;
import uk.ac.gla.shopping.R;

public class ScannerFragment extends Fragment {

    private Barcode currentBarcode;

    public static ScannerFragment newInstance() {
        ScannerFragment fragment = new ScannerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MVBarcodeScanner.ScanningMode mode = MVBarcodeScanner.ScanningMode.SINGLE_AUTO;
        @MVBarcodeScanner.BarCodeFormat int[] formats = {Barcode.PRODUCT, Barcode.UPC_A, Barcode.UPC_E, Barcode.EAN_8, Barcode.EAN_13};
        BarcodeCaptureFragment captureFragment = BarcodeCaptureFragment.instantiate(mode, formats);

        FragmentActivity mainActivity = getActivity();

        if (currentBarcode == null) {
            FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, captureFragment);
            transaction.commit();
            Toast toast = Toast.makeText(mainActivity.getApplicationContext(), "Position the product barcode inside the viewfinder to scan",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 300);
            toast.show();
        }

        captureFragment.setListener(new BarcodeCaptureFragment.BarcodeScanningListener() {
            @Override
            public void onBarcodeScanned(Barcode barcode) {
                currentBarcode = barcode;
                FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, ScannerFragment.this);
                transaction.commit();
            }

            @Override
            public void onBarcodesScanned(List<Barcode> barcodes) {
                // Not called in single auto mode
            }

            @Override
            public void onBarcodeScanningFailed(String reason) {
                Log.d("barcodeFail", reason);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scanner, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView titleText = view.findViewById(R.id.productTitle);
        TextView descriptionText = view.findViewById(R.id.productDescription);
        ImageView thumbnailImage = view.findViewById(R.id.productThumbnail);

        if (currentBarcode != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
            String url = "https://api.upcitemdb.com/prod/trial/lookup";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("upc", currentBarcode.displayValue);
            JsonObjectRequest upcLookupRequest = new JsonObjectRequest(url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String title = "Unknown product";
                    String description = "";
                    String image = null;
                    try {
                        JSONObject match = response.getJSONArray("items").getJSONObject(0);
                        title = match.getString("title");
                        description = match.getString("description");
                        image = match.getJSONArray("images").getString(0);
                    } catch (JSONException e) {
                        // Response error
                        Log.d("LookupUPC", e.getMessage());
                    }

                    if (image != null) {
                        new DownloadImageTask(thumbnailImage)
                                .execute(image);
                    }

                    titleText.setText(title);
                    descriptionText.setText(description);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // error
                }
            });

            requestQueue.add(upcLookupRequest);
        }

        view.findViewById(R.id.rescanButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new ScannerFragment()).commit();
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
