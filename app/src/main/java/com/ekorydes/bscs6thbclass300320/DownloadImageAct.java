package com.ekorydes.bscs6thbclass300320;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DownloadImageAct extends AppCompatActivity {

    private EditText imageNameET;
    private ImageView imageToDownloadIV;

    private Button downloadBtn;
    private Dialog objectDialog;

    private FirebaseFirestore objectFirebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_image);

        imageNameET=findViewById(R.id.imageNameET);
        imageToDownloadIV=findViewById(R.id.imageToDownloadIV);

        downloadBtn=findViewById(R.id.downloadImageBtn);

        objectDialog=new Dialog(this);
        objectDialog.setCancelable(false);

        objectDialog.setContentView(R.layout.please_wait_dialog);
        objectFirebaseFirestore=FirebaseFirestore.getInstance();

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageUrl();
            }
        });
    }

    private void getImageUrl()
    {
        objectDialog.show();
        objectFirebaseFirestore.collection("BSCSLinks")
                .document(imageNameET.getText().toString())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    String url=documentSnapshot.getString("url");
                    Glide.with(DownloadImageAct.this)
                            .load(url)
                            .into(imageToDownloadIV);
                    objectDialog.dismiss();
                }
                else
                {
                    objectDialog.dismiss();
                    Toast.makeText(DownloadImageAct.this, "No Such Document Exists", Toast.LENGTH_SHORT).show();
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        objectDialog.dismiss();
                        Toast.makeText(DownloadImageAct.this, "Fails to get url :"
                                +e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
