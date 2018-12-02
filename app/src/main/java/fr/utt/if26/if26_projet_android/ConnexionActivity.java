package fr.utt.if26.if26_projet_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class ConnexionActivity extends AppCompatActivity implements View.OnClickListener {

    private Button inscrireButton;
    private Button connecterButton;
    private TextView infosTextView;
    private EditText identifiantEditText;
    private EditText motDePasseEditText;

    private String pseudo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        // init data :
        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            pseudo = extra.getString("pseudo");
        }

        // init views :
        inscrireButton = findViewById(R.id.inscrireConnexionButton);
        connecterButton = findViewById(R.id.connecterConnexionButton);
        infosTextView = findViewById(R.id.infoConnexionTextView);
        identifiantEditText = findViewById(R.id.identifiantConnexionEditText);
        motDePasseEditText = findViewById(R.id.motDePasseConnexionEditText);
        infosTextView.setVisibility(View.GONE);
        if (pseudo == null || pseudo.length() > 0){
            infosTextView.setTextColor(Color.GREEN);
            infosTextView.setText("Inscription réussie ! Vous pouvez vous connecter :"); // todo @string
            infosTextView.setVisibility(View.VISIBLE);
            identifiantEditText.setText(pseudo);
        }

        SharedPreferences myPrefs= getSharedPreferences("dresseur", MODE_PRIVATE);
        int dresseur_id = myPrefs.getInt("dresseur_id", 0);
        if(dresseur_id != 0){
            openMap();
        }

    }

    /**
     * @param v bouton de connexion
     */
    @Override
    public void onClick(View v) {
//        System.out.println(getHashedPassword());
        DresseurDAO dresseurDAO = new DresseurDAO(this);
        String identifiant = identifiantEditText.getText().toString();
        String password = AeSimpleSHA1.getHashedPassword(motDePasseEditText.getText().toString());

        Dresseur dresseur = dresseurDAO.getDresseurByPseudo(identifiant);
        if(dresseur != null && password.equals(dresseur.getPassword())){
            SharedPreferences myPrefs = this.getSharedPreferences("dresseur", MODE_PRIVATE);
            myPrefs.edit().putInt("dresseur_id", dresseur.getId()).apply();
            openMap();
        } else {
            infosTextView.setTextColor(Color.RED);
            infosTextView.setText("Identifiant ou mot de passe incorrect"); // todo @string
            infosTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param v bouton d'inscription
     */
    public void onClickInscription(View v) {
        Intent intent = new Intent(ConnexionActivity.this, InscriptionActivity.class);
        startActivity(intent);
    }

    private void openMap(){
        Intent intent = new Intent(ConnexionActivity.this, MapsActivity.class);
        startActivity(intent);
    }

}
