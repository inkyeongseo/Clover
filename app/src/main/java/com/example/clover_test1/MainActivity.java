package com.example.clover_test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<Item> items= new ArrayList<>();

    ItemAdapter adapter;

    Button btn1, btn2, btn3, btn4, btn5;
    MediaPlayer mp;
    SeekBar seekBar;
    TextView text, test;
    public int play = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recycler);

        btn1 = (Button)findViewById(R.id.button1);
        btn2 = (Button)findViewById(R.id.button2);
        btn3 = (Button)findViewById(R.id.button3);
        btn4 = (Button)findViewById(R.id.button4);
        btn5 = (Button)findViewById(R.id.button5);
        text = (TextView)findViewById(R.id.text1);


        adapter= new ItemAdapter(this, items);
        recyclerView.setAdapter(adapter);

        final int[] playList = new int[4];
        playList[0] = R.raw.music1;
        playList[1] = R.raw.music2;
        playList[2] = R.raw.music3;
        playList[3] = R.raw.music4;


        mp = MediaPlayer.create(MainActivity.this, playList[play]);
        mp.start();
        Thread();
        seekBar = (SeekBar)findViewById(R.id.playbar);
        seekBar.setVisibility(ProgressBar.VISIBLE);
        seekBar.setMax(mp.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    mp.seekTo(progress);
                }
                int m = progress / 60000;
                int s = (progress % 60000) / 1000;
                String strTime = String.format("%02d:%02d", m, s);
                text.setText(strTime);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mp.start();

                Thread();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                try
                {
                    mp.prepare();
                }
                catch(IOException ie)
                {
                    ie.printStackTrace();
                }
                mp.seekTo(0);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.pause();
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                try
                {
                    mp.prepare();
                }
                catch(IOException ie)
                {
                    ie.printStackTrace();
                }
                mp.seekTo(0);

                play--;
                if(play>-1){
                    mp = MediaPlayer.create(MainActivity.this, playList[play]);
                    mp.start();

                    Thread();
                }else {
                    play = 3;
                    mp = MediaPlayer.create(MainActivity.this, playList[play]);
                    mp.start();

                    Thread();
                }


            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                try
                {
                    mp.prepare();
                }
                catch(IOException ie)
                {
                    ie.printStackTrace();
                }
                mp.seekTo(0);

                play++;
                if(play<playList.length){
                    mp = MediaPlayer.create(MainActivity.this, playList[play]);
                    mp.start();

                    Thread();
                }else {
                    play = 0;
                    mp = MediaPlayer.create(MainActivity.this, playList[play]);
                    mp.start();

                    Thread();
                }


            }
        });





        //????????????????????? ???????????? ????????? ??????
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        image();

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                image();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_green_light
        );
    }

    public void Thread(){
        Runnable task = new Runnable(){


            public void run(){
                // ????????? ???????????????
                while(mp.isPlaying()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    seekBar.setProgress(mp.getCurrentPosition());
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }



    public void image(){

        //????????? loadDBtoJson.php????????? ???????????? (DB????????????)?????? ??????
        //Volley+ ??????????????? ??????

        //????????????
        String serverUrl="http://contest.dothome.co.kr/android/image.php";

        //????????? JsonArray ?????? ????????????..
        //StringRequest??? ?????????..
        //JsonArrayRequest??? ????????? ??????
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.POST, serverUrl, null, new Response.Listener<JSONArray>() {
            //volley ?????????????????? GET????????? ?????? ??????????????? ????????? ?????? ???????????? ??????????????? ??????. ????????? POST ?????? ??????
            @Override
            public void onResponse(JSONArray response) {

                //??????????????? ???????????? ?????? JsonArray??? ??????

                items.clear();
                adapter.notifyDataSetChanged();
                try {

                    for(int i=0;i<response.length();i++){
                        JSONObject jsonObject= response.getJSONObject(i);

                        int image_id= Integer.parseInt(jsonObject.getString("image_id")); //no??? ?????????????????? ????????????.
                        String name=jsonObject.getString("name");
                        String msg = jsonObject.getString("msg");


                        //????????? ????????? ?????? ?????? IP??? ????????? ???????????????(uploads/xxxx.jpg) ?????? ?????? ??????.
                        name = "http://contest.dothome.co.kr/android/image/"+name;

                        items.add(0,new Item(image_id,name,msg)); // ??? ?????? ??????????????? ???????????? ?????? ??????, ?????? ?????? ?????????
                        adapter.notifyItemInserted(0);
                    }
                } catch (JSONException e) {e.printStackTrace();}

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        //?????? ?????? ????????? ??????????????? ????????? ?????? ??????
        RequestQueue requestQueue= Volley.newRequestQueue(this);

        //???????????? ?????? ?????? ??????
        requestQueue.add(jsonArrayRequest);


    }
}
