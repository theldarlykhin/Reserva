package com.hnttechs.www.reserva;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dell on 7/15/15.
 */
public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private final String[] gridValues;

    public GridViewAdapter(Context context, String[] gridValues) {
        this.context        = context;
        this.gridValues     = gridValues;
    }

    @Override
    public int getCount() { return gridValues.length; }

    @Override
    public Object getItem(int position) { return null; }

    @Override
    public long getItemId(int position) { return 0; }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {
            gridView = new View(context);
            gridView = inflater.inflate( R.layout.grid_item , null);

            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_item_label);
            textView.setText(gridValues[position]);
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);

            String arrLabel = gridValues[ position ];
            if (arrLabel.equals("Chinese Restaurant"))       { imageView.setImageResource(R.drawable.chinese); }
            else if (arrLabel.equals("Myanmar Restaurant"))  { imageView.setImageResource(R.drawable.myanmar); }
            else if (arrLabel.equals("Japan Restaurant"))    { imageView.setImageResource(R.drawable.japan); }
            else if (arrLabel.equals("European Restaurant")) { imageView.setImageResource(R.drawable.european); }
            else if (arrLabel.equals("Indian Restaurant"))   { imageView.setImageResource(R.drawable.indian); }
            else if (arrLabel.equals("Thailand Restaurant")) { imageView.setImageResource(R.drawable.thai); }
        } else { gridView = (View) convertView; }

        return gridView;
    }
}
