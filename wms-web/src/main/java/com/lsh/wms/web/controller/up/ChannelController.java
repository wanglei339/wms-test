package com.lsh.wms.web.controller.up;


import com.lsh.wms.api.model.up.UpChannel;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.service.up.UpChannelService;
import com.lsh.wms.web.constant.MediaTypes;
import com.lsh.wms.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/up/chn")
public class ChannelController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ChannelController.class);

    @Autowired
    private UpChannelService channelService;

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping("")
    public String chnIndex() {
        return "up/chn/chn";
    }

    /**
     * 列表
     *
     * @param draw
     * @param start
     * @param limit
     * @param keyword
     * @param response
     * @return
     */
    @RequestMapping(value = "/list", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Map<String, Object> chnList(
            @RequestParam(value = "draw", required = false) Integer draw,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "length", required = false) Integer limit,
            @RequestParam(value = "keyword", required = false) String keyword,
            HttpServletResponse response) {

        // 分页显示
        Integer num = channelService.countChannel(keyword);
        List<UpChannel> list = channelService.getChannelList(keyword, start, limit);
        // 返回结果
        Map<String, Object> result = getSuccessMap();
        result.put("draw", draw); //draw
        result.put("recordsTotal", num); //total
        result.put("recordsFiltered", num); //totalAfterFilter
        result.put("data", list.toArray());
        setResContent2Json(response);
        return result;
    }

    /**
     * 编辑
     *
     * @param id
     * @return
     */
    @RequestMapping("/page/edit")
    public ModelAndView chnEdit(@RequestParam(value = "id", required = false) Integer id) {
        UpChannel chn = null;
        if (id == null) {
            chn = new UpChannel();
            chn.setStatus(BusiConstant.EFFECTIVE_YES);
        } else {
            chn = channelService.getChannelById(id);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("chn", chn);
        modelAndView.setViewName("up/chn/chn_edit");
        return modelAndView;
    }

    /**
     * 保存
     *
     * @param channel
     * @param response
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> chnSave(UpChannel channel, HttpServletResponse response) {

        if (StringUtils.isBlank(channel.getChnCode())) {
            setResContent2Json(response);
            return getFailMap("渠道编码不能为空！");
        }
        if (channelService.existChannel(channel.getChnCode(), channel.getId())) {
            return getFailMap("渠道编码已存在！");
        }
        if (channel.getId() == null) {
            // 新增
            channel.setStatus(BusiConstant.EFFECTIVE_YES);
            // 保存
            channelService.insert(channel);
        } else {
            channelService.update(channel);
        }
        // 返回结果
        setResContent2Json(response);
        return getSuccessMap();
    }


}

