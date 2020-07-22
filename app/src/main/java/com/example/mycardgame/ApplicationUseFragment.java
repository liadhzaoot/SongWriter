package com.example.mycardgame;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.LinkedList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ApplicationUseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApplicationUseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ImageView instructionTV;
    private TextView pageCountTV;
    private TextView descripltionInstructionTV;
    private Button nextBtn;
    private Button prevBtn;
    private LinkedList<Integer> instractionList;
    private LinkedList<Spanned> descriptionInstList;
    private int pageNum = 0;
    private Animation nextAnimation,prevAnimation;

    // TODO: Rename and change types of parameters

    public ApplicationUseFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ApplicationUseFragment newInstance() {
        ApplicationUseFragment fragment = new ApplicationUseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * init linked list return the list
     * @return
     */
    public LinkedList<Integer> initInstractionList()
    {
        LinkedList<Integer> instructionList = new LinkedList<>();
        instructionList.add(R.drawable.app_instruction_1);//page 1
        instructionList.add(R.drawable.app_instruction_2);//page 2
        instructionList.add(R.drawable.app_instruction_3);//page 3
        instructionList.add(R.drawable.app_instruction_4);//page 4
        instructionList.add(R.drawable.app_instruction_5);//page 5
        instructionList.add(R.drawable.app_instruction_6);//page 6
        instructionList.add(R.drawable.app_instruction_7);//page 7
        instructionList.add(R.drawable.app_instruction_8);//page 8
        instructionList.add(R.drawable.app_instruction_17);//page 9
        instructionList.add(R.drawable.app_instruction_9);//page 10
        instructionList.add(R.drawable.app_instruction_10);//page 11
        instructionList.add(R.drawable.app_instruction_11);//page 12
        instructionList.add(R.drawable.app_instruction_12);//page 13
        instructionList.add(R.drawable.app_instruction_13);//page 14
        instructionList.add(R.drawable.app_instruction_14);//page 15
        instructionList.add(R.drawable.app_instruction_15);//page 16
        instructionList.add(R.drawable.app_instruction_16);//page 17


        return instructionList;
    }
    public LinkedList<Spanned> initDescriptionList()
    {
        LinkedList<Spanned> descriptionList = new LinkedList<>();
        descriptionList.add(Html.fromHtml(getString(R.string.description_page_1)));//page 1
        descriptionList.add(Html.fromHtml(getString(R.string.description_page_2)));//page 2
        descriptionList.add(Html.fromHtml(getString(R.string.description_page_3)));//page 3
        descriptionList.add(Html.fromHtml(getString(R.string.description_page_4)));//page 4
        descriptionList.add(Html.fromHtml(getString(R.string.description_page_5)));//page 5
        descriptionList.add(Html.fromHtml(getString(R.string.description_page_6)));//page 6
        descriptionList.add(Html.fromHtml(getString(R.string.description_page_7)));//page 7
        descriptionList.add(Html.fromHtml(getString(R.string.description_page_8)));//page 8
        descriptionList.add(Html.fromHtml(getString(R.string.description_page_17)));//page 9
        descriptionList.add(Html.fromHtml(getString(R.string.description_page_9)));//page 10
        descriptionList.add(Html.fromHtml(getString(R.string.description_page_10)));//page 11
        descriptionList.add(Html.fromHtml(getString(R.string.description_page_11)));//page 12
        descriptionList.add(Html.fromHtml(getString(R.string.description_page_12)));//page 13
        descriptionList.add(Html.fromHtml(getString(R.string.description_page_13)));//page 14
        descriptionList.add(Html.fromHtml(getString(R.string.description_page_14)));//page 15
        descriptionList.add(Html.fromHtml(getString(R.string.description_page_15)));//page 16
        descriptionList.add(Html.fromHtml(getString(R.string.description_page_16)));//page 17
        return descriptionList;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_application_use,container,false);
        nextAnimation = android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.lefttoright);
        prevAnimation = android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.lefttoright);
        //titleNumOfPageTV = v.findViewById(R.id.PageTitle);
        descripltionInstructionTV = v.findViewById(R.id.descripltionInstruction);
        instructionTV = v.findViewById(R.id.applicationUse);
        pageCountTV = v.findViewById(R.id.page_count);
        nextBtn = v.findViewById(R.id.nextBtn);
        prevBtn = v.findViewById(R.id.prevBtn);
        instractionList = new LinkedList<>();
        instractionList = initInstractionList();
        descriptionInstList = new LinkedList<>();
        descriptionInstList = initDescriptionList();
        pageCountTV.setText((int)(pageNum+1) + "/"+instractionList.size());
        /**
         * alwas show first the page 1
         */
        instructionTV.setImageResource(instractionList.get(pageNum));
        descripltionInstructionTV.setText(descriptionInstList.get(pageNum));
        prevBtn.setEnabled(false);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageNum < instractionList.size() - 1) { // check the page is not max
                    pageNum++; // increace the num of page
                    instructionTV.setImageResource(instractionList.get(pageNum)); // set the image of instruction
                    descripltionInstructionTV.setText(descriptionInstList.get(pageNum)); // set the text of description of image
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
                    instructionTV.setImageResource(instractionList.get(pageNum)); // set the text of instraction
                    descripltionInstructionTV.setText(descriptionInstList.get(pageNum)); // set the text of description of image
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
