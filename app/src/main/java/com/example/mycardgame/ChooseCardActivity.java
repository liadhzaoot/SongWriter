package com.example.mycardgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;

public class ChooseCardActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String RESULT = "ChooseCardResult";
    private CardsPack pack;
    private ImageView iv;
    private Button doneBtn,againBtn;
    private TextView packNameTxtV;
    private ImageView packBackImgV,finalChooseIv,img;
    private int cardIdInPack;
    private LinearLayout linearLayoutButtons,linearLayout;
    private HorizontalScrollView horizontalScrollView;
    private int cardChoose;
    private  LinearLayout gallertyLinearLayout;
    private View view;
    private LinkedList<ImageView> ivLinkedList;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_card);
        pack = (CardsPack) getIntent().getSerializableExtra(GameActivity.PACK);
        animation = AnimationUtils.loadAnimation(this,R.anim.fadein);
        setViews();
        packNameTxtV.setText(pack.getSubject());
        //action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Card Choose");
        actionBar.setDisplayHomeAsUpEnabled(true);

        linearLayoutButtons.setVisibility(View.INVISIBLE);

        //gallertyLinearLayout = findViewById(R.id.gallery);
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < pack.getPackCard().size(); i++) {
            iv = new ImageView(this);
            iv.setImageResource(pack.getPackCard().get(i).getImg());
            iv.setLayoutParams(new android.view.ViewGroup.LayoutParams(400 ,400));
            //------------- to change the marggin ----------------------------------------------------------------------
            ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(iv.getLayoutParams());
            marginParams.setMargins((int) dpFromPx(this,10),0,0,0);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
            //--------------------------------------------------------------------------------------------------------------
            iv.setLayoutParams(layoutParams);
            iv.setMaxHeight(150);
            iv.setMaxWidth(150);
            iv.setId(i);
            iv.setBackground(getResources().getDrawable(R.drawable.image_border));
            linearLayout.addView(iv);
            ivLinkedList.add(iv);

        }
        for (int i = 0; i < ivLinkedList.size(); i++) {
            ivLinkedList.get(i).setOnClickListener(this);
        }
//        img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(ChooseCardActivity.this, ""+iv.getId(), Toast.LENGTH_SHORT).show();
//            }
//        });
        //addToListListinear();

        findImageView();

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
                horizontalScrollView.setVisibility(View.VISIBLE);
                //horizontalScrollView.startAnimation(animation);
                //finalChooseIv.setImageResource(pack.getPackCard().get(cardChoose).getImg());
                finalChooseIv.setVisibility(View.INVISIBLE);
                linearLayoutButtons.setVisibility(View.INVISIBLE);
            }
        });

    }

    /**
     * px tp dpi
     * @param context
     * @param px
     * @return
     */
    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
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
        linearLayout = (LinearLayout)findViewById(R.id.gallery);
        // img = findViewById(R.id.imageView9);
        horizontalScrollView = findViewById(R.id.scrollView2);
        linearLayoutButtons = findViewById(R.id.doneOrAgainll);
        finalChooseIv = findViewById(R.id.finalChooseIv);
        doneBtn = findViewById(R.id.DoneBtn);
        againBtn = findViewById(R.id.chooseAgainBtn);
        packNameTxtV = findViewById(R.id.packNameTxV);
        packBackImgV = findViewById(R.id.packBackImgV);

        packNameTxtV.setText(pack.getSubject());
        packBackImgV.setImageResource(pack.getBackCardImg());
    }


    private void findImageView() {



    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < ivLinkedList.size(); i++) {
            if (ivLinkedList.get(i).getId() == v.getId()) {
                cardChoose = i;
                horizontalScrollView.setVisibility(View.INVISIBLE);
                finalChooseIv.setImageResource(pack.getPackCard().get(cardChoose).getImg());
                finalChooseIv.setVisibility(View.VISIBLE);
                //finalChooseIv.startAnimation(animation);
                linearLayoutButtons.setVisibility(View.VISIBLE);
                pack.setChoosenCard(pack.getPackCard().get(i));
                return;
            }
        }
    }




}
