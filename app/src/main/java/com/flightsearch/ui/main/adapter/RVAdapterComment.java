package com.flightsearch.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flightsearch.databinding.ViewHolderCommentBinding;
import com.flightsearch.databinding.ViewHolderUserSearchItemBinding;
import com.flightsearch.ui.main.activity.MainActivity;
import com.flightsearch.ui.main.fragment.FriendRequestsFragment;
import com.flightsearch.utils.base.BaseFragment;
import com.flightsearch.utils.base.MyRecyclerViewHolder;
import com.flightsearch.utils.firebase.BearerTokenGoogle;
import com.flightsearch.utils.helpers.HelperMethods;
import com.flightsearch.utils.models.out.OutCommentDTO;
import com.flightsearch.utils.models.out.OutUserDTO;
import com.flightsearch.utils.network.service.FlightSearchServicesApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RVAdapterComment extends RecyclerView.Adapter<MyRecyclerViewHolder<OutCommentDTO>> {

    private List<OutCommentDTO> comments;

    public RVAdapterComment(List<OutCommentDTO> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder<OutCommentDTO> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewHolderCommentBinding viewHolderCommentBinding = ViewHolderCommentBinding.inflate(layoutInflater, parent, false);
        return new ViewHolderComment(viewHolderCommentBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder<OutCommentDTO> holder, int position) {
        holder.bindTo(comments.get(position), position);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    protected class ViewHolderComment extends MyRecyclerViewHolder<OutCommentDTO> {

        ViewHolderCommentBinding binding;

        public ViewHolderComment(ViewHolderCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindTo(OutCommentDTO item, int position) {
            super.bindTo(item, position);
            binding.textViewName.setText(item.getUser().getName() + " " + item.getUser().getLastName());
            binding.textViewComment.setText(item.getCommentText());
            binding.textViewCommentDate.setText(HelperMethods.BackendExactTimeStringToDateStringForComment(item.getDateCreated()));
            binding.view1.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            binding.view2.setVisibility(position == comments.size() - 1 ? View.GONE : View.VISIBLE);
        }
    }

}
