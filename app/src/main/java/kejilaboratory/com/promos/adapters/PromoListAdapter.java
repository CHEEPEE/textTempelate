package kejilaboratory.com.promos.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kejilaboratory.com.promos.MainActivity;
import kejilaboratory.com.promos.R;
import kejilaboratory.com.promos.models.PromoItemsModel;

/**
 * Created by Keji's Lab on 10/06/2017.
 */

public class PromoListAdapter extends BaseAdapter {

    Context context;
    Activity act;
    ArrayList<PromoItemsModel> promoItemsModels;

    public PromoListAdapter(Context context, ArrayList<PromoItemsModel> promoItemsModels){

        this.context = context;
        this.promoItemsModels = promoItemsModels;
    }

    @Override
    public int getCount() {
        return promoItemsModels.size();
    }


    @Override
    public  Object getItem(int position){
        return promoItemsModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final PromoItemsModel promoItemsModel = promoItemsModels.get(position);

        if (convertView== null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.single_list_item,null);

        }

        FloatingActionButton send = (FloatingActionButton) convertView.findViewById(R.id.cardview_send);
        send.setFocusable(false);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(promoItemsModel.getPromoNumber(), null, promoItemsModel.getPromoCode(), null, null);

            }
        });

        TextView promo_code = (TextView) convertView.findViewById(R.id.adapter_promo_code);
        TextView promo_des=(TextView) convertView.findViewById(R.id.adapter_promo_des);

        promo_code.setText(promoItemsModel.getPromoCode());
        promo_des.setText(promoItemsModel.getPromoDes());



        return convertView;
    }
}
