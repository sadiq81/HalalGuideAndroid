package dk.eazyit.halalguide.ui.location_details.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import dk.eazyit.halalguide.R;
import dk.eazyit.halalguide.domain.HGLocation;
import dk.eazyit.halalguide.domain.HGLocationPicture;
import dk.eazyit.halalguide.ui.location_details.activities.HGLocationImagesActivity;

/**
 * Created by Privat on 18/08/15.
 */
public class HGLocationPictureAdapter extends RecyclerView.Adapter<HGLocationPictureAdapter.HGLocationPictureViewHolder> {

    private List<HGLocationPicture> pictures = new ArrayList<>();

    public HGLocationPictureAdapter() {
    }

    @Override
    public HGLocationPictureViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_cell_image, viewGroup, false);
        HGLocationPicture picture = pictures.get(i);
        return new HGLocationPictureViewHolder(v, picture.getLocationId());
    }

    @Override
    public void onBindViewHolder(HGLocationPictureViewHolder holder, int i) {
        HGLocationPicture picture = pictures.get(i);
        holder.image.setImageBitmap(null);
        Picasso.with(holder.image.getContext()).cancelRequest(holder.image);
        Picasso.with(holder.image.getContext()).load(picture.getPicture().getUrl()).into(holder.image);
        holder.itemView.setTag(picture);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public void setPictures(List<HGLocationPicture> pictures) {
        this.pictures = pictures;
        this.notifyDataSetChanged();
    }

    static class HGLocationPictureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.image)
        ImageView image;

        String objectId;

        public HGLocationPictureViewHolder(View convertView, String objectId) {
            super(convertView);
            this.objectId = objectId;
            ButterKnife.bind(this, convertView);

            convertView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent images = new Intent(v.getContext(), HGLocationImagesActivity.class);
            images.putExtra(HGLocation.INTENT, objectId);
            v.getContext().startActivity(images);
        }
    }


}
