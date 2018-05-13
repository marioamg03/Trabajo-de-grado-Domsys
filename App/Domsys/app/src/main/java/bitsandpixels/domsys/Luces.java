package bitsandpixels.domsys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Luces extends Fragment {

    private Switch switchBano;
    private Switch switchCocina;
    private Switch switchCuarto;
    private Switch switchGaraje;
    private Switch switchPatio;
    private Switch switchSala;

    private ImageView imgluz;

    private DatabaseReference lucesDatabase;
    private DatabaseReference luzADatabase;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Luces() {
        // Required empty public constructor
    }

    public static Luces newInstance(String param1, String param2) {
        Luces fragment = new Luces();
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
        lucesDatabase = FirebaseDatabase.getInstance().getReference().child("casa").child("status").child("luces");
        luzADatabase = FirebaseDatabase.getInstance().getReference().child("casa").child("status").child("validacionluz");
        //Mantengo sincronizado el contenido de la app.
        lucesDatabase.keepSynced(true);
        luzADatabase.keepSynced(true);

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_luces, container, false);

        switchBano = (Switch) view.findViewById(R.id.switchbano);
        switchCocina = (Switch) view.findViewById(R.id.switchcocina);
        switchCuarto = (Switch) view.findViewById(R.id.switchcuarto);
        switchGaraje = (Switch) view.findViewById(R.id.switchgaraje);
        switchPatio = (Switch) view.findViewById(R.id.switchpatio);
        switchSala = (Switch) view.findViewById(R.id.switchsala);

        imgluz = (ImageView) view.findViewById(R.id.imageView6);

        //Detectocambios en la base de datos.
        LucesDataChange(view);

        Switchs_cambios();

        return view;
    }

    // Verifica el estado de la base de datos y coloca los switchs en posicion.
    private void LucesDataChange(final View view){
        // Chequeo luces BANO
        lucesDatabase.child("bano").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                if (valorCambio.equals("encendido")){
                    switchBano.setChecked(true);
                }else{
                    switchBano.setChecked(false);
                }
                luzimg();
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        // Chequeo luces COCINA
        lucesDatabase.child("cocina").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                if (valorCambio.equals("encendido")){
                    switchCocina.setChecked(true);
                }else{
                    switchCocina.setChecked(false);
                }
                luzimg();
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        // Chequeo luces CUARTO
        lucesDatabase.child("cuarto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                if (valorCambio.equals("encendido")){
                    switchCuarto.setChecked(true);
                }else{
                    switchCuarto.setChecked(false);
                }
                luzimg();
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        luzADatabase.child("cuarto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                if (valorCambio.equals("encendido")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        switchCuarto.setThumbTintList(getResources().getColorStateList(R.color.colorPrimaryDarkL));
                    }
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                        switchCuarto.setThumbTintList(getResources().getColorStateList(R.color.colorAccent));
                    }
                }
                luzimg();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        // Chequeo luces GARAJE
        lucesDatabase.child("garaje").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                if (valorCambio.equals("encendido")){
                    switchGaraje.setChecked(true);
                }else{
                    switchGaraje.setChecked(false);
                }
                luzimg();
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        // Chequeo luces PATIO
        lucesDatabase.child("patio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                if (valorCambio.equals("encendido")){
                    switchPatio.setChecked(true);
                }else{
                    switchPatio.setChecked(false);
                }
                luzimg();
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        // Chequeo luces SALA
        lucesDatabase.child("sala").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                if (valorCambio.equals("encendido")){
                    switchSala.setChecked(true);
                }else {
                    switchSala.setChecked(false);
                }
                luzimg();
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    // Hace los cambios en la base de datos
    private void Switchs_cambios() {

        switchBano.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    lucesDatabase.child("bano").setValue("encendido");
                }else{
                    lucesDatabase.child("bano").setValue("apagado");
                }
            }
        });

        switchCocina.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    lucesDatabase.child("cocina").setValue("encendido");
                }else{
                    lucesDatabase.child("cocina").setValue("apagado");
                }
            }
        });

        switchCuarto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    lucesDatabase.child("cuarto").setValue("encendido");
                }else{
                    lucesDatabase.child("cuarto").setValue("apagado");
                }
            }
        });

        switchGaraje.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    lucesDatabase.child("garaje").setValue("encendido");
                }else{
                    lucesDatabase.child("garaje").setValue("apagado");
                }
            }
        });

        switchPatio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    lucesDatabase.child("patio").setValue("encendido");
                }else{
                    lucesDatabase.child("patio").setValue("apagado");
                }
            }
        });

        switchSala.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    lucesDatabase.child("sala").setValue("encendido");
                }else{
                    lucesDatabase.child("sala").setValue("apagado");
                }
            }
        });
    }

    private void luzimg(){
        if (switchBano.isChecked() && switchSala.isChecked() && switchPatio.isChecked() && switchCocina.isChecked() && switchGaraje.isChecked() && switchCuarto.isChecked()){
            imgluz.setImageResource(R.mipmap.lenc);
        }else{
            imgluz.setImageResource(R.mipmap.lapg);
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
        void onFragmentInteraction(Uri uri);
    }
}
