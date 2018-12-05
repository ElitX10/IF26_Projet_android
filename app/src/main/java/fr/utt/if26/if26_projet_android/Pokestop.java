package fr.utt.if26.if26_projet_android;

import android.os.Parcel;
import android.os.Parcelable;

public class Pokestop implements Parcelable {
    private int id;
    private boolean is_gym;
    private double latitude;
    private double longitude;
    private Dresseur dresseur;
    private String nom;

    public Pokestop(){
        super();
    }

    protected Pokestop(Parcel in) {
        id = in.readInt();
        is_gym = in.readByte() != 0;
        latitude = in.readDouble();
        longitude = in.readDouble();
        nom = in.readString();
        dresseur = in.readParcelable(Dresseur.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeByte((byte) (get_Is_gym() ? 1 : 0));
        dest.writeDouble(getLatitude());
        dest.writeDouble(getLongitude());
        dest.writeString(getNom());
        dest.writeParcelable(getDresseur(), flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Pokestop> CREATOR = new Creator<Pokestop>() {
        @Override
        public Pokestop createFromParcel(Parcel in) {
            return new Pokestop(in);
        }

        @Override
        public Pokestop[] newArray(int size) {
            return new Pokestop[size];
        }
    };

    @Override
    public String toString() {
        return "Pokestop{" +
                "id=" + id +
                ", is_gym=" + is_gym +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", dresseur=" + dresseur +
                ", nom='" + nom + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean get_Is_gym() {
        return is_gym;
    }

    public void set_Is_gym(boolean is_gym) {
        this.is_gym = is_gym;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Dresseur getDresseur() {
        return dresseur;
    }

    public void setDresseur(Dresseur dresseur) {
        this.dresseur = dresseur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
