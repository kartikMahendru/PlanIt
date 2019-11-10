package com.example.kartik.foodmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Event> list;
    private String resultEmail;
    private Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView eventName, eventLink, eventCount;
        public ImageView delete, share;
        public MyViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventLink = itemView.findViewById(R.id.event_link);
            delete = itemView.findViewById(R.id.delete_icon);
            share = itemView.findViewById(R.id.share_icon);
            eventCount = itemView.findViewById(R.id.event_count);
        }
    }

    public EventAdapter(Context context, ArrayList<Event> list, String resultEmail, Activity activity)
    {
        this.context = context;
        this.list = list;
        this.resultEmail = resultEmail;
        this.activity = activity;
    }

    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, final int position) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_event,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(final EventAdapter.MyViewHolder holder, final int position) {
        final Event item = list.get(position);
        holder.eventName.setText(String.valueOf(item.getEventName()));
        holder.eventLink.setText(String.valueOf(item.getEventLink()));
        holder.eventCount.setText(String.valueOf(item.getCount()));

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this event!")
                        .setConfirmText("Yes, delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                databaseReference.child("users").child(resultEmail).child("events").child(item.getEventLink()).removeValue();
                                databaseReference.child("events").child(item.getEventLink()).removeValue();

                                list.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, getItemCount());

                                sDialog
                                        .setTitleText("Deleted!")
                                        .setContentText("Your event has been deleted!")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .show();
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "Hey, please acknowledge your presence in my event " + item.getEventName()
                        + " at this link https://bit.ly/33biL90 using event code " + item.getEventLink() + " .";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share event link"));
            }
        });
    }
}