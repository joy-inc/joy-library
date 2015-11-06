package com.easy.bean;


import com.easy.utils.TextUtil;

/**
 * 200个热门城市
 */
public class HotCityItem {

    private String id = TextUtil.TEXT_EMPTY;
    private String cnname = TextUtil.TEXT_EMPTY;
    private String enname = TextUtil.TEXT_EMPTY;
    private String photo = TextUtil.TEXT_EMPTY;
    private String pinyin = TextUtil.TEXT_EMPTY;
    private boolean is_hot;
    private String lat = TextUtil.ZERO;
    private String lng = TextUtil.ZERO;

    public String getLat() {

        return lat;
    }

    public String getLng() {

        return lng;
    }

    public void setLat(String lat) {

        this.lat = TextUtil.isEmpty(lat) ? TextUtil.ZERO : lat;
    }

    public void setLng(String lng) {

        this.lng = TextUtil.isEmpty(lng) ? TextUtil.ZERO : lng;
    }

    public boolean isIs_hot() {

        return is_hot;
    }

    public void setIs_hot(boolean is_hot) {

        this.is_hot = is_hot;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = TextUtil.filterNull(id);
    }

    public String getCnname() {

        return cnname;
    }

    public void setCnname(String cnname) {

        this.cnname = TextUtil.filterNull(cnname);
    }

    public String getEnname() {

        return enname;
    }

    public void setEnname(String enname) {

        this.enname = TextUtil.filterNull(enname);
    }

    public String getPhoto() {

        return photo;
    }

    public void setPhoto(String photo) {

        this.photo = TextUtil.filterNull(photo);
    }

    public String getPinyin() {

        return pinyin;
    }

    public void setPinyin(String pinyin) {

        this.pinyin = TextUtil.filterNull(pinyin).toLowerCase();
    }

    @Override
    public String toString() {
        return "HotCityItem{" +
                "id='" + id + '\'' +
                ", cnname='" + cnname + '\'' +
                ", enname='" + enname + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", is_hot=" + is_hot +
                ", photo='" + photo + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                '}';
    }
}
