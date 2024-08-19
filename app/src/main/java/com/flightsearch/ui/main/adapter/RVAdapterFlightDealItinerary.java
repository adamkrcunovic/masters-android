package com.flightsearch.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flightsearch.databinding.ViewHolderTripItineraryInfoBinding;
import com.flightsearch.databinding.ViewHolderTripShortInfoBinding;
import com.flightsearch.utils.base.MyRecyclerViewHolder;
import com.flightsearch.utils.helpers.HelperMethods;
import com.flightsearch.utils.models.out.OutFlightDealDTO;
import com.flightsearch.utils.models.out.OutFlightSegmentDTO;

import java.util.List;

public class RVAdapterFlightDealItinerary extends RecyclerView.Adapter<MyRecyclerViewHolder<OutFlightSegmentDTO>>{

    private List<OutFlightSegmentDTO> flightSegments;
    private List<String> layovers;

    public RVAdapterFlightDealItinerary(List<OutFlightSegmentDTO> flightSegments, List<String> layovers) {
        this.flightSegments = flightSegments;
        this.layovers = layovers;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder<OutFlightSegmentDTO> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewHolderTripItineraryInfoBinding viewHolderTripItineraryInfoBinding = ViewHolderTripItineraryInfoBinding.inflate(layoutInflater, parent, false);
        return new TripItineraryInfoViewHolder(viewHolderTripItineraryInfoBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder<OutFlightSegmentDTO> holder, int position) {
        holder.bindTo(flightSegments.get(position), position);
    }

    @Override
    public int getItemCount() {
        return flightSegments.size();
    }

    protected class TripItineraryInfoViewHolder extends MyRecyclerViewHolder<OutFlightSegmentDTO> {

        ViewHolderTripItineraryInfoBinding binding;

        public TripItineraryInfoViewHolder(ViewHolderTripItineraryInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindTo(OutFlightSegmentDTO item, int position) {
            super.bindTo(item, position);
            binding.textViewDepartureAirport.setText(item.getFrom());
            String sameDayDeparture = flightSegments.get(0).getDeparture().split("T")[0].equals(item.getDeparture().split("T")[0]) ? "" : "(+1)";
            binding.textViewDepartureTime.setText(HelperMethods.dateStringToTime(item.getDeparture()) + sameDayDeparture);
            binding.textViewArrivalAirport.setText(item.getTo());
            String sameDayArrival = flightSegments.get(0).getDeparture().split("T")[0].equals(item.getArrival().split("T")[0]) ? "" : "(+1)";
            binding.textViewArrivalTime.setText(HelperMethods.dateStringToTime(item.getArrival()) + sameDayArrival);
            binding.textViewFlightCode.setText(item.getFlightCode());
            binding.textViewFlightDuration.setText(item.getDuration());

            if (position < flightSegments.size() - 1) {
                binding.viewSeparator1.setVisibility(View.INVISIBLE);
                binding.viewSeparator2.setVisibility(View.INVISIBLE);
                binding.textViewLayoverDuration.setVisibility(View.VISIBLE);
                binding.textViewLayoverDuration.setText(item.getTo() + " - Layover(" + layovers.get(position) + ")");
            }
        }
    }

}
