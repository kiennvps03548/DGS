package dgs.example.pungkook.dgs_android.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.daniribalbert.customfontlib.views.CustomFontTextView;
import java.util.List;
import dgs.example.pungkook.dgs_android.R;
import dgs.example.pungkook.dgs_android.model.OutputItem;

/**
 * Created by kien on 07/29/2016.
 */
public class OutputAdapter extends RecyclerView.Adapter<OutputAdapter.MyViewHolder> {

    private List<OutputItem> mList;
    private Context context;
    private int focusedItem = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CustomFontTextView txtName, txtValue;
        public CardView card_view;
        LinearLayout layout_view;
        public MyViewHolder(View view) {
            super(view);
            txtName = (CustomFontTextView) view.findViewById(R.id.txt_name);
            txtValue = (CustomFontTextView) view.findViewById(R.id.txt_value);
            card_view = (CardView) view.findViewById(R.id.card_view);
            layout_view = (LinearLayout) view.findViewById(R.id.thumbnail);
            view.setClickable(true);
        }
        @Override
        public void onClick(View view) {
        }
    }

    public OutputAdapter(Context context, List<OutputItem> moviesList) {
        this.context = context;
        this.mList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cardview, parent, false);
        final MyViewHolder pvh = new MyViewHolder(itemView);
        return pvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OutputItem menu = mList.get(position);
        holder.txtName.setText(menu.getOutput_name());
        holder.txtValue.setText(menu.getOutput_value());
        holder.itemView.setSelected(focusedItem == position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}