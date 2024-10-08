package com.flightsearch.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flightsearch.databinding.ViewHolderTripShortInfoBinding;
import com.flightsearch.utils.base.MyRecyclerViewHolder;
import com.flightsearch.utils.helpers.HelperMethods;
import com.flightsearch.utils.models.out.OutFlightDealDTO;
import com.flightsearch.utils.models.out.OutFlightSegmentDTO;

import java.util.List;

public class RVAdapterFlightDeal extends RecyclerView.Adapter<MyRecyclerViewHolder<OutFlightDealDTO>>{

    private List<OutFlightDealDTO> flightDeals;
    private OnFlightDealClickListener listener;
    private boolean isCityVisit;

    public interface OnFlightDealClickListener {
        void onClick(OutFlightDealDTO deal);
    }

    public RVAdapterFlightDeal(List<OutFlightDealDTO> flightDeals, OnFlightDealClickListener listener, boolean isCityVisit) {
        this.flightDeals = flightDeals;
        this.listener = listener;
        this.isCityVisit = isCityVisit;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder<OutFlightDealDTO> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewHolderTripShortInfoBinding viewHolderTripShortInfoBinding = ViewHolderTripShortInfoBinding.inflate(layoutInflater, parent, false);
        return new TripShortInfoViewHolder(viewHolderTripShortInfoBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder<OutFlightDealDTO> holder, int position) {
        holder.bindTo(flightDeals.get(position), position);
    }

    @Override
    public int getItemCount() {
        return flightDeals.size();
    }

    protected class TripShortInfoViewHolder extends MyRecyclerViewHolder<OutFlightDealDTO> {

        ViewHolderTripShortInfoBinding binding;

        public TripShortInfoViewHolder(ViewHolderTripShortInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindTo(OutFlightDealDTO item, int position) {
            super.bindTo(item, position);

            //region all
            binding.materialButtonPrice.setText("Total\n" + Math.round(item.getTotalPrice()) + "€");

            //region departure
            List<OutFlightSegmentDTO> fromSegments = item.getToSegments();
            binding.textViewDepartureDate.setText("Departure - " + HelperMethods.dateStringToDateWithName(fromSegments.get(0).getDeparture()));
            binding.textViewDepartureFlightTime.setText(item.getToDuration());
            binding.textViewDepartureFromAirport.setText(fromSegments.get(0).getFrom());
            binding.textViewDepartureFromAirportTime.setText(HelperMethods.dateStringToTime(fromSegments.get(0).getDeparture()));
            binding.textViewDepartureToAirport.setText(fromSegments.get(fromSegments.size() - 1).getTo());
            String sameDayDeparture = fromSegments.get(fromSegments.size() - 1).getArrival().split("T")[0].equals(fromSegments.get(0).getDeparture().split("T")[0]) ? "" : "(+1)";
            binding.textViewDepartureToAirportTime.setText(HelperMethods.dateStringToTime(fromSegments.get(fromSegments.size() - 1).getArrival()) + sameDayDeparture);
            if (fromSegments.size() == 1) {
                binding.textViewDepartureDirect.setVisibility(View.VISIBLE);
            } else {
                binding.imageViewDepartureStop1.setVisibility(View.VISIBLE);
                if (fromSegments.size() == 3) {
                    binding.viewDepartureLayover.setVisibility(View.VISIBLE);
                    binding.imageViewDepartureStop1.setVisibility(View.VISIBLE);
                }
                binding.textViewDepartureLayovers.setText(item.returnLayoversForScreen(true));
            }

            //region return
            List<OutFlightSegmentDTO> returnSegments = item.getFromSegments();
            if (returnSegments != null && !returnSegments.isEmpty()) {
                binding.viewTicketSeparator1.setVisibility(View.VISIBLE);
                binding.textViewTotalStay.setVisibility(View.VISIBLE);
                binding.viewTicketSeparator2.setVisibility(View.VISIBLE);
                binding.constraintLayoutReturn.setVisibility(View.VISIBLE);
                binding.textViewTotalStay.setText(item.getTotalStayDuration());

                binding.textViewReturnDate.setText("Return - " + HelperMethods.dateStringToDateWithName(returnSegments.get(returnSegments.size() - 1).getArrival()));
                binding.textViewReturnFlightTime.setText(item.getFromDuration());
                binding.textViewReturnFromAirport.setText(returnSegments.get(0).getFrom());
                binding.textViewReturnFromAirportTime.setText(HelperMethods.dateStringToTime(returnSegments.get(0).getDeparture()));
                binding.textViewReturnToAirport.setText(returnSegments.get(returnSegments.size() - 1).getTo());
                String sameDayReturn = returnSegments.get(returnSegments.size() - 1).getArrival().split("T")[0].equals(returnSegments.get(0).getDeparture().split("T")[0]) ? "" : "(+1)";
                binding.textViewReturnToAirportTime.setText(HelperMethods.dateStringToTime(returnSegments.get(returnSegments.size() - 1).getArrival()) + sameDayReturn);
                if (returnSegments.size() == 1) {
                    binding.textViewReturnDirect.setVisibility(View.VISIBLE);
                } else {
                    binding.imageViewReturnStop1.setVisibility(View.VISIBLE);
                    if (returnSegments.size() == 3) {
                        binding.viewReturnLayover.setVisibility(View.VISIBLE);
                        binding.imageViewReturnStop1.setVisibility(View.VISIBLE);
                    }
                    binding.textViewReturnLayovers.setText(item.returnLayoversForScreen(false));
                }

                binding.materialButtonPrice.setOnClickListener(v -> listener.onClick(item));
                binding.materialCardView.setOnClickListener(v -> listener.onClick(item));
            }

            //region cityVisit
            if (isCityVisit) {
                binding.constraintLayoutCityVisit.setVisibility(View.VISIBLE);
                String cities = item.getCityVisit().toString();
                binding.textViewCityVisitValue.setText(cities.substring(1, cities.length() - 1));
            }

            binding.materialCardView.setOnClickListener(v -> {
                listener.onClick(item);
            });

            binding.materialButtonPrice.setOnClickListener(v -> {
                listener.onClick(item);
            });
        }
    }

}
