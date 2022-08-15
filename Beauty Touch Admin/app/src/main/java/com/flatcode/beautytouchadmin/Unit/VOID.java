package com.flatcode.beautytouchadmin.Unit;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.flatcode.beautytouchadmin.Model.Post;
import com.flatcode.beautytouchadmin.Model.ShoppingCenter;
import com.flatcode.beautytouchadmin.Model.Tools;
import com.flatcode.beautytouchadmin.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

public class VOID {

    public static void IntentClear(Context context, Class c) {
        Intent intent = new Intent(context, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void Intent(Context context, Class c) {
        Intent intent = new Intent(context, c);
        context.startActivity(intent);
    }

    public static void IntentExtra(Context context, Class c, String key, String value) {
        Intent intent = new Intent(context, c);
        intent.putExtra(key, value);
        context.startActivity(intent);
    }

    public static void IntentExtra2(Context context, Class c, String key, String value, String key2, String value2) {
        Intent intent = new Intent(context, c);
        intent.putExtra(key, value);
        intent.putExtra(key2, value2);
        context.startActivity(intent);
    }

    public static void IntentExtra3(Context context, Class c, String key, String value,
                                    String key2, String value2, String key3, String value3) {
        Intent intent = new Intent(context, c);
        intent.putExtra(key, value);
        intent.putExtra(key2, value2);
        intent.putExtra(key3, value3);
        context.startActivity(intent);
    }

    public static void Glide(Boolean isUser, Context context, String Url, ImageView Image) {
        try {
            if (Url.equals(DATA.BASIC)) {
                if (isUser) {
                    Image.setImageResource(R.drawable.basic_user);
                } else {
                    Image.setImageResource(R.drawable.basic_user);
                }
            } else {
                Glide.with(context).load(Url).centerCrop().placeholder(R.color.image_profile).into(Image);
            }
        } catch (Exception e) {
            Image.setImageResource(R.drawable.basic_user);
        }
    }

    public static void CropImageSquare(Activity activity) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setMinCropResultSize(DATA.MIN_SQUARE, DATA.MIN_SQUARE)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(activity);
    }

    public static void Intro(Context context, ImageView background, ImageView backWhite, ImageView backDark) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (Objects.equals(sharedPreferences.getString("color_option", "ONE"), "ONE") ||
                Objects.equals(sharedPreferences.getString("color_option", "TWO"), "TWO") ||
                Objects.equals(sharedPreferences.getString("color_option", "THREE"), "THREE") ||
                Objects.equals(sharedPreferences.getString("color_option", "FOUR"), "FOUR") ||
                Objects.equals(sharedPreferences.getString("color_option", "FIVE"), "FIVE") ||
                Objects.equals(sharedPreferences.getString("color_option", "SIX"), "SIX") ||
                Objects.equals(sharedPreferences.getString("color_option", "SEVEN"), "SEVEN") ||
                Objects.equals(sharedPreferences.getString("color_option", "EIGHT"), "EIGHT") ||
                Objects.equals(sharedPreferences.getString("color_option", "NINE"), "NINE") ||
                Objects.equals(sharedPreferences.getString("color_option", "TEEN"), "TEEN")) {
            background.setImageResource(R.drawable.background_day);
            backWhite.setVisibility(View.VISIBLE);
            backDark.setVisibility(View.GONE);
        } else if (Objects.equals(sharedPreferences.getString("color_option", "NIGHT_ONE"), "NIGHT_ONE") ||
                Objects.equals(sharedPreferences.getString("color_option", "NIGHT_TWO"), "NIGHT_TWO") ||
                Objects.equals(sharedPreferences.getString("color_option", "NIGHT_THREE"), "NIGHT_THREE") ||
                Objects.equals(sharedPreferences.getString("color_option", "NIGHT_FOUR"), "NIGHT_FOUR") ||
                Objects.equals(sharedPreferences.getString("color_option", "NIGHT_FIVE"), "NIGHT_FIVE") ||
                Objects.equals(sharedPreferences.getString("color_option", "NIGHT_SIX"), "NIGHT_SIX") ||
                Objects.equals(sharedPreferences.getString("color_option", "NIGHT_SEVEN"), "NIGHT_SEVEN")) {
            background.setImageResource(R.drawable.background_night);
            backWhite.setVisibility(View.GONE);
            backDark.setVisibility(View.VISIBLE);
        }
    }

