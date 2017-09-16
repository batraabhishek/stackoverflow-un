package in.abhishek.stackoverflowunofficial.network;

/**
 * Created by abhishek on 16/09/17.
 */

public class NetworkResponse {

    private boolean mIsRequestSuccessful;
    private String mResponse;

    public NetworkResponse(boolean isRequestSuccessful, String response) {
        this.mIsRequestSuccessful = isRequestSuccessful;
        this.mResponse = response;
    }

    public boolean isRequestSuccessful() {
        return mIsRequestSuccessful;
    }

    public String getResponse() {
        return mResponse;
    }
}
