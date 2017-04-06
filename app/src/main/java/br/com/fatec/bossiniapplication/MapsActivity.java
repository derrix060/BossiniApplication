package br.com.fatec.bossiniapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int CAMERA_REQUEST = 1;
    private ImageView imageView;

    private List<Challenge> allChallenges = new ArrayList<>();

    private int actualQuestionIndex;

    private TextView questionField;
    private TextView answerField;
    private TextView pointsField;
    private String pointsText;

    private int points;

    private int times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        TextView txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        txtQuestion.setText(getString(R.string.txtQuestion));

        TextView txtAnswer = (TextView) findViewById(R.id.txtAnswer);
        txtAnswer.setText(getString(R.string.txtAsnwer));


        String questions [] = getResources().getStringArray(R.array.questions);
        String answers [] = getResources().getStringArray(R.array.answers);

        questionField = (TextView) findViewById(R.id.question);
        answerField =  (TextView) findViewById(R.id.answer);
        pointsField = (TextView) findViewById(R.id.txtPoints);
        pointsText = pointsField.getText().toString();

        for(int i=0; i< questions.length; i++){
            Challenge challenge = new Challenge(questions[i], answers[i]);
            allChallenges.add(challenge);
        }

        Collections.shuffle(allChallenges);

        final EditText editableField = (EditText) findViewById(R.id.answer);
        editableField.setVisibility(View.INVISIBLE);

        whenClickOnChangeName();

        whenClickOnTakePicture();

        final Button btOk = (Button) findViewById(R.id.btOk);

        final Button btStart = (Button) findViewById(R.id.btStart);

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualQuestionIndex = -1;
                points = 0;
                times = 2;

                setChallengeOnView();
                editableField.setVisibility(View.VISIBLE);
            }
        });

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Challenge actualChallenge = allChallenges.get(actualQuestionIndex);
                if(actualChallenge.isRight(answerField.getText().toString())){
                    points += 2;
                    setChallengeOnView();
                } else{
                    points -= 1;
                    times --;

                    if(times == 0){
                        setChallengeOnView();
                    }
                }

                System.out.println("points - " + points);
                System.out.println("Times - "+ times);

            }



        });

    }

    private Challenge getQuestion(){

        return allChallenges.get(++actualQuestionIndex);
    }

    private void setChallengeOnView(){

        Challenge challenge = getQuestion();

        questionField.setText(challenge.getQuestion());
    }

    private void whenClickOnTakePicture() {
        this.imageView = (ImageView)this.findViewById(R.id.imageView);
        Button photoButton = (Button) this.findViewById(R.id.btTakePicture);

        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }




    private void whenClickOnChangeName() {
        Button changeNameBt = (Button) findViewById(R.id.btChangeName);

        changeNameBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

                builder.setMessage(getText(R.string.txtTypeName));

                final TextView txtWelcome = (TextView) findViewById(R.id.txtWelcome);

                final EditText input = new EditText(MapsActivity.this);

                builder.setView(input);

                builder.setPositiveButton(getText(R.string.btChange), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        String typedText = input.getText().toString();

                        String welcomeString = getText(R.string.welcomeText).toString();

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(welcomeString);
                        stringBuilder.append(" ");
                        stringBuilder.append(typedText);

                        txtWelcome.setText(stringBuilder.toString());

                        Toast.makeText(MapsActivity.this, getText(R.string.sucessChangedName), Toast.LENGTH_SHORT).show();
                    }
                });

                builder.create().show();

            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
