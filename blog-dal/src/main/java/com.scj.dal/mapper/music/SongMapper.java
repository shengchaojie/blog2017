package com.scj.dal.mapper.music;

import com.scj.dal.MyMapper;
import com.scj.dal.ro.music.SongRO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
public interface SongMapper extends MyMapper<SongRO>{

    List<SongRO> findByParam(@Param("songName") String songName,@Param("albumName") String albumName,@Param("singerName") String singerName);

    Long countByParam (@Param("songName") String songName,@Param("albumName") String albumName,@Param("singerName") String singerName);

}
