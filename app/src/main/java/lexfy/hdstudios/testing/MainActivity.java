package lexfy.hdstudios.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button add, load;
    private EditText nameEt, professionEt;
    private TextView nameTv, professionTv;

    private ImageView imageView;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.btnAdd);
        load = findViewById(R.id.loadBtn);
        nameEt = findViewById(R.id.nameEt);
        professionEt = findViewById(R.id.professionEt);
        nameTv = findViewById(R.id.nameTv);
        professionTv = findViewById(R.id.professionTv);
        imageView = findViewById(R.id.imageView);

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addData();
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void addData() {

        String name = nameEt.getText().toString().trim();
        String profession = professionEt.getText().toString().trim();

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("profession", profession);

        db.collection("testing")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this,
                                "Document Added with ID" + documentReference.getId(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadData() {
        db.collection("testing")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document: task.getResult()){
                                //get data
                                String name = document.getString("name");
                                String profession = document.getString("profession");
                                String image = document.getString("image");

                                //set data
                                nameTv.setText(name);
                                professionTv.setText(profession);

                                try {
                                    Picasso.get().load(image).placeholder(R.drawable.ic_android_black).into(imageView);
                                }
                                catch (Exception e){
                                    imageView.setImageResource(R.drawable.ic_android_black);
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
