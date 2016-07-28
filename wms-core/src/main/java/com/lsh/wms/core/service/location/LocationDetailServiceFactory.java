package com.lsh.wms.core.service.location;

import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.lsh.wms.core.service.location.LocationDetailServiceFactory.LocationType.warehouse;

/**
 * location的具体service选用的工厂方法,通过传入不同的参数类型,实例化不同的service
 * 生产不同的service
 *
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/24 上午10:53
 */
@Component
public class LocationDetailServiceFactory {
    //定义各位置type的集合

    public enum LocationType {
        warehouse("warehouse", 1), region_area("region_area", 2), inventoryLost("inventoryLost", 3), goods_area("goods_area", 4), floor("floor", 5), temporary("temporary", 6), collection_area("collection_area", 7), back_area("back_area", 8), defective_area("defective_area", 9), dock_area("dock_area", 10), bin("bin", 11), pinking("picking", 12), stock_bin("stock_bin", 13), floor_bin("floor_bin", 14), temporary_bin("temporary_bin", 15), collection_bin("collection_bin", 16), back_bin("back_bin", 17), defective_bin("defective_bin", 18), passage("passage", 19);
        private String typeName;
        private int value;

        LocationType(String typeName, int value) {
            this.typeName = typeName;
            this.value = value;
        }

        public static LocationType getType(Integer type) {
            switch (type) {
                case 1: {
                    return LocationType.warehouse;
                }
                case 2: {
                    return LocationType.region_area;
                }
                case 3: {
                    return LocationType.inventoryLost;
                }
                case 4: {
                    return LocationType.goods_area;
                }
                case 5: {
                    return LocationType.floor;
                }
                case 7: {
                    return LocationType.collection_area;
                }
                case 8: {
                    return LocationType.back_area;
                }
                case 9: {
                    return LocationType.defective_area;
                }
                case 10: {
                    return LocationType.dock_area;
                }
                case 11: {
                    return LocationType.bin;
                }
                case 12: {
                    return LocationType.pinking;
                }
                case 13: {
                    return LocationType.stock_bin;
                }
                case 14: {
                    return LocationType.floor_bin;
                }
                case 15: {
                    return LocationType.temporary_bin;
                }
                case 16: {
                    return LocationType.collection_bin;
                }
                case 17: {
                    return LocationType.back_bin;
                }
                case 18: {
                    return LocationType.defective_bin;
                }
                case 19: {
                    return LocationType.passage;
                }
                default: {
                    return null;
                }
            }
        }
    }


    private static IStrategy iStrategy;

    //需要注入不同的service
    @Autowired
    private BaseinfoLocationService baseinfoLocationService;
    @Autowired
    private BaseinfoLocationBinService baseinfoLocationBinService;
    @Autowired
    private BaseinfoLocationDockService baseinfoLocationDockService;
    @Autowired
    private BaseinfoLocationPassageService baseinfoLocationPassageService;
    @Autowired
    private BaseinfoLocationRegionService baseinfoLocationRegionService;
    @Autowired
    private BaseinfoLocationShelfService baseinfoLocationShelfService;
    @Autowired
    private BaseinfoLocationWarehouseService baseinfoLocationWarehouseService;


    //生产service的方法,根据传入的model的类型不同
    public IStrategy createDetailServiceByModel(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {

        //0基本的location
        if (iBaseinfoLocaltionModel.getClass().getName().equalsIgnoreCase("BaseinfoLocation")) {
            iStrategy = baseinfoLocationService;
        }

        //1.货位
        if (iBaseinfoLocaltionModel.getClass().getName().equalsIgnoreCase("BaseinfoLocationBin")) {
            iStrategy = baseinfoLocationBinService;
        }
        //2.码头
        if (iBaseinfoLocaltionModel.getClass().getName().equalsIgnoreCase("BaseinfoLocationDock")) {
            iStrategy = baseinfoLocationDockService;
        }
        //3.通道
        if (iBaseinfoLocaltionModel.getClass().getName().equalsIgnoreCase("BaseinfoLocationPassage")) {
            iStrategy = baseinfoLocationPassageService;
        }
        //4.区域
        if (iBaseinfoLocaltionModel.getClass().getName().equalsIgnoreCase("BaseinfoLocationRegion")) {
            iStrategy = baseinfoLocationRegionService;
        }
        //5.货架,阁楼
        if (iBaseinfoLocaltionModel.getClass().getName().equalsIgnoreCase("BaseinfoLocationShelf")) {
            iStrategy = baseinfoLocationShelfService;
        }
        //6.仓库
        if (iBaseinfoLocaltionModel.getClass().getName().equalsIgnoreCase("BaseinfoLocationWarehouse")) {
            iStrategy = baseinfoLocationWarehouseService;
        }
        return iStrategy;
    }

    //    创建service通过前端传过来的type类型
    public IStrategy createDetailServiceByType(Integer type) {
        LocationType locationType = LocationType.getType(type);

        switch (locationType) {
            case warehouse: {
                iStrategy = baseinfoLocationWarehouseService;
                return iStrategy;
            }
            case region_area: {
                iStrategy = baseinfoLocationRegionService;
                return iStrategy;
            }
            case inventoryLost: {
                iStrategy = baseinfoLocationRegionService;
                return iStrategy;
            }
            case goods_area: {
                iStrategy = baseinfoLocationRegionService;
                return iStrategy;
            }
            case floor: {
                iStrategy = baseinfoLocationRegionService;
                return iStrategy;
            }
            case temporary: {
                iStrategy = baseinfoLocationRegionService;
                return iStrategy;
            }
            case collection_area: {
                iStrategy = baseinfoLocationRegionService;
                return iStrategy;
            }
            case back_area: {
                iStrategy = baseinfoLocationRegionService;
                return iStrategy;
            }
            case defective_area: {
                iStrategy = baseinfoLocationRegionService;
                return iStrategy;
            }
            case dock_area: {
                iStrategy = baseinfoLocationDockService;
            }
            case bin: {
                iStrategy = baseinfoLocationBinService;
                return iStrategy;
            }
            case pinking: {
                iStrategy = baseinfoLocationBinService;
                return iStrategy;
            }
            case stock_bin: {
                iStrategy = baseinfoLocationBinService;
                return iStrategy;
            }
            case floor_bin: {
                iStrategy = baseinfoLocationBinService;
                return iStrategy;
            }
            case temporary_bin: {
                iStrategy = baseinfoLocationBinService;
                return iStrategy;
            }
            case collection_bin: {
                iStrategy = baseinfoLocationBinService;
                return iStrategy;
            }
            case back_bin: {
                iStrategy = baseinfoLocationBinService;
                return iStrategy;
            }
            case defective_bin: {
                iStrategy = baseinfoLocationBinService;
                return iStrategy;
            }
            case passage: {
                iStrategy = baseinfoLocationPassageService;
                return iStrategy;
            }
            default: {
                //抛异常,数据不合法
                return null;
            }

        }

    }
}
