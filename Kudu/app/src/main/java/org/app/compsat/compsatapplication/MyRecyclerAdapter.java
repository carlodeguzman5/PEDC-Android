package org.app.compsat.compsatapplication;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by carlo on 11/1/2015.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    private JSONArray months;
    private JSONArray events;
    private Activity context;

    private final Typeface tf_futura_bold;
    private final Typeface tf_opensans_regular;
    private final Typeface tf_opensans_bold;
    private final Typeface tf_opensans_light;
    private final Typeface tf_futura;
    private final Typeface tf_futura_condensed;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mTextView;
        private LinearLayout mLinearLayout;
        private ImageView mImageView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.month_text);
            mLinearLayout = (LinearLayout)v.findViewById(R.id.events_list);
            mImageView = (ImageView) v.findViewById(R.id.calendarImage);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyRecyclerAdapter(Activity context, JSONArray months, JSONArray events) {
        this.months = months;
        this.events = events;
        this.context = context;

        tf_futura_bold = Typeface.createFromAsset(context.getAssets(), "fonts/FuturaLT-Bold.ttf");
        tf_futura = Typeface.createFromAsset(context.getAssets(), "fonts/FuturaLT.ttf");
        tf_futura_condensed= Typeface.createFromAsset(context.getAssets(), "fonts/FuturaLT-Condensed.ttf");
        tf_opensans_bold= Typeface.createFromAsset(context.getAssets(), "fonts/FuturaLT-Bold.ttf");
        tf_opensans_regular= Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        tf_opensans_light= Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Light.ttf");

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try {
            Log.d("events", events +"");
            JSONObject tempArray = new JSONObject(months.getString(position));
            //JSONObject tempArray2 = new JSONObj(events.toString());

            holder.mTextView.setText(tempArray.get("month").toString().toUpperCase());
            holder.mTextView.setTypeface(tf_opensans_regular);

            holder.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            switch (tempArray.get("month").toString()){
                case "August": holder.mImageView.setImageResource(R.drawable.august);
                    break;
                case "September": holder.mImageView.setImageResource(R.drawable.september);
                    break;
                case "October": holder.mImageView.setImageResource(R.drawable.october);
                    break;
                case "November": holder.mImageView.setImageResource(R.drawable.november);
                    break;
                case "December": holder.mImageView.setImageResource(R.drawable.december);
                    break;
                case "January": holder.mImageView.setImageResource(R.drawable.january);
                    break;
                case "February": holder.mImageView.setImageResource(R.drawable.february);
                    break;
                case "March": holder.mImageView.setImageResource(R.drawable.march);
                    break;
                case "April": holder.mImageView.setImageResource(R.drawable.april);
                    break;
                case "May": holder.mImageView.setImageResource(R.drawable.may);
                    break;
                case "June": holder.mImageView.setImageResource(R.drawable.june);
                    break;
                case "July": holder.mImageView.setImageResource(R.drawable.june);
                    break;
            }


            holder.mLinearLayout.removeAllViewsInLayout();

            for(int i = 0 ; i < events.length(); i++){
                JSONObject tempArray2 = new JSONObject(events.get(i).toString());

                if(!tempArray2.get("month").toString().equals(tempArray.get("month").toString())){
                }
                else{

                    View view = context.getLayoutInflater().inflate(R.layout.event_list_item, null);
                    TextView event_text = (TextView) view.findViewById(R.id.event_name);
                    TextView day_text = (TextView) view.findViewById(R.id.day_text);

                    event_text.setTypeface(tf_opensans_light);
                    day_text.setTypeface(tf_opensans_bold);

                    event_text.setText(tempArray2.get("event_name").toString());
                    day_text.setText(tempArray2.get("event_date").toString().split("-")[2]);
                    holder.mLinearLayout.addView(view);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return months.length();
    }
}