    /*public static void Logo(Context context, ImageView background) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        if (sharedPreferences.getString("color_option", "ONE").equals("ONE")) {
            background.setImageResource(R.drawable.logo);
        } else if (sharedPreferences.getString("color_option", "NIGHT_ONE").equals("NIGHT_ONE")) {
            background.setImageResource(R.drawable.logo_night);
        }
    }*/

    public static void getNrFromServer(String server, TextView name) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(server);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i;
                String Name = name.getText().toString();
                if (server.equals(DATA.USERS)) {
                    i = (int) (dataSnapshot.getChildrenCount() - 1);
                    name.setText(Name + " " + "( " + i + " )");
                } else if (server.equals(DATA.M_TOOLS)) {
                    Tools tools = dataSnapshot.getValue(Tools.class);
                    if (Name.contains("Old")) {
                        name.setText(Name + " " + tools.getYear() + " | " + tools.getOldSession());
                    } else {
                        name.setText(Name + " " + tools.getYear() + " | " + tools.getSession());
                    }
                } else {
                    i = (int) (dataSnapshot.getChildrenCount());
                    name.setText(Name + " " + "( " + i + " )");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public static void moreOptionDialog(Context context, Post item) {
        String id = item.getPostid();
        String name = item.getName();

        //options to show in dialog
        String[] options = {"Edit", "Delete"};

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose...").setItems(options, (dialog, which) -> {
            //handle dialog option click
            if (which == 0) {
                //Edit clicked ,Open new activity to edit the book info
                IntentExtra(context, CLASS.POST_EDIT, DATA.POST_ID, id);
            } else if (which == 1) {
                //Delete Clicked
                dialogOptionDelete(context, DATA.EMPTY + id, DATA.EMPTY + name, false);
            }
        }).show();
    }

    public static void moreShoppingCenters(Context context, ShoppingCenter item) {
        String id = item.getId();
        String name = item.getName();

        //options to show in dialog
        String[] options = {"Edit", "Delete"};

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose...").setItems(options, (dialog, which) -> {
            //handle dialog option click
            if (which == 0) {
                //Edit clicked ,Open new activity to edit the book info
                IntentExtra(context, CLASS.SHOPPING_CENTRES_EDIT, DATA.SHOPPING_CENTER_ID, id);
            } else if (which == 1) {
                //Delete Clicked
                dialogOptionDelete(context, DATA.EMPTY + id, DATA.EMPTY + name, true);
            }
        }).show();
    }

    public static void dialogOptionDelete(Context context, String id, String name, boolean isPharmacy) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView title = dialog.findViewById(R.id.title);
        if (isPharmacy) {
            title.setText(R.string.do_you_want_to_delete_the_pharmacy);
        } else {
            title.setText(R.string.do_you_want_to_delete_the_post);
        }

        dialog.findViewById(R.id.yes).setOnClickListener(view -> {
            if (isPharmacy) {
                delete(dialog, context, DATA.SHOPPING_CENTERS, id, name);
            } else {
                delete(dialog, context, DATA.POSTS, id, name);
            }
        });

        dialog.findViewById(R.id.no).setOnClickListener(view2 -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    public static void delete(Dialog dialogDelete, Context context, String database, String id, String name) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait");
        dialog.setMessage("is deleted  " + name + " ...");
        dialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(database);
        reference.child(id).removeValue().addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "Post deleted successfully...", Toast.LENGTH_SHORT).show();
            dialogDelete.dismiss();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            dialogDelete.dismiss();
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public static void CropImageSlider(Activity activity) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setMinCropResultSize(DATA.MIX_SLIDER_X, DATA.MIX_SLIDER_Y)
                .setAspectRatio(16, 9)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(activity);
    }

    public static void CropImageShoppingCenter(Activity activity) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setMinCropResultSize(DATA.MIX_SLIDER_X, DATA.MIX_SLIDER_Y)
                .setAspectRatio(2, 1)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(activity);
    }

    public static void CropImageSession(Activity activity) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setMinCropResultSize(DATA.MIN_SQUARE, DATA.MIN_SQUARE)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(activity);
    }

    public static String getFileExtension(Uri uri, Context context) {
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}