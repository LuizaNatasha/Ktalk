
package com.example.k_talk.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.k_talk.Aulas.Aula01Activity;
import com.example.k_talk.Aulas.Aula02Activity;
import com.example.k_talk.Aulas.Aula03Activity;
import com.example.k_talk.Aulas.Aula04Activity;
import com.example.k_talk.Aulas.Aula05Activity;
import com.example.k_talk.Aulas.Aula06Activity;
import com.example.k_talk.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AulasFragment extends Fragment {

    private ImageButton btn_aula1, btn_aula2, btn_aula3;
    private ImageButton btn_aula4, btn_aula5, btn_aula6;

    public AulasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_aulas, container, false);


        btn_aula1 = (ImageButton) view.findViewById(R.id.btn_aula1);
        btn_aula2 = (ImageButton) view.findViewById(R.id.btn_aula2);
        btn_aula3 = (ImageButton) view.findViewById(R.id.btn_aula3);
        btn_aula4 = (ImageButton) view.findViewById(R.id.btn_aula4);
        btn_aula5 = (ImageButton) view.findViewById(R.id.btn_aula5);
        btn_aula6 = (ImageButton) view.findViewById(R.id.btn_aula6);

        btn_aula1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getActivity(), Aula01Activity.class);
                startActivity(intent);
            }
        });

        btn_aula2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getActivity(), Aula02Activity.class);
                startActivity(intent);
            }
        });

        btn_aula3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getActivity(), Aula03Activity.class);
                startActivity(intent);
            }
        });

        btn_aula4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getActivity(), Aula04Activity.class);
                startActivity(intent);
            }
        });

        btn_aula5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getActivity(), Aula05Activity.class);
                startActivity(intent);
            }
        });

        btn_aula6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getActivity(), Aula06Activity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
