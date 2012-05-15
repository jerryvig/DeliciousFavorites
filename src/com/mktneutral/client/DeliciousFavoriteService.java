package com.mktneutral.client;

import java.util.ArrayList;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("deliciousFavorites")
public interface DeliciousFavoriteService extends RemoteService {
    ArrayList<DeliciousFavorite> getFavorites( DeliciousFavoriteQuery _query ) throws Exception;   
    // ArrayList<DeliciousFavorite> searchFavorites( DeliciousFavoriteQuery _query );
}