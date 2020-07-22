package com.example.mycardgame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mycardgame.DataBase.DatabaseHandler;
import com.zolad.zoominimageview.ZoomInImageView;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import uk.co.senab.photoview.PhotoViewAttacher;


public class GameActivity extends AppCompatActivity implements clickLisinear, Serializable, View.OnClickListener {
    public static final String PACK_SUBJECT = "packSubject";
    public static final String PACK_CARD_LIST = "packCardList";
    public static final String CARD_PACK_LINKED_LIST = "cardPackLinkedList";
    public static final String INDEX = "index";
    public static final String PACK_TYPE = "packType";
    public static final String PACK = "pack";
    public static final String SONG_NAME = "songName";
    /**
     * list of packs (גודל 5)
     */
    private LinkedList<CardsPack> cardsPackLinkedList;
    private LinkedList<String> packSubject = new LinkedList<>();
    private LinkedList<Integer> numberOfCardsList = new LinkedList<>();
    private transient LinkedList<ImageView> imageViewCardList = new LinkedList<>();
    private transient LinkedList<ImageView> imageViewArrowList = new LinkedList<>();
    private LinkedList<Integer> lyricsPhotoList;
    private LinkedList<Integer> melodyPhotoList;
    private LinkedList<Integer> instrumentPhotoList;
    private LinkedList<Integer> chordPhotoList;
    private LinkedList<Integer> themePhotoList;
    private LinkedList<Integer> backCradsPhotoList;
    private LinkedList<TextView> packNameTextViewList;
    private LinearLayout llShuffleOrChooseMode, llNoteRecBtn;
    private ZoomInImageView imgCardChoose;
    private int packIntChoose;
    private Card choosenCard;
    private Button shuffleBtn, chooseBtn, btnNote, btnRecord, btnFinish, changeChooseBtn;
    private TextView txtStart, songTitle,cardSubject;
    private int numberOfPackClick = 0;
    private PhotoViewAttacher photoViewAttacher;
    ArrayList<backListener> backListener;
    //animation
    Animation blink;
    //number Of Packs
    private int numberOfPacks = 5;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        shuffleBtn = findViewById(R.id.btnShuffle);
        chooseBtn = findViewById(R.id.btnChoose);
        shuffleBtn.setOnClickListener(this);
        chooseBtn.setOnClickListener(this);
        cardsPackLinkedList = new LinkedList<>();
        imgCardChoose = findViewById(R.id.imgCardChoose);

        llNoteRecBtn = findViewById(R.id.buttonPanel);
        songTitle = findViewById(R.id.songTitle);
        btnNote = findViewById(R.id.noteBtn);
        btnRecord = findViewById(R.id.recordBtn);
        packNameTextViewList = new LinkedList<>();
        backCradsPhotoList = new LinkedList<>();
        txtStart = findViewById(R.id.txtStart);
        txtStart.setBackgroundResource(R.color.CornflowerBlue);
        cardSubject = findViewById(R.id.cardSubject);
        //btnFinish = findViewById(R.id.finishBTN);
        //btnFinish.setVisibility(View.INVISIBLE);
        llShuffleOrChooseMode = findViewById(R.id.shuffleOrChooseMode);
        backListener = new ArrayList<backListener>();
        songTitle.setText((CharSequence) getIntent().getSerializableExtra(StartActivity.SONG_NAME));
        changeChooseBtn = findViewById(R.id.changeChooseBtn);
        blink = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.blink_anim);
        //action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Clinical Card Game");
        actionBar.setDisplayHomeAsUpEnabled(true);

        initBackCradsPhotoList();
        initPackNameTextViewList();
        addImageViewToList();
        addSubjectPackStringToList();
        addNumberOfCardsToList();
        creatCardImgList();

        createCardPack(5, packSubject);
        addToListListinear();
        initImageWithTheLastPic();


//        btnFinish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //the user Finished The song
//                saveState(true);
//                Intent intent = new Intent(v.getContext(), FinalPage.class);
//                intent.putExtra(PACK, cardsPackLinkedList);
//                intent.putExtra(SONG_NAME, songTitle.getText().toString());
//                startActivity(intent);
//                //finish();
//
//            }
//        });

    }

    /*
         packSubject.add("Theme");
        packSubject.add("Melody");
        packSubject.add("Instruments");
        packSubject.add("Lyrics");
        packSubject.add("CordProgression");
         */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent i = new Intent(this, StartActivity.class);
                startActivity(i);

                if (numberOfPackClick != 5) {

                    //the song is not finished
                    saveState(false);
                } else {
                    saveState(true);
                }

                finish();
                return true;
