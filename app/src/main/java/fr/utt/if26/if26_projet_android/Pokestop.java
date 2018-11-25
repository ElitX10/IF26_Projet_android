package fr.utt.if26.if26_projet_android;

import android.os.Parcel;
import android.os.Parcelable;

public class Pokestop implements Parcelable {
    private int id;
    private boolean is_gym;
    private long latitude;
    private long longitude;
    private Dresseur dresseur;

    public Pokestop(){
        super();
    }

    protected Pokestop(Parcel in) {
        id = in.readInt();
        is_gym = in.readByte() != 0;
        latitude = in.readLong();
        longitude = in.readLong();
        dresseur = in.readParcelable(Dresseur.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeByte((byte) (get_Is_gym() ? 1 : 0));
        dest.writeLong(getLatitude());
        dest.writeLong(getLongitude());
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

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public Dresseur getDresseur() {
        return dresseur;
    }

    public void setDresseur(Dresseur dresseur) {
        this.dresseur = dresseur;
    }
}
