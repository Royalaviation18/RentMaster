package com.fab.rent.DeliveryBoy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fab.rent.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DelliveredItemActivity extends AppCompatActivity {


    private Button addProduct;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription;
    private String Description, Name, saveCurrentDate, saveCurrentTime, downloadImageUrl, deliveryRandomKey,orderid;
    private Uri ImageUri;
    private ProgressDialog Loading;
    private static final int GalleryPick = 1;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dellivered_item);


        addProduct = (Button) findViewById(R.id.btnproduct);
        InputProductImage = (ImageView) findViewById(R.id.productimage);
        InputProductName = (EditText) findViewById(R.id.productname);
        InputProductDescription = (EditText) findViewById(R.id.productdescription);
        Loading = new ProgressDialog(this);
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Delivered Product Images");
        orderid=getIntent().getStringExtra("orid");

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OpenGallery();
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValidateData();
            }
        });


    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }

    private void ValidateData() {
        Description = InputProductDescription.getText().toString();
        Name = InputProductName.getText().toString();


        if (ImageUri == null) {
            Toast.makeText(this, "Product Image is Required!", Toast.LENGTH_SHORT).show();
        }
        if (Name.isEmpty()) {
            InputProductName.setError("Please Enter product name!");
            InputProductName.requestFocus();
            return;
        }
        if (Description.isEmpty()) {
            InputProductDescription.setError("Product Description is required!");
            InputProductDescription.requestFocus();
            return;
        } else {

            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {

        Loading.setTitle("Adding New Product");
        Loading.setMessage("Please wait,while we are adding the new product");
        Loading.setCanceledOnTouchOutside(false);
        Loading.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat(" MM dd,yyyy ");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat(" HH:mm:ss a ");
        saveCurrentTime = currentTime.format(calendar.getTime());

        deliveryRandomKey = saveCurrentDate + saveCurrentTime;  //generating random key for the product

        StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + deliveryRandomKey + ".jpg"); //name by which images are being stored

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message = e.toString();
                Toast.makeText(DelliveredItemActivity.this, "Error" + message, Toast.LENGTH_LONG).show();
                Loading.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(DelliveredItemActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();


                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(DelliveredItemActivity.this, "Getting Product Image Url", Toast.LENGTH_LONG).show();
                            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("OrderDetails");
                            HashMap<String,Object> userMap= new HashMap<>();
                            userMap.put("delivery","Done");
                            reference.child(orderid).updateChildren(userMap);


                            HashMap<String, Object> productMap = new HashMap<>();
                            productMap.put("did", deliveryRandomKey);
                            productMap.put("date", saveCurrentDate);
                            productMap.put("time", saveCurrentTime);
                            productMap.put("description", Description);
                            productMap.put("image", downloadImageUrl);

                            ProductsRef= FirebaseDatabase.getInstance().getReference().child("Delivery");

                            ProductsRef.child(deliveryRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        Intent intent = new Intent(DelliveredItemActivity.this, DeliveryHomeActivity.class);
                                        startActivity(intent);
                                        Loading.dismiss();
                                        Toast.makeText(DelliveredItemActivity.this, "Done", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });





                        }
                    }
                });
            }
        });


    }


}