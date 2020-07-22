package com.example.mycardgame;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

public class CardsPack implements Serializable {
    /**
     * packCardNumber -  מתחיל מ-1 משתנה חד חד ערכי
     *
     */
    private LinkedList<Card> packCard = new LinkedList<>();
    private int packCardNumber;
    private String subject;
    private int numOfCards;
    private transient  Context context;
    private transient  ImageView imageView;
    private int backCardImg;
    private Card.Subject packSubject;
    private LinkedList<Integer> PhotoList;
    private transient LinkedList<clickLisinear> clickLisinearLinkedList;
    private boolean isClick = false;
    private Card choosenCard;
    private int randomNumCard;
    /**
     * cardmekadem - משתנה חד חד ערכי לקלף
     */
    public CardsPack(final int packCardNumber, final String subject,
                     int numOfCards, int numCardMekadem, final Context context,
                     final ImageView imageView, final Card.Subject packSubject, LinkedList<Integer> PhotoList, int backCardImg)
    {
//        this.packCard = packCard;

        this.packCardNumber = packCardNumber;
        this.subject = subject;
        this.numOfCards = numOfCards;
        this.context = context;
        this.imageView = imageView;
        this.packSubject = packSubject;
        this.PhotoList = PhotoList;
        this.backCardImg = backCardImg;
        clickLisinearLinkedList = new LinkedList<>();


        for(int i = 1; i <= numOfCards; i++)
        {
            packCard.add(new Card(numCardMekadem+i, imageView, subject,context,
                                    packSubject,PhotoList.get(i-1),i-1));
        }
        //changeImg();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,subject , Toast.LENGTH_SHORT).show();
                //imageView.setBackgroundResource(R.drawable.image_border);
                for (int i = 0; i < clickLisinearLinkedList.size(); i++) {
                    clickLisinearLinkedList.get(i).clickLisinear(packCardNumber);
                }
            }
        });
//        if(packCardNumber == 1)  ce(R.drawable.);
//        else if(packCardNumber == 2)
//        packCard.get(0).getImageView().setImageResource(R.drawable.ink_blurry);
//        else if(packCardNumber == 3)
//            packCard.get(0).getImageView().setImageResource(R.drawable.ink_blurry);
//        else if(packCardNumber == 4)
//            packCard.get(0).getImageView().setImageResource(R.drawable.ink_blurry);
//        else if(packCardNumber == 5)
//            packCard.get(0).getImageView().setImageResource(R.drawable.ink_blurry);

    }
    public void addListinearToList(clickLisinear l)
    {
        clickLisinearLinkedList.add(l);
    }
    public void createList(LinkedList<Integer> list)
    {
        /**
         * המקום הראשון בכל רשימה הוא הגב של הקלף
         */
        switch (packSubject) {
            case Theme: {
                list.add(R.drawable.theme_back);

            }
            case Lyrics:
                list.add(R.drawable.lyrics_back);
            case Melody:
                list.add(R.drawable.melody_back);
            case Instrument:
                list.add(R.drawable.instruments_back);
            case ChordProgression:
                list.add(R.drawable.chord_progression_back);

        }

    }
public Card shuffleCard()
{
    Random r = new Random();
    randomNumCard = r.nextInt(this.getNumOfCards());
    //changeImg();
    return this.getPackCard().get(randomNumCard);
}

    public int getBackCardImg() {
        return backCardImg;
    }

    public void setBackCardImg(int backCardImg) {
        this.backCardImg = backCardImg;
    }

    public void changeImg()
    {
        imageView.setImageResource(packCard.get(randomNumCard).getImg());
    }
    public int getNumOfCards() {
        return numOfCards;
    }

    public void setNumOfCards(int numOfCards) {
        this.numOfCards = numOfCards;
    }

    public LinkedList<Card> getPackCard() {
        return packCard;
    }

    public void setPackCard(LinkedList<Card> packCard) {
        this.packCard = packCard;
    }

    public int getPackCardNumber() {
        return packCardNumber;
    }

    public void setPackCardNumber(int packCardNumber) {
        this.packCardNumber = packCardNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    public Card.Subject getPackSubject() {
        return packSubject;
    }

    public void setPackSubject(Card.Subject packSubject) {
        this.packSubject = packSubject;
    }
    public LinkedList<Integer> getPhotoList() {
        return PhotoList;
    }

    public void setPhotoList(LinkedList<Integer> photoList) {
        PhotoList = photoList;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public Card getChoosenCard() {
        return choosenCard;
    }

    public void setChoosenCard(Card choosenCard) {
        this.choosenCard = choosenCard;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
