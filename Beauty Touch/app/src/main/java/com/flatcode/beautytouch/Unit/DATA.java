package com.flatcode.beautytouch.Unit;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DATA {
    //Database
    public static String USERS = "Users";
    public static String POSTS = "Posts";
    public static String SKIN_PRODUCTS = "Skin Products";
    public static String HAIR_PRODUCTS = "Hair Products";
    public static String SHOPPING_CENTERS = "ShoppingCenters";
    public static String M_TOOLS = "Mtools";
    public static String M_REWARD = "MReward";
    public static String NAME = "name";
    public static final String AD_CLICKED = "AdClicked";
    public static final String AD_LOADED = "AdLoaded";
    public static final String BANNER_SKIN = "BannerSkin";
    public static final String BANNER_HAIR = "BannerHair";
    public static final String BANNER_FAVORITES = "BannerFavorites";
    public static final String BANNER_SHOPPING_CENTRES = "BannerShoppingCenters";
    public static final String INTERSTITIAL_HOME = "InterstitialHome";
    public static String ADS_LOADED_COUNT = "adsLoadedCount";
    public static String ADS_CLICKED_COUNT = "adsClickedCount";
    public static String AD_CLICK = "adClick";
    public static String AD_LOAD = "adLoad";
    public static String M_AD = "Mad";
    public static String NULL = "null";
    public static String SAVES = "Saves";
    public static String LIKES = "Likes";
    public static String POST_ID = "postId";
    public static String HOT_PRODUCT = "HotProduct";
    public static int CURRENT_VERSION = 2;
    public static String BASIC = "basic";
    public static String USER_NAME = "username";
    public static String IMAGE_URL = "imageurl";
    public static String IMAGE_LINKS = "ImageLinks";
    public static String EMPTY = "";
    public static String SPACE = " ";
    public static String ID = "id";
    public static String IMAGE = "image";
    public static String PUBLISHER_NAME = "KTWe3PaSUSbv3xulRKSwUgConC92"; //id_
    public static String APP_NAME = "Beauty Touch"; //app_
    public static String WHATSAPP = "https://wa.me/message/E2YOU4NVTIEAD1"; //Whatsapp_
    public static String FB_DESINGER = "fb://profile/100037312172320"; //Facebook_designer
    public static String FB_DESINGER_2 = "https://www.facebook.com/mohamed.deeb.50115"; //Facebook_designer
    public static String FB_PROGRAMMER = "fb://profile/100075460898489"; //Facebook_programmer
    public static String FB_PROGRAMMER_2 = "https://www.facebook.com/100075460898489"; //Facebook_programmer
    public static int MIN_SQUARE = 500;
    //Shared
    public static String COLOR_OPTION = "color_option";
    //Other
    public static String DOT = ".";
    public static final FirebaseAuth AUTH = FirebaseAuth.getInstance();
    public static final FirebaseUser FIREBASE_USER = AUTH.getCurrentUser();
    public static final String FirebaseUserUid = FIREBASE_USER.getUid();
}