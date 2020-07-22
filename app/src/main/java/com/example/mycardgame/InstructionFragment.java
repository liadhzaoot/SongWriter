package com.example.mycardgame;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.LinkedList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InstructionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InstructionFragment extends Fragment {
    private TextView titleNumOfPageTV;
    private TextView instructionTV;
    private TextView pageCountTV;
    private Button nextBtn;
    private Button prevBtn;
    private LinkedList<Spanned> instractionList;
    private int pageNum = 0;
    private Animation nextAnimation,prevAnimation;
    public InstructionFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static InstructionFragment newInstance() {
        InstructionFragment fragment = new InstructionFragment();
        Bundle args = new Bundle();

        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    /**
     * init linked list return the list
     * @return
     */
    public LinkedList<Spanned> initInstractionList()
    {
        LinkedList<Spanned> instructionList = new LinkedList<>();
        instructionList.add(Html.fromHtml(getString(R.string.page1_instruction)));//page 1
        instructionList.add(Html.fromHtml(getString(R.string.page2_instruction)));//page 2
        instructionList.add(Html.fromHtml(getString(R.string.page3_instruction)));//page 3
        instructionList.add(Html.fromHtml(getString(R.string.page4_instruction)));//page 4

        return instructionList;
    }
    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_instruction,container,false);
        nextAnimation = android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.lefttoright);
        prevAnimation = android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.lefttoright);
        //titleNumOfPageTV = v.findViewById(R.id.PageTitle);
        instructionTV = v.findViewById(R.id.instraction);
        pageCountTV = v.findViewById(R.id.page_count);
        nextBtn = v.findViewById(R.id.nextBtn);
        prevBtn = v.findViewById(R.id.prevBtn);
        instractionList = new LinkedList<>();
        instractionList = initInstractionList();
        pageCountTV.setText((int)(pageNum+1) + "/"+instractionList.size());
        /**
         * alwas show first the page 1
         */
        instructionTV.setText(instractionList.get(pageNum));
        prevBtn.setEnabled(false);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageNum < instractionList.size() - 1) { // check the page is not max
                    pageNum++; // increace the num of page
                    instructionTV.setText(instractionList.get(pageNum)); // set the text of instraction
                    instructionTV.startAnimation(nextAnimation);
                   // titleNumOfPageTV.setText("Page "+(int)(pageNum+1)); //set the page title number
                    pageCountTV.setText((int)(pageNum+1) + "/"+instractionList.size());
                    prevBtn.setEnabled(true);
                }
                if(pageNum>=instractionList.size() - 1)
                {
                    nextBtn.setEnabled(false);
                    prevBtn.setEnabled(true);

                }

            }
        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageNum>0)
                {
                    instructionTV.startAnimation(prevAnimation);
                    pageNum--; // decreace the num of page
                    instructionTV.setText(instractionList.get(pageNum)); // set the text of instraction
                    //titleNumOfPageTV.setText("Page "+(int)(pageNum+1)); //set the page title number
                    pageCountTV.setText((int)(pageNum+1) + "/"+instractionList.size());
                    nextBtn.setEnabled(true);
                }
                if(pageNum<=0)
                {
                    nextBtn.setEnabled(true);
                    prevBtn.setEnabled(false);
                }
            }
        });
        return v;
    }
}
