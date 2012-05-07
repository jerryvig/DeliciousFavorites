package com.mktneutral.client;

import java.util.ArrayList;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("deliciousFavorites")
public interface DeliciousFavoriteService extends RemoteService {
    ArrayList<DeliciousFavorite> getFavorites( String sortColumn, String sortDirection, int startRow );
    
    ArrayList<DeliciousFavorite> searchFavorites( String sortColum, String sortDirection, int startRow, String searchQuery );
}