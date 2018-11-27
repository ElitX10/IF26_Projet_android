package fr.utt.if26.if26_projet_android;

import android.os.Parcel;
import android.os.Parcelable;

public class Dresseur implements Parcelable {
    private int id;
    private String pseudo;
    private String password;

    public Dresseur(){
        super();
    }

    public Dresseur(int id, String pseudo, String password) {
        super();
        this.id = id;
        this.pseudo = pseudo;
        this.password = password;
    }

    protected Dresseur(Parcel in) {
        super();
        id = in.readInt();
        pseudo = in.readString();
        password = in.readString();
    }

    public static final Creator<Dresseur> CREATOR = new Creator<Dresseur>() {
        @Override
        public Dresseur createFromParcel(Parcel in) {
            return new Dresseur(in);
        }

        @Override
        public Dresseur[] newArray(int size) {
            return new Dresseur[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getPseudo());
        dest.writeString(getPassword());
    }

    @Override
    public String toString() {
        return "Dresseur{" +
                "id=" + id +
                ", pseudo='" + pseudo + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    todo : gestion du mot de passes!!! supprimer ou modifier les getters/setters/tostring si inultile
}
