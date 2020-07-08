package com.example.mycardgame;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.LinkedList;


public class Card implements Serializable {




    enum Subject {
        Theme,
        Melody,
        Lyrics,
        Instrument,
        ChordProgression
    }


    private int cardNumberId;
    private transient  ImageView imageView;
    private String subject;
    private transient Context context;
    private transient Drawable drawable;
    public static Subject cardSubject;
    private int img;
    private int cardNumberInPack;
    private LinkedList<clickLisinear> clickLisinearLinkedList;

    public Card(int cardNumberId, ImageView imageView, final String subject, final Context context, Subject cardSubject, int img, final int cardNumberInPack) {

        this.cardNumberId = cardNumberId;
        this.imageView = imageView;
        this.subject = subject;
        this.context = context;
        this.cardSubject = cardSubject;
        this.img = img;
        this.cardNumberInPack = cardNumberInPack;
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, subject, Toast.LENGTH_SHORT).show();
  //        });
        clickLisinearLinkedList = new LinkedList<>();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,subject , Toast.LENGTH_SHORT).show();
                for (int i = 0; i < clickLisinearLinkedList.size(); i++) {
                    clickLisinearLinkedList.get(i).clickLisinear(cardNumberInPack);
                }
            }
        });

    }

//    private void setImageToImageView()
//    {
//
//        //TODO: CREAT IMAGE VIEW;
//        imageView.setImageDrawable();
//    }

public int getImg() {
    return img;
}

    public void setImg(int img) {
        this.img = img;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getCardNumberId() {
        return cardNumberId;
    }

    public void setCardNumberId(int cardNumberId) {
        this.cardNumberId = cardNumberId;
    }

//    public void changePhoto() {
//        imageView.setImageResource(R.drawable.image1);
//    }
        public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

//    public void changeImg(int img)
//    {
//        imageView.setImageResource(img);
//
//    }
    public void click()
    {

    }


    public void addListinearToList(clickLisinear l)
    {
        clickLisinearLinkedList.add(l);
    }
//    public  ShuffleList(LinkedList<Integer> Mainlist,LinkedList<Integer> HelpList)
//    {
//
//        Random r = new Random();
//        int num,count = numberOfPlayers;
//        LinkedList<Integer> listRandom = new LinkedList<Integer>();
//        if(numberOfPlayers > 1) {
//            for (int i = 0; i < numberOfPlayers; i++) {
//                listRandom.add(i);
//            }
//            for(int k=0;k<key.length;k++) {
//                num = r.nextInt(count);
//                key[k] = listRandom.get(num);
//                listRandom.remove(num);
//                count--;
//            }
//    }
//    public void getImgFromList(LinkedList<Integer> list);
}
