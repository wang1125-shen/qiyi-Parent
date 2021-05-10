package com.offcn.order.service.impl;

import com.offcn.enums.OrderStatusEnum;
import com.offcn.order.mapper.TOrderMapper;
import com.offcn.order.pojo.TOrder;
import com.offcn.order.pojo.TReturn;
import com.offcn.order.service.OrderService;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import com.offcn.response.AppResponse;
import com.offcn.utils.AppDateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TOrderMapper orderMapper;

    @Autowired
    private ProjectServiceFeign projectServiceFeign;
    /**
     * 保存订单方法
     *
     * @param vo
     * @return
     */
    @Override
    public TOrder saveOrder(OrderInfoSubmitVo vo) {
        TOrder order = new TOrder();
        //获得令牌
        String accessToken = vo.getAccessToken();
        //从缓存中得到会员ID
        String memberId = (String) redisTemplate.opsForValue().get(accessToken);
        if (!StringUtils.isEmpty(memberId)) {
            order.setMemberid(Integer.parseInt(memberId));
            BeanUtils.copyProperties(vo, order);
            //生成订单编号
            String orderNum = UUID.randomUUID().toString().replace("-", "");
            order.setOrdernum(orderNum);
            //支付状态  未支付
            order.setStatus(OrderStatusEnum.UNPAY.getCode() + "");
            //是否开发票
            order.setInvoice(vo.getInvoice().toString());
            //订单创建时间
            order.setCreatedate(AppDateUtils.getFormatTime());
            //从项目模块中得到回报列表
            AppResponse<List<TReturn>> returnAppResponse = projectServiceFeign.returnInfo(vo.getProjectid());
            List<TReturn> tReturnList = returnAppResponse.getData();
            TReturn tReturn = tReturnList.get(0);
            //计算回报金额   支持数量*支持金额+运费
            Integer totalMoney = vo.getRtncount() * tReturn.getSupportmoney() + tReturn.getFreight();
            order.setMoney(totalMoney);

            orderMapper.insertSelective(order);
            return order;
        }else {
            return null;
        }
    }

}
