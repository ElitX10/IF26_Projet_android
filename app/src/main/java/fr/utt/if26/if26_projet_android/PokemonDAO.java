package fr.utt.if26.if26_projet_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class PokemonDAO extends PokeAppDBDAO {

    public PokemonDAO(Context context) {
        super(context);
    }

    /**
     * @param pokemon
     * Enregistrement d'un pokemon dans la BDD
     */
    public void save(Pokemon pokemon){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.ID_POKEMON, pokemon.getId());
        values.put(DataBaseHelper.NOM_POKEMON, pokemon.getNom());

        database.insert(DataBaseHelper.POKEMON_TABLE, null, values);
    }

    /**
     * Ajoute tout les pokemon de la 1er génération dans la BDD
     */
    public void loadFirstGen(){
        List<Pokemon> pokemons = new ArrayList<Pokemon>();
        String pokemonNames[] = {"Bulbizarre", "Herbizarre", "Florizarre", "Salameche", "Reptincel",
                "Dracaufeu", "Carapuce", "Carabaffe", "Tortank", "Chenipan", "Chrysacier", "Papillusion",
                "Aspicot", "Coconfort", "Dardargnan", "Roucool", "Roucoups", "Roucarnage", "Rattata",
                "Rattatac", "Piafabec", "Rapasdepic", "Abo", "Arbok", "Pikachu", "Raichu", "Sabelette",
                "Sablaireau", "Nidoran♀", "Nidorina", "Nidoqueen", "Nidoran♂", "Nidorino", "Nidoking",
                "Melofee", "Melodelfe", "Goupix", "Feunard", "Rondoudou", "Grodoudou", "Nosferapti",
                "Nosferalto", "Mystherbe", "Ortide", "Rafflesia", "Paras", "Parasect", "Mimitoss",
                "Aeromite", "Taupiqueur", "Triopikeur", "Miaouss", "Persian", "Psykokwak", "Akwakwak",
                "Ferosinge", "Colossinge", "Caninos", "Arcanin", "Ptitard", "Tetarte", "Tartard",
                "Abra", "Kadabra", "Alakazam", "Machoc", "Machopeur", "Mackogneur", "Chetiflor",
                "Boustiflor", "Empiflor", "Tentacool", "Tentacruel", "Racaillou", "Gravalanch", "Grolem",
                "Ponyta", "Galopa", "Ramoloss", "Flagadoss", "Magneti", "Magneton", "Canarticho",
                "Doduo", "Dodrio", "Otaria", "Lamantine", "Tadmorv", "Grotadmorv", "Kokiyas", "Crustabri",
                "Fantominus", "Spectrum", "Ectoplasma", "Onix", "Soporifik", "Hypnomade", "Kraby",
                "Krabboss", "Voltorbe", "Electrode", "Noeunoeuf", "Noadkoko", "Osselait", "Ossatueur",
                "Kicklee", "Tygnon", "Excelangue", "Smogo", "Smogogo", "Rhinocorne", "Rhinoferos",
                "Leveinard", "Saquedeneu", "Kangourex", "Hypotrempe", "Hypocean", "Poissirene", "Poissoroy",
                "Stari", "Staross", "M. Mime", "Insecateur", "Lippoutou", "Elektek", "Magmar", "Scarabrute",
                "Tauros", "Magicarpe", "Leviator", "Lokhlass", "Metamorph", "Evoli", "Aquali", "Voltali",
                "Pyroli", "Porygon", "Amonita", "Amonistar", "Kabuto", "Kabutops", "Ptera", "Ronflex",
                "Artikodin", "Electhor", "Sulfura", "Minidraco", "Draco", "Dracolosse", "Mewtwo", "Mew"};
        for(int i=1; i<=151; i++){
            pokemons.add(new Pokemon(i, pokemonNames[i-1]));
        }
        for(Pokemon pokemon : pokemons){
            this.save(pokemon);
        }
    }

    public ArrayList<Pokemon> getAllPokemons(){
        ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>();
        String query = "SELECT * FROM " + DataBaseHelper.POKEMON_TABLE; // todo : order
        Cursor cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()){
            pokemons.add(new Pokemon(cursor.getInt(0), cursor.getString(1)));
        }
        cursor.close();
        return pokemons;
    }

    public Pokemon getPokemon(int id) {
        String query = "SELECT * FROM " + DataBaseHelper.POKEMON_TABLE
                + " WHERE " + DataBaseHelper.POKEMON_TABLE + "." + DataBaseHelper.ID_POKEMON
                + " = '" + id + "'";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            Pokemon pokemon = new Pokemon(cursor.getInt(0),
                    cursor.getString(1));
            return pokemon;
        }
        cursor.close();
        return null;
    }
}