/**
 * if finish choose the cards then show the next btn
 */
            case R.id.nextTV:
                if (numberOfPackClick == 5) {
                    //item.setVisible(true);
                    saveState(true);
                    Intent intent = new Intent(this, FinalPage.class);
                    intent.putExtra(PACK, cardsPackLinkedList);
                    intent.putExtra(SONG_NAME, songTitle.getText().toString());
                    startActivity(intent);

                }
                else{
                    Toast.makeText(this, "You have to choose all the cards", Toast.LENGTH_SHORT).show();
                }
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * saving the last state of spesific song
     */
    public void saveState(boolean is_finished) {
        //convert bool to int
        //0 = false
        //1 = true
        int bool;
        if (is_finished)
            bool = 1;
        else
            bool = 0;


        ArrayList<Integer> arrayList = new ArrayList<>();
        DatabaseHandler dbHendler = new DatabaseHandler(this);
        //check if the user set card img
        // if he had so add to list the cardID(INT)
        // else add null
        for (int i = 0; i < cardsPackLinkedList.size(); i++) {
            if (cardsPackLinkedList.get(i).getChoosenCard() == null) {
                arrayList.add(0);
            } else {
                arrayList.add(cardsPackLinkedList.get(i).getChoosenCard().getCardNumberId());
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss ");
        Date date = new Date(System.currentTimeMillis());

        dbHendler.insertSong(songTitle.getText().toString(),
                arrayList.get(0),
                arrayList.get(1),
                arrayList.get(3),
                arrayList.get(4),
                arrayList.get(2),
                formatter.format(date).toString(),
                bool);
    }


    /**
     * 1 - Theme
     * 2 - Melody
     * 3 - Instruments
     * 4 - Lyrics
     * 5 - CordProgression
     **/
    public void initPackNameTextViewList() {
        TextView tv;
        tv = findViewById(R.id.ThemeTxt);
        packNameTextViewList.add(tv);
        tv = findViewById(R.id.MelodyTxt);
        packNameTextViewList.add(tv);
        tv = findViewById(R.id.InstrumentsTxt);
        packNameTextViewList.add(tv);
        tv = findViewById(R.id.LyricsTxt);
        packNameTextViewList.add(tv);
        tv = findViewById(R.id.CordProgressionTxt);
        packNameTextViewList.add(tv);

    }

    /**
     * done text
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.next_menu, menu);

        return true;
    }

    public void creatCardImgList() {
        int k;
        for (int i = 0; i < packSubject.size(); i++) {
            switch (packSubject.get(i)) {
                case "Theme": {
                    themePhotoList = new LinkedList<>();
                    themePhotoList.add(R.drawable.animals);
                    themePhotoList.add(R.drawable.celebration);
                    themePhotoList.add(R.drawable.courage);
                    themePhotoList.add(R.drawable.creativity);
                    themePhotoList.add(R.drawable.curiosity);
                    themePhotoList.add(R.drawable.emotions);
                    themePhotoList.add(R.drawable.family);
                    themePhotoList.add(R.drawable.friendship);
                    themePhotoList.add(R.drawable.future);
                    themePhotoList.add(R.drawable.healing);
                    themePhotoList.add(R.drawable.love);
                    themePhotoList.add(R.drawable.letting_go);
                    themePhotoList.add(R.drawable.life_experiance);
                    themePhotoList.add(R.drawable.nature);
                    themePhotoList.add(R.drawable.patience);
                    themePhotoList.add(R.drawable.perseverance);
                    themePhotoList.add(R.drawable.positiver_thinking);
                    themePhotoList.add(R.drawable.pride);
                    themePhotoList.add(R.drawable.rebirth);
                    themePhotoList.add(R.drawable.relationships);
                    themePhotoList.add(R.drawable.relaxation);
                    themePhotoList.add(R.drawable.spirituality);
                    themePhotoList.add(R.drawable.traveling);
                    themePhotoList.add(R.drawable.trust);


                }
                case "Lyrics": {
                    lyricsPhotoList = new LinkedList<>();
                    lyricsPhotoList.add(R.drawable.fill_in_the_blank_1);
                    lyricsPhotoList.add(R.drawable.fill_in_the_blank_2);
                    lyricsPhotoList.add(R.drawable.metaphor_1);
                    lyricsPhotoList.add(R.drawable.metaphor_2);
                    lyricsPhotoList.add(R.drawable.the_rap_song);
                    lyricsPhotoList.add(R.drawable.theme_cards_song);
                    lyricsPhotoList.add(R.drawable.associations);
                    lyricsPhotoList.add(R.drawable.black_out_lyrics);
                    lyricsPhotoList.add(R.drawable.blues_inspired_lyrics);
                    lyricsPhotoList.add(R.drawable.brainstorm);
                    lyricsPhotoList.add(R.drawable.songs_9_lines);
                    lyricsPhotoList.add(R.drawable.titles_1_song);

                }
                case "Melody": {
                    melodyPhotoList = new LinkedList<>();
                        melodyPhotoList.add(R.drawable.pentatonic_scale_1);
                        melodyPhotoList.add(R.drawable.pentatonic_scale_2);
                        melodyPhotoList.add(R.drawable.random_notes_1);
                        melodyPhotoList.add(R.drawable.random_notes_2);
                        melodyPhotoList.add(R.drawable.image_1);
                        melodyPhotoList.add(R.drawable.image_2);
                        melodyPhotoList.add(R.drawable.intonation_1);
                        melodyPhotoList.add(R.drawable.intonation_2);
                        melodyPhotoList.add(R.drawable.environment_1);
                        melodyPhotoList.add(R.drawable.environment_2);
                        melodyPhotoList.add(R.drawable.favorite_song_1);
                        melodyPhotoList.add(R.drawable.favorite_song_2);
                    //melodyPhotoList.add(R.drawable.pentatonic_scale_1);
//                melodyPhotoList.add(R.drawable.tamboline);
//                melodyPhotoList.add(R.drawable.orff_instruments);
//                melodyPhotoList.add(R.drawable.paddle_frame);
//                melodyPhotoList.add(R.drawable.shakers);
//                melodyPhotoList.add(R.drawable.sticks);
//                melodyPhotoList.add(R.drawable.bells);
//                melodyPhotoList.add(R.drawable.blocks);
//                melodyPhotoList.add(R.drawable.body_percussion);
//                melodyPhotoList.add(R.drawable.djambe);
//                melodyPhotoList.add(R.drawable.guitar);
//                melodyPhotoList.add(R.drawable.keyboard);
                }
                case "Instrument": {
                    instrumentPhotoList = new LinkedList<>();
//                for(k=0;k<numberOfCardsList.get(i);k++)
//                     instrumentPhotoList.add(R.drawable.instruments_back);
                    instrumentPhotoList.add(R.drawable.voice);
                    instrumentPhotoList.add(R.drawable.tamboline);
                    instrumentPhotoList.add(R.drawable.orff_instruments);
                    instrumentPhotoList.add(R.drawable.paddle_frame);
                    instrumentPhotoList.add(R.drawable.shakers);
                    instrumentPhotoList.add(R.drawable.sticks);
                    instrumentPhotoList.add(R.drawable.bells);
                    instrumentPhotoList.add(R.drawable.blocks);
                    instrumentPhotoList.add(R.drawable.body_percussion);
                    instrumentPhotoList.add(R.drawable.djambe);
                    instrumentPhotoList.add(R.drawable.guitar);
                    instrumentPhotoList.add(R.drawable.keyboard);
                }
                case "Chord": {
                    chordPhotoList = new LinkedList<>();
//                for(k=0;k<numberOfCardsList.get(i);k++)
//                      chordPhotoList.add(R.drawable.chord_progression_back);
                    chordPhotoList.add(R.drawable.chords_1);
                    chordPhotoList.add(R.drawable.chords_2);
                    chordPhotoList.add(R.drawable.chords_3);
                    chordPhotoList.add(R.drawable.chords_4);
                    chordPhotoList.add(R.drawable.chords_5);
                    chordPhotoList.add(R.drawable.chords_6);
                    chordPhotoList.add(R.drawable.chords_7);
                    chordPhotoList.add(R.drawable.chords_8);
                    chordPhotoList.add(R.drawable.chords_9);
                    chordPhotoList.add(R.drawable.chords_10);
                    chordPhotoList.add(R.drawable.chords_11);
                    chordPhotoList.add(R.drawable.chords_12);

                }
            }
        }
    }

    public void addToListListinear() {
        for (int i = 0; i < cardsPackLinkedList.size(); i++) {
            cardsPackLinkedList.get(i).addListinearToList(this);
        }
    }

    /**
     * הפונקציה מחזירה את רשימת התמונות של כל נושא
     *
     * @return
     */
    public LinkedList<Integer> getImgListBySubject(int i) {
        switch (packSubject.get(i)) {
            case "Theme": {
                return themePhotoList;
            }
            case "Lyrics": {
                return lyricsPhotoList;

            }
            case "Melody": {
                return melodyPhotoList;
            }
            case "Instrument": {
                return instrumentPhotoList;
            }
            case "Chord": {
                return chordPhotoList;
            }
        }
        return null;
    }

    /**
     * add image to view card
     * add arraws and animatiom
     */

    public void addImageViewToList() {
        imageViewCardList.add((ImageView) findViewById(R.id.ImgTheme));
        imageViewCardList.add((ImageView) findViewById(R.id.ImgCordProgression));
        imageViewCardList.add((ImageView) findViewById(R.id.ImgInstruments));
        imageViewCardList.add((ImageView) findViewById(R.id.ImgLyrics));
        imageViewCardList.add((ImageView) findViewById(R.id.ImgMelody));

        for (int i = 0; i < imageViewCardList.size(); i++) {
            imageViewCardList.get(i).setImageResource(backCradsPhotoList.get(i));
        }

//help arrow
        imageViewArrowList.add((ImageView) findViewById(R.id.helpArrowTheme));
        imageViewArrowList.add((ImageView) findViewById(R.id.helpArrowChordProgrretion));
        imageViewArrowList.add((ImageView) findViewById(R.id.helpArrowInstruments));
        imageViewArrowList.add((ImageView) findViewById(R.id.helpArrowLyrics));
        imageViewArrowList.add((ImageView) findViewById(R.id.helpArrowMelody));

//        final Animation fadeIn = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.fadein);
//        final Animation fadeOut = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.fadeout);
//        fadeIn.setDuration(1000);
//        fadeOut.setDuration(1000);
//        fadeOut.setStartOffset(1000);
        for (int i = 0; i < imageViewArrowList.size(); i++) {
            imageViewArrowList.get(i).setImageResource(R.drawable.ic_arrow_downward_24dp);
        }


        for (int i = 0; i < imageViewArrowList.size(); i++)
            imageViewArrowList.get(i).startAnimation(blink);

        blink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                for (int i = 0; i < imageViewArrowList.size(); i++)
                    imageViewArrowList.get(i).startAnimation(blink);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

//        imageViewCardList.get(0).setImageResource(R.drawable.theme_back);
//        imageViewCardList.get(1).setImageResource(R.drawable.melody_back);
//        imageViewCardList.get(2).setImageResource(R.drawable.instruments_back);
//        imageViewCardList.get(3).setImageResource(R.drawable.lyrics_back);
//        imageViewCardList.get(4).setImageResource(R.drawable.chord_progression_back);


    }

    /**
     * 1 pack - ThemeCard, 24
     * 2 pack - Melody, 12
     * 3 pack - Instruments, 12
     * 4 pack - Lyrics, 12
     * 5 pack - CardProgression, 12
     *
     * @param numberOfPacks
     * @param packSubject
     */
    public void createCardPack(int numberOfPacks, LinkedList<String> packSubject) {
        /**
         * יוצר את החפיסות הקלפים
         * cardmekadem - משתנה חד חד ערכי לקלף
         * i-1 = בגלל שהלולאה מתחילה ב-1
         */
        Card.Subject s;
        LinkedList<Integer> lli;
        for (int i = 0; i < numberOfPacks; i++) {
            s = returnCardSubjectLikeStringCard(i);
            lli = getImgListBySubject(i);
            cardsPackLinkedList.add(new CardsPack(i, packSubject.get(i), numberOfCardsList.get(i)
                    , (i + 1) * 100, this, imageViewCardList.get(i), s, lli, backCradsPhotoList.get(i)));
        }

    }

    public void initBackCradsPhotoList() {
        backCradsPhotoList.add(R.drawable.theme_front);
        backCradsPhotoList.add(R.drawable.melody_front);
        backCradsPhotoList.add(R.drawable.instruments_front);
        backCradsPhotoList.add(R.drawable.lyrics_front);
        backCradsPhotoList.add(R.drawable.chord_progression_front);
    }

    /**
     * פונקציה מחזירה את סוג בחפיסה בהתאם ל
     * STRING
     * <p>
     * <p>
     * 1 - Theme
     * 2 - Melody
     * 3 - Instruments
     * 4 - Lyrics
     * 5 - CordProgression
     *
     * @param i
     * @return
     */
    private Card.Subject returnCardSubjectLikeStringCard(int i) {
        if (packSubject.get(i).equals("Theme")) {
            return Card.Subject.Theme;
        }
        if (packSubject.get(i).equals("Melody")) {
            return Card.Subject.Melody;
        }
        if (packSubject.get(i).equals("Instrument")) {
            return Card.Subject.Instrument;
        }
        if (packSubject.get(i).equals("Lyrics")) {
            return Card.Subject.Lyrics;
        }
        if (packSubject.get(i).equals("Chord")) {
            return Card.Subject.ChordProgression;
        }
        return null;
    }


    private void addSubjectPackStringToList() {
        packSubject.add("Theme");
        packSubject.add("Melody");
        packSubject.add("Instrument");
        packSubject.add("Lyrics");
        packSubject.add("Chord");
    }

    private void addNumberOfCardsToList() {
        /**
         * מוסיף את מספר הקלפים ביחס להערה הרשומה
         */
        numberOfCardsList.add(24);// theme
        numberOfCardsList.add(12);//melody
        numberOfCardsList.add(12);//Instruments
        numberOfCardsList.add(12);//Lyrics
        numberOfCardsList.add(12);//CardProgression
    }

    @Override
    public void clickLisinear(int i) {
        //Log.d("liadddd","Game Activity Click Listinear");
        /**
         * גיסא 1
         */
//        Intent intent = new Intent(this,shuffleOrChooseMode.class);
//        intent.putExtra(CARD_PACK_LINKED_LIST,cardsPackLinkedList);
//        intent.putExtra(INDEX,i);
//        StartActivity(intent);

        /**
         * גירסא 2
         */
        txtStart.setVisibility(View.INVISIBLE);
        packIntChoose = i;
        cardSubject.setText(cardsPackLinkedList.get(packIntChoose).getSubject());
        /**
         * אם החבילה עוד לא נלחצה
         */
        if (!cardsPackLinkedList.get(packIntChoose).isClick()) {
            changeImgCardChoose(backCradsPhotoList.get(i), imgCardChoose);
//            imgCardChoose.setImageResource(backCradsPhotoList.get(i));
//            imgCardChoose.setVisibility(View.VISIBLE);
            llShuffleOrChooseMode.setVisibility(View.VISIBLE);
            llNoteRecBtn.setVisibility(View.INVISIBLE);
            changeChooseBtn.setVisibility(View.INVISIBLE);
        } else {
            packClick(packIntChoose);
        }


    }

    public void changeImgCardChoose(int img, ImageView iv) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        iv.setImageResource(img);
        iv.setVisibility(View.VISIBLE);
        iv.startAnimation(animation);
    }

    public CardsPack[] LinkedListToArrayList(LinkedList<CardsPack> list) {
        CardsPack[] cp = new CardsPack[list.size()];
        for (int i = 0; i < list.size(); i++) {
            cp[i] = list.get(i);
        }
        return cp;
    }

    @Override
    public void onClick(View v) {
        // Toast.makeText(this, "liadddd", Toast.LENGTH_SHORT).show();
        switch (v.getId()) {

            //shuffleBtn.setVisibility();
            case R.id.btnShuffle: {
                //cardPackList.get(packCardInteger).getPackCard().get(getCardAfterShuffle(cardPackList, packCardInteger));
//                Intent intent = new Intent(this,ShuffleActivity.class);
//                intent.putExtra(CARD_PACK_LIST,cardPackList);
//                intent.putExtra(INDEX,packCardInteger);
//                StartActivity(intent);
                //packClick(0);
                Intent intent = new Intent(this, ShuffleActivity.class);
                intent.putExtra(PACK, cardsPackLinkedList.get(packIntChoose));
                startActivityForResult(intent, 2);
            }
            break;

            case R.id.btnChoose: {
//                Intent i = new Intent(this, ChooseCardActivity.class);
//                i.putExtra("pack",cardsPackLinkedList.get(packIntChoose));
//                StartActivity(i);
                Intent intent = new Intent(this, ChooseCardActivity.class);
                intent.putExtra(PACK, cardsPackLinkedList.get(packIntChoose));
                startActivityForResult(intent, 1);

            }
            break;
            case R.id.recordBtn: {

            }
            break;
            case R.id.noteBtn: {
                Intent intent1 = new Intent();
                //    intent1.putExtra(cardsPackLinkedList.get(packIntChoose).getSubject());
            }
            break;
            default:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == ChooseCardActivity.RESULT_OK) {
                // Card result=data.getExtras(ChooseCardActivity.RESULT);
                Card res = (Card) data.getSerializableExtra(ChooseCardActivity.RESULT);
                choosenCard = res;
                cardsPackLinkedList.get(packIntChoose).setChoosenCard(choosenCard);
                changeImgCardChoose(res.getImg(), imgCardChoose);
                packClick(packIntChoose);
            }
            if (resultCode == ChooseCardActivity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == 2) {
            if (resultCode == ShuffleActivity.RESULT_OK) {
                // Card result=data.getExtras(ChooseCardActivity.RESULT);
                Card res = (Card) data.getSerializableExtra(ShuffleActivity.RESULT);
                choosenCard = res;
                changeImgCardChoose(choosenCard.getImg(), imgCardChoose);
                cardsPackLinkedList.get(packIntChoose).setChoosenCard(choosenCard);
                packClick(packIntChoose);
            }
            if (resultCode == ChooseCardActivity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    public void packClick(int curPack) {
//        photoViewAttacher = new PhotoViewAttacher(imgCardChoose);
//        photoViewAttacher.update();
        /**
         * בודק אם החבילה נלחצה בעבר או לא
         */

//        imgCardChoose.setImageResource(backCradsPhotoList.get(i));
//        imgCardChoose.setVisibility(View.VISIBLE);
        llShuffleOrChooseMode.setVisibility(View.INVISIBLE);
        /**
         * אם החבילה נלחצה
         */
        if (cardsPackLinkedList.get(curPack).isClick()) {
            //cardsPackLinkedList.get(packIntChoose).setChoosenCard(choosenCard);
            changeImgCardChoose(cardsPackLinkedList.get(curPack).getChoosenCard().getImg(), imgCardChoose);
//            imgCardChoose.setImageResource(cardsPackLinkedList.get(packIntChoose).getChoosenCard().getImg());
//            imgCardChoose.setVisibility(View.VISIBLE);
            llNoteRecBtn.setVisibility(View.VISIBLE);
            changeChooseBtn.setVisibility(View.VISIBLE);

        } else {
            txtStart.setVisibility(View.INVISIBLE);

            cardsPackLinkedList.get(curPack).setClick(true);
            /*
            packNameTextViewList.get(curPack).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
             */

            //stop animation
            //---------------------------------------------------------------------------------------------------------------


            if (imageViewArrowList.size() - 1 >= curPack) {
                imageViewArrowList.get(curPack).setAnimation(null);
                imageViewArrowList.get(curPack).setVisibility(View.INVISIBLE);
                //imageViewArrowList.remove(curPack);
            }
            //---------------------------------------------------------------------------------------------------------------
//
            //מחליף את התמונה באפשריות למטה
            // cardsPackLinkedList.get(curPack).setBackCardImg(choosenCard.getImg());

            changeImgCardChoose(cardsPackLinkedList.get(curPack).getBackCardImg(), cardsPackLinkedList.get(curPack).getImageView());
            cardsPackLinkedList.get(curPack).setChoosenCard(choosenCard);
            /*
             cardsPackLinkedList.get(curPack).getImageView().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
             */


            changeImgCardChoose(cardsPackLinkedList.get(curPack).getChoosenCard().getImg(), imgCardChoose);

            llNoteRecBtn.setVisibility(View.VISIBLE);
            changeChooseBtn.setVisibility(View.VISIBLE);
            numberOfPackClick++;
        }
        /**
         *  if finish choose the cards then show the next btn
         */
//        if (numberOfPackClick == 5) {
//            //btnFinish.setVisibility(View.VISIBLE);
//            //btnFinish.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fadein));
//        }

        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NotePad.class);
                intent.putExtra(PACK_TYPE, cardsPackLinkedList.get(packIntChoose).getSubject());
                intent.putExtra(SONG_NAME, songTitle.getText().toString());
                startActivity(intent);
            }
        });
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RecordActivity.class);
                intent.putExtra(PACK_TYPE, cardsPackLinkedList.get(packIntChoose).getSubject());
                intent.putExtra(SONG_NAME, songTitle.getText().toString());
                intent.putExtra(PACK, cardsPackLinkedList.get(packIntChoose));
                startActivity(intent);
            }
        });
        changeChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //invisible
                changeChooseBtn.setVisibility(View.INVISIBLE);
                llNoteRecBtn.setVisibility(View.INVISIBLE);
                //visible
                llShuffleOrChooseMode.setVisibility(View.VISIBLE);
            }
        });

    }


    public Card searchImageViewById(int idCard) {
        for (int i = 0; i < cardsPackLinkedList.size(); i++) {
            for (int j = 0; j < cardsPackLinkedList.get(i).getPackCard().size(); j++) {
                if (cardsPackLinkedList.get(i).getPackCard().get(j).getCardNumberId() == idCard)
                    return cardsPackLinkedList.get(i).getPackCard().get(j);
            }

        }
        return null;
    }

    /**
     * 1 - Theme
     * 2 - Melody
     * 3 - Instruments
     * 4 - Lyrics
     * 5 - CordProgression
     **/
    public void initImageWithTheLastPic() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add((Integer) getIntent().getSerializableExtra(RecyclerViewAdapter.THEME));
        arrayList.add((Integer) getIntent().getSerializableExtra(RecyclerViewAdapter.MELODY));
        arrayList.add((Integer) getIntent().getSerializableExtra(RecyclerViewAdapter.INSTRUMENT));
        arrayList.add((Integer) getIntent().getSerializableExtra(RecyclerViewAdapter.LYRICS));
        arrayList.add((Integer) getIntent().getSerializableExtra(RecyclerViewAdapter.CHORD_POGRESSION));
        String s = (String) getIntent().getSerializableExtra(RecyclerViewAdapter.SONG_NAME);
        boolean isNewSong = true;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) != null) {
                //אם השיר הוא שיר חדש
                isNewSong = false;
                break;
            }
        }

        // count how many is full
        int counter = 0;
