package com.scj.service.user;


import com.scj.service.vo.BarrageVO;

import java.util.List;

/**
 * Created by shengcj on 2016/7/19.
 */
public interface BarrageService {
    /**
     * 保存一个弹幕
     * @param barrageVO
     * @return
     */
    boolean saveBarrage(BarrageVO barrageVO);

    /**
     * 获取所有弹幕
     * @return
     */
    List<BarrageVO> getBarrages();
}
