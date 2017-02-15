package com.think42lab.arangam.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.think42lab.arangam.R;
import com.think42lab.arangam.model.TicketSlot;

import java.util.List;

/**
 * Created by think42lab on 19/12/16.
 */

public class TicketSlotRecyclerAdapter extends RecyclerView.Adapter<TicketSlotRecyclerAdapter.ViewHolder>{

    private Activity mActivity;
    private List<TicketSlot> mTicketSlotList;
    OnSlotSelectListener onSlotSelectListener;

    public TicketSlotRecyclerAdapter(Activity activity, List<TicketSlot> ticketSlotList) {
        mActivity = activity;
        mTicketSlotList = ticketSlotList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_slot_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            final TicketSlot ticketSlot = getValueAt(position);
            holder.slot.setText(String.valueOf(ticketSlot.getSlotValue()));

            holder.slotContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSlotSelectListener != null) {
                        onSlotSelectListener.onSlotSelected(position);
                    }
                }
            });

            holder.slot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSlotSelectListener != null) {
                        onSlotSelectListener.onSlotSelected(position);
                    }
                }
            });


            if (ticketSlot.isSelected()) {
                holder.slotContent.setBackgroundColor(mActivity.getResources().getColor(R.color.selectedSlotContentBackground));
                holder.slot.setTextColor(mActivity.getResources().getColor(R.color.white));
            } else {
                holder.slotContent.setBackgroundColor(mActivity.getResources().getColor(R.color.slotContentBackground));
                holder.slot.setTextColor(mActivity.getResources().getColor(R.color.black));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (mTicketSlotList != null) ? mTicketSlotList.size(): 0;
    }

    public TicketSlot getValueAt(final int position) {
        return mTicketSlotList.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView slot;
        public final LinearLayout slotContent;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            slotContent = (LinearLayout) view.findViewById(R.id.slot_content);
            slot = (TextView) view.findViewById(R.id.slot);
        }
    }

    public interface OnSlotSelectListener{
        public void onSlotSelected(final int position);
    }

    public OnSlotSelectListener getOnSlotSelectListener() {
        return onSlotSelectListener;
    }

    public void setOnSlotSelectListener(OnSlotSelectListener onSlotSelectListener) {
        this.onSlotSelectListener = onSlotSelectListener;
    }

    public void removeSelectedValue(){
        for(TicketSlot ticketSlot : mTicketSlotList){
            ticketSlot.setSelected(false);
        }
    }

    public TicketSlot getSelectedValue(){
        for(TicketSlot ticketSlot : mTicketSlotList){
            if(ticketSlot.isSelected()){
                return ticketSlot;
            }
        }
        return null;
    }

}
