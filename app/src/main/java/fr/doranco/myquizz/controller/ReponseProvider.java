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
import java.util.List;

import fr.doranco.myquizz.entity.Question;
import fr.doranco.myquizz.modele.MyDatabaseSQLite;

public class ReponseProvider extends ContentProvider {
    private SQLiteDatabase db;
    private static final String TABLE_NAME_REPONSES = "reponses";
    private static final String PROVIDER_NAME_REPONSES = "fr.doranco.myquizz.controller.ReponseProvider";
    public static final Uri CONTENT_REPONSES_URI = Uri.parse("content://" + PROVIDER_NAME_REPONSES + "/" + TABLE_NAME_REPONSES);

    private static UriMatcher uriMatcher;
    private static final int uriCode = 1;

    private static HashMap<String, String> values;

    static {
        // Default: no match.
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME_REPONSES, "reponses", uriCode);
        uriMatcher.addURI(PROVIDER_NAME_REPONSES, "reponses/*", uriCode);
        uriMatcher.addURI(PROVIDER_NAME_REPONSES, "reponses/#", uriCode);

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
        queryBuilder.setTables(TABLE_NAME_REPONSES);


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

        Long rowId = db.insert(TABLE_NAME_REPONSES, "", values);
        if (rowId > 0) {
            Uri _uri = ContentUris.withAppendedId(uri, rowId);
            // Notify registered observers that a row was updated
            // and attempt to sync changes to the network.
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLiteException("Erreur lors de l'insertion" + CONTENT_REPONSES_URI);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch(uriMatcher.match(uri)){
            case uriCode :
                count = db.delete(TABLE_NAME_REPONSES, selection, selectionArgs);
                break;
            default :
                throw new IllegalArgumentException("URI inconnue : " + uri);
        }
        //observateur qui notifie
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {


        int count = 0;
        switch(uriMatcher.match(uri)){
            case uriCode :
                count = db.update(TABLE_NAME_REPONSES, values, selection, selectionArgs);
                break;
            default :
                throw new IllegalArgumentException("URI inconnue : " + uri);
        }
        //observateur qui notifie
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}
