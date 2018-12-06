package cn.jiiiiiin.module.common.dto.mngauth;

import cn.jiiiiiin.module.common.entity.mngauth.Admin;
import lombok.Data;

@Data
public class AdminDto extends Admin {

    private Long createTimestamp;

    private String createTimeStr;

}
