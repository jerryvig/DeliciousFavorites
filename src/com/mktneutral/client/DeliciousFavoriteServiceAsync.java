package com.mktneutral.client;

import java.util.ArrayList;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DeliciousFavoriteServiceAsync {
    void getFavorites( String[] args, AsyncCallback<ArrayList<DeliciousFavorite>> callback );
}