package fr.utt.if26.if26_projet_android;

import android.os.Parcel;
import android.os.Parcelable;

public class Pokemon implements Parcelable {
    private int id;
    private String nom;

    public Pokemon(){
        super();
    }

    public Pokemon(int id, String nom){
        super();
        this.id = id;
        this.nom = nom;
    }

    protected Pokemon(Parcel in) {
        super();
        id = in.readInt();
        nom = in.readString();
    }

    public static final Creator<Pokemon> CREATOR = new Creator<Pokemon>() {
        @Override
        public Pokemon createFromParcel(Parcel in) {
            return new Pokemon(in);
        }

        @Override
        public Pokemon[] newArray(int size) {
            return new Pokemon[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getNom());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }

    public String spinnerVal(){
        return "#" + id + " " + nom;
    }
}
