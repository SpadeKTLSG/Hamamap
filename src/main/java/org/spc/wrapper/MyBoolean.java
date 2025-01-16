package org.spc.wrapper;

import lombok.Data;

/**
 * MyBoolean
 * <p>
 * 自定义布尔类型
 *
 * @note 用于递归执行回调
 */
@Data
public class MyBoolean {

    public boolean value;

    public MyBoolean(boolean value) {
        this.value = value;
    }
}
