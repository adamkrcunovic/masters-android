package com.flightsearch.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flightsearch.databinding.ViewHolderSavedTripShortInfoBinding;
import com.flightsearch.databinding.ViewHolderTripItineraryInfoBinding;
import com.flightsearch.utils.base.MyRecyclerViewHolder;
import com.flightsearch.utils.helpers.HelperMethods;
import com.flightsearch.utils.models.out.OutFlightDealDTO;
import com.flightsearch.utils.models.out.OutFlightSegmentDTO;
import com.flightsearch.utils.models.out.OutTripDTO;
import com.flightsearch.utils.models.out.OutUserDTO;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterTripShort extends RecyclerView.Adapter<MyRecyclerViewHolder<OutTripDTO>>{

    private List<OutTripDTO> trips;
    private boolean showPrice;
    private OnFlightDealClickListener listener;

    public interface OnFlightDealClickListener {
        void onClick(OutTripDTO item);
    }

    public RVAdapterTripShort(List<OutTripDTO> trips, boolean showPrice, OnFlightDealClickListener listener) {
        this.trips = trips;
        this.showPrice = showPrice;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder<OutTripDTO> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewHolderSavedTripShortInfoBinding viewHolderSavedTripShortInfoBinding = ViewHolderSavedTripShortInfoBinding.inflate(layoutInflater, parent, false);
        return new SavedTripShortViewHolder(viewHolderSavedTripShortInfoBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder<OutTripDTO> holder, int position) {
        holder.bindTo(trips.get(position), position);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    protected class SavedTripShortViewHolder extends MyRecyclerViewHolder<OutTripDTO> {

        ViewHolderSavedTripShortInfoBinding binding;

        public SavedTripShortViewHolder(ViewHolderSavedTripShortInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindTo(OutTripDTO item, int position) {
            super.bindTo(item, position);
            boolean hasFromFlight = item.getFromSegments() != null && !item.getFromSegments().isEmpty();

            OutFlightSegmentDTO firstTo = item.getToSegments().get(0);
            OutFlightSegmentDTO lastTo = item.getToSegments().get(item.getToSegments().size() - 1);
            OutFlightSegmentDTO firstFrom = hasFromFlight ? item.getFromSegments().get(0) : new OutFlightSegmentDTO();
            OutFlightSegmentDTO lastFrom = hasFromFlight ? item.getFromSegments().get(item.getFromSegments().size() - 1) : new OutFlightSegmentDTO();

            binding.textViewTitle.setText(item.getItineraryName());
            binding.textViewToTime.setText(HelperMethods.dateStringToDateWithName(firstTo.getDeparture()));
            binding.textViewFromTime.setText(HelperMethods.dateStringToDateWithName(hasFromFlight ? lastFrom.getArrival() : lastTo.getArrival()));

            binding.textViewDepartureFromAirport.setText(firstTo.getFrom());
            binding.textViewArrivalAirport.setText(hasFromFlight ? lastFrom.getTo() : lastTo.getTo());
            if (hasFromFlight) {
                binding.textViewMultiCity1.setVisibility(View.VISIBLE);
                binding.textViewMultiCity1.setText(lastTo.getTo());
                if (!lastTo.getTo().equals(firstFrom.getFrom())) {
                    binding.textViewMultiCity2.setVisibility(View.VISIBLE);
                    binding.viewSeparatorMultiCity.setVisibility(View.VISIBLE);
                    binding.textViewMultiCity2.setText(firstFrom.getFrom());
                }
            }

            binding.textViewCreator.setText(item.getCreator().getName() + " " + item.getCreator().getLastName());
            if (showPrice) {
                binding.textViewCurrentPriceTitle.setVisibility(View.VISIBLE);
                binding.textViewCurrentPrice.setVisibility(View.VISIBLE);
                binding.textViewCurrentPrice.setText(item.getTotalPrice() + "€ / " + item.getCurrentPrice() + "€");
            }
            
            String members = "";
            int i = 0;
            for (OutUserDTO member : item.getInvitedMembers()
                    ) {
                if (i < 3) members += member.getName() + " " + member.getLastName() + (item.getInvitedMembers().indexOf(member) != item.getInvitedMembers().size() - 1 ? "\n" : "");
                if (i == 3) members += "...";
                i++;
            }
            if (members.length() > 0) {
                binding.textViewMembersTitle.setVisibility(View.VISIBLE);
                binding.textViewMembers.setVisibility(View.VISIBLE);
                binding.textViewMembers.setText(members);
            }

            binding.materialCardView.setOnClickListener(v -> {
                listener.onClick(item);
            });
        }
    }

}
