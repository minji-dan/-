package com.example.whoru.ui.notifications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.whoru.LoginActivity;
import com.example.whoru.MainActivity;
import com.example.whoru.R;
import com.example.whoru.databinding.FragmentNotificationsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationsFragment extends Fragment {
    private FirebaseAuth mFirebaseAuth;     //파이어베이스 인증처리 (로그인, 회원가입)
    private DatabaseReference mDatabaseRef;
    TextView readText;
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView readText = (TextView)root.findViewById(R.id.read_text);

        //회원이름 가져오기
        mFirebaseAuth =FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("whoRU");
        FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser();
        mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    String value =String.valueOf(task.getResult().getValue());
                    readText.setText(value+"님 안녕하세요!");
                }
            }
        });

        //읽기


        mFirebaseAuth = FirebaseAuth.getInstance();
        Button btn_logout=(Button) root.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그아웃 하기
                mFirebaseAuth.signOut();
                Toast.makeText(getActivity().getApplicationContext(), "로그아웃 성공", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        Button btn_delete=(Button) root.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad= new AlertDialog.Builder(getActivity());
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("!경고!");
                ad.setMessage("정말 탈퇴하시겠습니까?\n"+"회원님의 모든 정보가 삭제됩니다.");
                ad.setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mFirebaseAuth.getCurrentUser().delete();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getActivity().getApplicationContext(), "탈퇴완료", Toast.LENGTH_SHORT).show();
                    }
                });
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}