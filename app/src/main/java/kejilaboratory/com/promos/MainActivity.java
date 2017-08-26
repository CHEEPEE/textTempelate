package kejilaboratory.com.promos;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.StringTokenizer;
import kejilaboratory.com.databasehelperlibrary.DatabaseHelper;
import kejilaboratory.com.promos.adapters.PromoListAdapter;
import kejilaboratory.com.promos.models.PromoItemsModel;
public class MainActivity extends AppCompatActivity {
    ArrayList<PromoItemsModel> promoItemsModels;
    PromoListAdapter promoListAdapter;
    ListView prolistView;
    Context context;
    Toolbar toolbar;
    public Object imgCover = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlist);
        LinearLayout addPromo = (LinearLayout) findViewById(R.id.add_promo);
        prolistView = (ListView) findViewById(R.id.promlist_view);
        context = MainActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Text Templates");

        }
        DatabaseHelper.getInstance(context, "promosdb");
        addPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(MainActivity.this);
            }
        });
        getPromolist();
        reqPermissionCall();
        reqPermissionSMS();
        prolistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                showDialogEditDeleter(MainActivity.this,position);

                return true;
            }
        });
    }
    public void showDialog(final Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_promo);
        final EditText promoCode,promoDes,promoNum;
        promoCode = (EditText) dialog.findViewById(R.id.promo_code);
        promoDes = (EditText) dialog.findViewById(R.id.promo_des);
        promoNum = (EditText) dialog.findViewById(R.id.promo_number);
        final Button save_promo = (Button) dialog.findViewById(R.id.save_promo);
        dialog.show();
        save_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> message = new ArrayList<String>();
                if (getTrim(promoCode)==true){
                    message.add("Fill up Promo Code");
                }
                if (getTrim(promoDes)==true){
                    message.add("Fill up Promo Desciption");
                }if (getTrim(promoNum)==true){
                    message.add("Fill up Number");
                }
                if (message.size()==0){
                    String insertPromoCode = promoCode.getText().toString();
                    String insertPromoDes = promoDes.getText().toString();
                    String insertPromoNumber = promoNum.getText().toString();
                    String insert = "Insert Into promos(promo_code,promodes,promo_number) values('"+insertPromoCode+"'," +
                            "'"+insertPromoDes+"','"+insertPromoNumber+"')";
                    DatabaseHelper.execute(insert);
                    getPromolist();
                    dialog.dismiss();
                }else {
                    for (int i =0;i<message.size();i++){
                        Toaster(message.get(i));
                    }
                }
            }
        });
    }
    private boolean getTrim(EditText promoEditText){

        if (promoEditText.getText().toString().trim().length() == 0 || promoEditText.getText().toString().trim() == null){
            return true;
        }else {
            return false;
        }
    }

    public void Toaster(String text){
        Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT).show();
    }
    public void getPromolist(){
        if (promoItemsModels == null){
            promoItemsModels = new ArrayList<>();
        }else {
            promoItemsModels.clear();
        }
        Cursor c = DatabaseHelper.rawQuery("Select * from promos");
        c.moveToFirst();
        if (c !=null && c.getCount() !=0){
            if (c.moveToFirst()){
                do {
                    PromoItemsModel promoItemsModel = new PromoItemsModel();
                    promoItemsModel.setPromoId(c.getString(c.getColumnIndex("id")));
                    promoItemsModel.setPromoCode(c.getString(c.getColumnIndex("promo_code")));
                    promoItemsModel.setPromoDes(c.getString(c.getColumnIndex("promodes")));
                    promoItemsModel.setPromoNumber(c.getString(c.getColumnIndex("promo_number")));
                    promoItemsModels.add(promoItemsModel);
                }while (c.moveToNext());
            }
        }
        promoListAdapter = new PromoListAdapter(MainActivity.this,promoItemsModels);
        prolistView.setAdapter(promoListAdapter);
    }
    private void showDialogEditDeleter(Activity activity, int pos){
        final Dialog dialog = new Dialog(activity);
        final PromoItemsModel promoItemsModelspos = promoItemsModels.get(pos);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_or_delete_custom_dialog);
        final CardView deleteButton = (CardView) dialog.findViewById(R.id.delete_circle);
        final EditText promoCode,promoDes,promoNum;
        promoCode = (EditText) dialog.findViewById(R.id.promo_code);
        promoDes = (EditText) dialog.findViewById(R.id.promo_des);
        promoNum = (EditText) dialog.findViewById(R.id.promo_number);
        final Button save_promo = (Button) dialog.findViewById(R.id.save_promo);
        promoCode.setText(promoItemsModelspos.getPromoCode());
        promoDes.setText(promoItemsModelspos.getPromoDes());
        promoNum.setText(promoItemsModelspos.getPromoNumber());
        dialog.show();
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String query = "Delete from promos where id="+promoItemsModelspos.getPromoId()+";";
                DatabaseHelper.execute(query);
                getPromolist();

                dialog.dismiss();
            }
        });
        save_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ArrayList<String> message = new ArrayList<String>();
                if (getTrim(promoCode)==true){
                    message.add("Fill up Promo Code");
                }
                if (getTrim(promoDes)==true){
                    message.add("Fill up Promo Desciption");
                }if (getTrim(promoNum)==true){
                    message.add("Fill up Number");
                }
                if (message.size()==0){
                    String update = "Update promos Set promo_code = '"+promoCode.getText().toString()+"', promodes='"+promoDes.getText().toString()+"', promo_number='"+promoNum.getText().toString()+"' where id="+promoItemsModelspos.getPromoId()+";";
                    DatabaseHelper.execute(update);
                    getPromolist();
                    dialog.dismiss();
                }else {
                    for (int i =0;i<message.size();i++){
                        Toaster(message.get(i));
                    }
                }





            }
        });

    }
    private void reqPermissionSMS(){
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        1);
            } else {
            }
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.SEND_SMS},
                    1);
        }
    }
    private void reqPermissionCall(){
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.CALL_PHONE)) {
            } else {
            }
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    reqPermissionSMS();
                } else {
                    reqPermissionSMS();
                }
                return;
            }
        }
    }
}
