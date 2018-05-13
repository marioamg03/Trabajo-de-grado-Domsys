package bitsandpixels.domsys;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class TempHumed extends Fragment {


    private TextView viewTemp;
    private TextView viewHum;
    private ImageView imgtemp;
    private ImageView imghumd;

    private DatabaseReference temperaturaDatabase;
    private DatabaseReference humedadDatabase;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TempHumed() {
        // Required empty public constructor
    }

    public static TempHumed newInstance(String param1, String param2) {
        TempHumed fragment = new TempHumed();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //Ubicacion de los datos en la base
        temperaturaDatabase = FirebaseDatabase.getInstance().getReference().child("casa").child("status").child("temperatura");
        //Mantengo sincronizado el contenido de la app.
        temperaturaDatabase.keepSynced(true);

        //Ubicacion de los datos en la base
        humedadDatabase = FirebaseDatabase.getInstance().getReference().child("casa").child("status").child("humedad");
        //Mantengo sincronizado el contenido de la app.
        humedadDatabase.keepSynced(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temp_humed, container, false);

        viewTemp = (TextView) view.findViewById(R.id.valTemp);
        viewHum = (TextView) view.findViewById(R.id.valHum);

        imgtemp = (ImageView) view.findViewById(R.id.imageView2);
        imghumd = (ImageView) view.findViewById(R.id.imageView3);

        //Detectocambios en la base de datos.
        TempHumDataChange();

        return view;
    }

    private void TempHumDataChange(){

        // Chequeo temperatura
        temperaturaDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambioT  = (String) dataSnapshot.getValue();
                cambio_ImgTemp(valorCambioT);
                viewTemp.setText(valorCambioT + " °C");

            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        // Chequeo humedad
        humedadDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambioH  = (String) dataSnapshot.getValue();
                cambio_ImgHumd(valorCambioH);
                viewHum.setText(valorCambioH + " %");

            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void cambio_ImgTemp(String temperatura){
        double temp = Double.parseDouble(temperatura);
        if (temp <= 18){
            imgtemp.setImageResource(R.mipmap.temperatura1);
        }else if(temp > 18 && temp <=21){
            imgtemp.setImageResource(R.mipmap.temperatura2);
        }else if(temp > 21 && temp <=24){
            imgtemp.setImageResource(R.mipmap.temperatura3);
        }else if(temp > 24 && temp <=26){
            imgtemp.setImageResource(R.mipmap.temperatura4);
        }else if(temp > 26){
            imgtemp.setImageResource(R.mipmap.temperatura5);
        }
    }


    private void cambio_ImgHumd(String humedad){
        double humd = Double.parseDouble(humedad);
        if (humd <= 50){
            imghumd.setImageResource(R.mipmap.humedad1);
        }else if(humd > 50 && humd <=65){
            imghumd.setImageResource(R.mipmap.humedad2);
        }else if(humd > 65 && humd <=70){
            imghumd.setImageResource(R.mipmap.humedad3);
        }else if(humd > 70 && humd <=75){
            imghumd.setImageResource(R.mipmap.humedad4);
        }else if(humd > 75){
            imghumd.setImageResource(R.mipmap.humedad5);
        }
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
