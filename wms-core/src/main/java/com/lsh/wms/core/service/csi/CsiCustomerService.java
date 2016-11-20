package com.lsh.wms.core.service.csi;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.csi.CsiCustomerDao;
import com.lsh.wms.model.csi.CsiCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/11/12.
 */
@Component
@Transactional(readOnly = true)
public class CsiCustomerService {
    @Autowired
    private CsiCustomerDao customerDao;

    /**
     * 插入客户
     *
     * @param customer 客户
     */
    @Transactional(readOnly = false)
    public void insertCustomer(CsiCustomer customer) {
        customer.setCustomerId(RandomUtils.genId());
        customer.setCreatedAt(DateUtils.getCurrentSeconds());
        customer.setUpdatedAt(DateUtils.getCurrentSeconds());
        customerDao.insert(customer);
    }

    /**
     * 跟新客户
     *
     * @param customer 客户
     */
    @Transactional(readOnly = false)
    public void update(CsiCustomer customer) {
        customer.setUpdatedAt(DateUtils.getCurrentSeconds());
        customerDao.update(customer);
    }

    /**
     * 通过客户号编号查找客户
     *
     * @param customerId
     * @return
     * @throws BizCheckedException
     */
    public CsiCustomer getCustomerByCustomerId(Long customerId) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("customerId", customerId);
        List<CsiCustomer> customers = customerDao.getCsiCustomerList(mapQuery);
        if (customers.size() !=  1) {
            throw new BizCheckedException("2180013");
        }
        return customers.get(0);
    }

    /**
     * 删除客户,将isValid置为0
     *
     * @param customerId
     * @return
     */
    @Transactional(readOnly = false)
    public CsiCustomer deleteCustomer(Long customerId) {
        CsiCustomer customer = this.getCustomerByCustomerId(customerId);
        customer.setIsValid(0);
        this.update(customer);
        return customer;
    }


    /**
     * 根据查询条件返回客户list
     *
     * @param params
     * @return
     */
    public List<CsiCustomer> getCustomerList(Map<String, Object> params) {
        params.put("isValid", 1);    //有效的
        return customerDao.getCsiCustomerList(params);
    }

    /**
     * 计数
     *
     * @param params
     * @return
     */
    public Integer getCustomerCount(Map<String, Object> params) {
        params.put("isValid", 1);    //有效的
        return customerDao.countCsiCustomer(params);
    }

    public CsiCustomer getCustomerByCustomerCode(Long ownerId, String customerCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        //map.put("ownerId", ownerId);
        map.put("customerCode", customerCode);
        map.put("isValid", 1);   //有效的
        List<CsiCustomer> customers = this.getCustomerList(map);
        if(customers.size()==0){
            return null;
        }else{
            return customers.get(0);
        }
    }

    /**
     * 将customerIds的json字符串解析成id,并查处
     * id1 | id2 |id3
     * @param customerIds  id集合
     * @return  结果集合
     */
    public List<Map<String, Object>> ParseCustomerIds2Customers(String customerIds) throws BizCheckedException{
        //json的拆分
        List<Map<String,Object>> customerIdsMapList = JsonUtils.json2Obj(customerIds,List.class);
        List<Map<String, Object>> customerList = new ArrayList<Map<String, Object>>();
        for (Map<String,Object> oneMap : customerIdsMapList) {
            Long customerId = Long.valueOf(oneMap.get("customerId").toString());
            CsiCustomer customer = this.getCustomerByCustomerId(customerId);
            if (null == customer) {
                throw new BizCheckedException("2180018");
            }
            Map<String, Object> customerMap = new HashMap<String, Object>();
            customerMap.put("ownerId", customer.getOwnerId());
            customerMap.put("customerCode", customer.getCustomerCode());
            customerMap.put("customerName", customer.getCustomerName());
            customerMap.put("customerId", customer.getCustomerId());
            customerMap.put("customerType",customer.getCustomerType());
            customerList.add(customerMap);
        }
        return customerList;
    }

}

