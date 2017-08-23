package com.scj.web.query;

/**
 * Created by shengchaojie on 2017/5/31.
 */
public class SongQuery extends BaseQuery {

    private String songName;

    private String albumName;

    private String singerName;

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }
}
