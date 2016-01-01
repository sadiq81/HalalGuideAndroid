package dk.eazyit.halalguide.ui.common.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import dk.eazyit.halalguide.R;
import dk.eazyit.halalguide.domain.HGLanguage;
import dk.eazyit.halalguide.domain.HGLocation;
import dk.eazyit.halalguide.domain.HGLocationPicture;
import dk.eazyit.halalguide.domain.HGLocationType;
import dk.eazyit.halalguide.services.HGGPSService;
import dk.eazyit.halalguide.services.HGPictureService;

/**
 * Created by Privat on 08/07/15.
 */
public class HGLocationAdapter extends ArrayAdapter<HGLocation> {


    public HGLocationAdapter(Context context, List<HGLocation> values) {
        super(context, -1, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        final HGLocation location = getItem(position);

        int type = getItemViewType(position);
        switch (type) {
            case 0: {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.listview_cell_dining_cell, parent, false);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(R.layout.listview_cell_dining_cell, holder);
                } else {
                    holder = (ViewHolder) convertView.getTag(R.layout.listview_cell_dining_cell);
                }
                holder.pork.setVisibility(location.getPork() ? View.VISIBLE : View.GONE);
                holder.alcohol.setVisibility(location.getAlcohol() ? View.VISIBLE : View.GONE);
                holder.non_halal.setVisibility(location.getNonHalal() ? View.VISIBLE : View.GONE);
                break;
            }
            case 1: {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.listview_cell_mosque_cell, parent, false);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(R.layout.listview_cell_mosque_cell, holder);
                } else {
                    holder = (ViewHolder) convertView.getTag(R.layout.listview_cell_mosque_cell);
                }
                if (location.getLanguage().intValue() > 0) {
                    holder.language.setImageResource(HGLanguage.getLanguage(location.getLanguage().intValue()).getImageResourceId());
                    holder.language_label.setText(HGLanguage.getLanguage(location.getLanguage().intValue()).toString());
                } else {
                    holder.language.setImageResource(android.R.color.transparent);
                    holder.language_label.setText("");
                }
                break;
            }
            case 2: {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.listview_cell_shop_cell, parent, false);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(R.layout.listview_cell_shop_cell, holder);
                } else {
                    holder = (ViewHolder) convertView.getTag(R.layout.listview_cell_shop_cell);
                }
                break;
            }
        }

        final ViewHolder finalHolder = holder;
        final HGLocation finalLocation = location;

        HGPictureService.getInstance().thumbnailForLocation(location, new FindCallback<HGLocationPicture>() {
            @Override
            public void done(List<HGLocationPicture> list, ParseException e) {

                HGLocationType locationTypetype = HGLocationType.getLocation(finalLocation.getLocationType().intValue());
                finalHolder.thumbnail.setImageResource(locationTypetype.getImageResourceId());

                ColorStateList colorStateList = ColorStateList.valueOf(getContext().getResources().getColor(R.color.greenTintColor));
                Drawable drawable = finalHolder.thumbnail.getDrawable();
                drawable = DrawableCompat.wrap(drawable);
                if (list.size() > 0) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        finalHolder.thumbnail.setImageTintMode(null);
                        finalHolder.thumbnail.setImageTintList(null);
                    } else {
                        DrawableCompat.setTintList(drawable, null);
                        DrawableCompat.setTintMode(drawable, null);
                        finalHolder.thumbnail.setImageDrawable(drawable);
                    }
                    Picasso.with(getContext()).load(list.get(0).getThumbnail().getUrl()).into(finalHolder.thumbnail);
                } else {
                    if (Build.VERSION.SDK_INT >= 21) {
                        finalHolder.thumbnail.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
                        finalHolder.thumbnail.setImageTintList(colorStateList);
                    } else {
                        DrawableCompat.setTintList(drawable, colorStateList);
                        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP);
                        finalHolder.thumbnail.setImageDrawable(drawable);
                    }
                }
            }
        });

        holder.name.setText(location.getName());
        holder.road.setText(location.getAddressRoad() + " " + location.getAddressRoadNumber());
        holder.postal_code.setText(location.getAddressPostalCode() + " " + location.getAddressCity());
        holder.distance.setText(String.valueOf(HGGPSService.getInstance().distanceFromLocation(location.getLocation())));

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        HGLocation location = getItem(position);
        return location.getLocationType().intValue();
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    static class ViewHolder {
        @Bind(R.id.thumbnail)
        ImageView thumbnail;

        @Bind(R.id.name)
        TextView name;

        @Bind(R.id.road)
        TextView road;

        @Bind(R.id.postal_code)
        TextView postal_code;

        @Bind(R.id.distance)
        TextView distance;

        @Nullable
        @Bind(R.id.pork)
        ImageView pork;

        @Nullable
        @Bind(R.id.alcohol)
        ImageView alcohol;

        @Nullable
        @Bind(R.id.non_halal)
        ImageView non_halal;

        @Nullable
        @Bind(R.id.language)
        ImageView language;

        @Nullable
        @Bind(R.id.language_label)
        TextView language_label;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }
}


