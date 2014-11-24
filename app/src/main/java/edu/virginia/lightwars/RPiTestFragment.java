package edu.virginia.lightwars;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class RPiTestFragment extends Fragment {

    public RPiTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rpi_test, container, false);

        final EditText ip_address_edit = (EditText) v.findViewById(R.id.ip_address_edit);
        final EditText json_edit       = (EditText) v.findViewById(R.id.json_edit);

        Button post_button = (Button) v.findViewById(R.id.post_button);
        post_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View V)
            {
                PostService.startRPiPOST(getActivity(), ip_address_edit.getText().toString(),json_edit.getText().toString());
            }
        });

        Button random_button = (Button) v.findViewById(R.id.random_button);
        random_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View V)
            {
                json_edit.setText( LEDJSON.getRandomJSON() );
            }
        });


        return v;
    }


}
