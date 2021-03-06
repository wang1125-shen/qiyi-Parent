package com.offcn.order.controller;

import com.offcn.order.pojo.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import com.offcn.response.AppResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="保存订单）")
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/createOrder")
    @ApiOperation("保存订单")
    public AppResponse<TOrder> createOrder(@RequestBody OrderInfoSubmitVo vo){
        String memberId = redisTemplate.opsForValue().get(vo.getAccessToken());
        if (StringUtils.isEmpty(memberId)){
            AppResponse<TOrder> fail = AppResponse.fail(null);
            fail.setMsg("无此权限，请先登录");
            return fail;
        }
        try {
            TOrder order = orderService.saveOrder(vo);
            AppResponse<TOrder> orderAppResponse = AppResponse.ok(order);
            return orderAppResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.fail(null);
        }
    }

}
