package com.mktneutral.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DeliciousFavorite implements IsSerializable {
    private String description;
    private String href;
    private String tag;
    private String shared;
    private String privatePost;
    private String time;

    public DeliciousFavorite() {}

    public DeliciousFavorite( String _description, String _href, String _tag, String _shared, String _privatePost, String _time ) {
	description = _description;
	href = _href;
	tag = _tag;
	shared = _shared;
	privatePost = _privatePost;
	time = _time;
    }

    public String getDescription() {
	return description;
    }
  
    public String getHref() {
	return href;
    }

    public String getTag() {
	return tag;
    }

    public String getShared() {
        return shared;
    }

    public String getPrivate() {
	return privatePost;
    }

    public String getTime() {
	return time;
    }
}