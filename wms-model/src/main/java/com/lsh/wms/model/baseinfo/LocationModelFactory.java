package com.lsh.wms.model.baseinfo;

/**
 * location的工厂方法,通过type的不同,生成不同的LocationModel
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/24 下午5:29
 */
public class LocationModelFactory {
    private static IBaseinfoLocaltionModel iBaseinfoLocaltionModel;
    //做class的类型
    private static String modelName;

    //进行实例
    public IBaseinfoLocaltionModel creatLocationModelByType(Integer type){
        //生成货位
        if (type==11||type==12||type==13||type==14||type==15||type==16||type==17){
            iBaseinfoLocaltionModel = new BaseinfoLocationBin();
        }
        //码头
        if(type==10){
            iBaseinfoLocaltionModel = new BaseinfoLocationDock();
        }
        //通道
        if(18==type){
            iBaseinfoLocaltionModel = new BaseinfoLocationPassage();
        }
        //集货、暂存、退货、残次、地堆
        if (5==type||6==type||7==type||8==type||9==type){
            iBaseinfoLocaltionModel = new BaseinfoLocationRegion();
        }
        //阁楼、货架
        if (type==4){
            iBaseinfoLocaltionModel = new BaseinfoLocationShelf();
        }
        //仓库
        if (type==1){
            iBaseinfoLocaltionModel = new BaseinfoLocationWarehouse();

        }

            return iBaseinfoLocaltionModel;
    }

    //获取实现类的class类型
    public String getLocationClassByType(Integer type){

        //生成货位
        if (type==11||type==12||type==13||type==14||type==15||type==16||type==17){
            modelName = "BaseinfoLocationBin";
        }
        //码头
        if(type==10){
            modelName = "BaseinfoLocationDock";
        }
        //通道
        if(18==type){
            modelName =  "BaseinfoLocationPassage";
        }
        //集货、暂存、退货、残次、地堆
        if (5==type||6==type||7==type||8==type||9==type){
            modelName =  "BaseinfoLocationRegion";
        }
        //阁楼、货架
        if (type==4){
            modelName =  "BaseinfoLocationShelf";
        }
        //仓库
        if (type==1){
            modelName =  "BaseinfoLocationWarehouse";
        }
        return modelName;

    }


}
