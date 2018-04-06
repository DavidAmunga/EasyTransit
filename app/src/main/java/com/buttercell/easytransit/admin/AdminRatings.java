package com.buttercell.easytransit.admin;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buttercell.easytransit.R;
import com.buttercell.easytransit.model.Rating;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminRatings extends Fragment {

    private static final String TAG = "AdminRatings";
    private FirebaseFirestore firestore;
    private RecyclerView mList;
    FirestoreRecyclerAdapter<Rating, RatingViewHolder> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_ratings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("");

        mList = view.findViewById(R.id.ratingList);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.setHasFixedSize(true);

        firestore = FirebaseFirestore.getInstance();


        Query query = firestore.collection("Ratings").orderBy("timestamp", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<Rating> ratingOptions = new FirestoreRecyclerOptions.Builder<Rating>()
                .setQuery(query, Rating.class)
                .build();


        adapter = new FirestoreRecyclerAdapter<Rating, RatingViewHolder>(ratingOptions) {


            @Override
            protected void onBindViewHolder(@NonNull RatingViewHolder holder, int position, @NonNull Rating model) {
                holder.txtUserComments.setText(model.getUserComments());
                holder.txtUserName.setText(model.getUserName());
                holder.txtUserRating.setText(model.getUserRating());


            }

            @Override
            public RatingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_layout, parent, false);

                return new RatingViewHolder(view);
            }
        };
        adapter.notifyDataSetChanged();
        mList.setAdapter(adapter);


    }


    public static class RatingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtUserRating)
        TextView txtUserRating;
        @BindView(R.id.txtUserComments)
        TextView txtUserComments;
        @BindView(R.id.txtUserName)
        TextView txtUserName;

        public RatingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
