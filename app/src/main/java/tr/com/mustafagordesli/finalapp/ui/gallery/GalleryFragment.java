package tr.com.mustafagordesli.finalapp.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

import tr.com.mustafagordesli.finalapp.databinding.FragmentGalleryBinding;
import tr.com.mustafagordesli.finalapp.ui.adapter.CustomAdapter;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private FirebaseFirestore firestore;
    private CustomAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        firestore = FirebaseFirestore.getInstance();


        getdataFromFirebase();

        adapter = new CustomAdapter(requireContext(), new ArrayList<>());
        binding.recylerView.setAdapter(adapter);
        binding.recylerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        return root;
    }

    private void getdataFromFirebase() {
        CollectionReference postCollection = firestore.collection("posts");
        postCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> dataList = new ArrayList<>();

                    for (DocumentSnapshot document : task.getResult()) {
                        String imageUrl = document.getString("imageUrl");
                        String name = document.getString("name");
                        ArrayList<String> labellist = (ArrayList<String>) document.get("label");

                        String formatedLabels = formatLabels(labellist);
                        String data = "Image Url :" + (imageUrl != null ? imageUrl : "") +
                                "\nLabels :" + formatedLabels +
                                "\nName :" + (name != null ? name : "");

                        dataList.add(data);
                    }

                    adapter.setData(dataList);
                } else {
                    Toast.makeText(requireContext(), "veriler alınamadı" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String formatLabels(ArrayList<String> labelList){
        if(labelList == null || labelList.isEmpty()){
            return "";
        }

        StringBuilder formatedLabels = new StringBuilder();
        for(String label: labelList){
            formatedLabels.append(label).append(", ");
        }

        return formatedLabels.substring(0, formatedLabels.length()- 2);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}