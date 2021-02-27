package com.fab.rent.Navigation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.fab.rent.MainActivity;
import com.fab.rent.R;
import com.fab.rent.User.SettingsActivity;

import java.util.ArrayList;

import io.paperdb.Paper;


public class SettingsFragment extends Fragment {

    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_settings2, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView=getView().findViewById(R.id.listview2);

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("My Profile");
        arrayList.add("Support");
        arrayList.add("Logout");

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String option=adapterView.getItemAtPosition(i).toString();

                if(option.equals("Logout"))
                {
                    Paper.book().destroy();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }
                if(option.equals("My Profile"))
                {
                    Intent intent=new Intent(getActivity(), SettingsActivity.class);
                    startActivity(intent);
                }

                if(option.equals("Support"))
                {
                    Toast.makeText(getActivity(),"Support",Toast.LENGTH_LONG).show();
                }




            }
        });

    }
}