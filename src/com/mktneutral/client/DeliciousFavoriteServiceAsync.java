package com.mktneutral.client;

import java.util.ArrayList;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DeliciousFavoriteServiceAsync {
    void getFavorites( String sortColumn, String sortDirection, AsyncCallback<ArrayList<DeliciousFavorite>> callback );
}