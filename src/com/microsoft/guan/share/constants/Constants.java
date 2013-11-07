package com.microsoft.guan.share.constants;

public class Constants {
    // 应用的key 请到官方申请正式的appkey替换APP_KEY
    public static final String WEIBO_APP_KEY      = "553109217";
    public static final String WEIBO_APP_SECRET = "e24987529719a60d8cc8d050b988a5f5";// 替换为开发者的appkey，例如"94098772160b6f8ffc1315374d8861f9";
    public static final String TENCENT_APP_KEY = "801438135";
    public static final String TENCENT_APP_SECRET = "c5295fa023181f37f610e3a43c0891a4";// 替换为开发者的appkey，例如"94098772160b6f8ffc1315374d8861f9";

    public static final String RENREN_APP_ID = "155857";

    public static final String RENREN_API_KEY = "a522b135b5e64221b7d251570ada2f65";

    public static final String RENREN_SECRET_KEY = "d1a6bb61f333432fb6e0a49dc16c0150";

    // 替换为开发者REDIRECT_URL
    public static final String WEIBO_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static final String TENCENT_REDIRECT_URL = "http://www.microsoft.com";

    // 新支持scope：支持传入多个scope权限，用逗号分隔
    public static final String WEIBO_SCOPE = 
            "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";

    public static final String CLIENT_ID         = "client_id";
    public static final String RESPONSE_TYPE     = "response_type";
    public static final String USER_REDIRECT_URL = "redirect_uri";
    public static final String DISPLAY           = "display";
    public static final String USER_SCOPE        = "scope";
    public static final String PACKAGE_NAME      = "packagename";
    public static final String KEY_HASH          = "key_hash";
}
