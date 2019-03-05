package com.gamecodeschool.whereitssnap;

public class ContactContract {

 private ContactContract(){

 }


    public static  class ContractEntry{

        public static final String TABLE_PHOTO_NAME = "wis_table_photos_2";
        public static  final String TABLE_NAME = "wis_table_tag_2";
        public static final String TABLE_PHOTO_ID = "_id";
        public  static final String TABLE_PHOTO_IMAGETITLE="image_title";
        public static final String TABLE_PHOTO_TAG1= "tag1";
        public static final String TABLE_PHOTO_TAG2= "tag2";
        public static final String TABLE_PHOTO_TAG3="tag3";
        public static final String TABLE_TAGS_TAG= "tag";
        public static  final String TABLE_PHOTO_URI = "image_uri";

        //gps
        public static final String TABLE_ROW_LAT = "gps_location_LAT";
        public static final String TABLE_ROW_LONG = "gps_location_LONG";


    }

}
