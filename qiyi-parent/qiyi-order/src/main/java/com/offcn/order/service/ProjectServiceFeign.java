package com.offcn.order.service;


import com.offcn.order.pojo.TReturn;
import com.offcn.order.service.impl.ProjectServiceFeignException;
import com.offcn.response.AppResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "QIYIPROJECT",fallback = ProjectServiceFeignException.class)
public interface ProjectServiceFeign {
    @GetMapping("/project/details/returns/{projectId}")
    public AppResponse<List<TReturn>> returnInfo(@PathVariable(name="projectId") Integer projectId);

}
