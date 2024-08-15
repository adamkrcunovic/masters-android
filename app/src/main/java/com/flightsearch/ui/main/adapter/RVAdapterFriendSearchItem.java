package com.flightsearch.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flightsearch.application.MainApplication;
import com.flightsearch.databinding.FragmentSearchFriendsBinding;
import com.flightsearch.databinding.ViewHolderUserSearchItemBinding;
import com.flightsearch.ui.main.activity.MainActivity;
import com.flightsearch.ui.main.fragment.FriendRequestsFragment;
import com.flightsearch.ui.main.fragment.SearchFriendsFragment;
import com.flightsearch.utils.base.BaseFragment;
import com.flightsearch.utils.base.MyRecyclerViewHolder;
import com.flightsearch.utils.firebase.BearerTokenGoogle;
import com.flightsearch.utils.helpers.HelperMethods;
import com.flightsearch.utils.models.out.OutUserDTO;
import com.flightsearch.utils.network.service.FlightSearchServicesApi;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RVAdapterFriendSearchItem extends RecyclerView.Adapter<MyRecyclerViewHolder<OutUserDTO>> {

    private List<OutUserDTO> users;
    private OutUserDTO currentlyLoggedUser;
    private MainActivity activity;
    private FlightSearchServicesApi api;
    private BaseFragment fragment;

    public RVAdapterFriendSearchItem(List<OutUserDTO> users, OutUserDTO currentlyLoggedUser, MainActivity activity, FlightSearchServicesApi api, BaseFragment fragment) {
        this.users = users;
        this.currentlyLoggedUser = currentlyLoggedUser;
        this.activity = activity;
        this.api = api;
        this.fragment = fragment;
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
                if (isMe) binding.textViewStatus.setText("Myself");
                if (pending != null) binding.textViewStatus.setText("Pending");
                if (friend != null) binding.textViewStatus.setText("Friends");
            } else {
                binding.materialButtonPositive.setVisibility(View.VISIBLE);
                binding.materialButtonPositive.setText(request != null ? "Accept" : "Add Friend");
                if (request != null) binding.materialButtonNegative.setVisibility(View.VISIBLE);
            }

            binding.materialButtonPositive.setOnClickListener(v -> {
                if (binding.materialButtonPositive.getText().toString().equals("Accept")) {
                    acceptFriendRequest(item, true);
                } else {
                    sendFriendRequest(item);
                }
            });

            binding.materialButtonNegative.setOnClickListener(v -> {
                acceptFriendRequest(item, false);
            });
        }

        private void acceptFriendRequest(OutUserDTO friend, boolean acceptOrReject) {
            activity.showDialog();
            api.acceptOrRejectFriendRequest(friend.getId(), acceptOrReject).enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    activity.dismissDialog();
                    if (response.isSuccessful()) {
                        OutUserDTO foundUserFromList = null;
                        for (OutUserDTO user: currentlyLoggedUser.getRequests()
                             ) {
                            if (user.getId().equals(friend.getId())) foundUserFromList = user;
                        }
                        currentlyLoggedUser.getRequests().remove(foundUserFromList);
                        /*binding.materialButtonPositive.setVisibility(View.GONE);
                        binding.materialButtonNegative.setVisibility(View.GONE);*/
                        if (acceptOrReject) {
                            currentlyLoggedUser.getFriends().add(foundUserFromList);
                            //binding.textViewStatus.setText("Friend");
                            BearerTokenGoogle.sendNotifications(response.body(), "New friend", currentlyLoggedUser.getName() + " " + currentlyLoggedUser.getLastName() + " has accepted your friend request");
                        }
                        fragment.setAdapter(users);
                        ((FriendRequestsFragment)fragment).setLayout();
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable throwable) {
                    activity.dismissDialog();
                }
            });
        }

        private void sendFriendRequest(OutUserDTO friend) {
            activity.showDialog();
            api.sendFriendRequest(friend.getId()).enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    activity.dismissDialog();
                    if (response.isSuccessful()) {
                        currentlyLoggedUser.getPending().add(friend);
                        /*binding.materialButtonPositive.setVisibility(View.GONE);
                        binding.materialButtonNegative.setVisibility(View.GONE);
                        binding.textViewStatus.setText("Pending");*/
                        fragment.setAdapter(users);
                        BearerTokenGoogle.sendNotifications(response.body(), "New friend request", currentlyLoggedUser.getName() + " " + currentlyLoggedUser.getLastName() + " has sent you a friend request");
                        if (fragment instanceof FriendRequestsFragment) ((FriendRequestsFragment)fragment).setLayout();
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable throwable) {
                    activity.dismissDialog();
                }
            });
        }
    }

}
