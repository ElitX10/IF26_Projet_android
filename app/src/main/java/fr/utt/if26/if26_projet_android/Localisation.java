package fr.utt.if26.if26_projet_android;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;

public class Localisation implements Parcelable {
    private int id;
    private String time;
    private Pokestop pokestop;
    private  Pokemon pokemon;
    private  Dresseur dresseur;

    public  Localisation(){
        super();
    }

    public Localisation(int id, String time, Pokestop pokestop, Pokemon pokemon, Dresseur dresseur) {
        super();
        this.id = id;
        this.time = time;
        this.pokestop = pokestop;
        this.pokemon = pokemon;
        this.dresseur = dresseur;
    }

    protected Localisation(Parcel in) {
        id = in.readInt();
        time = in.readString();
        pokestop = in.readParcelable(Pokestop.class.getClassLoader());
        pokemon = in.readParcelable(Pokemon.class.getClassLoader());
        dresseur = in.readParcelable(Dresseur.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getTime());
        dest.writeParcelable(getPokestop(), flags);
        dest.writeParcelable(getPokemon(), flags);
        dest.writeParcelable(getDresseur(), flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Localisation> CREATOR = new Creator<Localisation>() {
        @Override
        public Localisation createFromParcel(Parcel in) {
            return new Localisation(in);
        }

        @Override
        public Localisation[] newArray(int size) {
            return new Localisation[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Pokestop getPokestop() {
        return pokestop;
    }

    public void setPokestop(Pokestop pokestop) {
        this.pokestop = pokestop;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public Dresseur getDresseur() {
        return dresseur;
    }

    public void setDresseur(Dresseur dresseur) {
        this.dresseur = dresseur;
    }
}
