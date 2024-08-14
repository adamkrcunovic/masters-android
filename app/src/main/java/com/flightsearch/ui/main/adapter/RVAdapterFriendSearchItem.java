package com.flightsearch.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.flightsearch.databinding.ViewHolderUserSearchItemBinding;
import com.flightsearch.utils.base.MyRecyclerViewHolder;
import com.flightsearch.utils.helpers.HelperMethods;
import com.flightsearch.utils.models.out.OutUserDTO;

import java.util.List;

public class RVAdapterFriendSearchItem extends RecyclerView.Adapter<MyRecyclerViewHolder<OutUserDTO>> {

    private List<OutUserDTO> users;
    private OutUserDTO currentlyLoggedUser;

    public RVAdapterFriendSearchItem(List<OutUserDTO> users, OutUserDTO currentlyLoggedUser) {
        this.users = users;
        this.currentlyLoggedUser = currentlyLoggedUser;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder<OutUserDTO> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewHolderUserSearchItemBinding viewHolderUserSearchItemBinding = ViewHolderUserSearchItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolderUserSearchItem(viewHolderUserSearchItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder<OutUserDTO> holder, int position) {
        holder.bindTo(users.get(position), position);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    protected class ViewHolderUserSearchItem extends MyRecyclerViewHolder<OutUserDTO> {

        ViewHolderUserSearchItemBinding binding;

        public ViewHolderUserSearchItem(ViewHolderUserSearchItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindTo(OutUserDTO item, int position) {
            super.bindTo(item, position);
            binding.textViewName.setText(item.getName() + " " + item.getLastName());
            binding.textViewCountry.setText(item.getCountry());
            binding.textViewBirthday.setText(HelperMethods.dateStringBEToDateWithName(item.getBirthday()));
            binding.textViewPreferences.setText(item.getPreferences());

            OutUserDTO friend = null;
            OutUserDTO pending = null;
            OutUserDTO request = null;
            boolean isMe = item.getId().equals(currentlyLoggedUser.getId());

            for (OutUserDTO friendFromUser: currentlyLoggedUser.getFriends()
                 ) {
                if (friendFromUser.getId().equals(item.getId())) {
                    friend = friendFromUser;
                }
            }
            for (OutUserDTO pendingFromUser: currentlyLoggedUser.getPending()
            ) {
                if (pendingFromUser.getId().equals(item.getId())) {
                    pending = pendingFromUser;
                }
            }
            for (OutUserDTO requestFromUser: currentlyLoggedUser.getRequests()
            ) {
                if (requestFromUser.getId().equals(item.getId())) {
                    request = requestFromUser;
                }
            }

            if (isMe || pending != null || friend != null) {
                binding.textViewStatus.setVisibility(View.VISIBLE);
                if (isMe) binding.textViewStatus.setText("Myself");
                if (pending != null) binding.textViewStatus.setText("Pending");
                if (friend != null) binding.textViewStatus.setText("Friends");
            } else {
                binding.materialButtonPositive.setVisibility(View.VISIBLE);
                binding.materialButtonPositive.setText(request != null ? "Accept" : "Add Friend");
                if (request != null) binding.materialButtonNegative.setVisibility(View.VISIBLE);
            }
        }
    }

}
