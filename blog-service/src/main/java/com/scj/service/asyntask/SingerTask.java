package com.scj.service.asyntask;

import com.scj.dal.ro.music.SingerRO;
import com.scj.service.music.SingerService;
import com.scj.service.music.impl.MusicServiceImpl;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shengchaojie on 2017/5/23.
 */
@Component
@Scope("prototype")
public class SingerTask extends BaseTask{

    private static final Logger logger = LoggerFactory.getLogger(SingerTask.class);

    @Resource
    private SingerService singerService;

    List<MusicServiceImpl.CatalogItem> catalogItems;

    public SingerTask() {
    }

    public SingerTask(String name, List<MusicServiceImpl.CatalogItem> catalogItems) {
        super(name);
        this.catalogItems = catalogItems;
    }

    @Override
    @Async("myTaskAsyncPool")
    public void doTask() {
        for(MusicServiceImpl.CatalogItem catalogItem:catalogItems){
            try{
                Document itemDoc = org.jsoup.Jsoup.connect(catalogItem.getUrl()).get();
                Elements singerItems =itemDoc.select("ul#m-artist-box li a.nm.nm-icn");
                List<SingerRO> singerROS =new ArrayList<>();
                for (Element singerItem:singerItems){
                    String url =singerItem.attr("href").trim();
                    String singerId =null;
                    if(url.contains("id=")){
                        singerId=url.substring(url.indexOf("id=")+3);
                    }else {
                        continue;
                    }
                    String singerName =singerItem.html();
                    //插入前判断是否存在
                    if(singerService.findById(Long.parseLong(singerId))!=null){
                        logger.info("歌手id:{}已经存在于数据库");
                        //这边的数据很固定 不做更新处理
                        continue;
                    }

                    //插入数据到数据库
                    SingerRO singerRO =new SingerRO();
                    singerRO.setId(Long.parseLong(singerId));
                    singerRO.setSingerName(singerName);
                    singerRO.setSingerUrl(BASE_URL+url);
                    singerRO.setFirstLetter(catalogItem.getFirstLetter());
                    singerRO.setCrawlTime(new Date());
                    singerROS.add(singerRO);
                }
                if(!CollectionUtils.isEmpty(singerROS)){
                    singerService.batchAdd(singerROS);
                }
            }catch (IOException ex){

            }
        }
    }

    public List<MusicServiceImpl.CatalogItem> getCatalogItems() {
        return catalogItems;
    }

    public void setCatalogItems(List<MusicServiceImpl.CatalogItem> catalogItems) {
        this.catalogItems = catalogItems;
    }

    public SingerService getSingerService() {
        return singerService;
    }

    public void setSingerService(SingerService singerService) {
        this.singerService = singerService;
    }
}
