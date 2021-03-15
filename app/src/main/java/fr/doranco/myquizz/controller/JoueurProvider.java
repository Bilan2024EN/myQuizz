package fr.doranco.myquizz.controller;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

import fr.doranco.myquizz.modele.MyDatabaseSQLite;

public class JoueurProvider extends ContentProvider  {

    private SQLiteDatabase db;
    private static final String TABLE_NAME_USERS = "joueurs";
    private static final String PROVIDER_NAME_USERS = "fr.doranco.myquizz.controller.JoueurProvider";
    public static final Uri CONTENT_USERS_URI = Uri.parse("content://" + PROVIDER_NAME_USERS + "/" + TABLE_NAME_USERS);

    private static final UriMatcher uriMatcher;
    private static final int uriCode = 1;

    private static HashMap<String, String> values;

    static {
        // Default: no match.
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // content://fr.doranco.contentprovider.UserProvider/Users -> match.
        uriMatcher.addURI(PROVIDER_NAME_USERS, "joueurs", uriCode);
        // content://fr.doranco.contentprovider.UserProvider/Users/* -> n'imote quels caractères.
        uriMatcher.addURI(PROVIDER_NAME_USERS, "joueurs/*", uriCode);
        // content://fr.doranco.contentprovider.UserProvider/Users/#  == n'importe quelles chaines numériques
        uriMatcher.addURI(PROVIDER_NAME_USERS, "joueurs/#", uriCode);

    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        MyDatabaseSQLite dbHelper = new MyDatabaseSQLite(context);
        db = dbHelper.getWritableDatabase();

        if (db != null)
            return true;

        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] columns,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_NAME_USERS);


        int matchCode = uriMatcher.match(uri);

        switch (matchCode) {
            case uriCode:
                queryBuilder.setProjectionMap(values);
                break;
            default:
                throw new IllegalArgumentException("URI inconnue: " + uri);
        }

        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = "id";
        }

        Cursor cursor = queryBuilder.query(db, columns, selection, selectionArgs, null, null, sortOrder);
        //cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) throws SQLiteException {

        Long rowId = db.insert(TABLE_NAME_USERS, "", values);
        if (rowId > 0) {
            Uri _uri = ContentUris.withAppendedId(uri, rowId);
            // Notify registered observers that a row was updated
            // and attempt to sync changes to the network.
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLiteException("Erreur lors de l'insertion" + CONTENT_USERS_URI);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {


        int count = 0;
        switch(uriMatcher.match(uri)){
            case uriCode :
                count = db.update(TABLE_NAME_USERS, values, selection, selectionArgs);
                break;
            default :
                throw new IllegalArgumentException("URI inconnue : " + uri);
        }
        //observateur qui notifie
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}
