package com.flatcode.beautytouchadmin.Unit;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DATA {

    public static final String PROFILE_ID = "profileId";
    public static final String USERS = "Users";
    public static final String POSTS = "Posts";
    public static final String ADS = "Ad";
    public static final String LIKES = "Likes";
    public static final String POST_ID = "postId";
    public static final String SHOPPING_CENTER_ID = "shoppingCenterId";
    public static final String SAVES = "Saves";
    public static final String M_AD = "Mad";
    public static final String NAME = "name";
    public static final String STARTED = "started";
    public static final String AD_CLICK = "adClick";
    public static final String AD_LOAD = "adLoad";
    public static final String SLIDER_SHOW = "ImageLinks";
    public static final String AD_CLICKED = "AdClicked";
    public static final String AD_LOADED = "AdLoaded";
    public static final String BANNER_SKIN = "BannerSkin";
    public static final String BANNER_HAIR = "BannerHair";
    public static final String BANNER_FAVORITES = "BannerFavorites";
    public static final String BANNER_SHOPPING_CENTRES = "BannerShoppingCenters";
    public static final String INTERSTITIAL_HOME = "InterstitialHome";
    public static int MIX_SLIDER_X = 680;
    public static int MIX_SLIDER_Y = 360;
    public static final String EMPTY = "";
    public static final String SPACE = " ";
    public static final String VIEWS = "views";
    public static final String SHOPPING_CENTERS = "ShoppingCenters";
    public static final String SLIDERS = "ImageLinks";
    public static final String HOT_PRODUCT = "HotProduct";
    public static final String M_TOOLS = "Mtools";
    public static final String BEAUTY_TOUCH = "Beauty Touch";
    public static final String PUBLICHER = "KTWe3PaSUSbv3xulRKSwUgConC92";
    public static final String APP_NAME = "Beauty Touch";
    public static final String ALL = "All";
    public static final String SKIN = "Skin";
    public static final String HAIR = "Hair";
    public static final String DOT = ".";
    //Other
    public static final FirebaseAuth AUTH = FirebaseAuth.getInstance();
    public static final FirebaseUser FIREBASE_USER = AUTH.getCurrentUser();
    public static final String FirebaseUserUid = FIREBASE_USER.getUid();

    //Database
    //public static String USERS = "Users";
    //public static String POSTS = "Posts";
    public static String SKIN_PRODUCTS = "Skin Products";
    public static String HAIR_PRODUCTS = "Hair Products";
    // public static String SHOPPING_CENTERS = "ShoppingCenters";
    //  public static String M_REWARD = "Mreward";
    public static String M_PREWARD = "MPreward";
    public static String M_POINTS = "Mpoints";
    // public static String SAVES = "Saves";
    // public static String LIKES = "Likes";
    // public static String POST_ID = "postId";
    // public static String HOT_PRODUCT = "HotProduct";
    public static int CURRENT_VERSION = 2;
    public static String BASIC = "basic";
    public static String USER_NAME = "username";
    public static String IMAGE_URL = "imageurl";
    //    public static String EMPTY = "";
    //public static String SPACE = " ";
    public static String ID = "id";
    public static String IMAGE = "image";
    public static int MIN_SQUARE = 500;
    //Shared
    public static String COLOR_OPTION = "color_option";
}