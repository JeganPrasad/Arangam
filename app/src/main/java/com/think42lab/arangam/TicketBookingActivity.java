package com.think42lab.arangam;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.think42lab.arangam.Adapters.TicketSlotRecyclerAdapter;
import com.think42lab.arangam.model.Segment;
import com.think42lab.arangam.model.TicketSlot;
import com.think42lab.arangam.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class TicketBookingActivity extends AppCompatActivity implements TicketSlotRecyclerAdapter.OnSlotSelectListener {

    private Segment segment;

    TextView venue;
    TextView accompanists;
    TextView segmentDateTime, ticketCost, availableTickets;

    TicketSlotRecyclerAdapter ticketSlotRecyclerAdapter;
    RecyclerView ticketSlotRecyclerView;
    List<TicketSlot> mSlotList;
    Button book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.TicketBookAppBaseTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_booking);
        setUpToolBar();
        initializeView();
        if(getIntent()!=null && getIntent().getSerializableExtra("segment")!=null ){
            segment = (Segment) getIntent().getSerializableExtra("segment");
        }
        loadData();

    }

    private void initializeView(){
        venue = (TextView) findViewById(R.id.venue);
        accompanists = (TextView) findViewById(R.id.accompanists);
        segmentDateTime = (TextView) findViewById(R.id.dateTime);
        ticketCost = (TextView) findViewById(R.id.ticketCost);
        availableTickets = (TextView) findViewById(R.id.availableTickets);
        ticketSlotRecyclerView = (RecyclerView) findViewById(R.id.slot_horizontal_recycler_view);
        ticketSlotRecyclerView.setLayoutManager(new LinearLayoutManager(ticketSlotRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        book = (Button) findViewById(R.id.book);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalNumberOfSeatsSelected = ticketSlotRecyclerAdapter.getSelectedValue().getSlotValue();
                int ticketPerCast = Integer.parseInt(segment.getTicketsCost());

                //Call web service here

                showBookingSuccessPopup();
            }
        });
    }

    private void loadData(){
        venue.setText(segment.getVenueName());
        accompanists.setText(segment.getAccompanists());

        /*if(TextUtils.isEmpty(segment.getAccompanists())){
            accompanists.setVisibility(View.GONE);
        }else {
            accompanists.setVisibility(View.VISIBLE);
        }*/

        segmentDateTime.setText(DateUtil.convertDateObjectToString(
                DateUtil.convertStringToCalendar(segment.getSegmentDate(), "dd.MM.yy").getTime(),
                "dd MMMM yyyy") + ", " + segment.getSegmentTime());

        ticketCost.setText("Rs " + segment.getTicketsCost() + " per person");
        availableTickets.setText(segment.getTicketsAvailable() + "  ticket(s)");

        generateSlot(10);
        mSlotList.get(0).setSelected(true);

        book.setText("Book (₹ " + (mSlotList.get(0).getSlotValue() *
                Integer.parseInt(segment.getTicketsCost())) +" )" );


        //To load slot list
        ticketSlotRecyclerAdapter = new TicketSlotRecyclerAdapter(this, mSlotList);
        ticketSlotRecyclerAdapter.setOnSlotSelectListener(this);
        ticketSlotRecyclerView.setAdapter(ticketSlotRecyclerAdapter);
    }

    private void setUpToolBar() {
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
    }

    private void generateSlot(int numberOfSlots){
        mSlotList = new ArrayList<>();
        TicketSlot ticketSlot = null;
        for (int slotCount=1; slotCount <= numberOfSlots; slotCount++ ){
            ticketSlot = new TicketSlot();
            ticketSlot.setSlotValue(slotCount);
            mSlotList.add(ticketSlot);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSlotSelected(int position) {
        ticketSlotRecyclerAdapter.removeSelectedValue();
        ticketSlotRecyclerAdapter.getValueAt(position).setSelected(true);
        book.setText("Book (₹ " + (ticketSlotRecyclerAdapter.getValueAt(position).getSlotValue() *
                Integer.parseInt(segment.getTicketsCost())) +" )" );
        ticketSlotRecyclerAdapter.notifyDataSetChanged();
        ticketSlotRecyclerView.scrollToPosition(position);
    }

    private void showBookingSuccessPopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this) ;
        builder.setView(R.layout.booking_success_popup);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final View cancel = alertDialog.findViewById(R.id.cancel);
        final TextView mobileNumber = (TextView) alertDialog.findViewById(R.id.mobileNumber);
        final View sms = alertDialog.findViewById(R.id.sms);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mobileNumber.getText().toString().length()!= 10){
                    Toast toast = Toast.makeText(TicketBookingActivity.this,"Invalid mobile number",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                alertDialog.dismiss();
                //Call sms generate web service here

                finish();
            }
        });
    }
}
