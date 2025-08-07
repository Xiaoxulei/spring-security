package com.xuxiaolei.springsecurityjwt.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
* 用户与角色关联表
* @TableName sys_user_role
*/
public class UserRole implements Serializable {

    /**
    * 用户ID
    */
    @NotNull(message="[用户ID]不能为空")
    @ApiModelProperty("用户ID")
    private Long userId;
    /**
    * 角色ID
    */
    @NotNull(message="[角色ID]不能为空")
    @ApiModelProperty("角色ID")
    private Long roleId;

    /**
    * 用户ID
    */
    private void setUserId(Long userId){
    this.userId = userId;
    }

    /**
    * 角色ID
    */
    private void setRoleId(Long roleId){
    this.roleId = roleId;
    }


    /**
    * 用户ID
    */
    private Long getUserId(){
    return this.userId;
    }

    /**
    * 角色ID
    */
    private Long getRoleId(){
    return this.roleId;
    }

}
