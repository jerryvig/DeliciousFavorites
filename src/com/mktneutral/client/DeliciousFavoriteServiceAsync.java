package com.mktneutral.client;

import java.util.ArrayList;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DeliciousFavoriteServiceAsync {
    void getFavorites( String sortColumn, String sortDirection, int startRow, AsyncCallback<ArrayList<DeliciousFavorite>> callback );

    void searchFavorites( String sortColum, String sortDirection, int startRow, String searchQuery,  AsyncCallback<ArrayList<DeliciousFavorite>> callback );
}