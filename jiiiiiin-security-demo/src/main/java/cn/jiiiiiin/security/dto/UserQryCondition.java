package cn.jiiiiiin.security.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户查询条件实体映射
 */
public class UserQryCondition {

    private String username;
    @ApiModelProperty(value = "年龄起始值")
    private int age;
    // 年龄区间
    @ApiModelProperty(value = "年龄终止值")
    private int ageTo;
    // ...

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(int ageTo) {
        this.ageTo = ageTo;
    }
}
