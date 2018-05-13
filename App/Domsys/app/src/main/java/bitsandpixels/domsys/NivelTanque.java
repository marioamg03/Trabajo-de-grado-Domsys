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

public class NivelTanque extends Fragment {

    private TextView viewTanque;
    private ImageView imgTanque;

    private DatabaseReference tanqueDatabase;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NivelTanque() {
        // Required empty public constructor
    }

    public static NivelTanque newInstance(String param1, String param2) {
        NivelTanque fragment = new NivelTanque();
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
        tanqueDatabase = FirebaseDatabase.getInstance().getReference().child("casa").child("status").child("tanque");
        //Mantengo sincronizado el contenido de la app.
        tanqueDatabase.keepSynced(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nivel_tanque, container, false);

        viewTanque = (TextView) view.findViewById(R.id.textTanq);

        imgTanque = (ImageView) view.findViewById(R.id.imageTanq);

        //Detectocambios en la base de datos.
        TamqueChange();

        return view;
    }

    private void TamqueChange() {

        // Chequeo temperatura
        tanqueDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambioTanq  = (String) dataSnapshot.getValue();
                cambio_ImgTanq(valorCambioTanq);
                viewTanque.setText(valorCambioTanq + " %");

            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }


    private void cambio_ImgTanq(String tanque){
        double tanq = Double.parseDouble(tanque);
        if (tanq <= 15){
            imgTanque.setImageResource(R.mipmap.tanq0);
        }else if(tanq > 15 && tanq <=30){
            imgTanque.setImageResource(R.mipmap.tanq1);
        }else if(tanq > 30 && tanq <=50){
            imgTanque.setImageResource(R.mipmap.tanq2);
        }else if(tanq > 50 && tanq <=80){
            imgTanque.setImageResource(R.mipmap.tanq3);
        }else if(tanq > 80){
            imgTanque.setImageResource(R.mipmap.tanq4);
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
