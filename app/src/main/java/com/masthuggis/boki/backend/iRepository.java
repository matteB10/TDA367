package com.masthuggis.boki.backend;

import android.content.Context;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.User;

import org.json.JSONObject;

import java.util.List;

public interface iRepository {
    Advertisement createAdvert(JSONObject jsonObject);
    User createUser();
    List<Advertisement> getAllAds();
    Advertisement getAdFromId(String UUID);
}
