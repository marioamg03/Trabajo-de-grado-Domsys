package bitsandpixels.domsys;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Ventilacion extends Fragment {

    private TextView viewVentilacion;
    private ImageView imgVentilacion;

    private Animation rotate;
    private SeekBar simpleSeekBar;


    private DatabaseReference ventilacionDatabase;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Ventilacion() {
    }

    public static Ventilacion newInstance(String param1, String param2) {
        Ventilacion fragment = new Ventilacion();
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
        ventilacionDatabase = FirebaseDatabase.getInstance().getReference().child("casa").child("status").child("ventilacion");
        //Mantengo sincronizado el contenido de la app.
        ventilacionDatabase.keepSynced(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if(getActivity()!=null) {
            rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotation);
        }
        View view = inflater.inflate(R.layout.fragment_ventilacion, container, false);

        simpleSeekBar =(SeekBar) view.findViewById(R.id.seekBar);

        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                cambio_ProgresoDatabase(progress);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });






        viewVentilacion = (TextView) view.findViewById(R.id.valVenti);

        imgVentilacion = (ImageView) view.findViewById(R.id.imageView4);

        //Detectocambios en la base de datos.
        VentilacionDataChange();

        return view;

    }


    private void VentilacionDataChange(){

        // Chequeo temperatura
        ventilacionDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambioV  = (String) dataSnapshot.getValue();
                cambio_ImgVent(valorCambioV);


            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void cambio_ImgVent(String ventilacion){

        int vent = Integer.parseInt(ventilacion);
        simpleSeekBar.setProgress(vent);

        if (vent > 0 &&vent <= 20){
            rotate.setDuration(8000);
            imgVentilacion.startAnimation(rotate);
        }else if(vent > 20 && vent <=40){
            rotate.setDuration(6000);
            imgVentilacion.startAnimation(rotate);
        }else if(vent > 40 && vent <=60){
            rotate.setDuration(3000);
            imgVentilacion.startAnimation(rotate);
        }else if(vent > 60 && vent <=80){
            rotate.setDuration(1000);
            imgVentilacion.startAnimation(rotate);
        }else if(vent > 80){
            rotate.setDuration(100);
            imgVentilacion.startAnimation(rotate);
        }else if(vent == 0){
            imgVentilacion.clearAnimation();
        }

        viewVentilacion.setText(ventilacion + " %");
    }

    private void cambio_ProgresoDatabase(int progreso){
        String dato = String.valueOf(progreso);
        cambio_ImgVent(dato);
        ventilacionDatabase.setValue(dato);
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
