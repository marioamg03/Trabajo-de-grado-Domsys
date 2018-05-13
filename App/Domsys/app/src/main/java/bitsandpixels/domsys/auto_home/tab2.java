package bitsandpixels.domsys.auto_home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import bitsandpixels.domsys.R;

public class tab2 extends Fragment {

    private Button btnVentilacion, btnNivel;
    private Switch switchVentilacion, switchNivel;
    private TextView viewVentilacion, viewNivel;
    private SeekBar seekBarVentilacion, seekBarNivel;


    private DatabaseReference autoHome;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_auto_home_tab2, container, false);

        switchVentilacion = (Switch) rootView.findViewById(R.id.switch_auto_ventilacion);
        switchNivel = (Switch) rootView.findViewById(R.id.switch_auto_nivel);

        btnVentilacion = (Button) rootView.findViewById(R.id.btn_ventilacion);
        btnNivel = (Button) rootView.findViewById(R.id.btn_nvl_tanque);

        seekBarVentilacion = (SeekBar)rootView.findViewById(R.id.seekBar_ventilacion);
        seekBarNivel = (SeekBar)rootView.findViewById(R.id.seekBar_nivel_tanque);

        viewVentilacion = (TextView) rootView.findViewById(R.id.text_ventilacion);
        viewNivel = (TextView) rootView.findViewById(R.id.text_nivel_tanque);

        seekBarVentilacion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                cambioVentilacionDatabase(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        seekBarNivel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                cambiNivelDatabase(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        DataChange();
        Switchs_cambios();

        return rootView;
    }

    private void cambiNivelDatabase(int progress) {

        String dato = String.valueOf(progress);
        autoHome.child("regulacion bomba").child("set_point").setValue(dato);
        viewNivel.setText("Nivel del Tanque: " + dato);
        Toast.makeText(getActivity(), "Valor Asignado!", Toast.LENGTH_SHORT).show();

    }

    private void cambioVentilacionDatabase(int progress) {

        String dato = String.valueOf(progress);
        autoHome.child("ventilacion").child("set_point").setValue(dato);
        viewVentilacion.setText("Regulacion de ventilacion: " + dato);
        Toast.makeText(getActivity(), "Valor Asignado!", Toast.LENGTH_SHORT).show();

    }

    private void Switchs_cambios() {
        switchVentilacion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    autoHome.child("ventilacion").child("habilitado").setValue("encendido");
                    btnVentilacion.setVisibility(View.VISIBLE);
                    viewVentilacion.setVisibility(View.VISIBLE);
                    seekBarVentilacion.setVisibility(View.VISIBLE);
                } else {
                    autoHome.child("ventilacion").child("habilitado").setValue("apagado");
                    btnVentilacion.setVisibility(View.INVISIBLE);
                    viewVentilacion.setVisibility(View.INVISIBLE);
                    seekBarVentilacion.setVisibility(View.INVISIBLE);
                }
            }
        });

        switchNivel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    autoHome.child("regulacion bomba").child("habilitado").setValue("encendido");
                    btnNivel.setVisibility(View.VISIBLE);
                    viewNivel.setVisibility(View.VISIBLE);
                    seekBarNivel.setVisibility(View.VISIBLE);
                } else {
                    autoHome.child("regulacion bomba").child("habilitado").setValue("apagado");
                    btnNivel.setVisibility(View.INVISIBLE);
                    viewNivel.setVisibility(View.INVISIBLE);
                    seekBarNivel.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    private void DataChange() {
        // Chequeo luces AUTO
        autoHome.child("ventilacion").child("habilitado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio = (String) dataSnapshot.getValue();
                if (valorCambio.equals("encendido")) {
                    switchVentilacion.setChecked(true);
                    btnVentilacion.setVisibility(View.VISIBLE);
                    viewVentilacion.setVisibility(View.VISIBLE);
                    seekBarVentilacion.setVisibility(View.VISIBLE);
                } else {
                    switchVentilacion.setChecked(false);
                    btnVentilacion.setVisibility(View.INVISIBLE);
                    viewVentilacion.setVisibility(View.INVISIBLE);
                    seekBarVentilacion.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        autoHome.child("regulacion bomba").child("habilitado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio = (String) dataSnapshot.getValue();
                if (valorCambio.equals("encendido")) {
                    switchNivel.setChecked(true);
                    btnNivel.setVisibility(View.VISIBLE);
                    viewNivel.setVisibility(View.VISIBLE);
                    seekBarNivel.setVisibility(View.VISIBLE);
                } else {
                    switchNivel.setChecked(false);
                    btnNivel.setVisibility(View.INVISIBLE);
                    viewNivel.setVisibility(View.INVISIBLE);
                    seekBarNivel.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Ubicacion de los datos en la base
        autoHome = FirebaseDatabase.getInstance().getReference().child("casa").child("auto-mode");
        //Mantengo sincronizado el contenido de la app.
        autoHome.keepSynced(true);

    }
}
