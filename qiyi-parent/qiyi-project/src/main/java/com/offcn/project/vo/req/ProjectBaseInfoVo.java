package com.offcn.project.vo.req;

import com.offcn.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class ProjectBaseInfoVo extends BaseVo {
    @ApiModelProperty("项目之前的临时token")
    private String projectToken;//项目临时token

    @ApiModelProperty("项目分类id")
    private List<Integer> typeids;//项目分类id

    @ApiModelProperty("项目标签id")
    private List<Integer> tagids;

    @ApiModelProperty("项目名称")
    private String name;

    @ApiModelProperty("项目简介")
    private String remark;// 项目简介

    @ApiModelProperty(value = "筹资金额",example = "0")
    private Integer money;// 筹资金额

    @ApiModelProperty(value = "筹资天数",example = "0")
    private Integer day;// 筹资天数

    @ApiModelProperty("项目头部图片")
    private String headerImage;// 项目头部图片

    @ApiModelProperty("项目详情图片")
    private List<String> detailsImage;// 项目详情图片


}
