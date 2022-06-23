package com.example.whoru.ui.dashboard;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whoru.CustomAdapter;
import com.example.whoru.Find;
import com.example.whoru.LoginActivity;
import com.example.whoru.R;
import com.example.whoru.databinding.FragmentDashboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.time.LocalDate;
import org.w3c.dom.Text;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DashboardFragment extends Fragment  {
    LocalDate now = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formatedNow = now.format(formatter);
    String value;
    String my_path;

    private FirebaseAuth mFirebaseAuth;     //파이어베이스 인증처리 (로그인, 회원가입)
    int y=0, m=0, d=0;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Find> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("whoRU");
        FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser();
        databaseReference.child("connect").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    value =String.valueOf(task.getResult().getValue());
                    my_path="whoRU/UserAccount/"+value+"/"+formatedNow;
                }
            }
        });
        Button btn=(Button) root.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        Button btn2=(Button) root.findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "조회 성공", Toast.LENGTH_SHORT).show();
                recyclerView =root.findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                layoutManager=new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                arrayList = new ArrayList<>();

                database=FirebaseDatabase.getInstance();


                databaseReference=database.getReference(my_path);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        arrayList.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Find find = snapshot.getValue(Find.class);
                            arrayList.add(find);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                adapter = new CustomAdapter(arrayList,getActivity());
                recyclerView.setAdapter(adapter);
            }
        });

        return root;
    }
    void showDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month+1;
                d = dayOfMonth;
                TextView textView=(TextView)getActivity().findViewById(R.id.textView);
                textView.setText(y+"년 "+m+"월 "+d+"일 ");


                mFirebaseAuth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference("whoRU");
                FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser();
                databaseReference.child("connect").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            value =String.valueOf(task.getResult().getValue());
                            if (m<10 && d<10){
                                my_path="whoRU/UserAccount/"+value+"/"+y+"-"+"0"+m+"-"+"0"+d;
                            }
                            else if (m>=10 && d<10){
                                my_path="whoRU/UserAccount/"+value+"/"+y+"-"+m+"-"+"0"+d;
                            }
                            else if (m<10 && d>=10){
                                my_path="whoRU/UserAccount/"+value+"/"+y+"-"+"0"+m+"-"+d;
                            }
                            else if (m>=10 && d>=10){
                                my_path="whoRU/UserAccount/"+value+"/"+y+"-"+m+"-"+d;
                            }
                        }
                    }
                });












            }
        },2019, 1, 11);


        datePickerDialog.setMessage("날짜 선택");
        datePickerDialog.show();
    }

}