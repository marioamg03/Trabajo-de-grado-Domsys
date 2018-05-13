package bitsandpixels.domsys;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Seguridad extends Fragment {

    private Switch switchAlarma;
    private DatabaseReference seguridadDatabase;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView imgseg;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Seguridad() {

    }

    public static Seguridad newInstance(String param1, String param2) {
        Seguridad fragment = new Seguridad();
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
        seguridadDatabase = FirebaseDatabase.getInstance().getReference().child("casa").child("status").child("seguridad");
        //Mantengo sincronizado el contenido de la app.
        seguridadDatabase.keepSynced(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seguridad, container, false);

        switchAlarma = (Switch) view.findViewById(R.id.switchSeguridad);
        imgseg = (ImageView) view.findViewById(R.id.imageView5);

        //Detectocambios en la base de datos.
          SeguridadDataChange();

          Switch_cambio();

        return view;
    }

    private void SeguridadDataChange() {
        // Chequeo el estado de la alarma
        seguridadDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                if (valorCambio.equals("activa")){
                    switchAlarma.setChecked(true);
                    if(getActivity()!=null){
                        imgseg.setImageResource(R.mipmap.son);
                    }
                }else{
                    switchAlarma.setChecked(false);
                    if(getActivity()!=null){
                        imgseg.setImageResource(R.mipmap.soff);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void Switch_cambio() {
        switchAlarma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    seguridadDatabase.setValue("activa");
                }else{
                    seguridadDatabase.setValue("desactiva");
                }
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
