package dk.eazyit.halalguide.ui.common.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import dk.eazyit.halalguide.R;
import dk.eazyit.halalguide.domain.HGCategory;
import dk.eazyit.halalguide.domain.HGLanguage;

/**
 * Created by Privat on 20/07/15.
 */
public class HGLanguageAdapter extends ArrayAdapter<HGLanguage> {

    private Context context;

    public HGLanguageAdapter(Context context) {
        super(context, -1, HGLanguage.values());
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_cell_language, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text.setText(getItem(position).getResource_id());

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.language)
        TextView text;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }
}
