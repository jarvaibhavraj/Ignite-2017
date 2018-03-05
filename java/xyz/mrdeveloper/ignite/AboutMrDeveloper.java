package xyz.mrdeveloper.ignite;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutMrDeveloper extends Fragment {

    TextView textViewDev1;
    TextView textViewDev2;
    TextView textViewDev3;
    TextView textViewDev4;
    TextView textViewDev5;
    TextView textViewDev6;

    TextView textViewEmail;

    public AboutMrDeveloper() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.about_mrdeveloper, null);

        textViewDev1 = (TextView) view.findViewById(R.id.textdev1);
        textViewDev2 = (TextView) view.findViewById(R.id.textdev2);
        textViewDev3 = (TextView) view.findViewById(R.id.textdev3);
        textViewDev4 = (TextView) view.findViewById(R.id.textdev4);
        textViewDev5 = (TextView) view.findViewById(R.id.textdev5);
        textViewDev6 = (TextView) view.findViewById(R.id.textdev6);

//        textViewEmail = (TextView) view.findViewById(R.id.textViewEmail);
//        textViewEmail.setText(Html.fromHtml(getString(R.string.email_us)));
//        textViewEmail.setMovementMethod(LinkMovementMethod.getInstance());

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Quicksand-Regular.ttf");

        textViewDev1.setTypeface(typeface);

        return view;
    }

}
