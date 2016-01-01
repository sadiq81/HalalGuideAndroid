package dk.eazyit.halalguide.ui.location_details.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import dk.eazyit.halalguide.R;
import dk.eazyit.halalguide.ui.common.views.HGBaseViewHolder;
import dk.eazyit.halalguide.ui.location_details.models.HGLocationDetailsViewModel;
import dk.eazyit.halalguide.ui.location_details.models.HGReviewModel;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Privat on 16/08/15.
 */
public class HGReviewAdapter extends ArrayAdapter<HGReviewModel> {

    HGLocationDetailsViewModel viewModel;
    private List<HGBaseViewHolder> viewHolders = new ArrayList<>();
    private List<Subscription> subscriptions = new ArrayList<>();

    public HGReviewAdapter(Context context, int resource, HGLocationDetailsViewModel viewModel) {
        super(context, resource);
        this.viewModel = viewModel;
        setupViewModel();
    }

    private void setupViewModel() {
        subscriptions.add(viewModel.reviewsModelsSubject.subscribe(new Action1<List<HGReviewModel>>() {
            @Override
            public void call(List<HGReviewModel> hgReviewsModel) {
                clear();
                addAll(hgReviewsModel);
                notifyDataSetChanged();
            }
        }));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HGLocationDetailsHeaderViewHolder headerViewHolder;
        HGReviewViewHolder viewHolder;

        switch (getItemViewType(position)) {
            case 0: {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_location_details_header, null);
                    headerViewHolder = new HGLocationDetailsHeaderViewHolder(convertView, this.viewModel);
                    viewHolders.add(headerViewHolder);
                    convertView.setTag(R.layout.activity_location_details_header, headerViewHolder);
                }
                break;
            }
            case 1: {
                HGReviewModel model = getItem(position - 1);
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_cell_review, null);
                    viewHolder = new HGReviewViewHolder(convertView);
                    viewHolders.add(viewHolder);
                    convertView.setTag(R.layout.listview_cell_review, viewHolder);
                } else {
                    viewHolder = (HGReviewViewHolder) convertView.getTag(R.layout.listview_cell_review);
                }
                viewHolder.setViewModel(model);

                break;
            }
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    public void onPause() {
        for (HGBaseViewHolder viewHolder : viewHolders) {
            viewHolder.onPause();
        }
        for (Subscription sub : subscriptions) {
            sub.unsubscribe();
        }
    }
}
