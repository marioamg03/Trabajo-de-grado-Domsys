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

public class Gas extends Fragment {

    private TextView viewGas;
    private ImageView imgGas;

    private DatabaseReference gasDatabase;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Gas() {
        // Required empty public constructor
    }

    public static Gas newInstance(String param1, String param2) {
        Gas fragment = new Gas();
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
        gasDatabase = FirebaseDatabase.getInstance().getReference().child("casa").child("status").child("co2");
        //Mantengo sincronizado el contenido de la app.
        gasDatabase.keepSynced(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gas, container, false);

        viewGas = (TextView) view.findViewById(R.id.valGas);

        imgGas = (ImageView) view.findViewById(R.id.imageView4);

        //Detectocambios en la base de datos.
        GasDataChange();

        return view;

    }

    private void GasDataChange(){

        // Chequeo gas
        gasDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambioG  = (String) dataSnapshot.getValue();
                cambio_ImgGas(valorCambioG);
                viewGas.setText(valorCambioG + " %");

            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void cambio_ImgGas(String gas){
        double temp = Double.parseDouble(gas);
        if (temp <= 18){
            imgGas.setImageResource(R.mipmap.gas1);
        }else if(temp > 18 && temp <=21){
            imgGas.setImageResource(R.mipmap.gas2);
        }else if(temp > 21 && temp <=24){
            imgGas.setImageResource(R.mipmap.gas3);
        }
    }



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
