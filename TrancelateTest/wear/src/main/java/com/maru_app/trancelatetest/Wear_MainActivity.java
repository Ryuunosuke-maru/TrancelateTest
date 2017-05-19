package com.maru_app.trancelatetest;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class Wear_MainActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear__main);
        final GridViewPager gvp = (GridViewPager) findViewById(R.id.gridPageView);
        gvp.setAdapter(new GridViewPagerAdapter(this.getFragmentManager()));
    }

    static class GridViewPagerAdapter extends FragmentGridPagerAdapter {

        public GridViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getFragment(int row, int col) {
            return MyFragment.newInstance();
        }

        @Override
        public int getRowCount() {
            return 1;
        }

        @Override
        public int getColumnCount(int i) {
            return 1;
        }
    }

    public static class MyFragment extends CardFragment {
        private GoogleApiClient client;

        @Override
        public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            this.client = new GoogleApiClient.Builder(this.getActivity())
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            Log.d("MyFragment", "onConnected");
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            Log.d("MyFragment", "onConnectionSuspended");
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            Log.d("MyFragment", "onConnectionFailed");
                        }
                    })
                    .addApi(Wearable.API)
                    .build();
            this.client.connect();

            Button button = new Button(this.getActivity());
            button.setText("OK");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("MyFragment", "onClick");
                            final String message = "Hello world";
                            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(client).await();
                            for (Node node : nodes.getNodes()) {
                                MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                                        client,
                                        node.getId(),
                                        "/hello",
                                        message.getBytes())
                                        .await();
                                if (result.getStatus().isSuccess()) {
                                    Log.d("onClick", "isSuccess is true");
                                } else {
                                    Log.d("onClick", "isSuccess is false");
                                }
                            }
                        }
                    }).start();
                }
            });
            return button;
        }

        public static MyFragment newInstance() {
            return new MyFragment();
        }
    }
}
