package cn.jiiiiiin.validation.spring;

import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

import java.util.Map;

@Setter
@Getter
@ToString
public class ValidationResult {
    /**
     * 校验结果是否有错
     */
    private boolean hasErr;

    /**
     * 校验错误信息
     */
    private Map<String, String> errorMsg;

}
