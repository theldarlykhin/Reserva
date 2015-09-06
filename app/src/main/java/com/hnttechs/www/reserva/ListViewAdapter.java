package com.hnttechs.www.reserva;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dell on 7/4/15.
 */
public class ListViewAdapter extends BaseAdapter {
    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public ListViewAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final TextView shop_name;
        TextView address;
        TextView phone_no;


        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        resultp = data.get(position);
        shop_name = (TextView) convertView.findViewById(R.id.name);
        address = (TextView) convertView.findViewById(R.id.address);
        phone_no = (TextView) convertView.findViewById(R.id.phone);
        LinearLayout data_layout = (LinearLayout) convertView.findViewById(R.id.data_layout);
        
        shop_name.setText(resultp.get(ListViewMain.NAME));
        address.setText(resultp.get(ListViewMain.ADDRESS));
        phone_no.setText(resultp.get(ListViewMain.PHONE));



        if(ListViewMain.btn_id.equals("Chinese Restaurant"))        { data_layout.setBackgroundResource(R.drawable.left_border_chinese);  }
        else if (ListViewMain.btn_id.equals("Myanmar Restaurant"))  { data_layout.setBackgroundResource(R.drawable.left_border_myanmar);  }
        else if (ListViewMain.btn_id.equals("Japan Restaurant"))    { data_layout.setBackgroundResource(R.drawable.left_border_japan);  }
        else if (ListViewMain.btn_id.equals("European Restaurant")) { data_layout.setBackgroundResource(R.drawable.left_border_european);  }
        else if (ListViewMain.btn_id.equals("Indian Restaurant"))   { data_layout.setBackgroundResource(R.drawable.left_border_indian);  }
        else if (ListViewMain.btn_id.equals("Thailand Restaurant")) { data_layout.setBackgroundResource(R.drawable.left_border_thailand);  }

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, SendDataActivity.class);
                intent.putExtra("restaurant_name",shop_name.getText().toString());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}