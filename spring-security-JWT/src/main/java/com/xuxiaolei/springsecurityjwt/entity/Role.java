package com.xuxiaolei.springsecurityjwt.entity;


import java.io.Serializable;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

/**
* 角色表
* @TableName sys_role
*/
public class Role implements Serializable {

    /**
    * 主键ID
    */
    @NotNull(message="[主键ID]不能为空")
    @ApiModelProperty("主键ID")
    private Long id;
    /**
    * 角色名称
    */
    @NotBlank(message="[角色名称]不能为空")
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("角色名称")
    @Length(max= 50,message="编码长度不能超过50")
    private String roleName;
    /**
    * 角色权限标识，如ROLE_ADMIN
    */
    @NotBlank(message="[角色权限标识，如ROLE_ADMIN]不能为空")
    @Size(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("角色权限标识，如ROLE_ADMIN")
    @Length(max= 100,message="编码长度不能超过100")
    private String roleKey;
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
    * 角色名称
    */
    private void setRoleName(String roleName){
    this.roleName = roleName;
    }

    /**
    * 角色权限标识，如ROLE_ADMIN
    */
    private void setRoleKey(String roleKey){
    this.roleKey = roleKey;
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
    * 角色名称
    */
    private String getRoleName(){
    return this.roleName;
    }

    /**
    * 角色权限标识，如ROLE_ADMIN
    */
    private String getRoleKey(){
    return this.roleKey;
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