//

/**
 *  דואג שהפעולה PACKCLICK תעשה את מה שהיא צריכה(המצב כאן הוא מצב הפןך)
 *         //אם אנחנו ביצירת שיר חדש אז אל תעשה כלום
 */

        if (!isNewSong) {
            //set the choosen card
            for (int i = 0; i < cardsPackLinkedList.size(); i++) {
                //איפה שיש תמונה
                if (arrayList.get(i) != 0) {
                    cardsPackLinkedList.get(i).setChoosenCard(searchImageViewById(arrayList.get(i)));
                    choosenCard = searchImageViewById(arrayList.get(i));
                    packClick(i);
                } else {
                    cardsPackLinkedList.get(i).setClick(true);
                }

                //packClick();
            }

            //מחזיר את המצב למצב המקורי
            for (int i = 0; i < cardsPackLinkedList.size(); i++) {
                if (arrayList.get(i) != 0) {
                    //cardsPackLinkedList.get(i).setChoosenCard(searchImageViewById(arrayList.get(i)));
                    cardsPackLinkedList.get(i).setClick(true);
                } else {
                    cardsPackLinkedList.get(i).setClick(false);
                }
            }
        }
// set the image
//        for (int i = 0; i < cardsPackLinkedList.size(); i++) {
//           changeImgCardChoose(cardsPackLinkedList.get(i).getChoosenCard().getImg(),imgCardChoose);
//        }

    }

    // שם אותך במצב שעזבת את השיר
    public void getsTheSongState() {

    }

    /**
     * when the back button clicked save the state
     */
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, StartActivity.class);
        i.putExtra(SONG_NAME, songTitle.getText().toString());
        startActivity(i);
        //addListener();
        if (numberOfPackClick != 5) {

            //the song is not finished
            saveState(false);
        } else {
            saveState(true);
        }
        finish();
    }

    public void addListener(backListener toAdd) {
        backListener.add(toAdd);
    }


}
