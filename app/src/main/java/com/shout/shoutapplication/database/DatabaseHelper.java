package com.shout.shoutapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.Utils;
import com.shout.shoutapplication.login.model.ContactModel;
import com.shout.shoutapplication.login.model.CountryListModel;
import com.shout.shoutapplication.main.Adapter.UploadResourceModel;
import com.shout.shoutapplication.main.Model.ChatMessage;
import com.shout.shoutapplication.main.Model.ShoutDefaultListModel;
import com.shout.shoutapplication.main.ShoutDefaultActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Capternal on 26/12/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // public Date date = new Date();
    private static final String DATABASENAME = "shout.db";
    //private final String DATABASENAME = "shout_"+date.getTime()+".db";
    private static final int DATABASEVERSION = 1;


    // TABLE NAME SHOUT RESOURCE
    public static final String strTableNameShoutResources = "shout_resources";
    public static final String strShoutResourceId = "resource_id";
    public static final String strShoutResourceShoutId = "resource_shout_id";
    public static final String strShoutResourceThumbnailPath = "resource_thumbnail_path";
    public static final String strShoutResourceVideoPath = "resource_video_path";

    public static final String strCreateTableNameShoutResource = "CREATE TABLE IF NOT EXISTS "
            + strTableNameShoutResources
            + "("
            + strShoutResourceId
            + " INTEGER PRIMARY KEY  AUTOINCREMENT,"
            + strShoutResourceShoutId
            + " VARCHAR(255),"
            + strShoutResourceThumbnailPath
            + " VARCHAR(255),"
            + strShoutResourceVideoPath
            + " VARCHAR(255)"
            + ")";


    // TABLE NAME MESSAGE USER LIST
    public static final String strTableNameMessageUserList = "message_user_list";

    public static final String strMessageUserListId = "message_user_list_id";
    public static final String strMessageUserListShoutId = "message_user_list_shout_id";
    public static final String strMessageUserListJson = "message_user_list_json";


    public static final String strCreateTableMessageUserList = "CREATE TABLE IF NOT EXISTS "
            + strTableNameMessageUserList
            + "("
            + strMessageUserListId
            + " INTEGER PRIMARY KEY  AUTOINCREMENT,"
            + strMessageUserListShoutId
            + " VARCHAR(255),"
            + strMessageUserListJson
            + " TEXT"
            + ")";

    // TABLE NAME MESSAGE BOARD
    public static final String strTableNameMessageBoard = "message_board";

    public static final String strMessageBoardId = "message_board_id";
    public static final String strMessageBoardJson = "message_board_json";


    public static final String strCreateTableMessageBoard = "CREATE TABLE IF NOT EXISTS "
            + strTableNameMessageBoard
            + "("
            + strMessageBoardId
            + " INTEGER PRIMARY KEY  AUTOINCREMENT,"
            + strMessageBoardJson
            + " TEXT"
            + ")";


    // TABLE NAME CHAT
    public static final String strTableNameChat = "chat";

    public static final String strChatRowId = "chat_row_id";
    public static final String strChatShoutId = "chat_shout_id";
    public static final String strChatUserId = "chat_user_id";
    public static final String strChatJSON = "chat_json";


    public static final String strCreateTableNameChat = "CREATE TABLE IF NOT EXISTS "
            + strTableNameChat
            + "("
            + strChatRowId
            + " INTEGER PRIMARY KEY   AUTOINCREMENT,"
            + strChatShoutId
            + " VARCHAR(255),"
            + strChatUserId
            + " VARCHAR(255),"
            + strChatJSON
            + " TEXT"
            + ")";

    //TABLE NAME FRIENDS

    public static final String strTableNameFriends = "friends";
    public static final String strFriendsUniqRowId = "id";
    public static final String friendsId = "friend_id";
    public static final String friendsName = "friend_name";
    public static final String friendsPhone = "friend_number";
    public static final String isFacebookFriend = "is_facebook_friend";
    public static final String isPhoneFriend = "is_contact_friend";
    public static final String profileImage = "profile_image";
    public static final String friendIsFriend = "is_friend";
    // BUTTON TYPE IS FOR DISPLAYING ACCEPT & REFRESH ICON
    public static final String friendButtonType = "button_type";
    // THIS VARIABLE STATES THAT THE USER IS ALREADY A SHOT FRIEND WHETHER HE HAS DELETED CONTACT NUMBER FROM HIS DEVICE CONTACT.
    public static final String friendIsShoutFriend = "is_shout_friend";



    public static final String strCreateTableFriends = "CREATE TABLE IF NOT EXISTS "
            + strTableNameFriends
            + "("
            + strFriendsUniqRowId
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + friendsId
            + " INTEGER,"
            + friendsName
            + " VARCHAR(255),"
            + friendsPhone
            + " VARCHAR(255),"
            + isFacebookFriend
            + " VARCHAR(255),"
            + isPhoneFriend
            + " VARCHAR(255),"
            + profileImage
            + " VARCHAR(255),"
            + friendIsFriend
            + " VARCHAR(255),"
            + friendButtonType
            + " VARCHAR(255),"
            + friendIsShoutFriend
            + " VARCHAR(255))";

    // TABLE NAME SHOUT
    public static final String strTableNameShout = "shout";

    public static final String strShoutId = "shout_id";
    public static final String strShoutUserId = "user_id";
    public static final String strShoutUserName = "user_name";
    public static final String strShoutUserPic = "user_pic";
    public static final String strShoutCommentCount = "comment_count";
    public static final String strShoutLikeCount = "like_count";
    public static final String strShoutEngageCount = "engaging_count";
    public static final String strShoutType = "shout_type";
    public static final String strShoutTitle = "title";
    public static final String strShoutDescription = "description";
    public static final String strShoutCreateDate = "created_date";
    public static final String strShoutLikeStatus = "is_shout_like";
    public static final String strShoutImage = "shout_image";
    public static final String strShoutHideStatus = "shout_hide_status";
    public static final String strShoutImages = "images";
    // BELOW DATA USED FOR SHOUT DETAIL SCREEN
    public static final String strShoutIsSearchable = "is_searchable";
    public static final String strShoutLatitude = "latitude";
    public static final String strShoutLongitude = "longitude";
    public static final String strShoutAddress = "address";
    public static final String strShoutCategoryName = "category";
    public static final String strShoutCategoryId = "category_id";
    public static final String strShoutIsHidden = "is_hidden";
    public static final String strShoutStartDate = "start_date";
    public static final String strShoutEndDate = "end_date";
    public static final String strShoutReShout = "reshout";
    public static final String strShoutContinueChat = "continue_chat";
    public static final String strShoutDistance = "km";
    // ON SHOUT BOARD IT IS zero=0 & ON MY SHOUT IN PROFILE SCREEN IT IS 1
    public static final String strIsOwnerShoutFlag = "is_owner_shout_flag";
    // TODO: 9/24/2016 Adding is_shout_friend parameter into local db for loading is_engage shouts.
    public static final String strShoutIsFriend = "is_friend";


    public static final String strCreateTableNameShout = "CREATE TABLE IF NOT EXISTS "
            + strTableNameShout
            + "("
            + strShoutId
            + " INTEGER,"
            + strShoutUserId
            + " VARCHAR(255),"
            + strShoutUserName
            + " VARCHAR(255),"
            + strShoutUserPic
            + " VARCHAR(255),"
            + strShoutCommentCount
            + " VARCHAR(255),"
            + strShoutLikeCount
            + " VARCHAR(255),"
            + strShoutEngageCount
            + " VARCHAR(255),"
            + strShoutType
            + " VARCHAR(255),"
            + strShoutTitle
            + " VARCHAR(255),"
            + strShoutDescription
            + " VARCHAR(255),"
            + strShoutCreateDate
            + " VARCHAR(255),"
            + strShoutLikeStatus
            + " VARCHAR(255),"
            + strShoutImage
            + " VARCHAR(255),"
            + strShoutHideStatus
            + " VARCHAR(255),"
            + strShoutImages
            + " VARCHAR(255),"
            + strShoutIsSearchable
            + " VARCHAR(255),"
            + strShoutLatitude
            + " VARCHAR(255),"
            + strShoutLongitude
            + " VARCHAR(255),"
            + strShoutAddress
            + " VARCHAR(255),"
            + strShoutCategoryName
            + " VARCHAR(255),"
            + strShoutCategoryId
            + " VARCHAR(255),"
            + strShoutIsHidden
            + " VARCHAR(255),"
            + strShoutStartDate
            + " VARCHAR(255),"
            + strShoutEndDate
            + " VARCHAR(255),"
            + strShoutReShout
            + " VARCHAR(255),"
            + strShoutContinueChat
            + " VARCHAR(255),"
            + strShoutDistance
            + " VARCHAR(255),"
            + strIsOwnerShoutFlag
            + " VARCHAR(255),"
            + strShoutIsFriend
            + " VARCHAR(255)"
            + ")";


    // TABLE NAME COUNTRY
    public static final String strTableNameCountry = "country";

    public static final String strCountryId = "id";
    public static final String strCountryIso = "iso";
    public static final String strCountryName = "name";
    public static final String strCountryNiceName = "nicename";
    public static final String strCountryIso3 = "iso3";
    public static final String strCountryNumCode = "numcode";
    public static final String strCountryPhoneCode = "phonecode";

    public static final String strCreateTableNameCountry = "CREATE TABLE IF NOT EXISTS "
            + strTableNameCountry
            + "("
            + strCountryId
            + " INTEGER PRIMARY KEY,"
            + strCountryIso
            + " VARCHAR(255),"
            + strCountryName
            + " VARCHAR(255),"
            + strCountryNiceName
            + " VARCHAR(255),"
            + strCountryIso3
            + " VARCHAR(255),"
            + strCountryNumCode
            + " INTEGER,"
            + strCountryPhoneCode
            + " INTEGER" + ")";

    String strDefaultCountryTableValues = "INSERT INTO country (id, iso, name, nicename, iso3, numcode, phonecode) VALUES (1, 'AF', 'AFGHANISTAN', 'Afghanistan', 'AFG', 4, 93),(2, 'AL', 'ALBANIA', 'Albania', 'ALB', 8, 355), (3, 'DZ', 'ALGERIA', 'Algeria', 'DZA', 12, 213), (4, 'AS', 'AMERICAN SAMOA', 'American Samoa', 'ASM', 16, 1684), (5, 'AD', 'ANDORRA', 'Andorra', 'AND', 20, 376), (6, 'AO', 'ANGOLA', 'Angola', 'AGO', 24, 244), (7, 'AI', 'ANGUILLA', 'Anguilla', 'AIA', 660, 1264), (8, 'AQ', 'ANTARCTICA', 'Antarctica', NULL, NULL, 0), (9, 'AG', 'ANTIGUA AND BARBUDA', 'Antigua and Barbuda', 'ATG', 28, 1268), (10, 'AR', 'ARGENTINA', 'Argentina', 'ARG', 32, 54), (11, 'AM', 'ARMENIA', 'Armenia', 'ARM', 51, 374), (12, 'AW', 'ARUBA', 'Aruba', 'ABW', 533, 297), (13, 'AU', 'AUSTRALIA', 'Australia', 'AUS', 36, 61), (14, 'AT', 'AUSTRIA', 'Austria', 'AUT', 40, 43), (15, 'AZ', 'AZERBAIJAN', 'Azerbaijan', 'AZE', 31, 994), (16, 'BS', 'BAHAMAS', 'Bahamas', 'BHS', 44, 1242), (17, 'BH', 'BAHRAIN', 'Bahrain', 'BHR', 48, 973), (18, 'BD', 'BANGLADESH', 'Bangladesh', 'BGD', 50, 880), (19, 'BB', 'BARBADOS', 'Barbados', 'BRB', 52, 1246), (20, 'BY', 'BELARUS', 'Belarus', 'BLR', 112, 375), (21, 'BE', 'BELGIUM', 'Belgium', 'BEL', 56, 32), (22, 'BZ', 'BELIZE', 'Belize', 'BLZ', 84, 501), (23, 'BJ', 'BENIN', 'Benin', 'BEN', 204, 229), (24, 'BM', 'BERMUDA', 'Bermuda', 'BMU', 60, 1441), (25, 'BT', 'BHUTAN', 'Bhutan', 'BTN', 64, 975), (26, 'BO', 'BOLIVIA', 'Bolivia', 'BOL', 68, 591), (27, 'BA', 'BOSNIA AND HERZEGOVINA', 'Bosnia and Herzegovina', 'BIH', 70, 387), (28, 'BW', 'BOTSWANA', 'Botswana', 'BWA', 72, 267), (29, 'BV', 'BOUVET ISLAND', 'Bouvet Island', NULL, NULL, 0), (30, 'BR', 'BRAZIL', 'Brazil', 'BRA', 76, 55), (31, 'IO', 'BRITISH INDIAN OCEAN TERRITORY', 'British Indian Ocean Territory', NULL, NULL, 246), (32, 'BN', 'BRUNEI DARUSSALAM', 'Brunei Darussalam', 'BRN', 96, 673), (33, 'BG', 'BULGARIA', 'Bulgaria', 'BGR', 100, 359), (34, 'BF', 'BURKINA FASO', 'Burkina Faso', 'BFA', 854, 226), (35, 'BI', 'BURUNDI', 'Burundi', 'BDI', 108, 257), (36, 'KH', 'CAMBODIA', 'Cambodia', 'KHM', 116, 855), (37, 'CM', 'CAMEROON', 'Cameroon', 'CMR', 120, 237), (38, 'CA', 'CANADA', 'Canada', 'CAN', 124, 1), (39, 'CV', 'CAPE VERDE', 'Cape Verde', 'CPV', 132, 238), (40, 'KY', 'CAYMAN ISLANDS', 'Cayman Islands', 'CYM', 136, 1345), (41, 'CF', 'CENTRAL AFRICAN REPUBLIC', 'Central African Republic', 'CAF', 140, 236), (42, 'TD', 'CHAD', 'Chad', 'TCD', 148, 235), (43, 'CL', 'CHILE', 'Chile', 'CHL', 152, 56), (44, 'CN', 'CHINA', 'China', 'CHN', 156, 86), (45, 'CX', 'CHRISTMAS ISLAND', 'Christmas Island', NULL, NULL, 61), (46, 'CC', 'COCOS (KEELING) ISLANDS', 'Cocos (Keeling) Islands', NULL, NULL, 672), (47, 'CO', 'COLOMBIA', 'Colombia', 'COL', 170, 57), (48, 'KM', 'COMOROS', 'Comoros', 'COM', 174, 269), (49, 'CG', 'CONGO', 'Congo', 'COG', 178, 242), (50, 'CD', 'CONGO, THE DEMOCRATIC REPUBLIC OF THE', 'Congo, the Democratic Republic of the', 'COD', 180, 242), (51, 'CK', 'COOK ISLANDS', 'Cook Islands', 'COK', 184, 682), (52, 'CR', 'COSTA RICA', 'Costa Rica', 'CRI', 188, 506), (53, 'CI', 'COTE D''IVOIRE', 'Cote D''Ivoire', 'CIV', 384, 225),(54, 'HR', 'CROATIA', 'Croatia', 'HRV', 191, 385), (55, 'CU', 'CUBA', 'Cuba', 'CUB', 192, 53), (56, 'CY', 'CYPRUS', 'Cyprus', 'CYP', 196, 357), (57, 'CZ', 'CZECH REPUBLIC', 'Czech Republic', 'CZE', 203, 420), (58, 'DK', 'DENMARK', 'Denmark', 'DNK', 208, 45), (59, 'DJ', 'DJIBOUTI', 'Djibouti', 'DJI', 262, 253), (60, 'DM', 'DOMINICA', 'Dominica', 'DMA', 212, 1767), (61, 'DO', 'DOMINICAN REPUBLIC', 'Dominican Republic', 'DOM', 214, 1809), (62, 'EC', 'ECUADOR', 'Ecuador', 'ECU', 218, 593), (63, 'EG', 'EGYPT', 'Egypt', 'EGY', 818, 20), (64, 'SV', 'EL SALVADOR', 'El Salvador', 'SLV', 222, 503), (65, 'GQ', 'EQUATORIAL GUINEA', 'Equatorial Guinea', 'GNQ', 226, 240), (66, 'ER', 'ERITREA', 'Eritrea', 'ERI', 232, 291), (67, 'EE', 'ESTONIA', 'Estonia', 'EST', 233, 372), (68, 'ET', 'ETHIOPIA', 'Ethiopia', 'ETH', 231, 251), (69, 'FK', 'FALKLAND ISLANDS (MALVINAS)', 'Falkland Islands (Malvinas)', 'FLK', 238, 500), (70, 'FO', 'FAROE ISLANDS', 'Faroe Islands', 'FRO', 234, 298), (71, 'FJ', 'FIJI', 'Fiji', 'FJI', 242, 679), (72, 'FI', 'FINLAND', 'Finland', 'FIN', 246, 358), (73, 'FR', 'FRANCE', 'France', 'FRA', 250, 33), (74, 'GF', 'FRENCH GUIANA', 'French Guiana', 'GUF', 254, 594), (75, 'PF', 'FRENCH POLYNESIA', 'French Polynesia', 'PYF', 258, 689), (76, 'TF', 'FRENCH SOUTHERN TERRITORIES', 'French Southern Territories', NULL, NULL, 0), (77, 'GA', 'GABON', 'Gabon', 'GAB', 266, 241), (78, 'GM', 'GAMBIA', 'Gambia', 'GMB', 270, 220), (79, 'GE', 'GEORGIA', 'Georgia', 'GEO', 268, 995), (80, 'DE', 'GERMANY', 'Germany', 'DEU', 276, 49), (81, 'GH', 'GHANA', 'Ghana', 'GHA', 288, 233), (82, 'GI', 'GIBRALTAR', 'Gibraltar', 'GIB', 292, 350), (83, 'GR', 'GREECE', 'Greece', 'GRC', 300, 30), (84, 'GL', 'GREENLAND', 'Greenland', 'GRL', 304, 299), (85, 'GD', 'GRENADA', 'Grenada', 'GRD', 308, 1473), (86, 'GP', 'GUADELOUPE', 'Guadeloupe', 'GLP', 312, 590), (87, 'GU', 'GUAM', 'Guam', 'GUM', 316, 1671), (88, 'GT', 'GUATEMALA', 'Guatemala', 'GTM', 320, 502), (89, 'GN', 'GUINEA', 'Guinea', 'GIN', 324, 224), (90, 'GW', 'GUINEA-BISSAU', 'Guinea-Bissau', 'GNB', 624, 245), (91, 'GY', 'GUYANA', 'Guyana', 'GUY', 328, 592), (92, 'HT', 'HAITI', 'Haiti', 'HTI', 332, 509), (93, 'HM', 'HEARD ISLAND AND MCDONALD ISLANDS', 'Heard Island and Mcdonald Islands', NULL, NULL, 0), (94, 'VA', 'HOLY SEE (VATICAN CITY STATE)', 'Holy See (Vatican City State)', 'VAT', 336, 39), (95, 'HN', 'HONDURAS', 'Honduras', 'HND', 340, 504), (96, 'HK', 'HONG KONG', 'Hong Kong', 'HKG', 344, 852), (97, 'HU', 'HUNGARY', 'Hungary', 'HUN', 348, 36), (98, 'IS', 'ICELAND', 'Iceland', 'ISL', 352, 354), (99, 'IN', 'INDIA', 'India', 'IND', 356, 91), (100, 'ID', 'INDONESIA', 'Indonesia', 'IDN', 360, 62), (101, 'IR', 'IRAN, ISLAMIC REPUBLIC OF', 'Iran, Islamic Republic of', 'IRN', 364, 98), (102, 'IQ', 'IRAQ', 'Iraq', 'IRQ', 368, 964), (103, 'IE', 'IRELAND', 'Ireland', 'IRL', 372, 353), (104, 'IL', 'ISRAEL', 'Israel', 'ISR', 376, 972), (105, 'IT', 'ITALY', 'Italy', 'ITA', 380, 39), (106, 'JM', 'JAMAICA', 'Jamaica', 'JAM', 388, 1876), (107, 'JP', 'JAPAN', 'Japan', 'JPN', 392, 81), (108, 'JO', 'JORDAN', 'Jordan', 'JOR', 400, 962), (109, 'KZ', 'KAZAKHSTAN', 'Kazakhstan', 'KAZ', 398, 7), (110, 'KE', 'KENYA', 'Kenya', 'KEN', 404, 254), (111, 'KI', 'KIRIBATI', 'Kiribati', 'KIR', 296, 686), (112, 'KP', 'KOREA, DEMOCRATIC PEOPLE''S REPUBLIC OF', 'Korea, Democratic People''s Republic of', 'PRK', 408, 850), (113, 'KR', 'KOREA, REPUBLIC OF', 'Korea, Republic of', 'KOR', 410, 82), (114, 'KW', 'KUWAIT', 'Kuwait', 'KWT', 414, 965), (115, 'KG', 'KYRGYZSTAN', 'Kyrgyzstan', 'KGZ', 417, 996), (116, 'LA', 'LAO PEOPLE''S DEMOCRATIC REPUBLIC', 'Lao People''s Democratic Republic', 'LAO', 418, 856), (117, 'LV', 'LATVIA', 'Latvia', 'LVA', 428, 371), (118, 'LB', 'LEBANON', 'Lebanon', 'LBN', 422, 961), (119, 'LS', 'LESOTHO', 'Lesotho', 'LSO', 426, 266), (120, 'LR', 'LIBERIA', 'Liberia', 'LBR', 430, 231), (121, 'LY', 'LIBYAN ARAB JAMAHIRIYA', 'Libyan Arab Jamahiriya', 'LBY', 434, 218), (122, 'LI', 'LIECHTENSTEIN', 'Liechtenstein', 'LIE', 438, 423), (123, 'LT', 'LITHUANIA', 'Lithuania', 'LTU', 440, 370), (124, 'LU', 'LUXEMBOURG', 'Luxembourg', 'LUX', 442, 352), (125, 'MO', 'MACAO', 'Macao', 'MAC', 446, 853), (126, 'MK', 'MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF', 'Macedonia, the Former Yugoslav Republic of', 'MKD', 807, 389), (127, 'MG', 'MADAGASCAR', 'Madagascar', 'MDG', 450, 261), (128, 'MW', 'MALAWI', 'Malawi', 'MWI', 454, 265), (129, 'MY', 'MALAYSIA', 'Malaysia', 'MYS', 458, 60), (130, 'MV', 'MALDIVES', 'Maldives', 'MDV', 462, 960), (131, 'ML', 'MALI', 'Mali', 'MLI', 466, 223), (132, 'MT', 'MALTA', 'Malta', 'MLT', 470, 356), (133, 'MH', 'MARSHALL ISLANDS', 'Marshall Islands', 'MHL', 584, 692), (134, 'MQ', 'MARTINIQUE', 'Martinique', 'MTQ', 474, 596), (135, 'MR', 'MAURITANIA', 'Mauritania', 'MRT', 478, 222), (136, 'MU', 'MAURITIUS', 'Mauritius', 'MUS', 480, 230), (137, 'YT', 'MAYOTTE', 'Mayotte', NULL, NULL, 269), (138, 'MX', 'MEXICO', 'Mexico', 'MEX', 484, 52), (139, 'FM', 'MICRONESIA, FEDERATED STATES OF', 'Micronesia, Federated States of', 'FSM', 583, 691), (140, 'MD', 'MOLDOVA, REPUBLIC OF', 'Moldova, Republic of', 'MDA', 498, 373), (141, 'MC', 'MONACO', 'Monaco', 'MCO', 492, 377), (142, 'MN', 'MONGOLIA', 'Mongolia', 'MNG', 496, 976), (143, 'MS', 'MONTSERRAT', 'Montserrat', 'MSR', 500, 1664), (144, 'MA', 'MOROCCO', 'Morocco', 'MAR', 504, 212), (145, 'MZ', 'MOZAMBIQUE', 'Mozambique', 'MOZ', 508, 258), (146, 'MM', 'MYANMAR', 'Myanmar', 'MMR', 104, 95), (147, 'NA', 'NAMIBIA', 'Namibia', 'NAM', 516, 264), (148, 'NR', 'NAURU', 'Nauru', 'NRU', 520, 674), (149, 'NP', 'NEPAL', 'Nepal', 'NPL', 524, 977), (150, 'NL', 'NETHERLANDS', 'Netherlands', 'NLD', 528, 31), (151, 'AN', 'NETHERLANDS ANTILLES', 'Netherlands Antilles', 'ANT', 530, 599), (152, 'NC', 'NEW CALEDONIA', 'New Caledonia', 'NCL', 540, 687), (153, 'NZ', 'NEW ZEALAND', 'New Zealand', 'NZL', 554, 64), (154, 'NI', 'NICARAGUA', 'Nicaragua', 'NIC', 558, 505), (155, 'NE', 'NIGER', 'Niger', 'NER', 562, 227), (156, 'NG', 'NIGERIA', 'Nigeria', 'NGA', 566, 234), (157, 'NU', 'NIUE', 'Niue', 'NIU', 570, 683), (158, 'NF', 'NORFOLK ISLAND', 'Norfolk Island', 'NFK', 574, 672), (159, 'MP', 'NORTHERN MARIANA ISLANDS', 'Northern Mariana Islands', 'MNP', 580, 1670), (160, 'NO', 'NORWAY', 'Norway', 'NOR', 578, 47), (161, 'OM', 'OMAN', 'Oman', 'OMN', 512, 968), (162, 'PK', 'PAKISTAN', 'Pakistan', 'PAK', 586, 92), (163, 'PW', 'PALAU', 'Palau', 'PLW', 585, 680), (164, 'PS', 'PALESTINIAN TERRITORY, OCCUPIED', 'Palestinian Territory, Occupied', NULL, NULL, 970), (165, 'PA', 'PANAMA', 'Panama', 'PAN', 591, 507), (166, 'PG', 'PAPUA NEW GUINEA', 'Papua New Guinea', 'PNG', 598, 675), (167, 'PY', 'PARAGUAY', 'Paraguay', 'PRY', 600, 595), (168, 'PE', 'PERU', 'Peru', 'PER', 604, 51), (169, 'PH', 'PHILIPPINES', 'Philippines', 'PHL', 608, 63), (170, 'PN', 'PITCAIRN', 'Pitcairn', 'PCN', 612, 0), (171, 'PL', 'POLAND', 'Poland', 'POL', 616, 48), (172, 'PT', 'PORTUGAL', 'Portugal', 'PRT', 620, 351),(173, 'PR', 'PUERTO RICO', 'Puerto Rico', 'PRI', 630, 1787),(174, 'QA', 'QATAR', 'Qatar', 'QAT', 634, 974),(175, 'RE', 'REUNION', 'Reunion', 'REU', 638, 262),(176, 'RO', 'ROMANIA', 'Romania', 'ROM', 642, 40),(177, 'RU', 'RUSSIAN FEDERATION', 'Russian Federation', 'RUS', 643, 70),(178, 'RW', 'RWANDA', 'Rwanda', 'RWA', 646, 250),(179, 'SH', 'SAINT HELENA', 'Saint Helena', 'SHN', 654, 290),(180, 'KN', 'SAINT KITTS AND NEVIS', 'Saint Kitts and Nevis', 'KNA', 659, 1869),(181, 'LC', 'SAINT LUCIA', 'Saint Lucia', 'LCA', 662, 1758),(182, 'PM', 'SAINT PIERRE AND MIQUELON', 'Saint Pierre and Miquelon', 'SPM', 666, 508),(183, 'VC', 'SAINT VINCENT AND THE GRENADINES', 'Saint Vincent and the Grenadines', 'VCT', 670, 1784),(184, 'WS', 'SAMOA', 'Samoa', 'WSM', 882, 684),(185, 'SM', 'SAN MARINO', 'San Marino', 'SMR', 674, 378),(186, 'ST', 'SAO TOME AND PRINCIPE', 'Sao Tome and Principe', 'STP', 678, 239),(187, 'SA', 'SAUDI ARABIA', 'Saudi Arabia', 'SAU', 682, 966),(188, 'SN', 'SENEGAL', 'Senegal', 'SEN', 686, 221),(189, 'CS', 'SERBIA AND MONTENEGRO', 'Serbia and Montenegro', NULL, NULL, 381),(190, 'SC', 'SEYCHELLES', 'Seychelles', 'SYC', 690, 248),(191, 'SL', 'SIERRA LEONE', 'Sierra Leone', 'SLE', 694, 232),(192, 'SG', 'SINGAPORE', 'Singapore', 'SGP', 702, 65),(193, 'SK', 'SLOVAKIA', 'Slovakia', 'SVK', 703, 421),(194, 'SI', 'SLOVENIA', 'Slovenia', 'SVN', 705, 386),(195, 'SB', 'SOLOMON ISLANDS', 'Solomon Islands', 'SLB', 90, 677),(196, 'SO', 'SOMALIA', 'Somalia', 'SOM', 706, 252),(197, 'ZA', 'SOUTH AFRICA', 'South Africa', 'ZAF', 710, 27),(198, 'GS', 'SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS', 'South Georgia and the South Sandwich Islands', NULL, NULL, 0),(199, 'ES', 'SPAIN', 'Spain', 'ESP', 724, 34),(200, 'LK', 'SRI LANKA', 'Sri Lanka', 'LKA', 144, 94),(201, 'SD', 'SUDAN', 'Sudan', 'SDN', 736, 249),(202, 'SR', 'SURINAME', 'Suriname', 'SUR', 740, 597),(203, 'SJ', 'SVALBARD AND JAN MAYEN', 'Svalbard and Jan Mayen', 'SJM', 744, 47),(204, 'SZ', 'SWAZILAND', 'Swaziland', 'SWZ', 748, 268),(205, 'SE', 'SWEDEN', 'Sweden', 'SWE', 752, 46),(206, 'CH', 'SWITZERLAND', 'Switzerland', 'CHE', 756, 41),(207, 'SY', 'SYRIAN ARAB REPUBLIC', 'Syrian Arab Republic', 'SYR', 760, 963),(208, 'TW', 'TAIWAN, PROVINCE OF CHINA', 'Taiwan, Province of China', 'TWN', 158, 886),(209, 'TJ', 'TAJIKISTAN', 'Tajikistan', 'TJK', 762, 992),(210, 'TZ', 'TANZANIA, UNITED REPUBLIC OF', 'Tanzania, United Republic of', 'TZA', 834, 255),(211, 'TH', 'THAILAND', 'Thailand', 'THA', 764, 66),(212, 'TL', 'TIMOR-LESTE', 'Timor-Leste', NULL, NULL, 670),(213, 'TG', 'TOGO', 'Togo', 'TGO', 768, 228),(214, 'TK', 'TOKELAU', 'Tokelau', 'TKL', 772, 690),(215, 'TO', 'TONGA', 'Tonga', 'TON', 776, 676),(216, 'TT', 'TRINIDAD AND TOBAGO', 'Trinidad and Tobago', 'TTO', 780, 1868),(217, 'TN', 'TUNISIA', 'Tunisia', 'TUN', 788, 216),(218, 'TR', 'TURKEY', 'Turkey', 'TUR', 792, 90),(219, 'TM', 'TURKMENISTAN', 'Turkmenistan', 'TKM', 795, 7370),(220, 'TC', 'TURKS AND CAICOS ISLANDS', 'Turks and Caicos Islands', 'TCA', 796, 1649),(221, 'TV', 'TUVALU', 'Tuvalu', 'TUV', 798, 688),(222, 'UG', 'UGANDA', 'Uganda', 'UGA', 800, 256),(223, 'UA', 'UKRAINE', 'Ukraine', 'UKR', 804, 380),(224, 'AE', 'UNITED ARAB EMIRATES', 'United Arab Emirates', 'ARE', 784, 971),(225, 'GB', 'UNITED KINGDOM', 'United Kingdom', 'GBR', 826, 44),(226, 'US', 'UNITED STATES', 'United States', 'USA', 840, 1),(227, 'UM', 'UNITED STATES MINOR OUTLYING ISLANDS', 'United States Minor Outlying Islands', NULL, NULL, 1),(228, 'UY', 'URUGUAY', 'Uruguay', 'URY', 858, 598),(229, 'UZ', 'UZBEKISTAN', 'Uzbekistan', 'UZB', 860, 998),(230, 'VU', 'VANUATU', 'Vanuatu', 'VUT', 548, 678),(231, 'VE', 'VENEZUELA', 'Venezuela', 'VEN', 862, 58),(232, 'VN', 'VIET NAM', 'Viet Nam', 'VNM', 704, 84),(233, 'VG', 'VIRGIN ISLANDS, BRITISH', 'Virgin Islands, British', 'VGB', 92, 1284),(234, 'VI', 'VIRGIN ISLANDS, U.S.', 'Virgin Islands, U.s.', 'VIR', 850, 1340),(235, 'WF', 'WALLIS AND FUTUNA', 'Wallis and Futuna', 'WLF', 876, 681),(236, 'EH', 'WESTERN SAHARA', 'Western Sahara', 'ESH', 732, 212),(237, 'YE', 'YEMEN', 'Yemen', 'YEM', 887, 967),(238, 'ZM', 'ZAMBIA', 'Zambia', 'ZMB', 894, 260),(239, 'ZW', 'ZIMBABWE', 'Zimbabwe', 'ZWE', 716, 263)";


    public DatabaseHelper(Context context) {
        super(context, "shout_" + new Date().getDay(), null, DATABASEVERSION);
    }

   /* @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }*/

   /* @Override
    public void onConfigure(SQLiteDatabase sqLiteDatabase) {
        super.onConfigure(sqLiteDatabase);
        sqLiteDatabase.execSQL(strCreateTableNameCountry);
        sqLiteDatabase.execSQL(strDefaultCountryTableValues);
        sqLiteDatabase.execSQL(strCreateTableNameShout);
        sqLiteDatabase.execSQL(strCreateTableNameChat);
        System.out.println("SHOUT TABLE CREATED..." + strCreateTableNameChat);
    }*/

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(strCreateTableNameCountry);
            sqLiteDatabase.execSQL(strDefaultCountryTableValues);
            sqLiteDatabase.execSQL(strCreateTableNameShout);
            sqLiteDatabase.execSQL(strCreateTableNameChat);
            sqLiteDatabase.execSQL(strCreateTableFriends);
            sqLiteDatabase.execSQL(strCreateTableMessageBoard);
            sqLiteDatabase.execSQL(strCreateTableMessageUserList);
            sqLiteDatabase.execSQL(strCreateTableNameShoutResource);

            System.out.println("SHOUT TABLE CREATED..." + strTableNameFriends);
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + strCreateTableNameCountry);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + strCreateTableNameShout);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + strCreateTableNameChat);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + strDefaultCountryTableValues);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + strCreateTableFriends);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + strCreateTableMessageBoard);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + strCreateTableMessageUserList);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + strCreateTableNameShoutResource);
            System.out.println("SHOUT TABLE DROPED...");
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<CountryListModel> getAllCountries() {
        ArrayList<CountryListModel> arrCountryListModel = new ArrayList<CountryListModel>();
        String strQuery = "SELECT * FROM country";
        SQLiteDatabase objSQLiteDatabase = this.getReadableDatabase();
        try {
            Cursor objCursor = objSQLiteDatabase.rawQuery(strQuery, null);
            if (objCursor.moveToFirst()) {
                do {
                    CountryListModel objCountryListModel = new CountryListModel(Integer.toString(objCursor.getInt(0)),
                            objCursor.getString(1), objCursor.getString(2),
                            objCursor.getString(3), objCursor.getString(4),
                            Integer.toString(objCursor.getInt(5)),
                            Integer.toString(objCursor.getInt(6)));

                    arrCountryListModel.add(objCountryListModel);

                } while (objCursor.moveToNext());
            }
            objCursor.close();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        objSQLiteDatabase.close();
        return arrCountryListModel;
    }

    public ArrayList<ShoutDefaultListModel> saveShout(JSONArray jsonArray, String owner_flag) {
        int intRecordCount = 0;
        ArrayList<ShoutDefaultListModel> arrShoutDefaultListModel = new ArrayList<ShoutDefaultListModel>();
        SQLiteDatabase objSqLiteDatabase = this.getWritableDatabase();
        try {
            for (int index = 0; index < jsonArray.length(); index++) {
                ContentValues objContentValues = new ContentValues();
                try {
                    objContentValues.put(strShoutId, jsonArray.getJSONObject(index).getString(strShoutId));
                    objContentValues.put(strShoutUserId, jsonArray.getJSONObject(index).getString(strShoutUserId));
                    objContentValues.put(strShoutUserName, jsonArray.getJSONObject(index).getString(strShoutUserName));
                    objContentValues.put(strShoutUserPic, jsonArray.getJSONObject(index).getString(strShoutUserPic));
                    objContentValues.put(strShoutCommentCount, jsonArray.getJSONObject(index).getString(strShoutCommentCount));
                    objContentValues.put(strShoutLikeCount, jsonArray.getJSONObject(index).getString(strShoutLikeCount));
                    objContentValues.put(strShoutEngageCount, jsonArray.getJSONObject(index).getString(strShoutEngageCount));
                    objContentValues.put(strShoutType, jsonArray.getJSONObject(index).getString(strShoutType));
                    objContentValues.put(strShoutTitle, jsonArray.getJSONObject(index).getString(strShoutTitle));
                    objContentValues.put(strShoutDescription, jsonArray.getJSONObject(index).getString(strShoutDescription));
                    objContentValues.put(strShoutCreateDate, jsonArray.getJSONObject(index).getString(strShoutCreateDate));
                    objContentValues.put(strShoutLikeStatus, jsonArray.getJSONObject(index).getString(strShoutLikeStatus));
                    objContentValues.put(strShoutImage, jsonArray.getJSONObject(index).getString(strShoutImage));
                    objContentValues.put(strShoutHideStatus, jsonArray.getJSONObject(index).getString(strShoutHideStatus));
                    objContentValues.put(strShoutImages, jsonArray.getJSONObject(index).getString(strShoutImages));
                    objContentValues.put(strShoutIsSearchable, jsonArray.getJSONObject(index).getString(strShoutIsSearchable));
                    objContentValues.put(strShoutLatitude, jsonArray.getJSONObject(index).getString(strShoutLatitude));
                    objContentValues.put(strShoutLongitude, jsonArray.getJSONObject(index).getString(strShoutLongitude));
                    objContentValues.put(strShoutAddress, jsonArray.getJSONObject(index).getString(strShoutAddress));
                    objContentValues.put(strShoutCategoryName, jsonArray.getJSONObject(index).getString(strShoutCategoryName));
                    objContentValues.put(strShoutCategoryId, jsonArray.getJSONObject(index).getString(strShoutCategoryId));
                    objContentValues.put(strShoutIsHidden, jsonArray.getJSONObject(index).getString(strShoutIsHidden));
                    objContentValues.put(strShoutStartDate, jsonArray.getJSONObject(index).getString(strShoutStartDate));
                    objContentValues.put(strShoutEndDate, jsonArray.getJSONObject(index).getString(strShoutEndDate));
                    objContentValues.put(strShoutReShout, jsonArray.getJSONObject(index).getString(strShoutReShout));
                    objContentValues.put(strShoutContinueChat, jsonArray.getJSONObject(index).getString(strShoutContinueChat));
                    if(jsonArray.getJSONObject(index).getString(strShoutDistance).equals("NAN Km")){
                        objContentValues.put(strShoutDistance, "0 Km");
                    }else{
                        objContentValues.put(strShoutDistance, jsonArray.getJSONObject(index).getString(strShoutDistance));
                    }
                    objContentValues.put(strIsOwnerShoutFlag, owner_flag);
                    objContentValues.put(strShoutIsFriend, jsonArray.getJSONObject(index).getString(strShoutIsFriend));

                    ShoutDefaultListModel objShoutDefaultListModel = new ShoutDefaultListModel(
                            jsonArray.getJSONObject(index).getString(strShoutId).toString(),
                            jsonArray.getJSONObject(index).getString(strShoutUserId).toString(),
                            jsonArray.getJSONObject(index).getString(strShoutUserName).toString(),
                            jsonArray.getJSONObject(index).getString(strShoutUserPic).toString(),
                            jsonArray.getJSONObject(index).getString(strShoutCommentCount).toString(),
                            jsonArray.getJSONObject(index).getString(strShoutLikeCount).toString(),
                            jsonArray.getJSONObject(index).getString(strShoutEngageCount).toString(),
                            jsonArray.getJSONObject(index).getString(strShoutType).toString(),
                            jsonArray.getJSONObject(index).getString(strShoutTitle).toString(),
                            jsonArray.getJSONObject(index).getString(strShoutDescription).toString(),
                            Integer.parseInt(jsonArray.getJSONObject(index).getString(strShoutLikeStatus).toString()),
                            jsonArray.getJSONObject(index).getString(strShoutCreateDate).toString(),
                            jsonArray.getJSONObject(index).getString(strShoutImage).toString(),
                            Integer.parseInt(jsonArray.getJSONObject(index).getString(strShoutHideStatus).toString()),
                            ShoutDefaultActivity.VIEW_PAGER_DEFAULT_POSITION,
                            Constants.SHOUT_PASS_ENGAGE_BUTTON_DYNAMIC_HEIGHT,
                            Constants.DEFAULT_Y,
                            jsonArray.getJSONObject(index).getString(strShoutImages).toString(),
                            jsonArray.getJSONObject(index).getString(strShoutIsSearchable),
                            jsonArray.getJSONObject(index).getString(strShoutLatitude),
                            jsonArray.getJSONObject(index).getString(strShoutLongitude),
                            jsonArray.getJSONObject(index).getString(strShoutAddress),
                            jsonArray.getJSONObject(index).getString(strShoutCategoryName),
                            jsonArray.getJSONObject(index).getString(strShoutCategoryId),
                            jsonArray.getJSONObject(index).getString(strShoutIsHidden),
                            jsonArray.getJSONObject(index).getString(strShoutStartDate),
                            jsonArray.getJSONObject(index).getString(strShoutEndDate),
                            jsonArray.getJSONObject(index).getString(strShoutReShout),
                            jsonArray.getJSONObject(index).getString(strShoutContinueChat),
                            jsonArray.getJSONObject(index).getString(strShoutDistance),
                            jsonArray.getJSONObject(index).getString(strShoutIsFriend)
                    );
                    long result = objSqLiteDatabase.insert(strTableNameShout, null, objContentValues);
                    if (result > 0) {
                        System.out.println("SHOUT SAVED TO LOCAL");
                        intRecordCount = intRecordCount + 1;
                        arrShoutDefaultListModel.add(objShoutDefaultListModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        objSqLiteDatabase.close();
        return arrShoutDefaultListModel;
    }

    public ArrayList<ShoutDefaultListModel> getShoutDefaultListModelArray(String owner_flag) {
        SQLiteDatabase objSqLiteDatabase = this.getWritableDatabase();
        ArrayList<ShoutDefaultListModel> arrShoutDefaultListModel = new ArrayList<ShoutDefaultListModel>();
        try {
            String strQuery = "SELECT * FROM " + strTableNameShout + " WHERE " + this.strIsOwnerShoutFlag + "='" + owner_flag + "' AND " + this.strShoutIsFriend + "='Y' ORDER BY " + strShoutId + " DESC";
            System.out.println("GET SHOUT LIST FROM LOCAL QUERY : " + strQuery);
            Cursor objCursor = objSqLiteDatabase.rawQuery(strQuery, null);
            System.out.println("PRASANNA PRINTLN DATA COUNT : " + objCursor.getCount());
            if (objCursor != null) {
                if (objCursor.moveToFirst()) {
                    do {
                        if (("null").equals(objCursor.getString(objCursor.getColumnIndex(strShoutImage)))) {
                            ShoutDefaultListModel objShoutDefaultListModel = new ShoutDefaultListModel(
                                    String.valueOf(objCursor.getInt(objCursor.getColumnIndex(strShoutId))),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutUserId)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutUserName)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutUserPic)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutCommentCount)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutLikeCount)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutEngageCount)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutType)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutTitle)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutDescription)),
                                    Integer.parseInt(objCursor.getString(objCursor.getColumnIndex(strShoutLikeStatus))),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutCreateDate)),
                                    "",
                                    Integer.parseInt(objCursor.getString(objCursor.getColumnIndex(strShoutHideStatus))),
                                    ShoutDefaultActivity.VIEW_PAGER_DEFAULT_POSITION,
                                    Constants.SHOUT_PASS_ENGAGE_BUTTON_DYNAMIC_HEIGHT,
                                    Constants.DEFAULT_Y,
                                    objCursor.getString(objCursor.getColumnIndex(strShoutImages)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutIsSearchable)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutLatitude)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutLongitude)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutAddress)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutCategoryName)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutCategoryId)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutIsHidden)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutStartDate)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutEndDate)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutReShout)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutContinueChat)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutDistance)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutIsFriend)));
                            arrShoutDefaultListModel.add(objShoutDefaultListModel);
                        } else {
                            ShoutDefaultListModel objShoutDefaultListModel = new ShoutDefaultListModel(
                                    String.valueOf(objCursor.getInt(objCursor.getColumnIndex(strShoutId))),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutUserId)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutUserName)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutUserPic)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutCommentCount)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutLikeCount)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutEngageCount)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutType)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutTitle)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutDescription)),
                                    Integer.parseInt(objCursor.getString(objCursor.getColumnIndex(strShoutLikeStatus))),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutCreateDate)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutImage)),
                                    Integer.parseInt(objCursor.getString(objCursor.getColumnIndex(strShoutHideStatus))),
                                    ShoutDefaultActivity.VIEW_PAGER_DEFAULT_POSITION,
                                    Constants.SHOUT_PASS_ENGAGE_BUTTON_DYNAMIC_HEIGHT,
                                    Constants.DEFAULT_Y,
                                    objCursor.getString(objCursor.getColumnIndex(strShoutImages)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutIsSearchable)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutLatitude)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutLongitude)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutAddress)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutCategoryName)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutCategoryId)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutIsHidden)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutStartDate)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutEndDate)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutReShout)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutContinueChat)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutDistance)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutIsFriend)));
                            arrShoutDefaultListModel.add(objShoutDefaultListModel);
                        }
                    } while (objCursor.moveToNext());
                }
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String strQuery = "SELECT * FROM " + strTableNameShout + " WHERE " + this.strIsOwnerShoutFlag + "='" + owner_flag + "' AND " + this.strShoutIsFriend + "='N' ORDER BY " + strShoutId + " DESC";
            System.out.println("GET SHOUT LIST FROM LOCAL QUERY : " + strQuery);
            Cursor objCursor = objSqLiteDatabase.rawQuery(strQuery, null);
            System.out.println("PRASANNA PRINTLN DATA COUNT : " + objCursor.getCount());
            if (objCursor != null) {
                if (objCursor.moveToFirst()) {
                    do {
                        if (("null").equals(objCursor.getString(objCursor.getColumnIndex(strShoutImage)))) {
                            ShoutDefaultListModel objShoutDefaultListModel = new ShoutDefaultListModel(
                                    String.valueOf(objCursor.getInt(objCursor.getColumnIndex(strShoutId))),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutUserId)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutUserName)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutUserPic)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutCommentCount)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutLikeCount)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutEngageCount)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutType)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutTitle)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutDescription)),
                                    Integer.parseInt(objCursor.getString(objCursor.getColumnIndex(strShoutLikeStatus))),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutCreateDate)),
                                    "",
                                    Integer.parseInt(objCursor.getString(objCursor.getColumnIndex(strShoutHideStatus))),
                                    ShoutDefaultActivity.VIEW_PAGER_DEFAULT_POSITION,
                                    Constants.SHOUT_PASS_ENGAGE_BUTTON_DYNAMIC_HEIGHT,
                                    Constants.DEFAULT_Y,
                                    objCursor.getString(objCursor.getColumnIndex(strShoutImages)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutIsSearchable)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutLatitude)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutLongitude)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutAddress)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutCategoryName)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutCategoryId)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutIsHidden)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutStartDate)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutEndDate)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutReShout)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutContinueChat)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutDistance)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutIsFriend)));
                            arrShoutDefaultListModel.add(objShoutDefaultListModel);
                        } else {
                            ShoutDefaultListModel objShoutDefaultListModel = new ShoutDefaultListModel(
                                    String.valueOf(objCursor.getInt(objCursor.getColumnIndex(strShoutId))),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutUserId)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutUserName)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutUserPic)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutCommentCount)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutLikeCount)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutEngageCount)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutType)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutTitle)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutDescription)),
                                    Integer.parseInt(objCursor.getString(objCursor.getColumnIndex(strShoutLikeStatus))),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutCreateDate)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutImage)),
                                    Integer.parseInt(objCursor.getString(objCursor.getColumnIndex(strShoutHideStatus))),
                                    ShoutDefaultActivity.VIEW_PAGER_DEFAULT_POSITION,
                                    Constants.SHOUT_PASS_ENGAGE_BUTTON_DYNAMIC_HEIGHT,
                                    Constants.DEFAULT_Y,
                                    objCursor.getString(objCursor.getColumnIndex(strShoutImages)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutIsSearchable)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutLatitude)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutLongitude)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutAddress)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutCategoryName)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutCategoryId)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutIsHidden)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutStartDate)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutEndDate)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutReShout)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutContinueChat)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutDistance)),
                                    objCursor.getString(objCursor.getColumnIndex(strShoutIsFriend)));
                            arrShoutDefaultListModel.add(objShoutDefaultListModel);
                        }
                    } while (objCursor.moveToNext());
                }
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        objSqLiteDatabase.close();
        return arrShoutDefaultListModel;
    }

    public void deleteShoutEntries() {
        SQLiteDatabase objSQLiteDatabase = this.getWritableDatabase();
        objSQLiteDatabase.delete(strTableNameShout, null, null);
        objSQLiteDatabase.close();
    }

    public void deleteTable(String strTableName) {
        SQLiteDatabase objSQLiteDatabase = this.getWritableDatabase();
        long count = objSQLiteDatabase.delete(strTableName, null, null);
        if (count > 0) {
            Utils.d("MESSAGE_BOARD", "RECORD DELETED SUCCESSFULLY");
        } else {
            Utils.d("MESSAGE_BOARD", "RECORD DELETION FAILED");
        }
        objSQLiteDatabase.close();
    }


    public ShoutDefaultListModel getShoutDetails(String strShoutId) {
        ShoutDefaultListModel objShoutDefaultListModel = new ShoutDefaultListModel();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String strQuery = "SELECT * FROM " + strTableNameShout + " WHERE " + this.strShoutId + " = " + Integer.parseInt(strShoutId);

            System.out.println("Sql : " + strQuery);

            Cursor objCursor = db.rawQuery(strQuery, null);

            System.out.println("Sql : " + objCursor.getCount());

            if (objCursor != null) {
                if (objCursor.moveToFirst()) {
                    System.out.println("1 CURSOR OBJECT : " + objCursor.getColumnIndex(strShoutImage));
                    System.out.println("2 CURSOR OBJECT : " + objCursor.getString(objCursor.getColumnIndex(strShoutImage)));
                    if (objCursor.getString(objCursor.getColumnIndex(strShoutImage)).equals("null")) {
                        objShoutDefaultListModel = new ShoutDefaultListModel(
                                String.valueOf(objCursor.getInt(objCursor.getColumnIndex(this.strShoutId))),
                                objCursor.getString(objCursor.getColumnIndex(strShoutUserId)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutUserName)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutUserPic)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutCommentCount)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutLikeCount)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutEngageCount)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutType)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutTitle)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutDescription)),
                                Integer.parseInt(objCursor.getString(objCursor.getColumnIndex(strShoutLikeStatus))),
                                objCursor.getString(objCursor.getColumnIndex(strShoutCreateDate)),
                                "",
                                Integer.parseInt(objCursor.getString(objCursor.getColumnIndex(strShoutHideStatus))),
                                ShoutDefaultActivity.VIEW_PAGER_DEFAULT_POSITION,
                                Constants.SHOUT_PASS_ENGAGE_BUTTON_DYNAMIC_HEIGHT,
                                Constants.DEFAULT_Y,
                                objCursor.getString(objCursor.getColumnIndex(strShoutImages)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutIsSearchable)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutLatitude)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutLongitude)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutAddress)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutCategoryName)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutCategoryId)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutIsHidden)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutStartDate)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutEndDate)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutReShout)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutContinueChat)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutDistance)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutIsFriend)));
                    } else {
                        objShoutDefaultListModel = new ShoutDefaultListModel(
                                String.valueOf(objCursor.getInt(objCursor.getColumnIndex(this.strShoutId))),
                                objCursor.getString(objCursor.getColumnIndex(strShoutUserId)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutUserName)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutUserPic)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutCommentCount)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutLikeCount)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutEngageCount)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutType)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutTitle)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutDescription)),
                                Integer.parseInt(objCursor.getString(objCursor.getColumnIndex(strShoutLikeStatus))),
                                objCursor.getString(objCursor.getColumnIndex(strShoutCreateDate)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutImage)),
                                Integer.parseInt(objCursor.getString(objCursor.getColumnIndex(strShoutHideStatus))),
                                ShoutDefaultActivity.VIEW_PAGER_DEFAULT_POSITION,
                                Constants.SHOUT_PASS_ENGAGE_BUTTON_DYNAMIC_HEIGHT,
                                Constants.DEFAULT_Y,
                                objCursor.getString(objCursor.getColumnIndex(strShoutImages)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutIsSearchable)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutLatitude)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutLongitude)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutAddress)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutCategoryName)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutCategoryId)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutIsHidden)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutStartDate)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutEndDate)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutReShout)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutContinueChat)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutDistance)),
                                objCursor.getString(objCursor.getColumnIndex(strShoutIsFriend)));
                    }

                }
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return objShoutDefaultListModel;
    }

    public int getShoutId(String strPosition) {
        int intLastRecordId = 0;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor objCuror = db.rawQuery("SELECT * FROM " + strTableNameShout + " ORDER BY " + strShoutId + " DESC", null);
            if (objCuror != null) {
                if (strPosition.equals("FIRST")) {
                    if (objCuror.moveToFirst()) {
                        intLastRecordId = objCuror.getInt(objCuror.getColumnIndex(strShoutId));
                    }
                } else {
                    if (objCuror.moveToLast()) {
                        intLastRecordId = objCuror.getInt(objCuror.getColumnIndex(strShoutId));
                    }
                }
            }
            db.close();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intLastRecordId;
    }

    public boolean updateCommentCount(int count, String shoutId) {
        boolean result = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues args = new ContentValues();
            args.put(strShoutCommentCount, count);
            result = db.update(strTableNameShout, args, strShoutId + "=?", new String[]{shoutId}) > 0;
            db.close();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean updateLikeCount(int count, String shoutId) {
        boolean result = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues args = new ContentValues();
            args.put(strShoutLikeCount, count);
            args.put(strShoutLikeStatus, "1");
            result = db.update(strTableNameShout, args, strShoutId + "=?", new String[]{shoutId}) > 0;
            db.close();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void saveChatMessages(String shout_id, String user_id, String objJsonArray) {
        try {
            String strQueryIdExists = "SELECT * FROM " + strTableNameChat + " WHERE " + strChatShoutId + "='" + shout_id + "' AND " + strChatUserId + "='" + user_id + "'";
            System.out.println("RECORD EXISTS CHECK QUERY : " + strQueryIdExists);
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor objCursor = db.rawQuery(strQueryIdExists, null);
            int count = objCursor.getCount();
            db.close();

            if (count > 0) {
                System.out.println("QUERY RESULT : " + count);
                // UPDATE RECORD
                db = this.getWritableDatabase();

                try {
                    ContentValues args = new ContentValues();
                    args.put(strChatJSON, String.valueOf(objJsonArray));
                    boolean result = db.update(strTableNameChat, args, strChatShoutId + "=? AND " + strChatUserId + "=?", new String[]{shout_id, user_id}) > 0;
                    if (result) {
                        System.out.println("CHAT RECORD UPDATED SUCCESSFULLY");
                    } else {
                        System.out.println("CHAT RECORD UPDATION FAILED");
                    }
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                db.close();
            } else {
                //INSERT RECORD
                try {
                    db = this.getWritableDatabase();
                    ContentValues args = new ContentValues();
                    args.put(strChatShoutId, shout_id);
                    args.put(strChatUserId, user_id);
                    args.put(strChatJSON, String.valueOf(objJsonArray));
                    long result = db.insert(strTableNameChat, null, args);
                    if (result > 0) {
                        System.out.println("CHAT RECORD INSERTED SUCCESSFULLY");
                    } else {
                        System.out.println("CHAT RECORD INSERTION FAILED");
                    }
                    db.close();
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public ArrayList<ChatMessage> getLastMessages(String shout_id, String user_id) {
        ArrayList<ChatMessage> arrChatMessages = new ArrayList<ChatMessage>();

        String strResult = "";
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            String strQuery = "SELECT " + strChatJSON + " FROM " + strTableNameChat + " WHERE " + strChatShoutId + "='" + shout_id + "' AND " +
                    strChatUserId + "='" + user_id + "'";
            System.out.println("GET LAST 25 MESSAGES : " + strQuery);
            Cursor objCursor = db.rawQuery(strQuery, null);
            if (objCursor.getCount() > 0) {
                if (objCursor.moveToFirst()) {
                    JSONArray objJsonArray = new JSONArray(objCursor.getString(objCursor.getColumnIndex(strChatJSON)));
                    System.out.println("CHAT JSON ARRAY IN DB :" + objJsonArray);
                    for (int index = 0; index < objJsonArray.length(); index++) {
                        ChatMessage objChatMessage = new ChatMessage(
                                Boolean.parseBoolean(objJsonArray.getJSONObject(index).getString("side")),
                                objJsonArray.getJSONObject(index).getString("message"),
                                objJsonArray.getJSONObject(index).getString("profile_url"),
                                objJsonArray.getJSONObject(index).getString("timeformat"),
                                objJsonArray.getJSONObject(index).getString("message_type"),
                                objJsonArray.getJSONObject(index).getString("image_path"),
                                null,
                                objJsonArray.getJSONObject(index).getString("shout_id"),
                                objJsonArray.getJSONObject(index).getString("is_processed"),
                                objJsonArray.getJSONObject(index).getString("from_id"),
                                objJsonArray.getJSONObject(index).getString("to_id"),
                                objJsonArray.getJSONObject(index).getString("name"),
                                strShoutTitle,
                                objJsonArray.getJSONObject(index).getString("chat_id"),
                                objJsonArray.getJSONObject(index).getString("image_thumb_path")
                        );
                        arrChatMessages.add(objChatMessage);
                    }
                }
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return arrChatMessages;
    }

    //FRIENDS TABLE FUNCTIONS

    public void saveFriends(JSONArray jsonArray) {
        try {
            deleteFriends();
            SQLiteDatabase objSqLiteDatabase = this.getWritableDatabase();
            ContentValues contentValues;
            for (int index = 0; index < jsonArray.length(); index++) {
                if (jsonArray.getJSONObject(index).getString("is_friend").equals("Y")) {
                    if (jsonArray.getJSONObject(index).getString("phone").length() >= 10) {
                        contentValues = new ContentValues();
                        contentValues.put(friendsId, jsonArray.getJSONObject(index).getString("id"));
                        contentValues.put(friendsName, jsonArray.getJSONObject(index).getString("name"));
                        contentValues.put(friendsPhone, jsonArray.getJSONObject(index).getString("phone"));
                        contentValues.put(isFacebookFriend, jsonArray.getJSONObject(index).getString("is_facebook_friend"));
                        contentValues.put(isPhoneFriend, jsonArray.getJSONObject(index).getString("is_phone_friend"));
                        contentValues.put(profileImage, jsonArray.getJSONObject(index).getString("profile_image"));
                        contentValues.put(friendIsFriend, jsonArray.getJSONObject(index).getString("is_friend"));
                        contentValues.put(friendButtonType, jsonArray.getJSONObject(index).getString("button_type"));
                        contentValues.put(friendIsShoutFriend, jsonArray.getJSONObject(index).getString("is_shout_friend"));
                        //// TODO: 9/2/2016 add Friends to the database for performance loading
                        long result = objSqLiteDatabase.insert(strTableNameFriends, null, contentValues);
                        if (result != -1) {
                            System.out.println("FRIENDS DATA INSERTED : " + result);
                        }
                    }
                } else if (jsonArray.getJSONObject(index).getString("is_friend").equals("N")) {
                    if (jsonArray.getJSONObject(index).getString("phone").length() >= 10) {
                        contentValues = new ContentValues();
                        contentValues.put(friendsId, jsonArray.getJSONObject(index).getString("id"));
                        contentValues.put(friendsName, jsonArray.getJSONObject(index).getString("name"));
                        contentValues.put(friendsPhone, jsonArray.getJSONObject(index).getString("phone"));
                        contentValues.put(isFacebookFriend, jsonArray.getJSONObject(index).getString("is_facebook_friend"));
                        contentValues.put(isPhoneFriend, jsonArray.getJSONObject(index).getString("is_phone_friend"));
                        contentValues.put(profileImage, "");
                        contentValues.put(friendIsFriend, jsonArray.getJSONObject(index).getString("is_friend"));
                        contentValues.put(friendButtonType, jsonArray.getJSONObject(index).getString("button_type"));
                        contentValues.put(friendIsShoutFriend, jsonArray.getJSONObject(index).getString("is_shout_friend"));
                        //// TODO: 9/2/2016 add Friends to the database for performance loading
                        long result = objSqLiteDatabase.insert(strTableNameFriends, null, contentValues);
                        if (result != -1) {
                            System.out.println("FRIENDS DATA INSERTED : " + result);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ContactModel> getAllFriendsList() {
        ArrayList<ContactModel> arrayListContactModel = new ArrayList<ContactModel>();
        String contactName = "", contactNumber = "", contactId = "";
        boolean isCheck = false;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String strQuery = "SELECT * FROM " + strTableNameFriends + " WHERE " + this.friendIsFriend + "='Y'  ORDER BY " + friendsName;
        System.out.println("GET FRIENDS LIST FROM LOCAL QUERY : " + strQuery);
        Cursor objCursor = sqLiteDatabase.rawQuery(strQuery, null);
        if (objCursor != null) {
            if (objCursor.moveToFirst()) {
                do {
                    ContactModel contactModel = new ContactModel(
                            objCursor.getInt(objCursor.getColumnIndex(strFriendsUniqRowId)),
                            objCursor.getString(objCursor.getColumnIndex(friendsName)),
                            objCursor.getString(objCursor.getColumnIndex(friendsPhone)),
                            objCursor.getString(objCursor.getColumnIndex(friendsId)),
                            objCursor.getString(objCursor.getColumnIndex(isFacebookFriend)),
                            objCursor.getString(objCursor.getColumnIndex(isPhoneFriend)),
                            isCheck, objCursor.getString(objCursor.getColumnIndex(profileImage)), 0,
                            objCursor.getString(objCursor.getColumnIndex(friendButtonType)),
                            objCursor.getString(objCursor.getColumnIndex(friendIsShoutFriend)));

                    arrayListContactModel.add(contactModel);
                    System.out.println("GET FRIENDS LIST FROM LOCAL QUERY : " + contactId + ",\n Contact Name: " + contactName + ", \n Contact Number: " + contactNumber);
                } while (objCursor.moveToNext());
            }
        }
        sqLiteDatabase.close();
        return arrayListContactModel;
    }

    public ArrayList<ContactModel> getAllNonFriendsList() {
        ArrayList<ContactModel> arrayListContactModel = new ArrayList<ContactModel>();
        String contactName = "", contactNumber = "", contactId = "";
        boolean isCheck = false;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//        String strQuery = "SELECT * FROM " + strTableNameFriends + " ORDER BY " + friendsName;
        String strQuery = "SELECT * FROM " + strTableNameFriends + " WHERE " + this.friendIsFriend + "='N' ORDER BY " + friendsName;
        System.out.println("GET FRIENDS LIST FROM LOCAL QUERY : " + strQuery);
        Cursor objCursor = sqLiteDatabase.rawQuery(strQuery, null);
        if (objCursor != null) {
            if (objCursor.moveToFirst()) {
                do {
                    ContactModel contactModel = new ContactModel(
                            objCursor.getInt(objCursor.getColumnIndex(strFriendsUniqRowId)),
                            objCursor.getString(objCursor.getColumnIndex(friendsName)),
                            objCursor.getString(objCursor.getColumnIndex(friendsPhone)),
                            objCursor.getString(objCursor.getColumnIndex(friendsId)),
                            objCursor.getString(objCursor.getColumnIndex(isFacebookFriend)),
                            objCursor.getString(objCursor.getColumnIndex(isPhoneFriend)),
                            isCheck, "", 1,
                            objCursor.getString(objCursor.getColumnIndex(friendButtonType)),
                            objCursor.getString(objCursor.getColumnIndex(friendIsShoutFriend)));
                    arrayListContactModel.add(contactModel);
                    System.out.println("GET FRIENDS LIST FROM LOCAL QUERY : " + contactId + ",\n Contact Name: " + contactName + ", \n Contact Number: " + contactNumber);
                } while (objCursor.moveToNext());
            }
        }
        return arrayListContactModel;
    }


    public void deleteFriends() {
        try {
            SQLiteDatabase objSqLiteDatabase = this.getWritableDatabase();
            int result = objSqLiteDatabase.delete(strTableNameFriends, "1", null);
            System.out.println("RECORD DELETED FROM FRIENDS TABLE" + result);
            objSqLiteDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void insertMessageBoardItems(String strResult) {
        SQLiteDatabase objSQLiteDatabase = this.getWritableDatabase();
        try {
            ContentValues objContentValues = new ContentValues();
            objContentValues.put(strMessageBoardJson, strResult);
            long count = objSQLiteDatabase.insert(strTableNameMessageBoard, null, objContentValues);
            if (count > 0) {
                Utils.d("MESSAGE_BOARD", "MESSAGE BOARD DATA SAVED.");
            } else {
                Utils.d("MESSAGE_BOARD", "MESSAGE BOARD DATA FAILED TO SAVE.");
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        objSQLiteDatabase.close();
    }


    public String getMessageBoardListItems() {
        String strResult = "";
        SQLiteDatabase objSQLiteDatabase = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + strTableNameMessageBoard;
        try {
            Cursor objCursor = objSQLiteDatabase.rawQuery(strQuery, null);
            if (objCursor.moveToFirst()) {
                strResult = objCursor.getString(objCursor.getColumnIndex(strMessageBoardJson));
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        objSQLiteDatabase.close();
        return strResult;
    }

    public void insertMessageUserListData(String strShoutId, String strResult) {
        SQLiteDatabase objSQLiteDatabase = this.getWritableDatabase();
        try {
            String strQuery = "SELECT * FROM " + strTableNameMessageUserList + " WHERE " + strMessageUserListShoutId + "='" + strShoutId + "'";

            Cursor objCursor = objSQLiteDatabase.rawQuery(strQuery, null);
            if (objCursor.getCount() > 0) {
                // UPDATE
                ContentValues objContentValues = new ContentValues();
                objContentValues.put(strMessageUserListShoutId, strShoutId);
                objContentValues.put(strMessageUserListJson, strResult);
                boolean result = objSQLiteDatabase.update(strTableNameMessageUserList, objContentValues, strChatShoutId + "=? AND " + strMessageUserListShoutId + "=?", new String[]{strShoutId}) > 0;
                if (result) {
                    Utils.d("MESSAGE_USER_LIST", "MESSAGE USER LIST DATA UPDATED.");
                } else {
                    Utils.d("MESSAGE_USER_LIST", "MESSAGE USER LIST DATA FAILED TO UPDATE.");
                }
            } else {
                // INSERT
                ContentValues objContentValues = new ContentValues();
                objContentValues.put(strMessageUserListShoutId, strShoutId);
                objContentValues.put(strMessageUserListJson, strResult);

                long count = objSQLiteDatabase.insert(strTableNameMessageUserList, null, objContentValues);
                if (count > 0) {
                    Utils.d("MESSAGE_USER_LIST", "MESSAGE USER LIST DATA SAVED.");
                } else {
                    Utils.d("MESSAGE_USER_LIST", "MESSAGE USER LIST DATA FAILED TO SAVE.");
                }
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        objSQLiteDatabase.close();
    }

    public String getMessageUserListItems(String strTempShoutId) {
        String strResult = "";
        SQLiteDatabase objSQLiteDatabase = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + strTableNameMessageUserList + " WHERE " + strMessageUserListShoutId + "='" + strTempShoutId + "'";
        try {
            Cursor objCursor = objSQLiteDatabase.rawQuery(strQuery, null);
            if (objCursor.moveToFirst()) {
                strResult = objCursor.getString(objCursor.getColumnIndex(strMessageUserListJson));
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        objSQLiteDatabase.close();
        return strResult;
    }

    public boolean getRecordCountOfTable(String strTable) {
        boolean result = false;
        try {
            SQLiteDatabase objSqLiteDatabase = this.getReadableDatabase();
            Cursor objCursor = objSqLiteDatabase.rawQuery("SELECT * FROM " + strTable, null);
            if (objCursor.getCount() > 0) {
                result = true;
            } else {
                result = false;
            }
            objSqLiteDatabase.close();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ShoutDefaultListModel updateShout(ShoutDefaultListModel objShoutDefaultListModel, String shoutId) {
        ShoutDefaultListModel objTempModelObject = null;
        objTempModelObject = objShoutDefaultListModel;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues args = new ContentValues();
            args.put(this.strShoutId, objShoutDefaultListModel.getSHOUT_ID());
            args.put(this.strShoutUserId, objShoutDefaultListModel.getUSER_ID());
            args.put(this.strShoutUserName, objShoutDefaultListModel.getUSER_NAME());
            args.put(this.strShoutUserPic, objShoutDefaultListModel.getPROFILE_IMAGE_URL());
            args.put(this.strShoutCommentCount, objShoutDefaultListModel.getCOMMENT_COUNT());
            args.put(this.strShoutLikeCount, objShoutDefaultListModel.getLIKE_COUNT());
            args.put(this.strShoutEngageCount, objShoutDefaultListModel.getENGAGE_COUNT());
            args.put(this.strShoutType, objShoutDefaultListModel.getSHOUT_TYPE());
            args.put(this.strShoutTitle, objShoutDefaultListModel.getTITLE());
            args.put(this.strShoutDescription, objShoutDefaultListModel.getDESCRIPTION());
            args.put(this.strShoutCreateDate, objShoutDefaultListModel.getDATE_TIME());
            args.put(this.strShoutLikeStatus, objShoutDefaultListModel.getIS_SHOUT_LIKED());
            args.put(this.strShoutImage, objShoutDefaultListModel.getSHOUT_IMAGE());
            args.put(this.strShoutHideStatus, objShoutDefaultListModel.getIS_SHOUT_HIDDEN());
            args.put(this.strShoutImages, objShoutDefaultListModel.getStrShoutImages());
            args.put(this.strShoutIsSearchable, objShoutDefaultListModel.getIS_SEARCHABLE());
            args.put(this.strShoutLatitude, objShoutDefaultListModel.getSHOUT_LATITUDE());
            args.put(this.strShoutLongitude, objShoutDefaultListModel.getSHOUT_LONGITUDE());
            args.put(this.strShoutAddress, objShoutDefaultListModel.getSHOUT_ADDRESS());
            args.put(this.strShoutCategoryName, objShoutDefaultListModel.getSHOUT_CATEGORY_NAME());
            args.put(this.strShoutCategoryId, objShoutDefaultListModel.getSHOUT_CATEGORY_ID());
            args.put(this.strShoutIsHidden, objShoutDefaultListModel.getIS_HIDE());
            args.put(this.strShoutStartDate, objShoutDefaultListModel.getSTART_DATE());
            args.put(this.strShoutEndDate, objShoutDefaultListModel.getEND_DATE());
            args.put(this.strShoutReShout, objShoutDefaultListModel.getRE_SHOUT());
            args.put(this.strShoutContinueChat, objShoutDefaultListModel.getCONTINUE_CHAT());
            args.put(this.strShoutDistance, objShoutDefaultListModel.getDISTANCE());
            boolean result = db.update(strTableNameShout, args, this.strShoutId + "=?", new String[]{shoutId}) > 0;
            if (result) {
                System.out.println("SHOUT MODEL UPDATED INTO LOCAL.");
            } else {
                System.out.println("SHOUT MODEL NOT UPDATED INTO LOCAL.");
            }
            db.close();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objTempModelObject;
    }

    public String getAllShoutId() {
        JSONArray objJsonArray = new JSONArray();
        SQLiteDatabase objSQLiteDatabase = this.getReadableDatabase();
        String strQuery = "SELECT " + this.strShoutId + " FROM " + strTableNameShout;
        Cursor objCursor = objSQLiteDatabase.rawQuery(strQuery, null);
        if (objCursor != null) {
            objCursor.moveToFirst();
            do {
                try {
                    objJsonArray.put(new JSONObject().put("shout_id", objCursor.getInt(objCursor.getColumnIndex(this.strShoutId))));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (objCursor.moveToNext());
        }
        objSQLiteDatabase.close();
        return objJsonArray.toString();
    }

    public void deleteMyShouts() {
        try {
            SQLiteDatabase objSqLiteDatabase = getWritableDatabase();
            String strQuery = "DELETE FROM " + strTableNameShout + " WHERE " + strIsOwnerShoutFlag + "=1";
            Cursor objCursor = objSqLiteDatabase.rawQuery(strQuery, null);
            if (objCursor.getCount() > 0) {
                System.out.println("MY SHOUT LOCAL DATA DELETED");
            }
            objSqLiteDatabase.close();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int getNearByFriendsShoutCount() {
        SQLiteDatabase objSqLiteDatabase = this.getReadableDatabase();
        String strQuery = "SELECT * FROM " + strTableNameShout + " WHERE " + this.strShoutIsFriend + "='N' ORDER BY " + this.strShoutId + " DESC";
        System.out.println("NEAR BY COUNT QUERY : " + strQuery);
        Cursor objCursor = objSqLiteDatabase.rawQuery(strQuery, null);
        if (objCursor.getCount() > 0) {
            objCursor.moveToLast();
            return objCursor.getInt(objCursor.getColumnIndex(this.strShoutId));
        }
        objSqLiteDatabase.close();
        return 0;
    }

    public void deleteUnFriendShouts() {
        try {
            SQLiteDatabase objSqLiteDatabase = getWritableDatabase();
            String strQuery = "DELETE FROM " + strTableNameShout + " WHERE " + this.strShoutIsFriend + "='N'";
            objSqLiteDatabase.rawQuery(strQuery, null);
            objSqLiteDatabase.close();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateProfilePic(String url, String id) {
        try {
            SQLiteDatabase objSqLiteDatabase = this.getWritableDatabase();
            String strQuery = "UPDATE " + this.strTableNameShout + " SET " + this.strShoutUserPic + "= '" + url + "' WHERE " + this.strShoutUserId + "='" + id + "'";
            System.out.println("PROFILE PIC UPDATE QUERY : " + strQuery);

            Cursor objCursor = objSqLiteDatabase.rawQuery(strQuery, null);
            if (objCursor.getCount() > 0) {
                System.out.println("USER PROFILE UPDATED SUCCESSFULLY ");
            } else {
                System.out.println("USER PROFILE UPDATION FAILED");
            }
            objSqLiteDatabase.close();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteShoutById(String shoutId) {
        try {
            SQLiteDatabase objSqLiteDatabase = getWritableDatabase();
            String strQuery = "DELETE FROM " + strTableNameShout + " WHERE " + this.strShoutId + "='" + shoutId + "'";
            Cursor objCursor = objSqLiteDatabase.rawQuery(strQuery, null);
            if (objCursor.getCount() > 0) {
                System.out.println("MY SHOUT LOCAL DATA DELETED");
            } else {
                System.out.println("MY SHOUT LOCAL DATA DELETED");
            }
            objSqLiteDatabase.close();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UploadResourceModel getShoutResource() {

        UploadResourceModel objUploadResourceModel=null;

        try {
            SQLiteDatabase objSQLiteDatabase = this.getReadableDatabase();
            String strQuery = "SELECT * FROM " + strTableNameShoutResources;
            Utils.d("DATABASE:", "DATA" + strQuery);
            Cursor objCursor = objSQLiteDatabase.rawQuery(strQuery, null);
            if (objCursor.moveToFirst()) {
                objUploadResourceModel = new UploadResourceModel(
                        objCursor.getInt(objCursor.getColumnIndex(strShoutResourceId)),
                        objCursor.getString(objCursor.getColumnIndex(strShoutResourceShoutId)),
                        objCursor.getString(objCursor.getColumnIndex(strShoutResourceThumbnailPath)),
                        objCursor.getString(objCursor.getColumnIndex(strShoutResourceVideoPath)));
                Utils.d("DATABASE" ,"RESOURCE SHOUT ID " + objUploadResourceModel.getShoutId());
                return objUploadResourceModel;
            }
            objSQLiteDatabase.close();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objUploadResourceModel;
    }


    public long addToBackgroundTask(String shout_id, String resource_thumbnail_path, String resource_video_path) {
        long result = 0;
        try {
            SQLiteDatabase objSQLiteDatabase = this.getWritableDatabase();

            ContentValues objContentValues = new ContentValues();
            objContentValues.put(strShoutResourceShoutId, shout_id);
            objContentValues.put(strShoutResourceThumbnailPath, resource_thumbnail_path);
            objContentValues.put(strShoutResourceVideoPath, resource_video_path);
            result = objSQLiteDatabase.insert(strTableNameShoutResources, null, objContentValues);
            if (result > 0) {
                Utils.d("DATABASE", "RESOURCE ADDED TO UPLOAD IN BACKGROUND");
            }
            objSQLiteDatabase.close();
            return result;
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean deleteUploadedResource(String Id) {
        try {
            boolean result;
            SQLiteDatabase objSqLiteDatabase = getWritableDatabase();
           /* String strQuery = "DELETE FROM " + strTableNameShoutResources + " WHERE " + this.strShoutResourceId + "='" + Id + "'";
            Utils.d("DATABASE","SHOUT RESUORCE DELETE QUERY: " + strQuery);
            objSqLiteDatabase.rawQuery(strQuery, null);*/
            result = objSqLiteDatabase.delete(strTableNameShoutResources, this.strShoutResourceId + "=" + Id, null) > 0;
            objSqLiteDatabase.close();
            return result;
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isResourceExists() {
        try {
            SQLiteDatabase objSqLiteDatabase = getReadableDatabase();
            String strQuery = "SELECT * FROM " + strTableNameShoutResources;
            Cursor objCursor = objSqLiteDatabase.rawQuery(strQuery, null);
            if (objCursor.getCount() > 0) {
                objSqLiteDatabase.close();
                return true;
            } else {
                objSqLiteDatabase.close();
                return false;
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public ContactModel getFriendModel(int rowId) {
        ContactModel objContactModel = new ContactModel();
        boolean isCheck = false;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String strQuery = "SELECT * FROM " + strTableNameFriends + " WHERE " + this.friendIsFriend + "='Y' AND " + this.strFriendsUniqRowId + "=" + rowId ;
        System.out.println("GET FRIEND DATA FROM LOCAL QUERY : " + strQuery);
        Cursor objCursor = sqLiteDatabase.rawQuery(strQuery, null);
        if (objCursor != null) {
            if (objCursor.moveToFirst()) {
                do {
                            objContactModel = new ContactModel(
                            objCursor.getInt(objCursor.getColumnIndex(strFriendsUniqRowId)),
                            objCursor.getString(objCursor.getColumnIndex(friendsName)),
                            objCursor.getString(objCursor.getColumnIndex(friendsPhone)),
                            objCursor.getString(objCursor.getColumnIndex(friendsId)),
                            objCursor.getString(objCursor.getColumnIndex(isFacebookFriend)),
                            objCursor.getString(objCursor.getColumnIndex(isPhoneFriend)),
                            isCheck, objCursor.getString(objCursor.getColumnIndex(profileImage)), 0,
                            objCursor.getString(objCursor.getColumnIndex(friendButtonType)),
                                    objCursor.getString(objCursor.getColumnIndex(friendIsShoutFriend)));
                } while (objCursor.moveToNext());
            }
        }
        sqLiteDatabase.close();
        return objContactModel;
    }

    public boolean updateFriend(ContactModel objContactModel) {
        boolean result = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(friendsId, objContactModel.getId());
            contentValues.put(friendsName, objContactModel.getContactName());
            contentValues.put(friendsPhone, objContactModel.getContactNumber());
            contentValues.put(isFacebookFriend, objContactModel.getIsFacebookFriend());
            contentValues.put(isPhoneFriend, objContactModel.getIsPhoneFriend());
            contentValues.put(profileImage, objContactModel.getProfileImage());
            contentValues.put(friendIsFriend, "Y");
            contentValues.put(friendButtonType, objContactModel.getButtonType());
            result = db.update(strTableNameFriends, contentValues, strFriendsUniqRowId + "=?", new String[]{String.valueOf(objContactModel.getTableId())}) > 0;
            db.close();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
