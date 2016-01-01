package dk.eazyit.halalguide.ui.common.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import dk.eazyit.halalguide.HGSettings;
import dk.eazyit.halalguide.R;
import dk.eazyit.halalguide.domain.HGCategory;
import dk.eazyit.halalguide.domain.HGDiningCategory;
import dk.eazyit.halalguide.domain.HGShopCategory;

public class HGCategoryListAdapter extends ArrayAdapter<HGCategory> {

    private Context context;
    private List<HGCategory> selected;

    public HGCategoryListAdapter(Context context, List<HGCategory> values, List<HGCategory> selected) {
        super(context, -1, values);
        this.context = context;
        this.selected = selected;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_cell_category, null);

            holder = new ViewHolder(convertView);
            holder.selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox view = (CheckBox) v;
                    HGCategory category = (HGCategory) view.getTag(R.id.CATEGORY_TAG);
                    if (view.isChecked()) {
                        HGCategoryListAdapter.this.selected.add(category);
                        addCategory(category);
                    } else {
                        HGCategoryListAdapter.this.selected.remove(category);
                        removeCategory(category);
                    }
                }
            });
            convertView.setTag(R.id.VIEW_HOLDER_TAG, holder);

        } else {

            holder = (ViewHolder) convertView.getTag(R.id.VIEW_HOLDER_TAG);
        }

        HGCategory category = getItem(position);
        holder.selected.setChecked(HGCategoryListAdapter.this.selected.contains(category));
        holder.selected.setTag(R.id.CATEGORY_TAG, category);
        holder.text.setText(category.getResource_id());

        return convertView;
    }

    private void addCategory(HGCategory category) {
        if (category instanceof HGDiningCategory) {
            List<HGDiningCategory> list = HGSettings.getInstance().getCategoriesFilter();
            list.add((HGDiningCategory) category);
            HGSettings.getInstance().setCategoriesFilter(list);
        } else if (category instanceof HGShopCategory) {
            List<HGShopCategory> list = HGSettings.getInstance().getShopCategoriesFilter();
            list.add((HGShopCategory) category);
            HGSettings.getInstance().setShopCategoriesFilter(list);
        }
    }

    private void removeCategory(HGCategory category) {
        if (category instanceof HGDiningCategory) {
            List<HGDiningCategory> list = HGSettings.getInstance().getCategoriesFilter();
            list.remove((HGDiningCategory) category);
            HGSettings.getInstance().setCategoriesFilter(list);
        } else if (category instanceof HGShopCategory) {
            List<HGShopCategory> list = HGSettings.getInstance().getShopCategoriesFilter();
            list.remove((HGShopCategory) category);
            HGSettings.getInstance().setShopCategoriesFilter(list);
        }
    }

    static class ViewHolder {
        @Bind(R.id.description)
        TextView text;
        @Bind(R.id.selected)
        CheckBox selected;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }
}