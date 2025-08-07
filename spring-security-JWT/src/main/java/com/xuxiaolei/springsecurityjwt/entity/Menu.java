package com.xuxiaolei.springsecurityjwt.entity;


import java.io.Serializable;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

/**
* 菜单权限表
* @TableName sys_menu
*/
public class Menu implements Serializable {

    /**
    * 主键ID
    */
    @NotNull(message="[主键ID]不能为空")
    @ApiModelProperty("主键ID")
    private Long id;
    /**
    * 菜单名称
    */
    @NotBlank(message="[菜单名称]不能为空")
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("菜单名称")
    @Length(max= 50,message="编码长度不能超过50")
    private String menuName;
    /**
    * 前端路由地址
    */
    @Size(max= 200,message="编码长度不能超过200")
    @ApiModelProperty("前端路由地址")
    @Length(max= 200,message="编码长度不能超过200")
    private String path;
    /**
    * 权限标识，如user:list
    */
    @Size(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("权限标识，如user:list")
    @Length(max= 100,message="编码长度不能超过100")
    private String perms;
    /**
    * 菜单类型（0目录 1菜单 2按钮）
    */
    @NotNull(message="[菜单类型（0目录 1菜单 2按钮）]不能为空")
    @ApiModelProperty("菜单类型（0目录 1菜单 2按钮）")
    private Integer type;
    /**
    * 父菜单ID
    */
    @ApiModelProperty("父菜单ID")
    private Long parentId;
    /**
    * 显示顺序
    */
    @ApiModelProperty("显示顺序")
    private Integer orderNum;
    /**
    * 是否可见（0否 1是）
    */
    @ApiModelProperty("是否可见（0否 1是）")
    private Integer visible;
    /**
    * 状态（0禁用 1启用）
    */
    @ApiModelProperty("状态（0禁用 1启用）")
    private Integer status;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
    * 更新时间
    */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
    * 主键ID
    */
    private void setId(Long id){
    this.id = id;
    }

    /**
    * 菜单名称
    */
    private void setMenuName(String menuName){
    this.menuName = menuName;
    }

    /**
    * 前端路由地址
    */
    private void setPath(String path){
    this.path = path;
    }

    /**
    * 权限标识，如user:list
    */
    private void setPerms(String perms){
    this.perms = perms;
    }

    /**
    * 菜单类型（0目录 1菜单 2按钮）
    */
    private void setType(Integer type){
    this.type = type;
    }

    /**
    * 父菜单ID
    */
    private void setParentId(Long parentId){
    this.parentId = parentId;
    }

    /**
    * 显示顺序
    */
    private void setOrderNum(Integer orderNum){
    this.orderNum = orderNum;
    }

    /**
    * 是否可见（0否 1是）
    */
    private void setVisible(Integer visible){
    this.visible = visible;
    }

    /**
    * 状态（0禁用 1启用）
    */
    private void setStatus(Integer status){
    this.status = status;
    }

    /**
    * 创建时间
    */
    private void setCreateTime(Date createTime){
    this.createTime = createTime;
    }

    /**
    * 更新时间
    */
    private void setUpdateTime(Date updateTime){
    this.updateTime = updateTime;
    }


    /**
    * 主键ID
    */
    private Long getId(){
    return this.id;
    }

    /**
    * 菜单名称
    */
    private String getMenuName(){
    return this.menuName;
    }

    /**
    * 前端路由地址
    */
    private String getPath(){
    return this.path;
    }

    /**
    * 权限标识，如user:list
    */
    private String getPerms(){
    return this.perms;
    }

    /**
    * 菜单类型（0目录 1菜单 2按钮）
    */
    private Integer getType(){
    return this.type;
    }

    /**
    * 父菜单ID
    */
    private Long getParentId(){
    return this.parentId;
    }

    /**
    * 显示顺序
    */
    private Integer getOrderNum(){
    return this.orderNum;
    }

    /**
    * 是否可见（0否 1是）
    */
    private Integer getVisible(){
    return this.visible;
    }

    /**
    * 状态（0禁用 1启用）
    */
    private Integer getStatus(){
    return this.status;
    }

    /**
    * 创建时间
    */
    private Date getCreateTime(){
    return this.createTime;
    }

    /**
    * 更新时间
    */
    private Date getUpdateTime(){
    return this.updateTime;
    }

}
