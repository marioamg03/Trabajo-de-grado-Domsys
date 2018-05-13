package bitsandpixels.domsys;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import at.markushi.ui.CircleButton;

public class Garaje extends Fragment {

    private CircleButton botonPuerta;

    private DatabaseReference garajeDatabase;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Garaje() {

    }

    public static Garaje newInstance(String param1, String param2) {
        Garaje fragment = new Garaje();
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
        garajeDatabase = FirebaseDatabase.getInstance().getReference().child("casa").child("status").child("pgaraje");
        //Mantengo sincronizado el contenido de la app.
        garajeDatabase.keepSynced(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_garaje, container, false);

        botonPuerta = (CircleButton) view.findViewById(R.id.botonP);
        //Detectocambios en la base de datos.
        Puerta_DataChange();
        Puerta_presionar();

        return view;
    }

    private void Puerta_presionar() {
    botonPuerta.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            garajeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String valorCambio  = (String) dataSnapshot.getValue();
                    if (valorCambio.equals("abierta")){
                        garajeDatabase.setValue("cerrada");
                    }else{
                        garajeDatabase.setValue("abierta");
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(getActivity(),"ERROR BASE DE DATOS",Toast.LENGTH_SHORT).show();
                }
            });
        }
    });


    }

    private void Puerta_DataChange(){
        // Chequeo puerta
        garajeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                if (valorCambio.equals("abierta")){
                    if(getActivity()!=null){
                        botonPuerta.setColor(getResources().getColor(R.color.colorPrimary));
                        botonPuerta.setImageResource(R.mipmap.pabierto);
                    }

                }else{
                    if(getActivity()!=null){
                        botonPuerta.setColor(getResources().getColor(R.color.colorPrimaryDark));
                        botonPuerta.setImageResource(R.mipmap.pcerrado);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
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
        void onFragmentInteraction(Uri uri);
    }
}
