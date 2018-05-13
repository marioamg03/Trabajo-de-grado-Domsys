package bitsandpixels.domsys;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import at.markushi.ui.CircleButton;

public class Aspersores extends Fragment {

    private CircleButton botonAspersores;

    private DatabaseReference aspersoresDatabase;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Aspersores() {
    }

    public static Aspersores newInstance(String param1, String param2) {
        Aspersores fragment = new Aspersores();
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
        aspersoresDatabase = FirebaseDatabase.getInstance().getReference().child("casa").child("status").child("aspersores");
        //Mantengo sincronizado el contenido de la app.
        aspersoresDatabase.keepSynced(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_aspersores, container, false);

        botonAspersores = (CircleButton) view.findViewById(R.id.botonA);
        //Detectocambios en la base de datos.
        aspersores_DataChange();
        aspersores_presionar();

        return view;
    }


    private void aspersores_presionar() {
        botonAspersores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aspersoresDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String valorCambio  = (String) dataSnapshot.getValue();
                        if (valorCambio.equals("activados")){
                            aspersoresDatabase.setValue("desactivados");
                        }else{
                            aspersoresDatabase.setValue("activados");
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

    private void aspersores_DataChange(){
        // Chequeo puerta
        aspersoresDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                if (valorCambio.equals("activados")){
                    if(getActivity()!=null){
                        botonAspersores.setColor(getResources().getColor(R.color.colorPrimary));
                        botonAspersores.setImageResource(R.mipmap.aspe);
                    }

                }else{
                    if(getActivity()!=null){
                        botonAspersores.setColor(getResources().getColor(R.color.colorPrimaryDark));
                        botonAspersores.setImageResource(R.mipmap.aspa);
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
