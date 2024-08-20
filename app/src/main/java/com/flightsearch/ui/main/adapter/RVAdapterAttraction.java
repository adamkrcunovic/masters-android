package com.flightsearch.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flightsearch.databinding.ViewHolderAttractionBinding;
import com.flightsearch.databinding.ViewHolderCommentBinding;
import com.flightsearch.utils.base.MyRecyclerViewHolder;
import com.flightsearch.utils.helpers.HelperMethods;
import com.flightsearch.utils.models.helper.PlanAttractionsDTO;
import com.flightsearch.utils.models.out.OutCommentDTO;

import java.util.List;

public class RVAdapterAttraction extends RecyclerView.Adapter<MyRecyclerViewHolder<PlanAttractionsDTO>> {

    private List<PlanAttractionsDTO> attractions;

    public RVAdapterAttraction(List<PlanAttractionsDTO> attractions) {
        this.attractions = attractions;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder<PlanAttractionsDTO> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewHolderAttractionBinding viewHolderAttractionBinding = ViewHolderAttractionBinding.inflate(layoutInflater, parent, false);
        return new ViewHolderAttraction(viewHolderAttractionBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder<PlanAttractionsDTO> holder, int position) {
        holder.bindTo(attractions.get(position), position);
    }

    @Override
    public int getItemCount() {
        return attractions.size();
    }

    protected class ViewHolderAttraction extends MyRecyclerViewHolder<PlanAttractionsDTO> {

        ViewHolderAttractionBinding binding;

        public ViewHolderAttraction(ViewHolderAttractionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindTo(PlanAttractionsDTO item, int position) {
            super.bindTo(item, position);
            binding.view1.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            binding.view2.setVisibility(position == attractions.size() - 1 ? View.GONE : View.VISIBLE);
            binding.textViewAttraction.setText(item.getAttractionName());
            binding.textViewAttractionPlan.setText(item.getGetAttractionText());
        }
    }

}
