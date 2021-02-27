package com.fab.rent.Seller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fab.rent.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddNewProductActivity extends AppCompatActivity {

    private String CategoryName,SubCategoryName,Description,Price,Pname,saveCurrentDate,saveCurrentTime;
    private Button AddNewProduct;
    private ImageView InputProductImage;
    private EditText InputProductName,InputProductDescription,InputProductPrice;
    private static  final int GalleryPick=1;
    private Uri ImageUri;
    private String productRandomKey,downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef,sellersRef;
    private ProgressDialog Loading;
    private TextView weekly,monthly;
    private String week,month;
    private double weekprice;
    private double monthlyprice;
    private String weekfinal,monthfinal;
    private int securityAmt;

    private String sName,sAddress,sPhone,sEmail,sID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);

       CategoryName=getIntent().getExtras().get("Category").toString();
       SubCategoryName=getIntent().getExtras().get("SubCategory").toString();

        ProductImagesRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef= FirebaseDatabase.getInstance().getReference().child("Products");
        sellersRef=FirebaseDatabase.getInstance().getReference().child("Sellers");

        AddNewProduct=(Button)findViewById(R.id.add_new_product);
        InputProductImage=(ImageView)findViewById(R.id.select_product_image);
        InputProductName=(EditText)findViewById(R.id.product_name);
        InputProductDescription=(EditText)findViewById(R.id.product_description);
        InputProductPrice=(EditText)findViewById(R.id.product_price);
        weekly=(TextView)findViewById(R.id.tv_weekly);
        monthly=(TextView)findViewById(R.id.tv_monthly);
        Loading =new ProgressDialog(this);
        securityAmt=getIntent() .getIntExtra("securityAmt",0);





        InputProductPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!InputProductPrice.equals(""))
                {
                    week=InputProductPrice.getText().toString();
                    weekprice=Integer.parseInt(week)*7;
                    weekprice=(weekprice-(0.1*weekprice));
                    monthlyprice=Integer.parseInt(week)*30;
                    monthlyprice=(monthlyprice-(0.2*monthlyprice));

                   weekfinal=Double.toString(weekprice);
                   monthfinal=Double.toString(monthlyprice);


                    weekly.setText("Weekly Price is " + weekfinal );
                    monthly.setText("Monthly Price is " + monthfinal);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });





        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OpenGallery();
            }
        });

        AddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValidateProductData();
            }
        });

        sellersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    sName=snapshot.child("name").getValue().toString();
                    sPhone=snapshot.child("phone").getValue().toString();
                    sAddress=snapshot.child("address").getValue().toString();
                    sID=snapshot.child("sid").getValue().toString();
                    sEmail=snapshot.child("email").getValue().toString();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    private void OpenGallery()
    {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri=data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }

    private void ValidateProductData()
    {
        Description=InputProductDescription.getText().toString();
        Price=InputProductPrice.getText().toString();
        Pname=InputProductName.getText().toString();


        if(ImageUri==null)
        {
            Toast.makeText(this, "Product Image is Required!", Toast.LENGTH_SHORT).show();
        }
        if(Pname.isEmpty())
        {
            InputProductName.setError("Please Enter product name!");
            InputProductName.requestFocus();
            return;
        }
        if(Description.isEmpty())
        {
            InputProductDescription.setError("Product Description is required!");
            InputProductDescription.requestFocus();
            return;
        }
        if(Price.isEmpty())
        {
            InputProductPrice.setError("Please Enter the price for the product!");
            InputProductPrice.requestFocus();
            return;
        }
        else
        {

            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {

        Loading.setTitle("Adding New Product");
        Loading.setMessage("Please wait,while we are adding the new product");
        Loading.setCanceledOnTouchOutside(false);
        Loading.show();

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat(" MM dd,yyyy ");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat(" HH:mm:ss a ");
        saveCurrentTime=currentTime.format(calendar.getTime());

        productRandomKey=saveCurrentDate+saveCurrentTime;  //generating random key for the product

        StorageReference filePath=ProductImagesRef.child(ImageUri.getLastPathSegment()+ productRandomKey + ".jpg"); //name by which images are being stored

        final UploadTask uploadTask=filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message=e.toString();
                Toast.makeText(SellerAddNewProductActivity.this,"Error" + message,Toast.LENGTH_LONG).show();
                Loading.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(SellerAddNewProductActivity.this,"Image Uploaded Successfully",Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful())
                        {
                            throw task.getException();

                        }

                        downloadImageUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();


                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful())
                        {
                            downloadImageUrl=task.getResult().toString();
                            Toast.makeText(SellerAddNewProductActivity.this,"Getting Product Image Url",Toast.LENGTH_LONG).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });






    }

    private void SaveProductInfoToDatabase() {

        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",Description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",CategoryName);
        productMap.put("subcategory",SubCategoryName);
        productMap.put("price",Price);
        productMap.put("pname",Pname);
        productMap.put("weeklyprice",weekfinal);
        productMap.put("monthlyprice",monthfinal);
        productMap.put("sellerName",sName);
        productMap.put("sellerAddress",sAddress);
        productMap.put("sellerPhone",sPhone);
        productMap.put("sellerEmail",sEmail);
        productMap.put("sid",sID);
        productMap.put("productState","Not Approved");
        productMap.put("securityAmt",securityAmt);

        ProductsRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    Intent intent=new Intent(SellerAddNewProductActivity.this, SellerHoActivity.class);
                    startActivity(intent);
                    Loading.dismiss();
                    Toast.makeText(SellerAddNewProductActivity.this,"Product is Added successfully",Toast.LENGTH_LONG).show();

                }
                else
                {
                    Loading.dismiss();
                    String message=task.getException().toString();
                    Toast.makeText(SellerAddNewProductActivity.this,"Error" + message,Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}