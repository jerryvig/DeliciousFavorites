package com.mktneutral.client;

import java.util.ArrayList;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DeliciousFavoriteServiceAsync {
    void getFavorites( DeliciousFavoriteQuery query, AsyncCallback<ArrayList<DeliciousFavorite>> callback );
    // void searchFavorites( DeliciousFavoriteQuery query,  AsyncCallback<ArrayList<DeliciousFavorite>> callback );
}