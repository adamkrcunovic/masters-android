package com.flightsearch.utils.base.bottomSheet.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flightsearch.R;
import com.flightsearch.databinding.ViewHolderRadioButtonTextBinding;
import com.flightsearch.utils.base.BaseActivity;
import com.flightsearch.utils.base.MyRecyclerViewHolder;
import com.flightsearch.utils.base.bottomSheet.BottomSheetChooseRadio;
import com.flightsearch.utils.helpers.HelperMethods;
import com.flightsearch.utils.models.helper.DayDTO;
import com.flightsearch.utils.models.helper.MonthDTO;
import com.flightsearch.utils.models.out.OutAirportDTO;
import com.flightsearch.utils.models.out.OutCountryDTO;
import com.flightsearch.utils.models.out.OutDatePairsDTO;

import java.util.List;

public class RVAdapterRadioButtonText extends RecyclerView.Adapter<MyRecyclerViewHolder<Object>> {

    private List<Object> listOfObjects;
    private BottomSheetChooseRadio.OnObjectSelectListener listener;
    private Object selectedItem;

    public RVAdapterRadioButtonText(List<Object> listOfObjects, BottomSheetChooseRadio.OnObjectSelectListener listener, Object selectedItem) {
        this.listOfObjects = (List<Object>) listOfObjects.get(0);
        this.listener = listener;
        this.selectedItem = selectedItem;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder<Object> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewHolderRadioButtonTextBinding viewHolderRadioButtonTextBinding = ViewHolderRadioButtonTextBinding.inflate(layoutInflater, parent, false);
        return new RadioButtonViewHolder(viewHolderRadioButtonTextBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder<Object> holder, int position) {
        holder.bindTo(listOfObjects.get(position), position);
    }

    @Override
    public int getItemCount() {
        return listOfObjects.size();
    }

    protected class RadioButtonViewHolder extends MyRecyclerViewHolder<Object> {

        ViewHolderRadioButtonTextBinding binding;

        public RadioButtonViewHolder(ViewHolderRadioButtonTextBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindTo(Object item, int position) {
            super.bindTo(item, position);
            binding.view.setVisibility(getAdapterPosition() == listOfObjects.size() - 1 ? View.GONE : View.VISIBLE);
            if (item instanceof OutCountryDTO) {
                OutCountryDTO country = (OutCountryDTO) item;
                binding.textViewRadioButtonText.setText(country.getCountryName());
            }
            if (item instanceof MonthDTO) {
                MonthDTO month = (MonthDTO) item;
                binding.textViewRadioButtonText.setText(month.monthAndYear);
            }
            if (item instanceof DayDTO) {
                DayDTO day = (DayDTO) item;
                binding.textViewRadioButtonText.setText(day.dayString);
            }
            if (item instanceof OutDatePairsDTO) {
                OutDatePairsDTO datePair = (OutDatePairsDTO) item;
                binding.textViewRadioButtonText.setText(HelperMethods.dateStringBEToDateWithName(datePair.getStartDate()) + " - " + HelperMethods.dateStringBEToDateWithName(datePair.getEndDate()) + " (" + datePair.getDaysOff().size() + " day" + (datePair.getDaysOff().size() > 1 ? "s " : " ") + "off)");
            }
            if (item instanceof OutAirportDTO) {
                OutAirportDTO airport = (OutAirportDTO) item;
                binding.textViewRadioButtonText.setText(airport.getName());
            }
            if (item instanceof String) {
                binding.textViewRadioButtonText.setText((String)item);
                binding.checkbox.setChecked(selectedItem.equals(item));
            }
            binding.checkbox.setButtonDrawable(com.flightsearch.R.drawable.custom_checkbox_circle);
            if (!(item instanceof String)) binding.checkbox.setChecked(selectedItem == item);
            binding.constraintLayoutCard.setOnClickListener(v -> {
                listener.onObjectSelectChangeListener(item);
            });
        }
    }

}
