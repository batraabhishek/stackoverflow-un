package in.abhishek.stackoverflowunofficial.interfaces;

import android.net.NetworkInfo;

/**
 * Created by abhishek on 16/09/17
 */

public interface NetworkCallback {

    NetworkInfo getActiveNetworkInfo();
    void onError(int errorCode, String errorMessage);
    void onResult(String response);

}
