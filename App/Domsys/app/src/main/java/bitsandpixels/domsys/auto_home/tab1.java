package bitsandpixels.domsys.auto_home;

import android.app.TimePickerDialog;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import bitsandpixels.domsys.R;

public class tab1 extends Fragment{

     Button btnHoraLuces, btnHoraAspersor, btnHoraPatio;
     TextInputEditText horaInicioLuces, horaInicioAspersores, horaInicioPatio;
     TextInputEditText horaFinLuces, horaFinAspersores, horaFinPatio;
     TextInputLayout contenedor1, contenedor2, contenedor3, contenedor4, contenedor5, contenedor6;

     final Calendar c = Calendar.getInstance();
     private int hora,minutos;
     private Switch switchLuces, switchAspersor, switchPatio;
     private DatabaseReference autoHome;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_auto_home_tab1, container, false);

        switchLuces = (Switch) rootView.findViewById(R.id.switch_auto_luces);
        switchAspersor = (Switch) rootView.findViewById(R.id.switch_auto_aspersores);
        switchPatio = (Switch) rootView.findViewById(R.id.switch_auto_patio);

        btnHoraLuces = (Button) rootView.findViewById(R.id.btn_luces);
        btnHoraAspersor = (Button) rootView.findViewById(R.id.btn_aspersores);
        btnHoraPatio = (Button) rootView.findViewById(R.id.btn_patio);

        horaInicioLuces = (TextInputEditText) rootView.findViewById(R.id.hora_encendido_luces);
        horaInicioAspersores = (TextInputEditText) rootView.findViewById(R.id.hora_encendido_aspersor);
        horaInicioPatio = (TextInputEditText) rootView.findViewById(R.id.hora_encendido_patio);

        horaFinLuces = (TextInputEditText) rootView.findViewById(R.id.hora_apagado_luces);
        horaFinAspersores = (TextInputEditText) rootView.findViewById(R.id.hora_apagado_aspersor);
        horaFinPatio = (TextInputEditText) rootView.findViewById(R.id.hora_apagado_patio);

        contenedor1 = (TextInputLayout) rootView.findViewById(R.id.textInputLayout);
        contenedor2 = (TextInputLayout) rootView.findViewById(R.id.textInputLayout2);
        contenedor3 = (TextInputLayout) rootView.findViewById(R.id.textInputLayout3);
        contenedor4 = (TextInputLayout) rootView.findViewById(R.id.textInputLayout4);
        contenedor5 = (TextInputLayout) rootView.findViewById(R.id.textInputLayout5);
        contenedor6 = (TextInputLayout) rootView.findViewById(R.id.textInputLayout6);

        btnHoraLuces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asignarHorasLuces();
            }
        });

        btnHoraAspersor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asignarHorasAspersores();
            }
        });

        btnHoraPatio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asignarHorasPatio();
            }
        });


        autoLucesProcedimiento();

        //Detectocambios en la base de datos.
        LucesDataChange();
        chequeo();
        Switchs_cambios();

        return rootView;
    }




    private void chequeo(){

        autoHome.child("luces").child("apagado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                horaFinLuces.setText(valorCambio);
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        autoHome.child("luces").child("encendido").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                horaInicioLuces.setText(valorCambio);
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        autoHome.child("patio").child("apagado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                horaFinPatio.setText(valorCambio);
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        autoHome.child("patio").child("encendido").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                horaInicioPatio.setText(valorCambio);
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        autoHome.child("aspersores").child("apagado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                horaFinAspersores.setText(valorCambio);
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        autoHome.child("aspersores").child("encendido").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                horaInicioAspersores.setText(valorCambio);
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void asignarHorasLuces() {

        String horaEncendido;
        String horaApagado;

        horaEncendido = horaInicioLuces.getText().toString();
        horaApagado = horaFinLuces.getText().toString();

        autoHome.child("luces").child("encendido").setValue(horaEncendido);
        autoHome.child("luces").child("apagado").setValue(horaApagado);

        Toast.makeText(getActivity(), "Horas Asignadas!", Toast.LENGTH_SHORT).show();
    }

    private void asignarHorasAspersores() {

        String horaEncendido;
        String horaApagado;

        horaEncendido = horaInicioAspersores.getText().toString();
        horaApagado = horaFinAspersores.getText().toString();

        autoHome.child("aspersores").child("encendido").setValue(horaEncendido);
        autoHome.child("aspersores").child("apagado").setValue(horaApagado);
        Toast.makeText(getActivity(), "Horas Asignadas!", Toast.LENGTH_SHORT).show();
    }

    private void asignarHorasPatio() {

        String horaEncendido;
        String horaApagado;

        horaEncendido = horaInicioPatio.getText().toString();
        horaApagado = horaFinPatio.getText().toString();

        autoHome.child("patio").child("encendido").setValue(horaEncendido);
        autoHome.child("patio").child("apagado").setValue(horaApagado);
        Toast.makeText(getActivity(), "Horas Asignadas!", Toast.LENGTH_SHORT).show();
    }

    private void Switchs_cambios() {
        switchLuces.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    autoHome.child("luces").child("habilitado").setValue("encendido");
                    btnHoraLuces.setVisibility(View.VISIBLE);
                    contenedor1.setVisibility(View.VISIBLE);
                    contenedor2.setVisibility(View.VISIBLE);
                }else{
                    autoHome.child("luces").child("habilitado").setValue("apagado");
                    btnHoraLuces.setVisibility(View.INVISIBLE);
                    contenedor1.setVisibility(View.INVISIBLE);
                    contenedor2.setVisibility(View.INVISIBLE);
                }
            }
        });

        switchAspersor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    autoHome.child("aspersores").child("habilitado").setValue("encendido");
                    btnHoraAspersor.setVisibility(View.VISIBLE);
                    contenedor3.setVisibility(View.VISIBLE);
                    contenedor4.setVisibility(View.VISIBLE);
                }else{
                    autoHome.child("aspersores").child("habilitado").setValue("apagado");
                    btnHoraAspersor.setVisibility(View.INVISIBLE);
                    contenedor3.setVisibility(View.INVISIBLE);
                    contenedor4.setVisibility(View.INVISIBLE);
                }
            }
        });

        switchPatio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    autoHome.child("patio").child("habilitado").setValue("encendido");
                    btnHoraPatio.setVisibility(View.VISIBLE);
                    contenedor5.setVisibility(View.VISIBLE);
                    contenedor6.setVisibility(View.VISIBLE);
                }else{
                    autoHome.child("patio").child("habilitado").setValue("apagado");
                    btnHoraPatio.setVisibility(View.INVISIBLE);
                    contenedor5.setVisibility(View.INVISIBLE);
                    contenedor6.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void LucesDataChange() {
        // Chequeo luces AUTO
        autoHome.child("luces").child("habilitado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                if (valorCambio.equals("encendido")){
                    switchLuces.setChecked(true);
                    btnHoraLuces.setVisibility(View.VISIBLE);
                    contenedor1.setVisibility(View.VISIBLE);
                    contenedor2.setVisibility(View.VISIBLE);
                }else{
                    switchLuces.setChecked(false);
                    btnHoraLuces.setVisibility(View.INVISIBLE);
                    contenedor1.setVisibility(View.INVISIBLE);
                    contenedor2.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        // Chequeo luces ASPERSOR
        autoHome.child("aspersores").child("habilitado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                if (valorCambio.equals("encendido")){
                    switchAspersor.setChecked(true);
                    btnHoraAspersor.setVisibility(View.VISIBLE);
                    contenedor3.setVisibility(View.VISIBLE);
                    contenedor4.setVisibility(View.VISIBLE);
                }else{
                    switchAspersor.setChecked(false);
                    btnHoraAspersor.setVisibility(View.INVISIBLE);
                    contenedor3.setVisibility(View.INVISIBLE);
                    contenedor4.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        // Chequeo luces PATIO
        autoHome.child("patio").child("habilitado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valorCambio  = (String) dataSnapshot.getValue();
                if (valorCambio.equals("encendido")){
                    switchPatio.setChecked(true);
                    btnHoraPatio.setVisibility(View.VISIBLE);
                    contenedor5.setVisibility(View.VISIBLE);
                    contenedor6.setVisibility(View.VISIBLE);
                }else{
                    switchPatio.setChecked(false);
                    btnHoraPatio.setVisibility(View.INVISIBLE);
                    contenedor5.setVisibility(View.INVISIBLE);
                    contenedor6.setVisibility(View.INVISIBLE);
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

    private void autoLucesProcedimiento() {

        hora = c.get(Calendar.HOUR_OF_DAY);
        minutos = c.get(Calendar.MINUTE);

        horaInicioLuces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int horadeldia, int minutos) {
                        horaInicioLuces.setText(horadeldia + ":" + minutos);
                    }
                },hora,minutos,true);
                timePickerDialog.show();
            }
        });

        horaFinLuces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int horadeldia, int minutos) {
                        horaFinLuces.setText(horadeldia + ":" + minutos);
                    }
                },hora,minutos,true);
                timePickerDialog.show();
            }
        });

        horaInicioAspersores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int horadeldia, int minutos) {
                        horaInicioAspersores.setText(horadeldia + ":" + minutos);
                    }
                },hora,minutos,true);
                timePickerDialog.show();
            }
        });

        horaFinAspersores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int horadeldia, int minutos) {
                        horaFinAspersores.setText(horadeldia + ":" + minutos);
                    }
                },hora,minutos,true);
                timePickerDialog.show();
            }
        });

        horaInicioPatio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int horadeldia, int minutos) {
                        horaInicioPatio.setText(horadeldia + ":" + minutos);
                    }
                },hora,minutos,true);
                timePickerDialog.show();
            }
        });

        horaFinPatio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int horadeldia, int minutos) {
                        horaFinPatio.setText(horadeldia + ":" + minutos);
                    }
                },hora,minutos,true);
                timePickerDialog.show();
            }
        });





    }


}
