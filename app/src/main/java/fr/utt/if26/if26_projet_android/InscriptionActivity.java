package fr.utt.if26.if26_projet_android;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InscriptionActivity extends AppCompatActivity implements View.OnClickListener {

    private Button inscriptionButton;
    private TextView infosTextView;
    private EditText identifiantEditText;
    private EditText motDePasseEditText;
    private EditText confirmationMotDePasseEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        // init views :
        inscriptionButton = findViewById(R.id.inscrireInscriptionButton);
        infosTextView = findViewById(R.id.infoInscriptionTextView);
        identifiantEditText = findViewById(R.id.identifiantInscriptionEditText);
        motDePasseEditText = findViewById(R.id.motDePasseInscriptionEditText);
        confirmationMotDePasseEditText = findViewById(R.id.confirmationMotDePasseInscriptionEditText);
        infosTextView.setVisibility(View.GONE);

        // ajout d'une flèche de retour :
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //todo : @string
        actionBar.setTitle("Inscription");
    }

    @Override
    public void onClick(View v) {
        DresseurDAO dresseurDAO = new DresseurDAO(this);
        String pseudo = identifiantEditText.getText().toString();
        Dresseur dresseur = dresseurDAO.getDresseurByPseudo(pseudo);

        // si le pseudo existe deja :
        if(dresseur != null){
            infosTextView.setText("Ce nom d'utilisateur n'est pas disponible."); // todo @string
            infosTextView.setTextColor(Color.RED);
            infosTextView.setVisibility(View.VISIBLE);
        }
        // si l'identifiant est vide :
        else if (pseudo.trim().length() == 0){
            infosTextView.setText("Vous devez choisir un identifiant."); // todo @string
            infosTextView.setTextColor(Color.RED);
            infosTextView.setVisibility(View.VISIBLE);
        }
        // si le mot de passe est trop court :
        else if (motDePasseEditText.getText().toString().length() < 6) {
            infosTextView.setText("Le mot de passe est trop court : 6 caractères minimum."); // todo @string
            infosTextView.setTextColor(Color.RED);
            infosTextView.setVisibility(View.VISIBLE);
        }
        // si la confirmation du mot de passe n'est pas bonne :
        else if (!motDePasseEditText.getText().toString().equals(confirmationMotDePasseEditText.getText().toString())) {
            infosTextView.setText("La confirmation du mot de passe n'est pas valide."); // todo @string
            infosTextView.setTextColor(Color.RED);
            infosTextView.setVisibility(View.VISIBLE);
        }
        // si tout est ok :
        else {
            String password = AeSimpleSHA1.getHashedPassword(motDePasseEditText.getText().toString());
            if(password.length() != 0){
                dresseurDAO.save(new Dresseur(pseudo, password));
                Intent intent = new Intent(InscriptionActivity.this, ConnexionActivity.class);
                intent.putExtra("pseudo", pseudo);
                startActivity(intent);
            } else {
                infosTextView.setText("Erreur lors de l'inscription"); // todo @string
                infosTextView.setTextColor(Color.RED);
                infosTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * @param item
     * @return
     * Ferme l'activité et revient sur l'activité parente
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
