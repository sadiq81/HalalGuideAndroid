package dk.eazyit.halalguide.ui.location_details.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import dk.eazyit.halalguide.R;
import dk.eazyit.halalguide.domain.HGLocationPicture;
import dk.eazyit.halalguide.domain.HGSmiley;

/**
 * Created by Privat on 18/08/15.
 */
public class HGLocationSmileyAdapter extends RecyclerView.Adapter<HGLocationSmileyAdapter.HGLocationSmileyViewHolder>  {

    private List<HGSmiley> smileys = new ArrayList<>();

    public HGLocationSmileyAdapter() {
    }

    public HGLocationSmileyAdapter(List<HGSmiley> smileys) {
        this.smileys = smileys;
    }

    @Override
    public HGLocationSmileyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_cell_smiley, viewGroup, false);
        return new HGLocationSmileyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HGLocationSmileyViewHolder holder, int i) {
        HGSmiley smiley = smileys.get(i);

        holder.smileyDate.setText(smiley.date);

        holder.smileyImage.setImageBitmap(null);
        Picasso.with(holder.smileyImage.getContext()).cancelRequest(holder.smileyImage);
        Picasso.with(holder.smileyImage.getContext()).load(smiley.smiley).into(holder.smileyImage);
        holder.itemView.setTag(smiley);
    }

    @Override
    public int getItemCount() {
        return smileys.size();
    }

    public void setSmileys(List<HGSmiley> smileys) {
        this.smileys.clear();
        this.smileys.addAll(smileys);
        this.notifyDataSetChanged();
    }

    class HGLocationSmileyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.smileyImage)
        ImageView smileyImage;

        @Bind(R.id.smileyDate)
        TextView smileyDate;

        public HGLocationSmileyViewHolder(View convertView) {
            super(convertView);
            ButterKnife.bind(this, convertView);
            convertView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            HGSmiley smiley = HGLocationSmileyAdapter.this.smileys.get(getAdapterPosition());
            Context context = v.getContext();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(smiley.report));
            context.startActivity(browserIntent);
        }
    }
}
