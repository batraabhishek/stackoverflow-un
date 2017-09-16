package in.abhishek.stackoverflowunofficial.network;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import in.abhishek.stackoverflowunofficial.interfaces.NetworkCallback;
import in.abhishek.stackoverflowunofficial.utils.NetworkUtil;

/**
 * Created by abhishek on 16/09/17
 */

public class NetworkTask extends AsyncTask<String, Void, NetworkResponse> {


    private NetworkCallback mCallback;

    public NetworkTask(NetworkCallback networkCallback) {
        this.mCallback = networkCallback;
    }

    @Override
    protected void onPreExecute() {
        if (mCallback != null) {
            NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected() ||
                    (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                            && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                mCallback.onError(1, "Not connected to internet");
                cancel(true);
            }
        }
    }


    @Override
    protected NetworkResponse doInBackground(String... urls) {
        NetworkResponse networkResponse = null;

        if (!isCancelled() && urls != null && urls.length > 0) {
            String urlString = urls[0];
            InputStream stream = null;
            HttpsURLConnection connection = null;
            String result;
            try {
                URL url = new URL(urlString.replace(" ", "%20"));
                connection = (HttpsURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                // Retrieve the response body as an InputStream.
                stream = connection.getInputStream();
                if (stream != null) {
                    result = NetworkUtil.readStream(stream, 1000000);
                    networkResponse = new NetworkResponse(true, result);
                } else {
                    networkResponse = new NetworkResponse(true, "Got no response from server");
                }
            } catch (IOException e) {
                if (mCallback != null) {
                    networkResponse = new NetworkResponse(false, e.getMessage());
                }
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        // Do nothing
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } else {
            networkResponse = new NetworkResponse(false, "Request cancelled");
        }

        return networkResponse;
    }

    @Override
    protected void onPostExecute(NetworkResponse networkResponse) {
        if(networkResponse != null && mCallback !=  null) {
            if(networkResponse.isRequestSuccessful()) {
                mCallback.onResult(networkResponse.getResponse());
            } else {
                mCallback.onError(0, networkResponse.getResponse());
            }
        }
    }
}
