package tr.com.mustafagordesli.finalapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import tr.com.mustafagordesli.finalapp.R;
import tr.com.mustafagordesli.finalapp.databinding.FragmentLabelBinding;


public class LabelFragment extends Fragment {
    private FragmentLabelBinding binding;

    public LabelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLabelBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        LinearLayout linear = binding.linearLayout;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference labelRef = db.collection("label");

        labelRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        Label label = document.toObject(Label.class);
                        CheckBox checkBox = new CheckBox(getActivity());
                        checkBox.setText(label.getLabelText());
                        linear.addView(checkBox);
                    }
                }
            }
        });

        Button btnAddLabel = binding.btnAddLabel;

        btnAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editLabel = binding.editLabel;
                String labelText = editLabel.getText().toString();
                labelRef.add(new Label(labelText))
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                //Toast.makeText(getActivity(), "Label eklendi", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Label eklenemedi", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return root;
    }


}