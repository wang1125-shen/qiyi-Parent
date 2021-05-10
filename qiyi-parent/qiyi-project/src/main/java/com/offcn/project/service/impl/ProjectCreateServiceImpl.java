package com.offcn.project.service.impl;


import com.alibaba.fastjson.JSON;
import com.offcn.enums.ProjectStatusEnume;
import com.offcn.project.contants.ProjectConstant;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.mapper.*;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {

    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectImagesMapper projectImagesMapper;

    @Autowired
    private TProjectTagMapper projectTagMapper;

    @Autowired
    private TProjectTypeMapper projectTypeMapper;

    @Autowired
    private TReturnMapper tReturnMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    /*
* 初始化项目
* */
    @Override
    public String initCreateProject(Integer memberId) {
        String token = UUID.randomUUID().toString().replace("-", "");
        //项目临时对象
        ProjectRedisStorageVo initVo = new ProjectRedisStorageVo();
        initVo.setMemberid(memberId);
        //initVo转为json字符串
        String jsonString = JSON.toJSONString(initVo);
        stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+token,jsonString);
        return token;
    }

    /*
    * 保存项目信息
    * */
    @Override
    public void saveProjectInfo(ProjectStatusEnume auth, ProjectRedisStorageVo project) {
        //1.保存项目的基本信息,获取到数据库的id
        TProject projectBase = new TProject();
        BeanUtils.copyProperties(project,projectBase);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        projectBase.setCreatedate(simpleDateFormat.format(new Date()));
        //1.1 基本的信息插入,获取到刚才保存好的项目的自增id
        projectMapper.insertSelective(projectBase);
        //1.2 获取到刚才的id
        Integer projectBaseId = projectBase.getId();
        String headerImage = project.getHeaderImage();
        TProjectImages images = new TProjectImages(null,projectBaseId,headerImage, ProjectImageTypeEnume.HEADER.getCode());
        //2. 将项目上传的图片保存起来
        //2.1 保存头图
        projectImagesMapper.insertSelective(images);

        List<String> detailsImage = project.getDetailsImage();
        //2.2 保存详情图
        for (String string : detailsImage) {
            TProjectImages img = new TProjectImages(null, projectBaseId, string, ProjectImageTypeEnume.DETAILS.getCode());
            projectImagesMapper.insertSelective(img);
        }
        //3. 保存项目的标签信息
        List<Integer> tagids = project.getTagids();
        for (Integer tagid : tagids) {
            TProjectTag tProjectTag = new TProjectTag(null,projectBaseId,tagid);
            projectTagMapper.insertSelective(tProjectTag);
        }
        //4. 保存项目的分类信息
        List<Integer> typeids = project.getTypeids();
        for (Integer tid : typeids) {
            TProjectType tProjectType = new TProjectType(null,projectBaseId,tid);
            projectTypeMapper.insertSelective(tProjectType);
        }
        //5. 保存回报信息
        List<TReturn> returns = project.getProjectReturns();
        //设置项目的id
        for (TReturn tReturn : returns) {
            tReturn.setProjectid(projectBaseId);
            tReturnMapper.insertSelective(tReturn);
        }
        //6. 删除临时数据
        stringRedisTemplate.delete(ProjectConstant.TEMP_PROJECT_PREFIX+project.getProjectToken());

    }
}
