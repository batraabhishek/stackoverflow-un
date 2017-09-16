package in.abhishek.stackoverflowunofficial.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Locale;

import in.abhishek.stackoverflowunofficial.interfaces.NetworkCallback;
import in.abhishek.stackoverflowunofficial.network.NetworkTask;


/**
 * Created by abhishek on 16/09/17
 */

public class NetworkFragment extends Fragment {

    private static final String FILTER = "!-MOiNm40F1UCMVj-.1dRhCUPR.**(d.Zw";
    private static final String STACH_URL = "https://api.stackexchange.com/2.2/search" +
            "/advanced?pagesize=100&order=desc&sort=activity&q=%s" +
            "&site=stackoverflow&filter=%s";


    private static final String TAG = "NetworkFragment";
    private String mUrlString;
    private NetworkCallback mCallback;
    private NetworkTask mDownloadTask;


    public static NetworkFragment getInstance(FragmentManager fragmentManager) {

        NetworkFragment networkFragment = (NetworkFragment) fragmentManager
                .findFragmentByTag(TAG);

        if (networkFragment == null) {
            networkFragment = new NetworkFragment();
            Bundle args = new Bundle();
            networkFragment.setArguments(args);
            fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
        }
        return networkFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (NetworkCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onDestroy() {
        cancelFetch();
        super.onDestroy();
    }

    public void fetchResult(String query) {
        cancelFetch();

        String url = String.format(Locale.getDefault(), STACH_URL, query, FILTER);
        mDownloadTask = new NetworkTask(mCallback);
        mDownloadTask.execute(url);
    }

    public void cancelFetch() {
        if (mDownloadTask != null) {
            mDownloadTask.cancel(true);
        }
    }

}
