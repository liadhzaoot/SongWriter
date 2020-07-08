package com.example.mycardgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.LinkedList;

public class ShuffleActivity extends AppCompatActivity implements Serializable {
    public static final String RESULT = "ShuffleCardResult";
    private CardsPack pack;
    private ImageView iv;
    private Button doneBtn, againBtn,shuffleBtn;
    private TextView packNameTxtV;
    private ImageView packBackImgV, finalChooseIv, img;
    private int cardIdInPack;
    private LinearLayout linearLayoutButtons, linearLayout;
    private HorizontalScrollView horizontalScrollView;
    private int cardChoose;
    private LinearLayout gallertyLinearLayout;
    private View view;
    private LinkedList<ImageView> ivLinkedList;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuffle);

        pack = (CardsPack) getIntent().getSerializableExtra(GameActivity.PACK);
        setViews();
        animation = AnimationUtils.loadAnimation(this,R.anim.fadein);
        //action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Shuffle");
        actionBar.setDisplayHomeAsUpEnabled(true);

        linearLayoutButtons.setVisibility(View.INVISIBLE);

        //gallertyLinearLayout = findViewById(R.id.gallery);
        LayoutInflater inflater = LayoutInflater.from(this);


        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT,pack.getChoosenCard());
                setResult(ChooseCardActivity.RESULT_OK,returnIntent);
                finish();
            }
        });
        againBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pack.setChoosenCard(pack.shuffleCard());
                finalChooseIv.setImageResource(pack.getChoosenCard().getImg());
                finalChooseIv.startAnimation(animation);
            }
        });
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pack.setChoosenCard(pack.shuffleCard());
                finalChooseIv.setImageResource(pack.getChoosenCard().getImg());
                finalChooseIv.startAnimation(animation);
                finalChooseIv.setVisibility(View.VISIBLE);
                linearLayoutButtons.setVisibility(View.VISIBLE);
                shuffleBtn.setVisibility(View.INVISIBLE);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addToListListinear() {
        for (int i = 0; i<pack.getPackCard().size();i++)
        {
            //pack.getPackCard().get(i).addListinearToList(this);
        }
    }
    public void setViews()
    {
        ivLinkedList = new LinkedList<>();
        //linearLayout = (LinearLayout)findViewById(R.id.gallery);
        // img = findViewById(R.id.imageView9);
        linearLayoutButtons = findViewById(R.id.doneOrAgainll);
        finalChooseIv = findViewById(R.id.finalChooseIv);
        doneBtn = findViewById(R.id.DoneBtn);
        againBtn = findViewById(R.id.shuffleAgainBtn);
        packNameTxtV = findViewById(R.id.packNameTxV);
        packBackImgV = findViewById(R.id.packBackImgV);
        shuffleBtn = findViewById(R.id.shuffleBtn);

        packNameTxtV.setText(pack.getSubject());
        packBackImgV.setImageResource(pack.getBackCardImg());
    }



}

